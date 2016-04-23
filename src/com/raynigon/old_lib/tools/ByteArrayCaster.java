package com.raynigon.old_lib.tools;

import java.nio.ByteBuffer;

public class ByteArrayCaster {

	@Deprecated
	public static long getLongFromBytes(byte[] array){
		if(array.length!=8){
			throw new IllegalArgumentException("Array isnt 8 bytes long");
		}
		ByteBuffer bbufLong = ByteBuffer.allocate(8);
		bbufLong.put(array);
		bbufLong.flip();
		long retvar = bbufLong.getLong();
		bbufLong = null;
		return retvar;
	}
	
	@Deprecated
	public static int getIntFromBytes(byte[] array){
		if(array.length!=4){
			throw new IllegalArgumentException("Array isnt 4 bytes long");
		}
		ByteBuffer bbufint = ByteBuffer.allocate(4);
		bbufint.put(array);
		bbufint.flip();
		int retvar = bbufint.getInt();
		bbufint = null;
		return retvar;
	}
	
	@Deprecated
	public static short getShortFromBytes(byte[] array) {
		if(array.length!=2){
			throw new IllegalArgumentException("Array isnt 2 bytes long");
		}
		ByteBuffer bbufint = ByteBuffer.allocate(2);
		bbufint.put(array);
		bbufint.flip();
		short retvar = bbufint.getShort();
		bbufint = null;
		return retvar;
	}
	
	@Deprecated
	public static float getFloatFromBytes(byte[] array) {
		if(array.length!=4){
			throw new IllegalArgumentException("Array isnt 2 bytes long");
		}
		ByteBuffer bbufint = ByteBuffer.allocate(4);
		bbufint.put(array);
		bbufint.flip();
		float retvar = bbufint.getFloat();
		bbufint = null;
		return retvar;
	}
	
	@Deprecated
	public static double getDoubleFromBytes(byte[] array) {
		if(array.length!=8){
			throw new IllegalArgumentException("Array isnt 2 bytes long");
		}
		ByteBuffer bbufint = ByteBuffer.allocate(8);
		bbufint.put(array);
		bbufint.flip();
		double retvar = bbufint.getDouble();
		bbufint = null;
		return retvar;
	}
	
	@Deprecated
	public static byte[] getBytesForInt(int counter){
		byte[] length = new byte[4];
		ByteBuffer bbufint = ByteBuffer.allocate(4);
		bbufint.putInt(counter);
		bbufint.flip();
		bbufint.get(length);
		bbufint = null;
		return length;
	}
	
	@Deprecated
	public static byte[] getBytesForLong(long counter){
		byte[] length = new byte[8];
		ByteBuffer bbufLong = ByteBuffer.allocate(8);
		bbufLong.putLong(counter);
		bbufLong.flip();
		bbufLong.get(length);
		bbufLong = null;
		return length;
	}
	
	@Deprecated
	public static byte[] getBytesForShort(short counter){
		byte[] length = new byte[2];
		ByteBuffer bbufLong = ByteBuffer.allocate(2);
		bbufLong.putShort(counter);
		bbufLong.flip();
		bbufLong.get(length);
		bbufLong = null;
		return length;
	}
	
	@Deprecated
	public static byte[] getBytesForFloat(float counter){
		byte[] length = new byte[4];
		ByteBuffer bbufLong = ByteBuffer.allocate(4);
		bbufLong.putFloat(counter);
		bbufLong.flip();
		bbufLong.get(length);
		bbufLong = null;
		return length;
	}
	
	@Deprecated
	public static byte[] getBytesForDouble(double counter){
		byte[] length = new byte[8];
		ByteBuffer bbufLong = ByteBuffer.allocate(8);
		bbufLong.putDouble(counter);
		bbufLong.flip();
		bbufLong.get(length);
		bbufLong = null;
		return length;
	}
	
	@Deprecated
	public static String madeByteString(byte[] array){
		String result = "";
		for(byte b : array){
			result = result + ";" + String.valueOf(b);
		}
		result = result.substring(1);
		return result;
	}
	
	@Deprecated
	public static byte[] madeByteArrayFromByteString(String byte_string){
		String[] splits = byte_string.split(";");
		byte[] bytes = new byte[splits.length];
		for(int i=0;i<splits.length;i++){
			bytes[i] = Byte.parseByte(splits[i]);
		}
		return bytes;
	}

}
