package com.raynigon.lib.crypting;

import org.apache.commons.codec.binary.Base64;


/**Generated on 09.09.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * The Base64 Crypter en- and decrypts a given array of bytes
 * @author Simon Schneider
 */
public class RaynigonBase64 extends Base64 implements Crypter{

	@Override
	public byte[] encrypt(byte[] to_encrypt) {
		return this.encode(to_encrypt);
	}

	@Override
	public byte[] decrypt(byte[] to_decrypt) {
		return this.decode(to_decrypt);
	}

}
