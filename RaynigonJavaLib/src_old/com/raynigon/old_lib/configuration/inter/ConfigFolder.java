package com.raynigon.old_lib.configuration.inter;

import java.util.Map.Entry;
import java.util.Set;



/**This is a ConfigFolder which represents a ConfigPart
 * @author Simon Schneider
 */
public interface ConfigFolder extends ConfigPart{
	
	
	/**This method returns a ConfigPart with the given name
	 * @param name	the name of the ConfigPart
	 * @return	the ConfigPart with the given name. If its null there is no ConfigPart with the given name in this folder
	 */
	public ConfigPart getConfigPart(String name);
	
	
	/**This method adds a new ConfigPart in this ConfigFolder
	 * @param name	the name of this part
	 * @param cp	the ConfigPart itself
	 */
	public void addChild(String name, ConfigPart cp);
	
	
	/**This method returns a Set of all ConfigParts in this Folder
	 * @return the Set of all ConfigParts in this Folder
	 */
	public Set<Entry<String, ConfigPart>> getConfigParts();
}
