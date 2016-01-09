package com.raynigon.old_lib.files.helpers;

import java.awt.GraphicsEnvironment;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.raynigon.lib.crypting.Crypter;
import com.raynigon.old_lib.files.main.RaynigonFile;
import com.raynigon.old_lib.files.main.RaynigonKeyValueFile;
import com.raynigon.old_lib.files.main.RaynigonPlainFile;
import com.raynigon.old_lib.files.main.RaynigonXMLFile;
import com.raynigon.old_lib.tools.ByteArrayCaster;

public class RaynigonFileCreator {

	public static RaynigonFile createEmptyRaynigonFile(File f) throws Exception{
		f.createNewFile();
		FileOutputStream fos = new FileOutputStream(f);
		createEmptyRaynigonStream(fos, "plain");
		return getRaynigonFileFor(f);
	}
	
	public static RaynigonFile createEmptyRaynigonFile(File f, RaynigonFileTypes type) throws Exception{
		f.createNewFile();
		FileOutputStream fos = new FileOutputStream(f);
		createEmptyRaynigonStream(fos, type.toString());
		if(type==RaynigonFileTypes.XML){
			fos.write((new String("<root></root>")).getBytes());
		}
		return getRaynigonFileFor(f);
	}
	
	private static void createEmptyRaynigonStream(OutputStream os, String type) throws Exception{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.newDocument();
		Element root = doc.createElement("header");
		
		Element value = doc.createElement("crypted");
		value.setTextContent("false");
		root.appendChild(value);
		
		value = doc.createElement("file_type");
		value.setTextContent(type);
		root.appendChild(value);
		
		doc.appendChild(root);
		
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
		stream.close();
		os.close();
	}
	
	public static RaynigonFile getRaynigonFileFor(File f) throws Exception{
		FileInputStream fis = new FileInputStream(f);
		if(fis.available()<4){
			fis.close();
			throw new IOException("Empty File");
		}
		RayHeader header = getFileHeader(fis, null, f.getName());
		String type = header.getValue("file_type");
		RaynigonFile file = null;
		if(type.equalsIgnoreCase("xml")){
			file = new RaynigonXMLFile(header, f, fis);
		}else if(type.equalsIgnoreCase("keyvalue")){
			file = new RaynigonKeyValueFile(header, f, fis);
		}else if(type.equalsIgnoreCase("plain")){
			file = new RaynigonPlainFile(header, f, fis);
		}
		return file;
	}
	
	public static RaynigonFile getRaynigonFileFor(File f, byte[] key) throws Exception{
		FileInputStream fis = new FileInputStream(f);
		if(fis.available()<4){
			fis.close();
			throw new IOException("Empty File");
		}
		RayHeader header = getFileHeader(fis, key, f.getName());
		String type = header.getValue("file_type");
		if(type.equalsIgnoreCase("xml")){
			return new RaynigonXMLFile(header, f, fis, key);
		}else if(type.equalsIgnoreCase("keyvalue")){
			return new RaynigonKeyValueFile(header, f, fis, key);
		}else if(type.equalsIgnoreCase("plain")){
			return new RaynigonPlainFile(header, f, fis, key);
		}
		return null;
	}
	
	private static RayHeader getFileHeader(InputStream is, byte[] ckey, String fname) throws Exception{
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
		RayHeader header = new RayHeader(header_size);
		ByteArrayInputStream bais = new ByteArrayInputStream(bheader);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(bais);
		NodeList nodes = doc.getDocumentElement().getChildNodes();
		Node node = null;
		for(int i=0;i<nodes.getLength();i++){
			node = nodes.item(i);
			if(node instanceof Element){
				header.putPair(node.getNodeName().toLowerCase(), node.getTextContent());
			}
		}
		Crypter cp = null;
		if(header.getValue("crypted").equalsIgnoreCase("true")){
			cp = createCrypter(ckey, header, fname);
			header.setCrypter(cp);
		}
		bais.close();
		return header;
	}

	/**
	 * @return
	 * @throws IOException 
	 */
	private static Crypter createCrypter(byte[] crypting_key, RayHeader header, String fname) throws IOException {
		Crypter cp = null;
			try{
				String crypter_class = header.getValue("crypting_method");
				@SuppressWarnings("unchecked")
				Class<? extends Crypter> clazz = (Class<? extends Crypter>) Class.forName(crypter_class);
				Constructor<?extends Crypter> con = clazz.getConstructor(byte[].class);
				if(con==null){
					throw new IOException("Unable to encrypt cause Constructor not found");
				}
				if(crypting_key==null){
					if(GraphicsEnvironment.isHeadless() || fname==null){
						throw new IOException("Unable to encrypt, key not found");
					}else{
						String key = JOptionPane.showInputDialog("Please insert key for File: "+fname);
						crypting_key = key.getBytes();
					}
				}
				cp = con.newInstance(crypting_key);
			}catch(Exception e){
				if(e instanceof IOException){
					throw (IOException) e;
				}else{
					throw new IOException("Unable to encrypt", e);
				}
			}
		return cp;
	}
}
