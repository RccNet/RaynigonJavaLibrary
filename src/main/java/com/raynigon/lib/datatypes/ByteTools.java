package com.raynigon.lib.datatypes;

/** The Byte Tools Class allows you to create byte arrays from simple data types and simple data types from byte arrays
 * @author Simon Schneider
 * @since 12.05.16
 * @version 0.0.2
 */
public class ByteTools {

	/**
	 * Represents the Size of a Byte
	 */
	private static final int SIZE_OF_BYTE = 8;
	
	
	/**Converts an array of bytes into a long, the length of the array has to be 8 or greater.
	 * if the array has more than 8 elements only the first 8 will be used
	 * @param array		the array which should be converted
	 * @return			the long value of input array
	 * @throws ByteShiftingException 	is thrown if the array is too short
	 */
	public static long getLong(byte[] array){
		if(array.length<8) throw new ByteShiftingException("Array is too short 8 Bytes needed");
		long value 	= (array[0] & 0xFFL) << (7 * SIZE_OF_BYTE)
		    		| (array[1] & 0xFFL) << (6 * SIZE_OF_BYTE)
		    		| (array[2] & 0xFFL) << (5 * SIZE_OF_BYTE)
		    		| (array[3] & 0xFFL) << (4 * SIZE_OF_BYTE)
		    		| (array[4] & 0xFFL) << (3 * SIZE_OF_BYTE)
		    		| (array[5] & 0xFFL) << (2 * SIZE_OF_BYTE)
		    		| (array[6] & 0xFFL) << (1 * SIZE_OF_BYTE)
		    		| (array[7] & 0xFFL);
	    return value;
	}
	
	/**Converts an array of bytes into an integer, the length of the array has to be 4 or greater.
	 * if the array has more than 4 elements only the first 4 will be used
	 * @param array		the array which should be converted
	 * @return			the integer value of input array
	 * @throws ByteShiftingException 	is thrown if the array is too short
	 */
	public static int getInt(byte[] array){
		if(array.length<4) throw new ByteShiftingException("Array is too short 4 Bytes needed");
		int value 	= (array[0] & 0x000000FF) << (3 * SIZE_OF_BYTE)
	    			| (array[1] & 0x000000FF) << (2 * SIZE_OF_BYTE)
	    			| (array[2] & 0x000000FF) << (1 * SIZE_OF_BYTE)
	    			| (array[3] & 0x000000FF);
	    return value;
	}
	
	/**Converts an array of bytes into a long, the length of the array has to be 2 or greater.
	 * if the array has more than 2 elements only the first 2 will be used
	 * @param array		the array which should be converted
	 * @return			the long value of input array
	 * @throws ByteShiftingException 	is thrown if the array is too short
	 */
	public static short getShort(byte[] array) {
		if(array.length<2) throw new ByteShiftingException("Array is too short 2 Bytes needed");
		return (short) ((array[1] << SIZE_OF_BYTE) 
					  + (array[0] & 0xFF));
	}
	
	/**Converts an array of bytes into a float, the length of the array has to be 4 or greater.
	 * if the array has more than 4 elements only the first 4 will be used
	 * @param array		the array which should be converted
	 * @return			the float value of input array
	 * @throws ByteShiftingException 	is thrown if the array is too short
	 */
	public static float getFloat(byte[] array) {
		return Float.intBitsToFloat(getInt(array));
	}
	
	/**Converts an array of bytes into a double, the length of the array has to be 8 or greater.
	 * if the array has more than 8 elements only the first 8 will be used
	 * @param array		the array which should be converted
	 * @return			the double value of input array
	 * @throws ByteShiftingException 	is thrown if the array is too short
	 */
	public static double getDouble(byte[] array) {
		return Double.longBitsToDouble(getLong(array));
	}
	
	
	/**Converts an integer to a byte array
	 * @param value	the value which should be converted
	 * @return	the byte array with the length 4
	 */
	public static byte[] getBytes(int value){
		byte[] array = new byte[4];
		array[0] = (byte) (value >> (3 << 3));
		array[1] = (byte) (value >> (2 << 3));
		array[2] = (byte) (value >> (1 << 3));
		array[3] = (byte) (value >> 0);
		return array;
	}
	
	/**Converts an long to a byte array
	 * @param 	value	the value which should be converted
	 * @return	the byte array with the length 8
	 */
	public static byte[] getBytes(long value){
		byte[] array = new byte[8];
		array[0] = (byte) (value >> (7 << 3));
		array[1] = (byte) (value >> (6 << 3));
		array[2] = (byte) (value >> (5 << 3));
		array[3] = (byte) (value >> (4 << 3));
		array[4] = (byte) (value >> (3 << 3));
		array[5] = (byte) (value >> (2 << 3));
		array[6] = (byte) (value >> (1 << 3));
		array[7] = (byte) (value >> 0);
		return array;
	}
	
	/**Converts a short to a byte array
	 * @param 	value	the value which should be converted
	 * @return	the byte array with the length 2
	 */
	public static byte[] getBytes(short value){
		byte[] array = new byte[2];
		array[0] = (byte) (value >> (1 << 3));
		array[1] = (byte) (value >> 0);
		return array;
	}
	
	/**Converts a float to a byte array
	 * @param 	value	the value which should be converted
	 * @return	the byte array with the length 4
	 */
	public static byte[] getBytes(float value){
		return getBytes(Float.floatToIntBits(value));
	}
	
	
	/**Converts a double to a byte array
	 * @param 	value	the value which should be converted
	 * @return	the byte array with the length 8
	 */
	public static byte[] getBytes(double value){
		return getBytes(Double.doubleToLongBits(value));
	}
}
