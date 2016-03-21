package com.raynigon.lib.primitive_data_types;

import com.raynigon.old_lib.tools.ByteArrayCaster;

public class ByteTools {

	public static long getLongFromBytes(byte[] array){
		return ByteArrayCaster.getLongFromBytes(array);
	}
	
	public static int getIntFromBytes(byte[] array){
		if(array.length<4) throw new ByteShiftingException("Array is too short 4 Bytes needed");
		int value = 0;
	    for (int i = 0; i < 4; i++) 
	        value += (array[i] & 0x000000FF) << ((4 - 1 - i) * 8);
	    return value;
	}
	
	public static short getShortFromBytes(byte[] array) {
		return ByteArrayCaster.getShortFromBytes(array);
	}
	
	public static float getFloatFromBytes(byte[] array) {
		return ByteArrayCaster.getFloatFromBytes(array);
	}
	
	public static double getDoubleFromBytes(byte[] array) {
		return ByteArrayCaster.getDoubleFromBytes(array);
	}
	
	public static byte[] getBytesForInt(int counter){
		return ByteArrayCaster.getBytesForInt(counter);
	}
	
	public static byte[] getBytesForLong(long counter){
		return ByteArrayCaster.getBytesForLong(counter);
	}
	
	public static byte[] getBytesForShort(short counter){
		return ByteArrayCaster.getBytesForShort(counter);
	}
	
	public static byte[] getBytesForFloat(float counter){
		return ByteArrayCaster.getBytesForFloat(counter);
	}
	
	public static byte[] getBytesForDouble(double counter){
		return ByteArrayCaster.getBytesForDouble(counter);
	}
	
	public static String madeByteString(byte[] array){
		return ByteArrayCaster.madeByteString(array);
	}
	
	public static byte[] madeByteArrayFromByteString(String byte_string){
		return ByteArrayCaster.madeByteArrayFromByteString(byte_string);
	}
}
