package com.meiliwan.emall.commons.log;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.db.names.DBNameResolver;
import ch.qos.logback.classic.db.names.DefaultDBNameResolver;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.db.DBAppenderBase;
import ch.qos.logback.core.db.DBHelper;

import com.meiliwan.emall.commons.util.BaseConfig;
import com.meiliwan.emall.commons.util.NetworkUtils;
import com.meiliwan.emall.commons.util.StringUtil;

/**
 * The DBAppender inserts logging events into three database tables in a format
 * independent of the Java programming language.
 * 
 * For more information about this appender, please refer to the online manual
 * at http://logback.qos.ch/manual/appenders.html#DBAppender
 * 
 * @author Ceki G&uuml;lc&uuml;
 * @author Ray DeCampo
 * @author S&eacute;bastien Pennec
 */
public class DBAppender extends DBAppenderBase<ILoggingEvent> {
	protected String insertPropertiesSQL;
	protected String insertExceptionSQL;
	protected String insertSQL;
	protected static final Method GET_GENERATED_KEYS_METHOD;
	private static String localIP;
	private DBNameResolver dbNameResolver;
	
	
	static final int LEVEL_INDEX = 1;
	static final int INFO_INDEX = 2;
	static final int REMARK_INDEX = 3;
	static final int SERVER_IP_INDEX = 4;
	static final int CLIENT_IP_INDEX = 5;
	static final int MODULE_INDEX = 6;
	static final int APPLICATION_INDEX = 7;
	static final int CREATE_TIME_INDEX = 8;
	static final int TITLE_INDEX = 9;
	static final String PROJECT_NAME = "projectName";

	static {
		// PreparedStatement.getGeneratedKeys() method was added in JDK 1.4
		Method getGeneratedKeysMethod;
		try {
			// the
			getGeneratedKeysMethod = PreparedStatement.class.getMethod(
					"getGeneratedKeys", (Class[]) null);
		} catch (Exception ex) {
			getGeneratedKeysMethod = null;
		}
		GET_GENERATED_KEYS_METHOD = getGeneratedKeysMethod;

		try {
			localIP = NetworkUtils.getFirstNoLoopbackAddress();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setDbNameResolver(DBNameResolver dbNameResolver) {
		this.dbNameResolver = dbNameResolver;
	}

	@Override
	public void start() {
		if (dbNameResolver == null)
			dbNameResolver = new DefaultDBNameResolver();
		super.start();
		System.out.println("Logger DBAppender start");
		addInfo("Logger DBAppender start");
	}

	@Override
	public void append(ILoggingEvent eventObject) {
		if(eventObject.getLevel().toString().equals(Level.DEBUG.levelStr)){
			return ;
		}
		Connection connection = null;
		try {
			connection = connectionSource.getConnection();
			connection.setAutoCommit(false);
			PreparedStatement insertStatement;
			insertStatement = connection.prepareStatement(getInsertSQL());

			// inserting an event and getting the result must be exclusive
			synchronized (this) {
				subAppend(eventObject, connection, insertStatement);
				// eventId = selectEventId(insertStatement, connection);
			}
			// secondarySubAppend(eventObject, connection, eventId);

			// we no longer need the insertStatement
			close(insertStatement);

			connection.commit();

		} catch (Throwable sqle) {
			addError("problem appending event", sqle);
		} finally {
			DBHelper.closeConnection(connection);
		}
	}

	private void close(PreparedStatement statement) throws SQLException {

		if (statement != null) {
			statement.close();
		}
	}

	@Override
	protected void subAppend(ILoggingEvent event, Connection connection,
			PreparedStatement insertStatement) throws Throwable {
		bindLoggingEventWithInsertStatement(insertStatement, event);
		boolean updateCount = insertStatement.execute();
		if (updateCount) {
			addWarn("Failed to insert loggingEvent");
		}

	}

	/**
	 * error
	 * [0] Exception [1] remark [2] runtime context [3] client IP
	 * info warn
	 * [0] Exception [1] remark [2] runtime context [3] client IP
	 * @param stmt
	 * @param event
	 * @throws java.sql.SQLException
	 */
	void bindLoggingEventWithInsertStatement(PreparedStatement stmt,
			ILoggingEvent event) throws SQLException {
		//System.out.println("--"+event+"--");
		Object[] objs = event.getArgumentArray();
		if ( event.getLevel().toString().equals(Level.ERROR.levelStr)) {
			stmt.setString(LEVEL_INDEX, event.getLevel().levelStr);
			
			// This is expensive... should we do it every time?
	    		Exception exc = (Exception) objs[0]; 
	    		StringWriter sw = new StringWriter();  
	        PrintWriter pw = new PrintWriter(sw);  
	        if(exc!=null){
	        		exc.printStackTrace(pw);
	        }
			StringReader reader = new StringReader(sw.toString());
			event.getArgumentArray();
			stmt.setCharacterStream(INFO_INDEX, reader);
			if(exc!=null&&!StringUtil.checkNull(exc.getMessage())){
				stmt.setString(TITLE_INDEX,asStringTruncatedTo200(exc.getMessage()));
			}else{
				stmt.setString(TITLE_INDEX,asStringTruncatedTo200(event));
			}
		}else{
			stmt.setString(LEVEL_INDEX, event.getLevel().levelStr);
			StringReader reader = new StringReader(getArrayArg(objs,0));
			stmt.setCharacterStream(INFO_INDEX, reader);
			stmt.setString(TITLE_INDEX,asStringTruncatedTo200(getArrayArg(objs,0)));
		}
		
		stmt.setString(SERVER_IP_INDEX, localIP);
		
		StringReader readerremark = new StringReader(getArrayArg(objs,1)+"\r\n\n");
		stmt.setCharacterStream(REMARK_INDEX, readerremark);
		int len = event.getLoggerName().lastIndexOf(".");
		stmt.setString(MODULE_INDEX, event.getLoggerName().substring(len+1, event.getLoggerName().length()));
		stmt.setString(CLIENT_IP_INDEX, asStringTruncatedTo50(getArrayArg(objs,2)));
		
		String projectName = BaseConfig.getValue(PROJECT_NAME);
		stmt.setString(APPLICATION_INDEX,asStringTruncatedTo50(projectName) );
		stmt.setTimestamp(CREATE_TIME_INDEX,
				new Timestamp(event.getTimeStamp()));
	}

	/**
	 * @param argArray
	 * @return
	 * @throws java.sql.SQLException
	 */
	String getArrayArg(Object[] argArray,int num) throws SQLException {
		   
	    int arrayLen = argArray != null ? argArray.length : 0;
	    if(arrayLen<3){
	    		return null;
	    }
	    if(num==0){
	    		return asStringTruncatedTo50(argArray[0]); 
	    }
	    if(num==1){
	    		return (String) argArray[1]; 
	    }
	    if(num==2){
	    		return (String) argArray[2]; 
	    }
//	    if(arrayLen>0){
//	    		String ip = asStringTruncatedTo50(argArray[argArray.length-1]);
//	    		Pattern p = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)");  
//	    		Matcher m = p.matcher(ip); 
//	    		if(m.matches()){
//	    			return ip;
//	    		}
//	    }
	    return null;
	  }
	
	/**
	 * @param argArray
	 * @return
	 * @throws java.sql.SQLException
	 */
	String getAPP(Object[] argArray) throws SQLException {
		   
	    int arrayLen = argArray != null ? argArray.length : 0;
	    if(arrayLen>1){
	    		String app = asStringTruncatedTo50(argArray[argArray.length-2]);
    			return app;
	    }
	    return null;
	  }
	
	String asStringTruncatedTo50(Object o) {
	     String s = null;
	     if(o != null) {
	         s= o.toString();
	     }

	    if(s == null) {
	      return null;
	    }
	    if(s.length() <= 50) {
	      return s;
	    } else {
	      return s.substring(0, 50);
	    }
	  }
	
	String asStringTruncatedTo200(Object o) {
	     String s = null;
	     if(o != null) {
	         s= o.toString();
	     }

	    if(s == null) {
	      return null;
	    }
	    if(s.length() <= 200) {
	      return s;
	    } else {
	      return s.substring(0, 200);
	    }
	  }
	
	StringBuilder bindCallerData(StringBuilder caller,
			StackTraceElement[] callerDataArray) {

		StackTraceElement callerData = callerDataArray[0];
		if (callerData != null) {
			caller.append(callerData.getFileName());
			caller.append(callerData.getClassName());
			caller.append(callerData.getMethodName());
			caller.append(Integer.toString(callerData.getLineNumber()));
			caller.append(" \r\n");
		}
		return caller;
	}

	@Override
	protected Method getGeneratedKeysMethod() {
		return GET_GENERATED_KEYS_METHOD;
	}

	@Override
	protected String getInsertSQL() {

		return "INSERT INTO mlw_log "
				+ "(level,info,remark,server_ip,client_ip,module,application,create_time,title) "
				+ "VALUES (?,?,?,?,?,?,?,?,?)";
	}

	@Override
	protected void secondarySubAppend(ILoggingEvent eventObject,
			Connection connection, long eventId) throws Throwable {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		Pattern p = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)");  
		  
		Matcher m = p.matcher("118.182.20.242"); 
		System.out.println(m.matches());
	}
}
