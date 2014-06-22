package single;
import java.sql.Connection;
import java.sql.DriverManager;

import base.BaseSqlite;

public class singleDAO  extends BaseSqlite
{
	public singleDAO(String file, Boolean autoComm) {
		super(file, autoComm);
	}
	
	/**
	 * 连接sqlite数据库方法
	 * 返回类型为Connection
	 * */
	protected Connection getConn()throws Exception
	{
		Connection connection = null;
		try
		{
		Class.forName(driverClass);   
		connection = DriverManager.getConnection(url);
		connection.setAutoCommit(autoComm);
	     }catch(Exception e)
			{
	    	logger.error(e.toString());
	    	throw e;
			}
		return connection;
	}
}


