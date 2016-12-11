package test.raynigon.lib.io;

import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.raynigon.lib.io.IOUtils;

public class TestIOUtils {

	private byte[] data_byte;
	/*private String data_text;
	private byte[] data_bt_utf_8;
	private byte[] data_bt_utf_16;*/
	
	
	@Before
	public void setUp() throws Exception {
		Random r = new Random();
		data_byte = new byte[4096];
		r.nextBytes(data_byte);
	}
	
	@Test
	public void copyStreamToStream() throws IOException {
		InputStream is = new ByteArrayInputStream(data_byte);
		OutputStream os = new ByteArrayOutputStream();
		IOUtils.copy(is, os);
		byte[] result = ((ByteArrayOutputStream) os).toByteArray();
		assertArrayEquals(data_byte, result);
	}

}
