package com.raynigon.lib.datatypes;


public class NumbersExtension {

	public static boolean canParseByte(String arg0){
		try{
			Byte.parseByte(arg0);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}
	
	public static boolean canParseShort(String arg0){
		try{
			Short.parseShort(arg0);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}

	public static boolean canParseInt(String arg0){
		try{
			Integer.parseInt(arg0);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}

	public static boolean canParseLong(String arg0){
		try{
			Long.parseLong(arg0);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}
	
	public static boolean canParseFloat(String arg0){
		try{
			Float.parseFloat(arg0);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}
	
	public static boolean canParseDouble(String arg0){
		try{
			Double.parseDouble(arg0);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}
	
}
