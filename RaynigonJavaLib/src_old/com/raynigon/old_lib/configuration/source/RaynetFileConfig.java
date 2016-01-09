package com.raynigon.old_lib.configuration.source;

import java.util.Set;

import com.raynigon.old_lib.configuration.general.ConfigFileHandlerV2;
import com.raynigon.old_lib.configuration.inter.FileConfig;

/**This is a FileConfig it is used in the RaynigonConfiguration
 * @author Simon Schneider
 */
public class RaynetFileConfig implements FileConfig{

	private ConfigFileHandlerV2 cfh;
	private String name;
	private String apath;
	
	/**Creates a new FileConfig from 
	 * @param cfh	a ConfigFileHandler which saved the entrys
	 * @param name	the name of this file
	 * @param path	the absolute path to this file
	 */
	public RaynetFileConfig(ConfigFileHandlerV2 cfh, String name, String path){
		this.cfh = cfh;
		this.name = name;
		this.apath = path;
	}

	@Override
	public boolean contains(String key){
		return cfh.containsKey(key);
	}

	@Override
	public Set<String> getKeySet(){
		return cfh.getKeySet();
	}

	@Override
	public String getString(String key) {
		return cfh.getString(key);
	}

	@Override
	public int getInteger(String key) {
		return cfh.getInteger(key);
	}

	@Override
	public long getLong(String key) {
		return cfh.getLong(key);
	}
	
	@Override
	public double getDouble(String key) {
		return cfh.getDouble(key);
	}

	@Override
	public float getFloat(String key) {
		return cfh.getFloat(key);
	}

	@Override
	public boolean getBoolean(String key) {
		return cfh.getBoolean(key);
	}

	@Override
	public void setPair(String key, String value) {
		cfh.setPairString(key, value);
	}

	@Override
	public void setPair(String key, int value) {
		cfh.setPairInteger(key, value);
	}

	@Override
	public void setPair(String key, long value) {
		cfh.setPairLong(key, value);
	}
	
	@Override
	public void setPair(String key, double value) {
		cfh.setPairDouble(key, value);
	}

	@Override
	public void setPair(String key, float value) {
		cfh.setPairFloat(key, value);
	}

	@Override
	public void setPair(String key, boolean value) {
		cfh.setPairBoolean(key, value);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getAbsolutePath() {
		return apath;
	}


}
