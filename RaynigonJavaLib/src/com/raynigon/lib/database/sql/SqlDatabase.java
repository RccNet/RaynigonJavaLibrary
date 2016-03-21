package com.raynigon.lib.database.sql;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import com.raynigon.lib.database.utils.DatabaseBulkWriter;
import com.raynigon.lib.json.JSONArray;


/**Generated on 09.09.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * This is an abstraction of an SqlDatabase, it can be used to connect to different databases without changing source code
 * @author Simon Schneider
 */
/**
 * @author Simon Schneider
 *
 */
/**
 * @author Simon Schneider
 *
 */
public abstract class SqlDatabase{
	
	/**Creates a SqlDatabase Object from an Properties Object
	 * <p>
	 * <p>
	 * <p>
	 * Following Properties should be set:
	 * <table>
	 * <colgroup>
	 * <col width="60%" />
	 * <col width="40%" />
	 * </colgroup>
	 * <tr>
	 * <th>Property</th>
	 * <th>Comment</th>
	 * </tr>
	 * <tr><td>type = [ sqllite | mysql | postgresql ]</td><td>Use one of them</td></tr>
	 * <tr><td>path = some_path/sql_lite_db.sqlite</td><td>the path to the sqllite database file</td></tr>
	 * <tr><td>host = 123.123.123.123</td><td>the hostname of the mysql/postgresql server</td></tr>			
	 * <tr><td>username = my_user</td><td>the user of the mysql/postgresql server</td></tr>
	 * <tr><td>password = my_pwd</td><td>the password for the user, named above</td></tr>
	 * <tr><td>database = my_db</td><td>the name of the Database which should be used</td></tr>
	 * <tr><td>port = 12345</td><td>the port of the mysql/postgresql server (this is optional)</td></tr>
	 * <tr><td>logger = DatabaseLogger</td><td>the name of the logger on which should be logged, null is default (this is optional)</td></tr>
	 * </table>
	 * @param settings		The Properties Object from which the data should be read
	 * @return	an {@link SqlDatabase} according to the informations given by the Properties Object
	 * @throws SQLException			thrown if an error occurs during the creation of a Database
	 * @throws UnknownHostException		thrown if a mysql/postgresql hostname was unable to find
	 */
	//TODO refactor this Method its ugly
	public static SqlDatabase createFromProperties(Properties settings) throws SQLException, UnknownHostException{
		if(settings==null){
			throw new NullPointerException("Properties object is null");
		}
		String loggerName = settings.getProperty("logger", "null");
		Logger logger = null;
		if(!loggerName.equalsIgnoreCase("null"))
			logger = Logger.getLogger(loggerName);
		String type = settings.getProperty("type", "sqllite").toLowerCase();
		if(type.equalsIgnoreCase("sqllite")){
			File path = new File(settings.getProperty("path", "sql_lite_db.slite").toLowerCase());
			SQLiteDatabase litedb = new SQLiteDatabase();
			litedb.setLogger(logger);
			litedb.openDatabase(path);
			return litedb;
		}else if(type.equalsIgnoreCase("mysql")){
			String ipAddrText = settings.getProperty("host", "localhost");
			InetAddress inetaddr = InetAddress.getByName(ipAddrText);
			String username = settings.getProperty("username", "user");
			String password = settings.getProperty("password", "passwd");
			String db_name = settings.getProperty("database", "my_database");
			int port = Integer.parseInt(settings.getProperty("port", "3306")); 
			MySqlDatabase mydb = new MySqlDatabase();
			mydb.setLogger(logger);
			mydb.connect(inetaddr, db_name, username, password, port);
			return mydb;
		}else if(type.equalsIgnoreCase("postgresql")){
			throw new SQLException("Not Supported");
		}else{
			return null;
		}
	}
	
	
	/**This shows the Connection Status, if a Connection is corrupt
	 * this Status will set to false and all income requests will wait until the Connection works again
	 */
	protected boolean connectionStatus = false;
	
	/**The Connection which is made by the subclass
	 */
	protected Connection conn = null;

	
	/** The Logger for the Database
	 */
	protected Logger log;
	
	
	/** This Method executes a query
	 * @param query		The query which should be executed
	 * @return			The ResultSet which is return by the execution. If return is null there is no result  
	 * @throws SQLException 	Is thrown if an error occurs while executing the query
	 */
	public ResultSet runQuery(String query) throws SQLException{
		checkConnectionStatus();
		Statement st = conn.createStatement();
		ResultSet rs = null;
		try{
			if (st.execute(query)) {
			       rs = st.getResultSet();
			}
		}catch(Throwable t){
			checkConnection();
			throw new SQLException(t);
		}
		return rs;
	}

	/** This Method executes a PreparedStatment
	 * @param ps		The PreparedStatment which should be executed
	 * @return			The ResultSet which is return by the execution. If return is null there is no result  
	 * @throws SQLException 	Is thrown if an error occurs while executing the query
	 */
	public ResultSet runQuery(PreparedStatement ps) throws SQLException{
		checkConnectionStatus();
		ResultSet rs = null;
		try{
			if (ps.execute()) {
		        rs = ps.getResultSet();
		    }
		}catch(Throwable t){
			checkConnection();
			throw new SQLException(t);
		}
		return rs;	
	}
	
	
	/**This Method creates an new PreparedStatment
	 * @param query	The Query from which the PreparedStatment should be created from
	 * @return		The PreparedStatment which was created
	 * @throws SQLException is thrown if an error occurs while creating the PreparedStatment
	 */
	public PreparedStatement createPreparedStatment(String query) throws SQLException{
		checkConnectionStatus();
		try{
			return conn.prepareStatement(query);
		}catch(Throwable t){
			checkConnection();
			throw new SQLException(t);
		}
	}
	
	/** Closes the Connection
	 * @throws Exception	is thrown if an error occurs while closing the connection
	 */
	public abstract void close() throws Exception;
	
	/**Closes the Connection Internal, 
	 * this has to be called in the implementation of the Close method
	 * @throws Exception
	 */
	protected void connectionClose() throws Exception{
		if(!conn.isClosed()){
			conn.close();
		}
	}
	
	/**This Method check if the Connection is valid
	 * if not it will wait until the Connection Status is true
	 */
	private void checkConnectionStatus() {
		try{
			while(!connectionStatus){
				Thread.sleep(250);
			}
		}catch(InterruptedException e){
			Thread.currentThread().interrupt();
		}
	}
	
	/**This Method should be called if an error occurs which source
	 * could a connection loss be.
	 */
	protected abstract void checkConnection();
	
	/**This Method is for intern work it is called when  query is executed
	 */
	protected abstract void queryRunned();
	
	/**This Method reconnects if the database is corrupt or damaged
	 * it also has to set the Connection Status to true, if the connection aborted
	 */
	protected abstract void reconnect();
	
	@Override
	public void finalize() throws Exception{
		if(!conn.isClosed()){
			close();
		}
	}

	/**Logs a Info Message to Console
	 * @param message	the message which should be logged
	 * @deprecated Just use the inherited log attribute
	 */
	@Deprecated
	protected void logInfo(String message) {
		if(log==null)
			System.out.println("[Info]:"+message);
		else
			log.log(Level.INFO, "SqlDatabase", message);
	}
	
	/**Logs an Exception to Console
	 * @param exception	the Exception which should be logged
	 */
	protected void logException(Exception exception) {
		exception.printStackTrace(System.err);
	}
	
	
	/** Writes a Bulk of Data into the Database
	 * @param root				An XML Document containing the Data for the Bulk Insert
	 * @throws SQLException		thrown if an error occurs during the Bulk Insert
	 */
	//TODO add Format Specifiers in the JavaDoc
	public void writeDataBulk(Document root) throws SQLException{
		DatabaseBulkWriter.writeData(this, root);
	}
	
	/** Writes a Bulk of Data into the Database
	 * @param tables	an Array of JSON Objects representing a DB Table
	 * @throws SQLException		thrown if an error occurs during the Bulk Insert
	 */
	//TODO add Format Specifiers in the JavaDoc
	public void writeDataBulk(JSONArray tables) throws SQLException{
		DatabaseBulkWriter.writeData(this, tables);
	}
	
	/**Sets the Logger for this Database Interface, in default no logger will be used and everything will be send to the Standard Output Stream
	 * @param inLogger			The Logger on which will be logged, null if no logger should be used
	 */
	public void setLogger(Logger inLogger){
		log = inLogger;
	}
}
