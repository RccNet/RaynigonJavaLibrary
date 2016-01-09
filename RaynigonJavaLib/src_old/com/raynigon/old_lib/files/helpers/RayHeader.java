package com.raynigon.old_lib.files.helpers;

import java.util.HashMap;
import java.util.Map;

import com.raynigon.lib.crypting.Crypter;

/**
 * @author Simon Schneider
 *
 */
public final class RayHeader {

	private int length = 0;
	private Map<String, String> map = null;
	private Crypter crypter;
	
	/**Creates a new RayHeader.java
	 * @param header_size
	 */
	public RayHeader(int header_size) {
		length = header_size;
		map = new HashMap<String, String>();
	}

	public String getValue(String string) {
		return map.get(string);
	}

	public Map<String, String> getMap() {
		return map;
	}

	
	public void putPair(String key, String value) {
		map.put(key, value);
	}

	public void setCrypter(Crypter cp) {
		crypter = cp;
	}

	public Crypter getCrypter() {
		return crypter;
	}

	public int getLength(){
		return length;
	}
}
