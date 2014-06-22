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
	 * ���з�����������������
	 * �����ݿ������ӣ�ɾ�����޸ģ��½���ɾ�����
	 * */
	public  int executeSql(String sql)throws Exception
	{
		synchronized(BaseSqlite.class){
		int result = -1;
		try {
			conn = getConn();
			stmt = conn.createStatement();
			result = stmt.executeUpdate(sql);//ִ��SQL����ֵ����result
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
	/**���з���������ResultSet����
	 * �����ݿ���в�ѯ
	 * */
	public List<Map<String, Object>> querySql(String sql)throws Exception
	{
		List<Map<String, Object>> list = null;
		
		/* about the  synchronized:
		�����ͬһ��ʵ���ķ��ʣ������ڷ���������ʱ���synchronized���ɡ�
		���������ͬ��ʵ��������test�еġ�ÿһ��ʵ����ֻ�е����Լ��ķ�������synchronized�����õġ�
		��Ҫ��סstatic�ķ�ʽ
		*/
		synchronized(BaseSqlite.class){
		try
		{	conn = getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);  //ִ��SQL��䣬���ѽ��������ResultSet������
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
	 * ˽�з���
	 * �ر�ResultSet����
	 * */
	private  int closeResultSet()throws Exception
	{
		int result = -1;
			try
			{
				rs.close(); //�ر�ResultSet����
				result += 1;
			}catch(Exception e)   
				{
					throw e;
				}
			return result;
	}
	/**
	 * ˽�з���
	 * �ر�Statement����
	 * */
	private int closeStatement()throws Exception
	{
		int result = -1;
			try
			{
				stmt.close();  //�ر�Statement����
				result += 1;
			}catch(Exception e)
				{
				logger.error(e.toString());
				throw e;
				}
			return result;
	}
	
	/**
	 * ˽�з���
	 * �ر������ݿ������
	 * */
	private int closeConnection()throws Exception
	{
		int result = -1;
			try
			{
				conn.close();  //�ر������ݿ������
				result += 1;
				
			}catch(Exception e)
				{
				logger.error(e.toString());
				throw e;
				}
			return result;
	}
	
	/*
	 *  ��resultSet ת��Ϊlist
	 *  ��ʱ�ر�resultSet
	 */
	private  List<Map<String, Object>> resultSetToList(ResultSet rs) throws Exception {   
        if (rs == null)   
            return Collections.emptyList();   
        ResultSetMetaData md = rs.getMetaData(); //�õ������(rs)�Ľṹ��Ϣ�������ֶ������ֶ�����   
        int columnCount = md.getColumnCount(); //���ش� ResultSet �����е�����   
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