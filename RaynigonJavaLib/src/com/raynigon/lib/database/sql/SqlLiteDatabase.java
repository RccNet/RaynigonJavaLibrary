package com.raynigon.lib.database.sql;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.sqlite.JDBC;

/**Generated on 09.09.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * The SqlLiteDatabase is an implementation of the SqlLite Java Database Connector (JDBC)
 * @author Simon Schneider
 */
public class SqlLiteDatabase extends SqlDatabase {
	
	private File f = null;
	
	/**This Method opens a sqllite Database from an File
	 * @param tf				The File From which the Database should be opened from
	 * @throws SQLException	is thrown if an error occurs while opening the database
	 */
	public void openDatabase(File tf) throws SQLException{
		f = tf;
		new JDBC();
		conn = DriverManager.getConnection("jdbc:sqllite:"+f.getAbsolutePath());
		connectionStatus = true;
		logInfo("SQLLite Database is ready");
	}

	@Override
	protected void checkConnection() {}	
	
	@Override
	protected void queryRunned() {}

	@Override
	public void reconnect() {}

	@Override
	public void close() throws Exception {
		connectionClose();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss");
		logInfo("SQLLite Database File was modified: "+sdf.format(new Date(f.lastModified())));
	}

	
}
