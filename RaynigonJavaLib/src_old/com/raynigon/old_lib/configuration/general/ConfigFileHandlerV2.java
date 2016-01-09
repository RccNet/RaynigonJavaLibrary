package com.raynigon.old_lib.configuration.general;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**This is a File Reader which formats a file like:
 * key=value
 * The values can get by the keys, see methods below
 * @author Simon Schneider
 */
public class ConfigFileHandlerV2 {

	/**
	 * The reader for reading
	 * */
	private BufferedReader reader = null;
	/**
	 * The writer for writing
	 * */
	private BufferedWriter writer = null;
	
	/**
	 * The Map for Saving Keys and Values
	 * */
	private Map<String, String> values = new HashMap<String, String>();

	/**
	 * LoggingStream
	 * */
	private PrintStream ps = null;
	
	
	/**
	 * File if a File exits
	 * */
	private File f = null;
	/**
	 * Create a new ConfigFileReader
	 * @param filename the filename of the file which should be read from
	 * @throws IOException is thrown if an error occurs while creating streams or reading from stream
	 * */
	public ConfigFileHandlerV2(String filename) throws IOException {
		this(new File(filename));
	}
	
	/**
	 * Create a new ConfigFileReader
	 * @param filename the filename of the file which should be read from
	 * @throws IOException is thrown if an error occurs while creating streams or reading from stream
	 * */
	public ConfigFileHandlerV2(File f2) throws IOException {
		f = f2;
		ps = System.out;
		readStreamToTable();
	}
	
	/**
	 * Create a new ConfigFileReader
	 * @param is reads from an inputstream
	 * @param os writes in this outputstream
	 * @throws IOException is thrown if an reading error occurs
	 * */
	public ConfigFileHandlerV2(InputStream is, OutputStream os) throws IOException{
		ps = System.out;
		reader = new BufferedReader(new InputStreamReader(is));
		if(os!=null){
			writer = new BufferedWriter(new OutputStreamWriter(os));
		}
		readStreamToTable();
		reader.close();
	}

	
	/**This method returns the File object if this CFH was created with one
	 * @return returns the FileObject or null, if this wasn't made with a File Object
	 */
	public File getFile(){
		return f;
	}

	/** Reads from a Stream into a value Table
	 * @throws IOException is thrown if a reading error occurs
	 * */
	private void readStreamToTable() throws IOException{
		String zeile;
		String[] infos;
		if(f!=null){
			reader = new BufferedReader(new FileReader(f));
		}
		zeile = reader.readLine();
		while (zeile != null) {
			if(zeile.length()>0){
				if(zeile.trim().charAt(0)!='#' && zeile != ""){
					infos = zeile.split("=");
					if(infos.length==2){
						values.put(infos[0].trim(), infos[1].trim());
					}else{
						String path = null;
						if(f!=null){
							path = f.getAbsolutePath();
						}else{
							path = reader.toString();
						}
						throw new IOException("ConfigFile reading error ("+path+")");
					}
				}
			}
			zeile = reader.readLine();
		}
		reader.close();
	}

	/** Writes from the Table to an OutputStream
	 * @param key An Array of PairHolders with the Key as first entry and value as second
	 * @param value An Array of PairHolders with the Key as first entry and value as second
	 * @throws IOException This IOException is thrown if an error occurs while writing
	 * */
	private void writeTableToStream(String[] key, String[] value) throws IOException{
		if(f!=null){
			writer = new BufferedWriter(new FileWriter(f));
		}
		int length = key.length;
		for(int i=0;i<length;i++){
			writer.write(key[i]+"="+value[i]+"\n");
		}
		writer.flush();
		writer.close();
	}

	/**
	 * Öffentliche Methoden
	 * */

	
	/**Reload with an Exception thrown is an error occurs
	 * @throws IOException is thrown if an error occurs while reading
	 * */
	public void reloadE() throws IOException{
		readStreamToTable();
	}
	
	/**Reloads from InputStream
	 * */
	public void reload(){
		try {
			reloadE();
		} catch (IOException e) {
			e.printStackTrace(ps);
		}
	}


	
	/**Looks if the given key exists in the file
	 * @param key	the key which should look up
	 * @return	true if the key exists in this file, false if not
	 */
	public boolean containsKey(String key){
		return values.containsKey(key);
	}

	
	/**Returns a set of all keys saved in this File
	 * @return the set of keys
	 */
	public Set<String> getKeySet(){

		return values.keySet();

	}

	
	
	/**Returns a String to the given key
	 * @param key	the key which is given
	 * @return	a String to the key which was given, if null the key doesn't exist
	 */
	public String getString(String key) {
		String out = null;
		out = values.get(key);
		if (out != null) {
			out = out.replaceAll("%-%", "=");
		}
		return out;
	}


	/**Returns a Integer to the given key
	 * @param key	the key which is given
	 * @return	a integer to the key which was given
	 */
	public int getInteger(String key) {
		String out;
		out = values.get(key);
		if (out != null) {
			out = out.replaceAll("%-%", "=");
			return Integer.valueOf(out);
		} else {
			return 0;
		}
	}


	/**Returns a Long to the given key
	 * @param key	the key which is given
	 * @return	a long to the key which was given
	 */
	public long getLong(String key) {
		String out;
		out = values.get(key);
		if (out != null) {
			out = out.replaceAll("%-%", "=");
			return Long.valueOf(out);
		} else {
			return 0l;
		}
	}
	
	/**Returns a Double to the given key
	 * @param key	the key which is given
	 * @return	a double to the key which was given
	 */
	public double getDouble(String key) {

		String out;
		out = values.get(key);
		if (out != null) {
			out = out.replaceAll("%-%", "=");
			return Double.valueOf(out);
		} else {
			return 0;
		}
	}



	/**Returns a float to the given key
	 * @param key	the key which is given
	 * @return	a float to the key which was given
	 */
	public float getFloat(String key) {
		String out;
		out = values.get(key);
		if (out != null) {
			out = out.replaceAll("%-%", "=");
			return Float.valueOf(out);
		} else {
			return 0;
		}

	}

	/**Returns a Byte to the given key
	 * @param key	the key which is given
	 * @return	a byte to the key which was given
	 */
	public byte getByte(String key) {
		String out;
		out = values.get(key);
		if (out != null) {
			out = out.replaceAll("%-%", "=");
			return Byte.valueOf(out);
		} else {
			return 0;
		}
	}

	/**Returns a Boolean to the given key
	 * @param key	the key which is given
	 * @return	a String to the key which was given
	 */
	public boolean getBoolean(String key) {
		String out;
		out = values.get(key);
		if (out != null) {
			out = out.replaceAll("%-%", "=");
			out = out.toLowerCase();
			return Boolean.valueOf(out);
		} else {
			return false;
		}

	}

	/**Sets a String value with a String key
	 * @param key	the key
	 * @param value	the value
	 */
	public void setPairString(String key, String value) {
		if(value==null){
			values.remove(key);
		}else{
			values.put(key, value.replaceAll("=", "%-%"));
		}

	}

	/**Sets a Integer value with a String key
	 * @param key	the key
	 * @param value	the value
	 */
	public void setPairInteger(String key, int value) {
		values.put(key, String.valueOf(value).replaceAll("=", "%-%"));
	}
	
	/**Sets a Long value with a String key
	 * @param key	the key
	 * @param value	the value
	 */
	public void setPairLong(String key, long value) {
		values.put(key, String.valueOf(value).replaceAll("=", "%-%"));
	}

	/**Sets a Double value with a String key
	 * @param key	the key
	 * @param value	the value
	 */
	public void setPairDouble(String key, double value) {
		values.put(key, String.valueOf(value).replaceAll("=", "%-%"));
	}


	/**Sets a Float value with a String key
	 * @param key	the key
	 * @param value	the value
	 */
	public void setPairFloat(String key, float value) {
		values.put(key, String.valueOf(value).replaceAll("=", "%-%"));
	}

	/**Sets a Byte value with a String key
	 * @param key	the key
	 * @param value	the value
	 */
	public void setPairByte(String key, byte value) {
		values.put(key, String.valueOf(value).replaceAll("=", "%-%"));
	}
	
	/**Sets a Boolean value with a String key
	 * @param key	the key
	 * @param value	the value
	 */
	public void setPairBoolean(String key, boolean value) {
		values.put(key, String.valueOf(value).replaceAll("=", "%-%"));
	}



	
	/**Saves the file and throw a Exception if an error occurs
	 * @throws IOException is thrown if an error occurs while saving the file
	 */
	public void saveE() throws IOException {
		String[] keys = new String[values.size()];
		String[] value = new String[keys.length];
		int i = 0;
		for(String key : values.keySet()){
			keys[i] = key;
			value[i] = values.get(key);
			i++;
		}		
		writeTableToStream(keys, value);
	}
	
	/**This Method saves the file and print the Exceptions directly to the console
	 */
	public void save() {
		try {
			saveE();
		} catch (IOException e) {
			e.printStackTrace(ps);
		}
	}

	/**This Method closes the File and throw a Exception if an Error occurs
	 * @throws IOException	is thrown if an error occurs while saving the file
	 */
	public void closeE() throws IOException{
		reader.close();
		if(writer!=null){
			writer.close();
		}
	}
	
	/**Just closes the file, if an Exception occurs it will directly printed to the console
	 */
	public void close() {
		try {
			closeE();
		} catch (IOException e) {
			e.printStackTrace(ps);
		}
	}
	
	
	/**This Method creates an empty file or directory on the given path
	 * @param path	the path of the new file/directory
	 * @return	true if it was a File, false if a directory was created
	 * @throws IOException	is thrown if an error occurs
	 */
	public static boolean createFileIfNotExist(String path) throws IOException{
		File f2 = new File(path);
		if (!f2.exists()) {
			if(!f2.isDirectory()){
				f2.createNewFile();
				return true;
			}else{
				f2.mkdirs();
				return true;
			}
		}
		return false;
	}

}


