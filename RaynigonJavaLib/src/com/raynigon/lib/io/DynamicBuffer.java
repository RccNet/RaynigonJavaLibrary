package com.raynigon.lib.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.UUID;

/**Generated on 24.10.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * @author Simon Schneider
 */
public class DynamicBuffer{
	
	private static final int MODE_ARRAY = 0x00;
	private static final int MODE_FILE = 0x01;
	
	private int mode = MODE_ARRAY;
	
	private File tmpFile; 
	private FileOutputStream fos;
	
	private int filled = 0;
	private byte[] array;
	
	protected DynamicBuffer(int size, File tmpFolder) throws IOException {
		array = new byte[size];
		generateEmptyFile(tmpFolder);
	}

	public void write(byte[] input, int offset, int length) throws IOException{
		if(offset>=input.length || (offset+length)>input.length) throw new IllegalArgumentException();//TODO add a message here
		makeLengthSure(length);
		if(mode==MODE_ARRAY){
			System.arraycopy(input, offset, array, filled, length);
			filled += length;
		}else{
			fos.write(input, offset, length);
		}
	}
	
	public void write(byte[] input) throws IOException{
		makeLengthSure(input.length);
		if(mode==MODE_ARRAY){
			System.arraycopy(input, 0, array, filled, input.length);
			filled += input.length;
		}else{
			fos.write(input);
		}
	}
	
	public byte[] toArray(){
		if(mode==MODE_FILE) return null;
		byte[] ret = new byte[filled];
		System.arraycopy(array, 0, ret, 0, filled);
		return ret;
	}
	
	/**
	 * @param length
	 * @throws IOException 
	 */
	private void makeLengthSure(long length) throws IOException {
		if(mode==MODE_FILE) return;
		if(filled+length<array.length) return;
		long nlength = (filled+length);
		if(nlength>Integer.MAX_VALUE){
			changeToFile();
			return;
		}
		array = Arrays.copyOf(array, (int) nlength);
	}

	private void changeToFile() throws IOException {
		if(mode==MODE_FILE) return;
		fos = new FileOutputStream(tmpFile);
		fos.write(array);
		fos.flush();
		filled = 0;
		array = new byte[0];
		System.gc();
	}

	public long size(){
		if(mode==MODE_ARRAY) return filled;
		else return tmpFile.length();
	}
	

	/**
	 * @param tmpFolder
	 * @throws IOException 
	 */
	private static File generateEmptyFile(File tmpFolder) throws IOException {
		UUID uuid = UUID.randomUUID();
		File f = new File(tmpFolder, uuid.toString()+".raytmp");
		while (f.exists()){
			uuid = UUID.randomUUID();
			f = new File(tmpFolder, uuid.toString()+".raytmp");
		}
		f.createNewFile();
		f.deleteOnExit();
		return f;
	}

}
