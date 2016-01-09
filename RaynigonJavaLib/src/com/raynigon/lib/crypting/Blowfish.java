package com.raynigon.lib.crypting;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**Generated on 09.09.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * The Blowfish Crypter en- and decrypts a given array of bytes
 * @author Simon Schneider
 */
public class Blowfish implements Crypter{
	
	private byte[] bkey = null;
	private String alogoritm = "Blowfish";
	private SecretKeySpec key;
	
	
	/** Creates a new instance with the given byte array as key
	 * @param bytes 	the key
	 */
	public Blowfish(byte[] bytes) {
		bkey = bytes;
		if(bkey==null || bkey.length==0){
			throw new IllegalArgumentException("Key is null");
		}
		checkKeySize(bkey.length);
		
		key = new SecretKeySpec(bkey, alogoritm);
	}

	private void checkKeySize(int length) {
		try{
			if(Cipher.getMaxAllowedKeyLength(alogoritm)<bkey.length){
				throw new CryptingException("Key is too long for default Java");
			}
		}catch(NoSuchAlgorithmException e){
			throw new CryptingException("KeySizeCheck:", e);
		}
	}

	@Override
	public byte[] encrypt(byte[] to_encrypt) throws CryptingException{
		try {
			Cipher cipher = Cipher.getInstance(alogoritm);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(to_encrypt);
		} catch (Exception e) {
			System.err.println("Blowfish Encrypting Exception, Debug Information:");
			System.err.println("KeySize:"+bkey.length);
			System.err.println("Algorithm:"+alogoritm);
			throw new CryptingException("Blowfish encryption:", e);
		}
    }
 
	@Override
    public byte[] decrypt(byte[] to_decrypt) throws CryptingException{
	    try {
	    	Cipher cipher = Cipher.getInstance(alogoritm);
		    cipher.init(Cipher.DECRYPT_MODE, key);
		    return cipher.doFinal(to_decrypt);
		} catch (Exception e) {
			throw new CryptingException("Blowfish decryption:", e);
		}
    }

	/**This Method returns the Crypting key that was given on creation
	 * @return	the Crypting Key
	 */
	public byte[] getKey() {
		return bkey;
	}
}
