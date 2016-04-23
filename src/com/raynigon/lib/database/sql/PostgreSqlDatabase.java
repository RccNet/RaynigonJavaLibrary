package com.raynigon.lib.database.sql;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import org.postgresql.Driver;

/**Generated on 09.09.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * The PostgreSqlDatabase is an implementation of the PostgreSql Java Database Connector (JDBC)
 * @author Simon Schneider
 */
public class PostgreSqlDatabase extends SqlDatabase {

	private Timer connection_timer = new Timer();
	private Timer reconnect_timer = new Timer();
	private long last_query = 0;
	private boolean reconnect_sheduled = false;

	private InetAddress address = null;
	private String dbname = null;
	private String username = null;
	private String password = null;
	private int port = 0;


	@Override
	public void close() throws Exception{
		connection_timer.cancel();
		reconnect_timer.cancel();
		connection_timer.purge();
		reconnect_timer.purge();
		connection_timer = null;
		reconnect_timer = null;
		connectionClose();
	}

	/** This Method start a connection to an PotgreServer
	 * @param address		The InetAdress which is used for the Connection
	 * @param dbname		The Name of the Database which should be connected to
	 * @param username		The User Name which should be used for this connection
	 * @param password		The password of the User which should be used for this connection
	 * @throws SQLException Thrown if an error during the connecting occurs
	 */
	public void connect(InetAddress address, String dbname, String username, String password) throws SQLException{
		connect(address, dbname, username, password, 3306);
	}

	/** This Method start a connection to an PotgreServer
	 * @param taddress		The InetAdress which is used for the Connection
	 * @param tdbname		The Name of the Database which should be connected to
	 * @param tusername		The User Name which should be used for this connection
	 * @param tpassword		The password of the User which should be used for this connection
	 * @param tport			The port of the MysqlServer which should be used
	 * @throws SQLException Thrown if an error during the connecting occurs
	 */
	public void connect(InetAddress taddress, String tdbname, String tusername, String tpassword, int tport) throws SQLException{
		try {
			address = taddress;
			dbname = tdbname;
			username = tusername;
			password = tpassword;
			port = tport;
			if(conn!=null){
				if(!conn.isClosed()){
					throw new IOException("Connection is in use");
				}
			}
			new Driver();
			conn = DriverManager.getConnection("jdbc:postgresql://"+address.getHostAddress()+":"+String.valueOf(port)+"/"+dbname, username, password);
			connectionStatus = true;
			connection_timer.schedule(new TimerTask(){
				@Override
				public void run() {
					long now = System.currentTimeMillis();
					if((now-last_query)>3000){
						checkConnection();
					}
				}
			}, 1000, 15000);
			logInfo("Connected to Database");
		} catch (Exception e) {
			throw new SQLException("Error while building PostgreSql connection", e);
		}
	}

	@Override
	public void reconnect(){
		reconnect_sheduled = false;
		if(address==null){
			throw new NullPointerException("Reconnect is only available if the Database was connect at least once");
		}
		try {
			if(!conn.isClosed()){
				conn.close();
			}
			connect(address, dbname, username, password, port);
			logInfo("Reconnected to Database");
		} catch (Exception e) {
			logException(e);
			if(!reconnect_sheduled){
				reconnect_sheduled = true;
				reconnect_timer = new Timer();
				reconnect_timer.schedule(new TimerTask(){public void run() {reconnect();}}, 1000);
			}
		}
	}

	@Override
	protected void checkConnection() {
		try {
			conn.createStatement().executeQuery("SHOW FULL TABLES");
			queryRunned();
		} catch (Exception e) {
			connectionStatus = false;
			logException(e);
			if(!reconnect_sheduled){
				reconnect();
			}
		}
	}

	@Override
	protected void queryRunned() {
		last_query = System.currentTimeMillis();
	}
}
