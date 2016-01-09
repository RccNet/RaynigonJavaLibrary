package com.raynigon.old_lib.configuration.inter;

/**This is a ConfigPart. It is used in the RaynigonConfiguration
 * A ConfigPart could be a Folder or a File
 * @author Simon Schneider
 *
 */
public interface ConfigPart {
	
	/**This method returns the Name of this ConfigPart
	 * @return the name of this ConfigPart
	 */
	public String getName();
	
	/**This method returns the absolute FileSystem Path to this ConfigPart
	 * @return the absolute Path in the FileSystem
	 * */
	public String getAbsolutePath();
}
