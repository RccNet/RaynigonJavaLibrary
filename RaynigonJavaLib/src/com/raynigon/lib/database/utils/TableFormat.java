package com.raynigon.lib.database.utils;

/**Generated on 04.11.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * This Class contains all meta data for a SQL Table
 * @author Simon Schneider
 */
public class TableFormat {

	private String name;
	private String[] columnNames;
	private String[] columnType;
	private int[] columnLength;
	private boolean[] columnPrimary;

	/**Creates a new Object with the name of the table and the amount of columns as parameters
	 * @param key		the name of the table
	 * @param length	the amount of columns
	 */
	public TableFormat(String key, int length) {
		name = key;
		columnNames = new String[length];
		columnType = new String[length];
		columnLength = new int[length];
		columnPrimary = new boolean[length];
	}
	
	/** Returns the Table name
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**Inserts the Meta Data for a specified column
	 * @param index			the index of the column
	 * @param inName		the name of the column
	 * @param inType		the type of the column
	 * @param inLength		the length of the data saved in the column
	 * @param inPrimary		true if the column is a primary key, false if not
	 */
	protected void setColumnData(int index, String inName, String inType, int inLength, boolean inPrimary){
		if(index>=columnLength.length) return;//Eventually throw Exception here
		columnNames[index] 		= inName;
		columnType[index] 		= inType;
		columnLength[index] 	= inLength;
		columnPrimary[index]	= inPrimary;
	}

}
