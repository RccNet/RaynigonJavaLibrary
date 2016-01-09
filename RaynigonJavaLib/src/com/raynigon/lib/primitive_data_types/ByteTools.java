package com.raynigon.lib.primitive_data_types;



public class ByteTools {

	public static long getLongFromBytes(byte[] array){
		return 0;
	}
	
	public static int getIntFromBytes(byte[] array){
		if(array.length<4) throw new ByteShiftingException("Array is too Short(4 needed(");
		int value = 0;
	    for (int i = 0; i < 4; i++) {
	        value += (array[i] & 0x000000FF) << ((4 - 1 - i) * 8);
	    }
	    return value;
	}
	
	public static short getShortFromBytes(byte[] array) {
		return 0;
	}
	
	public static float getFloatFromBytes(byte[] array) {
		return Float.NaN;
	}
	
	public static double getDoubleFromBytes(byte[] array) {
		return Double.NaN;
	}
	
	public static byte[] getBytesForInt(int counter){
		return null;
	}
	
	public static byte[] getBytesForLong(long counter){
		return null;
	}
	
	public static byte[] getBytesForShort(short counter){
		return null;
	}
	
	public static byte[] getBytesForFloat(float counter){
		return null;
	}
	
	public static byte[] getBytesForDouble(double counter){
		return null;
	}
	
	public static String madeByteString(byte[] array){
		return null;
	}
	
	public static byte[] madeByteArrayFromByteString(String byte_string){
		return null;
	}
}
