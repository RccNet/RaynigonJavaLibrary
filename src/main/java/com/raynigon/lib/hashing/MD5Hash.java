package com.raynigon.lib.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**Generated on 09.09.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * Is able to Hash Strings and byte arrays in MD5
 * @author Simon Schneider
 */
public class MD5Hash {

	/**Hashes a String and returns a String as result
	 * @param text	the hashed String
	 * @return	the hashed String
	 * @throws NoSuchAlgorithmException	Should not occur, if so, the JVM has no MD5 Hashing available.
	 */
	public String hashMD5(String text) throws NoSuchAlgorithmException{
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.reset();
        md5.update(text.getBytes());
        byte[] result = md5.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i=0; i<result.length; i++) {
            hexString.append(Integer.toHexString(0xFF & result[i]));
        }
        return hexString.toString();
	}
	
	/**Hashes a byte array and returns a byte array as result
	 * @param bytes	the hashed String
	 * @return	the hashed byte array
	 * @throws NoSuchAlgorithmException	Should not occur, if so, the JVM has no MD5 Hashing available.
	 */
	public String hashMD5(byte[] bytes) throws NoSuchAlgorithmException{
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.reset();
        md5.update(bytes);
        byte[] result = md5.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i=0; i<result.length; i++) {
            hexString.append(Integer.toHexString(0xFF & result[i]));
        }
        return hexString.toString();
	}
}
