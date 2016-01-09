package com.raynigon.lib.crypting;


/**Generated on 09.09.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * A Crypter is an interface for en- and decryption, 
 * the method of en- or decryption is defined by the underlying crypter.
 * Following are available:
 * <ul>
 * <li>{@link Blowfish}</li>
 * <li>{@link RaynigonBase64}</li>
 * <li>{@link RSA}</li>
 * </ul>
 * @author Simon Schneider
 */
public interface Crypter {
	
	/** This is the class for an Blowfish Crypter
	 */
	public static final Class<? extends Crypter> CRYPTER_BLOWFISH = Blowfish.class;
	
	/** This is the class for an Base64 Crypter
	 */
	public static final Class<? extends Crypter> CRYPTER_BASE64 = RaynigonBase64.class;
	
	/** This is the class for an RSA Crypter
	 */
	public static final Class<? extends Crypter> CRYPTER_RSA = RSA.class;
	
	
	/**This Method encrypts a byte array, 
	 * the method of encryption is defined by the underlying Crypter
	 * @param to_encrypt
	 * @return	the encrypted byte array
	 * @throws CryptingException 
	 */
	public byte[] encrypt(byte[] to_encrypt) throws CryptingException;
	
	/**This Method decrypts a byte array,
	 * the method of decryption is defined by the underlying Crypter
	 * @param to_decrypt
	 * @return	the decrypted byte array
	 * @throws CryptingException 
	 */
	public byte[] decrypt(byte[] to_decrypt) throws CryptingException;
	
}
