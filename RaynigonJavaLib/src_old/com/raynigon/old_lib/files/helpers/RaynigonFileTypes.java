package com.raynigon.old_lib.files.helpers;

/**
 * @author Simon Schneider
 *
 */
public enum RaynigonFileTypes {

	PLAIN(0),
	KEY_VALUE(1),
	XML(2);
	
	private int type = 0;
	
	/**Creates a new RaynigonFileTypes
	 * 
	 */
	private RaynigonFileTypes(int ttype) {
		type = ttype;
	}
}
