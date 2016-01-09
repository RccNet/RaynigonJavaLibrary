package com.raynigon.old_lib.files.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import com.raynigon.old_lib.files.helpers.RayHeader;

/**
 * @author Simon Schneider
 *
 */
public class RaynigonPlainFile extends RaynigonFile {

	private String text = null;
	
	public RaynigonPlainFile(File f) throws FileNotFoundException {
		super(f);
	}
	
	public RaynigonPlainFile(RayHeader header, File f) throws FileNotFoundException {
		super(header, f);
	}

	public RaynigonPlainFile(RayHeader header, File f, byte[] key) throws FileNotFoundException {
		super(header, f, key);
	}

	public RaynigonPlainFile(RayHeader header, File f, FileInputStream fis) throws FileNotFoundException {
		super(header, f, fis);
	}

	public RaynigonPlainFile(RayHeader header, File f, FileInputStream fis,
			byte[] key) throws FileNotFoundException {
		super(header, f, fis,key);
	}

	@Override
	protected void parseData(InputStream is) throws Exception {
		byte[] a = new byte[is.available()];
		is.read(a);
		text = new String(a);
	}

	@Override
	protected void writeData(OutputStream os) throws Exception {
		if(text!=null){
			os.write(text.getBytes());
		}
	}

	public void setText(String ttext){
		text = ttext;
	}
	
	public String getText(){
		return text;
	}
	
	public String toString(){
		return getText();
	}
}
