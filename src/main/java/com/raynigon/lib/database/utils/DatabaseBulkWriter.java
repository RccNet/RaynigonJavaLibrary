package com.raynigon.lib.database.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.raynigon.lib.database.sql.SqlDatabase;

public class DatabaseBulkWriter {

	public static void writeData(SqlDatabase inDatabase, Document inDocument) throws SQLException{
		Element root = inDocument.getDocumentElement();
		NodeList tableNodeList = root.getElementsByTagName("Table");
		List<Table> tables = createTablesFromXML(tableNodeList);
		bulkInsert(tables, inDatabase);
	}

	//TODO Refactor
	public static void writeData(SqlDatabase inDatabase, JSONArray inTables) throws SQLException{
		List<Table> tables = new ArrayList<DatabaseBulkWriter.Table>(inTables.length());
		JSONObject jsonTable;
		JSONObject jsonDs;
		for(Object obj : inTables){
			if(!(obj instanceof JSONObject))
				continue;
			
			jsonTable = (JSONObject) obj;
			Table table = new Table(jsonTable.getString("name"));
			for(Object data : jsonTable.getJSONArray("data")){
				if(!(obj instanceof JSONObject))
					continue;
				
				jsonDs = (JSONObject) data;
				DataSet ds = new DataSet();
				
				for(String key : jsonDs.keySet())
					ds.fields.add(new Pair(key, jsonDs.getString(key)));
			}
		}
		
		bulkInsert(tables, inDatabase);
	}
	
	private static void bulkInsert(List<Table> tables, SqlDatabase inDb) throws SQLException {
		for(Table table : tables){
			for(DataSet ds : table.dataSets){
				StringBuilder query = new StringBuilder("INSERT INTO "+table.name+" (");
				for(Pair p : ds.fields)
					query.append("`"+p.first+"`,");
				
				query.setCharAt(query.length()-1, ')');
				query.append(" VALUES (");
				
				for(Pair p : ds.fields)
					query.append("\""+p.second+"\",");
				
				query.setCharAt(query.length()-1, ')');
				inDb.runQuery(query.toString());
			}
		}
	}
	
	private static List<Table> createTablesFromXML(NodeList inNl) {
		List<Table> tables = new ArrayList<DatabaseBulkWriter.Table>(inNl.getLength());
		Element xmlTable;
		for(int i=0;i<inNl.getLength();i++){
			if(! (inNl.item(i) instanceof Element))
				continue;
			
			xmlTable = ((Element) inNl.item(i));
			if(xmlTable.getAttribute("name").equalsIgnoreCase(""))
				continue;
			
			Table table = new Table(xmlTable.getAttribute("name"));
			insertDataSetsFromXML(table, xmlTable.getChildNodes());
			if(table.dataSets.size()==0)
				continue;
			
			tables.add(table);
		}
		return tables;
	}
	
	
	private static void insertDataSetsFromXML(Table table, NodeList inNl) {
		Element xmlDataSet;
		for(int i=0;i<inNl.getLength();i++){
			if(! (inNl.item(i) instanceof Element))
				continue;
	
			xmlDataSet = ((Element) inNl.item(i));
			DataSet ds = new DataSet();
			insertFieldsFromXML(ds, xmlDataSet.getChildNodes());
			if(ds.fields.size()==0)
				continue;
			
			table.dataSets.add(ds);
		}
	}


	private static void insertFieldsFromXML(DataSet ds, NodeList inNl) {
		Element xmlField;
		for(int i=0;i<inNl.getLength();i++){
			if(! (inNl.item(i) instanceof Element))
				continue;
	
			xmlField = ((Element) inNl.item(i));
			if(xmlField.getAttribute("name").equalsIgnoreCase(""))
				continue;
			
			if(xmlField.getTextContent().equalsIgnoreCase(""))
				continue;
			
			ds.fields.add(new Pair(xmlField.getAttribute("name"), xmlField.getTextContent()));
		}
	}


	static class Table{
		public String name;
		public List<DataSet> dataSets = new ArrayList<DataSet>();
		public Table(String inName) {name = inName;}
	}
	
	static class DataSet{
		public List<Pair> fields = new ArrayList<Pair>();
	}
	
	static class Pair{
		public Pair(String inFirst, String inSecond) {
			first = inFirst;
			second = inSecond;
		}
		public String first;
		public String second;
	}
}
