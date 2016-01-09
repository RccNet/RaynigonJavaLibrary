package com.raynigon.lib.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**Generated on 04.11.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * @author Simon Schneider
 */
public class IOUtils {

	
	public static void copy(InputStream inIs, StringWriter inSw, Charset inCs) throws IOException {
		//TODO Optimize this!!!
		ArrayList<Byte> bytes = new ArrayList<Byte>(inIs.available());
		while(inIs.available()>0){
			bytes.add(new Byte((byte) inIs.read()));
		}
		byte[] data = new byte[bytes.size()];
		for(int i=0;i<data.length;i++){
			data[i] = bytes.get(i).byteValue();
		}
		inSw.write(new String(data, inCs));
	}
}
