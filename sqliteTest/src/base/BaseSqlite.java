package base;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public abstract class BaseSqlite {
	protected final static Logger logger = Logger.getLogger(BaseSqlite.class);  
	protected String url = "jdbc:sqlite:";
	protected String driverClass = "org.sqlite.JDBC";
	protected Connection conn;
	protected Statement stmt;
	protected ResultSet rs;
	protected Boolean autoComm;
	
	public BaseSqlite() {
	}

	public BaseSqlite(String file) {
		this.url = url + file;
	}
	
	public BaseSqlite(String file, Boolean autoComm) {
		this.url = url + file;
		this.autoComm = autoComm;
	}
	
	protected abstract Connection getConn() throws Exception;
	
	/**
	 * 公有方法，返回整数类型
	 * 对数据库进行添加，删除，修改，新建表，删除表等
	 * */
	public  int executeSql(String sql)throws Exception
	{
		synchronized(BaseSqlite.class){
		int result = -1;
		try {
			conn = getConn();
			stmt = conn.createStatement();
			result = stmt.executeUpdate(sql);//执行SQL语句把值赋予result
			result += 1;
		} catch (SQLException e) {
			logger.error(e.toString());
			throw e;
		}
		finally{
			result += closeStatement();
			result += closeConnection();
		}
			return result;
		}   
	  }
	/**公有方法，返回ResultSet类型
	 * 对数据库进行查询
	 * */
	public List<Map<String, Object>> querySql(String sql)throws Exception
	{
		List<Map<String, Object>> list = null;
		
		/* about the  synchronized:
		如果是同一个实例的访问，可以在方法声明的时候加synchronized即可。
		但是如果不同的实例，比如test中的。每一个实例内只有调用自己的方法，加synchronized是无用的。
		需要锁住static的方式
		*/
		synchronized(BaseSqlite.class){
		try
		{	conn = getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);  //执行SQL语句，并把结果集赋予ResultSet对象中
			list = resultSetToList(rs);
		}catch(Exception e){
			logger.error(e.toString());
			throw e;
		}
		finally{
			 closeResultSet();
			 closeStatement();
			 closeConnection();
		}
		}
		return list;
	}

	/**
	 * 私有方法
	 * 关闭ResultSet对象
	 * */
	private  int closeResultSet()throws Exception
	{
		int result = -1;
			try
			{
				rs.close(); //关闭ResultSet对象
				result += 1;
			}catch(Exception e)   
				{
					throw e;
				}
			return result;
	}
	/**
	 * 私有方法
	 * 关闭Statement对象
	 * */
	private int closeStatement()throws Exception
	{
		int result = -1;
			try
			{
				stmt.close();  //关闭Statement对象
				result += 1;
			}catch(Exception e)
				{
				logger.error(e.toString());
				throw e;
				}
			return result;
	}
	
	/**
	 * 私有方法
	 * 关闭与数据库的连接
	 * */
	private int closeConnection()throws Exception
	{
		int result = -1;
			try
			{
				conn.close();  //关闭与数据库的连接
				result += 1;
				
			}catch(Exception e)
				{
				logger.error(e.toString());
				throw e;
				}
			return result;
	}
	
	/*
	 *  将resultSet 转化为list
	 *  及时关闭resultSet
	 */
	private  List<Map<String, Object>> resultSetToList(ResultSet rs) throws Exception {   
        if (rs == null)   
            return Collections.emptyList();   
        ResultSetMetaData md = rs.getMetaData(); //得到结果集(rs)的结构信息，比如字段数、字段名等   
        int columnCount = md.getColumnCount(); //返回此 ResultSet 对象中的列数   
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();   
        Map<String, Object> rowData = null;   
        while (rs.next()) {   
         rowData = new HashMap<String, Object>(columnCount);   
         for (int i = 1; i <= columnCount; i++) {   
                 rowData.put(md.getColumnName(i), rs.getObject(i));   
         }   
         list.add(rowData);   
        }   
        return list;   
}
}