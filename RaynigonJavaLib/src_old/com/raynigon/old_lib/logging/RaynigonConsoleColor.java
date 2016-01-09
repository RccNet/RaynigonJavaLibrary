package com.raynigon.old_lib.logging;


/**This is just a Enum which represents the Colors,
 * which are able in an standard ascii console
 * @author Simon Schneider
 *
 */
public enum RaynigonConsoleColor {

	/**
	 * Dummy Comment
	 */
	BLACK("0;0;30"),
	/**
	 * Dummy Comment
	 */
	BLUE("0;0;34"),
	/**
	 * Dummy Comment
	 */
	GREEN("0;0;32"),
	/**
	 * Dummy Comment
	 */
	CYAN("0;0;36"),
	/**
	 * Dummy Comment
	 */
	RED("0;0;31"),
	/**
	 * Dummy Comment
	 */
	PURPLE("0;0;35"),
	/**
	 * Dummy Comment
	 */
	BROWN("0;0;33"),
	/**
	 * Dummy Comment
	 */
	LIGHT_GRAY("0;0;37"),
	/**
	 * Dummy Comment
	 */
	DARK_GRAY("0;1;30"),
	/**
	 * Dummy Comment
	 */
	LIGHT_BLUE("0;1;34"),
	/**
	 * Dummy Comment
	 */
	LIGHT_GREEN("0;1;32"),
	/**
	 * Dummy Comment
	 */
	LIGHT_CYAN("0;1;36"),
	/**
	 * Dummy Comment
	 */
	LIGHT_RED("0;1;31"),
	/**
	 * Dummy Comment
	 */
	LIGHT_PURPLE("0;1;35"),
	/**
	 * Dummy Comment
	 */
	YELLOW("0;1;33"),
	/**
	 * Dummy Comment
	 */
	WHITE("0;1;37"),
	
	/**
	 * Dummy Comment
	 */
	RESET("0;0;0");
	
	private String color = null;
	
	private RaynigonConsoleColor(String tcolor){
		color = "\033["+tcolor+"m";
	}
	
	/**This Method returns the saved Ascii Color
	 * @return the saved Ascii Color
	 */
	public String getAsciiColor(){
		return color;
	}
}
