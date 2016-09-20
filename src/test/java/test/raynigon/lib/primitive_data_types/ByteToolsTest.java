package test.raynigon.lib.primitive_data_types;

import static org.junit.Assert.*;

import org.junit.Test;

import com.raynigon.lib.datatypes.ByteTools;

public class ByteToolsTest {

	@Test
	public void testShort() {
		short value = (short) (Math.random()*Long.MAX_VALUE);
		byte[] array = ByteTools.getBytes(value);
		assertEquals(value, ByteTools.getShort(array));
	}
	
	@Test
	public void testInt() {
		int value = (int) (Math.random()*Integer.MAX_VALUE);
		byte[] array = ByteTools.getBytes(value);
		assertEquals(value, ByteTools.getInt(array));
	}
	
	@Test
	public void testLong() {
		long value = (long) (Math.random()*Long.MAX_VALUE);
		byte[] array = ByteTools.getBytes(value);
		assertEquals(value, ByteTools.getLong(array));
	}
	
	@Test
	public void testFloat() {
		float value = (float) (Math.random()*Float.MAX_VALUE);
		byte[] array = ByteTools.getBytes(value);
		assertEquals(value, ByteTools.getFloat(array), 0.001);
	}
	
	@Test
	public void testDouble() {
		double value = (double) (Math.random()*Float.MAX_VALUE);
		byte[] array = ByteTools.getBytes(value);
		assertEquals(value, ByteTools.getDouble(array), 0.001);
	}
}
