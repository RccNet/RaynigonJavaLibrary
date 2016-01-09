package com.raynigon.old_lib.files.main;

import java.awt.GraphicsEnvironment;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.raynigon.lib.crypting.Crypter;
import com.raynigon.old_lib.files.helpers.RayHeader;
import com.raynigon.old_lib.tools.ByteArrayCaster;

/**
 * @author Simon Schneider
 *
 */
public abstract class RaynigonFile {

	private InputStream is = null;
	private OutputStream os = null;
	private File file = null;
	private Map<String, String> header = null;
	private boolean header_was_read = false;
	private Crypter cp = null;
	private byte[] crypting_key = null;
	private boolean read_header = true;
	
	public RaynigonFile(InputStream tis) {
		this(tis, null, null);
	}
	
	public RaynigonFile(InputStream tis, OutputStream tos) {
		this(tis,tos,null);
	}
	
	public RaynigonFile(File f) throws FileNotFoundException {
		is = new FileInputStream(f);
		crypting_key = null;
		header = new HashMap<String, String>();
		file = f;
	}
	
	public RaynigonFile(InputStream tis, byte[] key) {
		this(tis, null, key);
	}
	
	public RaynigonFile(InputStream tis, OutputStream tos, byte[] tkey){
		is = tis;
		os = tos;
		crypting_key = tkey;
		header = new HashMap<String, String>();
	}
	
	public RaynigonFile(File f, byte[] key) throws FileNotFoundException {
		is = new FileInputStream(f);
		crypting_key = key;
		header = new HashMap<String, String>();
		file = f;
	}
	
	public RaynigonFile(RayHeader theader, InputStream tis) {
		this(theader, tis, null, null);
	}
	
	public RaynigonFile(RayHeader theader, InputStream tis, OutputStream tos) {
		this(theader, tis,tos,null);
	}
	
	public RaynigonFile(RayHeader theader, File f) throws FileNotFoundException {
		is = new FileInputStream(f);
		crypting_key = null;
		header = new HashMap<String, String>(theader.getMap());
		cp = theader.getCrypter();
		file = f;
		read_header = false;
	}
	
	public RaynigonFile(RayHeader theader, File f, InputStream tis) throws FileNotFoundException {
		is = tis;
		crypting_key = null;
		header = new HashMap<String, String>(theader.getMap());
		cp = theader.getCrypter();
		file = f;
		read_header = false;
	}
	
	public RaynigonFile(RayHeader theader, InputStream tis, byte[] key) {
		this(theader, tis, null, key);
	}
	
	public RaynigonFile(RayHeader theader, InputStream tis, OutputStream tos, byte[] tkey){
		is = tis;
		os = tos;
		crypting_key = tkey;
		header = new HashMap<String, String>(theader.getMap());
		cp = theader.getCrypter();
		read_header = false;
	}
	
	public RaynigonFile(RayHeader theader, File f, byte[] key) throws FileNotFoundException {
		is = new FileInputStream(f);
		crypting_key = key;
		header = new HashMap<String, String>(theader.getMap());
		cp = theader.getCrypter();
		read_header = false;
		file = f;
	}
	
	public RaynigonFile(RayHeader theader, File f, InputStream tis, byte[] key) throws FileNotFoundException {
		is = tis;
		crypting_key = key;
		header = new HashMap<String, String>(theader.getMap());
		cp = theader.getCrypter();
		read_header = false;
		file = f;
	}
	
	public void parseFile() throws Exception{
		try{
			if(read_header){
				readHeader();
			}
			if(cp!=null){
				byte[] data = new byte[is.available()];
				is.read(data);
				ByteArrayInputStream bis = new ByteArrayInputStream(cp.decrypt(data));
				parseData(bis);
				bis.close();
			}else{
				parseData(is);
			}
			is.close();
		}catch(Exception e){
			is.close();
			throw new Exception(e);
		}
	}
	
	public void writeFile() throws Exception{
		try{
			if(os==null && file!=null){
				os = new FileOutputStream(file);
			}else if(os==null && file==null){
				throw new IOException("Unable to write, missing outputstream");
			}
			if(!isReadOnly()){
				writeHeader();
				if(cp!=null){
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					writeData(bos);
					byte[] data = bos.toByteArray();
					data = cp.encrypt(data);
					os.write(data);
				}else{
					writeData(os);
				}
				os.flush();
				if(file!=null){
					os.close();
					os = null;
				}
			}else{
				throw new IOException("File is protected, unable to write");
			}
		}catch(Exception e){
			if(file!=null){
				os.close();
				os = null;
			}
			throw e;
		}
	}

	public boolean isReadOnly(){
		if(header.get("edit")!=null){
			if(header.get("edit").equalsIgnoreCase("false")){
				return true;
			}
		}
		return false;
	}
	
	public Map<String, String> getHeaderLines(){
		Map<String, String> map = new HashMap<String,String>();
		for(String key : header.keySet()){
			map.put(key, header.get(key));
		}
		return map;
	}
	
	public void setHeaderPair(String key, String value){
		header.put(key, value);
	}
	
	public void close() throws IOException{
		is.close();
		if(os!=null){
			os.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void readHeader() throws IOException, ParserConfigurationException, SAXException{
		if(!header_was_read){
			byte[] bint = new byte[4];
			is.read(bint);
			int header_size = ByteArrayCaster.getIntFromBytes(bint);
			byte[] bheader = new byte[header_size];
			is.read(bheader);
			bheader = Base64.decodeBase64(bheader);
			if(bheader.length<=0){
				is.close();
				throw new IOException("Unable to read from Stream");
			}
			ByteArrayInputStream bais = new ByteArrayInputStream(bheader);
			System.out.println(bais.available()+"/"+bheader.length);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(bais);
			NodeList nodes = doc.getDocumentElement().getChildNodes();
			Node node = null;
			for(int i=0;i<nodes.getLength();i++){
				node = nodes.item(i);
				if(node instanceof Element){
					header.put(node.getNodeName().toLowerCase(), node.getTextContent());
				}
			}
			if(header.get("crypted").equalsIgnoreCase("true") && cp==null){
				try{
					String crypter_class = header.get("crypting_method");
					Class<? extends Crypter> clazz = (Class<? extends Crypter>) Class.forName(crypter_class);
					Constructor<?extends Crypter> con = clazz.getConstructor(byte[].class);
					if(con==null){
						throw new IOException("Unable to encrypt cause Constructor not found");
					}
					if(crypting_key==null){
						if(GraphicsEnvironment.isHeadless() || file==null){
							throw new IOException("Unable to encrypt, key not found");
						}else{
							String key = JOptionPane.showInputDialog("Please insert key for File: "+file.getName());
							crypting_key = key.getBytes();
						}
					}
					con.newInstance(crypting_key);
				}catch(Exception e){
					if(e instanceof IOException){
						throw (IOException) e;
					}else{
						throw new IOException("Unable to encrypt", e);
					}
				}
			}
			bais.close();
			header_was_read = true;
		}
	}
	
	private void writeHeader() throws ParserConfigurationException, TransformerException, IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.newDocument();
		Element root = doc.createElement("header");
		doc.appendChild(root);
		Element value = null;
		for(String key : header.keySet()){
			value = doc.createElement(key);
			value.setTextContent(header.get(key));
			root.appendChild(value);
		}
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(stream);
		transformer.transform(source, result);
		byte[] header = stream.toByteArray();
		header = Base64.encodeBase64(header);
		byte[] header_size = ByteArrayCaster.getBytesForInt(header.length);
		os.write(header_size);
		os.write(header);
	}
	
	public void setCrypter(Crypter tcp){
		cp = tcp;
		header.put("crypted", "true");
		header.put("crypting_method", cp.getClass().getName());
	}
	
	protected abstract void parseData(InputStream is) throws Exception;
	protected abstract void writeData(OutputStream os) throws Exception;
}
