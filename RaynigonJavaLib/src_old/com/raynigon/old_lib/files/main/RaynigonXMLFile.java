package com.raynigon.old_lib.files.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.raynigon.old_lib.files.helpers.RayHeader;

/**
 * @author Simon Schneider
 *
 */
public class RaynigonXMLFile extends RaynigonFile {
	
	private Document doc = null;
	
	public RaynigonXMLFile(InputStream tis) {
		super(tis, null, null);
	}
	
	public RaynigonXMLFile(InputStream tis, OutputStream tos) {
		super(tis,tos,null);
	}
	
	public RaynigonXMLFile(File f) throws FileNotFoundException {
		super(new FileInputStream(f), new FileOutputStream(f), null);
	}
	
	public RaynigonXMLFile(InputStream tis, byte[] key) {
		super(tis, null, key);
	}
	
	public RaynigonXMLFile(InputStream tis, OutputStream tos, byte[] tkey) {
		super(tis, tos, tkey);
	}
	
	public RaynigonXMLFile(File f, byte[] key) throws FileNotFoundException {
		super(new FileInputStream(f), new FileOutputStream(f), key);
	}
	
	public RaynigonXMLFile(RayHeader header, File f) throws FileNotFoundException {
		super(header, f);
	}

	public RaynigonXMLFile(RayHeader header, File f, byte[] key) throws FileNotFoundException {
		super(header, f, key);
	}

	public RaynigonXMLFile(RayHeader header, File f, FileInputStream fis) throws FileNotFoundException {
		super(header, f, fis);
	}

	public RaynigonXMLFile(RayHeader header, File f, FileInputStream fis,
			byte[] key) throws FileNotFoundException {
		super(header, f, fis, key);
	}

	public Document getDocument(){
		return doc;
	}
	
	public void setDocument(Document tdoc){
		doc = tdoc;
	}

	@Override
	protected void parseData(InputStream is) throws Exception {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		System.out.println("size:"+is.available());
		doc = dBuilder.parse(is);
	}

	@Override
	protected void writeData(OutputStream os) throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(os);
		transformer.transform(source, result);
	}

}
