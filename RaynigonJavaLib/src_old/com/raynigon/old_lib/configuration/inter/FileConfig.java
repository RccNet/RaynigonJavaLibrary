package com.raynigon.old_lib.configuration.inter;

import java.util.Set;

/**This is a FileConfig it represents a file with a key and a value in every line
 * @author SimonSchneider
 */
public interface FileConfig extends ConfigPart{
	
	/**This method return if the given key is contained in this file
	 * @param key	the key which should be in this file
	 * @return	true if this file contains the key false if not
	 */
	public boolean contains(String key);
	
	/**This Method returns a Set of all keys in this File
	 * @return a set of keys in this file
	 */
	public Set<String> getKeySet();
	
	/** Returns a String value for the given key
	 * @param key
	 * @return the String value for the given key. if its null there is no such value
	 */
	public String getString(String key);
	
	/** Returns an Integer value for the given key
	 * @param key
	 * @return the Integer value for the given key.
	 */
	public int getInteger(String key);
	
	/** Returns an Long value for the given key
	 * @param key
	 * @return the Long value for the given key.
	 */
	public long getLong(String key);
	
	/** Returns a Double value for the given key
	 * @param key
	 * @return the Double value for the given key.
	 */
	public double getDouble(String key);
	
	/** Returns a Float value for the given key
	 * @param key
	 * @return the Float value for the given key.
	 */
	public float getFloat(String key);
	
	/** Returns a Boolean value for the given key
	 * @param key
	 * @return the Boolean value for the given key.
	 */
	public boolean getBoolean(String key);
	
	/**This method put a new entry in this file.
	 * If the an entry with the same key already exits the entry will be overridden
	 * @param key	the key of the entry
	 * @param value	the value of the entry
	 */
	public void setPair(String key, String value);
	
	/**This method put a new entry in this file.
	 * If the an entry with the same key already exits the entry will be overridden
	 * @param key	the key of the entry
	 * @param value	the value of the entry
	 */
	public void setPair(String key, int value);
	
	/**This method put a new entry in this file.
	 * If the an entry with the same key already exits the entry will be overridden
	 * @param key	the key of the entry
	 * @param value	the value of the entry
	 */
	public void setPair(String key, long value);
	
	/**This method put a new entry in this file.
	 * If the an entry with the same key already exits the entry will be overridden
	 * @param key	the key of the entry
	 * @param value	the value of the entry
	 */
	public void setPair(String key, double value);
	
	/**This method put a new entry in this file.
	 * If the an entry with the same key already exits the entry will be overridden
	 * @param key	the key of the entry
	 * @param value	the value of the entry
	 */
	public void setPair(String key, float value);
	
	/**This method put a new entry in this file.
	 * If the an entry with the same key already exits the entry will be overridden
	 * @param key	the key of the entry
	 * @param value	the value of the entry
	 */
	public void setPair(String key, boolean value);
	
}
