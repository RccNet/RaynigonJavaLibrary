package com.raynigon.lib.io;

import java.io.File;
import java.io.IOException;

/**Generated on 24.10.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * @author Simon Schneider
 */
public class DynamicBufferFactory {

	private static int defaultInitSize = 4096;
	private static File tmpFolder = getDefaultTmpFolder();
	
	private static File getDefaultTmpFolder() {
		return new File(System.getProperty("java.io.tmpdir"));
	}
	
	public void setTemporaryFolder(File newFolder){
		tmpFolder = newFolder;
	}
	

	public static DynamicBuffer createDynamicByteBuffer() throws IOException{
		return new DynamicBuffer(defaultInitSize, tmpFolder);
	}
	
	public static DynamicBuffer createDynamicByteBuffer(int inInitSize) throws IOException{
		return new DynamicBuffer(inInitSize, tmpFolder);
	}
}
