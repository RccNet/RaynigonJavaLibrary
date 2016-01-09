package com.raynigon.lib.database.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import com.raynigon.lib.io.IOUtils;
import com.raynigon.lib.json.JSONArray;
import com.raynigon.lib.json.JSONObject;


/**Generated on 04.11.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * Represents a Database Check Skript, this Skripts are in JSON Format and are able to upgrade Databases.
 * A registered Skript will be executed before any other requests were made.
 * @author Simon Schneider
 */
public class DatabaseCheckSkript {

	private String name;
	private int version;
	private String[] tables;
	
	private List<TableFormat> tableFormats;
	
	private DatabaseCheckSkript(){
		tableFormats = new LinkedList<TableFormat>();
	}
	
	public String getName() {
		return name;
	}
	
	public int getVersion(){
		return version;
	}
	
	public static DatabaseCheckSkript parseSkript(InputStream is) throws IOException{
		StringWriter writer = new StringWriter();
		IOUtils.copy(is, writer, StandardCharsets.UTF_8);
		return parseJsonString(writer.toString());
	}
	
	public static DatabaseCheckSkript parseSkript(File inFile) throws IOException {
		byte[] content = Files.readAllBytes(inFile.toPath());
		String jsonStr = new String(content, StandardCharsets.UTF_8);
		return parseJsonString(jsonStr);
	}
	
	private static DatabaseCheckSkript parseJsonString(String inStr){
		JSONObject obs = new JSONObject(inStr);
		DatabaseCheckSkript skript = new DatabaseCheckSkript();
		writeMeta(obs.getJSONObject("META_DATA"), skript);
		JSONObject tables = obs.getJSONObject("TABLES");
		for(String key : tables.keySet()){
			skript.tableFormats.add(parseTable(key, tables.getJSONArray(key)));
		}
		//prepared SQL Statements for executing before an upgrade, after a Upgrade was made and at start and at stop.
		//JSONObject defaultSQL = obs.getJSONObject("DEFAULT_SQL");
		return skript;
	}

	private static TableFormat parseTable(String key, JSONArray arr) {
		TableFormat tf = new TableFormat(key, arr.length());
		JSONObject line;
		for(int i=0;i<arr.length();i++){
			line = arr.getJSONObject(i);
			if(line.getString("length").equalsIgnoreCase("")) line.put("length", "-1");
			if(line.getString("primary").equalsIgnoreCase("")) line.put("primary", "false");
			tf.setColumnData(i, line.getString("name"), line.getString("type"), line.getInt("length"), line.getBoolean("primary"));
		}
		return tf;
	}

	private static void writeMeta(JSONObject metaData, DatabaseCheckSkript skript) {
		skript.name = metaData.getString("name");
		skript.version = metaData.getInt("version");
		JSONArray arr = metaData.getJSONArray("affectedTables");
		skript.tables = new String[arr.length()];
		for(int i=0;i<skript.tables.length;i++){
			skript.tables[i] = arr.getString(i);
		}
	}
}
