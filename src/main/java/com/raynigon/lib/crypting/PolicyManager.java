package com.raynigon.lib.crypting;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;

/**Generated on 28.11.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * This should automatically install the extended java security policies, but its too complicated due to windows security handling.
 * @author Simon Schneider
 */
class PolicyManager {
	
	protected  PolicyManager() {
		System.out.println("tgest");
	}
	
	private File getLibSecurityFolder(){
		String jvm_location;
		jvm_location = System.getProperties().getProperty("java.home") + File.separator + "lib" + File.separator + "security" + File.separator;
		return new File(jvm_location);
	}
	
	@SuppressWarnings("unused")
    private boolean isInstalled(){
		boolean check = false;
		try{
			check = Cipher.getMaxAllowedKeyLength("Blowfish")>16;
		}catch(NoSuchAlgorithmException e){}
		return check;
	}
	
	@SuppressWarnings("unused")
    private void install() throws IOException{
		File f = new File(getLibSecurityFolder(), "local_policy.jar");
		
		if(f.exists() && !f.delete())
			throw new IOException("Unable to delete old File");
		
		if(!f.createNewFile())
			throw new IOException("Unable to create new File");
		
		InputStream in = PolicyManager.class.getResourceAsStream("/com/raynigon/lib/res/local_policy.jar");
		Files.copy(in, f.toPath());
	}
}
