package com.raynigon.old_lib.configuration.source;

import java.util.HashMap;
import java.util.Map.Entry;

import com.raynigon.old_lib.configuration.inter.ConfigFolder;
import com.raynigon.old_lib.configuration.inter.ConfigPart;

import java.util.Set;


/**This is a ConfigFolder
 * @author Simon Schneider
 *
 */
public class RaynetConfigFolder implements ConfigFolder{

	private HashMap<String, ConfigPart> configurations = null;
	private String name = null;
	private String apath = null;
	
	/**Creates a new ConfigFolder from the name of this folder and the path of this folder
	 * THIS WILL NOT LOAD THE VALUES FROM THE PATH
	 * @param string	the name of this folder
	 * @param path		the absolute path to this folder
	 */
	public RaynetConfigFolder(String string, String path){
		apath = path;
		configurations = new HashMap<String, ConfigPart>();
		name = string;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public ConfigPart getConfigPart(String name) {
		return configurations.get(name);
	}

	@Override
	public void addChild(String name, ConfigPart cp) {
		configurations.put(name, cp);
	}

	@Override
	public String getAbsolutePath() {
		return apath;
	}

	@Override
	public Set<Entry<String, ConfigPart>> getConfigParts() {
		return configurations.entrySet();
	}


}
