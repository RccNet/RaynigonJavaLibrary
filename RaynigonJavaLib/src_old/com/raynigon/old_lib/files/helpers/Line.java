package com.raynigon.old_lib.files.helpers;

/**
 * @author Simon Schneider
 *
 */
public class Line {

	public enum LineType{
		Comment,
		KeyValuePair;
		//Group
	}
	
	private String key = null;
	private String value = null;
	private String line = null;
	private LineType lt = null;
	
	public Line(String tline, LineType tlt){
		line = tline;
		lt = tlt;
	}
	
	public Line(String tkey,String tvalue, LineType tlt){
		key = tkey;
		value = tvalue;
		lt = tlt;
	}
	
	public String getKey(){
		return key;
	}
	
	@Override
	public String toString() {
		if(lt==LineType.Comment){
			return line+"\n";
		}else{
			return key+"="+value+"\n";
		}
	}
}
