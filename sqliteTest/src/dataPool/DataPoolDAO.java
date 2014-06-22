package dataPool;

import java.sql.Connection;

import base.BaseSqlite;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DataPoolDAO extends BaseSqlite{
	 private  static ComboPooledDataSource ds;
	    public DataPoolDAO(String file) {
		super(file);
	    }
	    
		public DataPoolDAO() {
		}

		public ComboPooledDataSource getDataSource(){ 
	    	ComboPooledDataSource ds = new ComboPooledDataSource();  
	        try {//��������  
	            ds.setDriverClass(driverClass);  
	            //���ݿ�����  
	            ds.setJdbcUrl(url);  
	           
	            //��ʼ������������  
	            ds.setInitialPoolSize(10);  
	            //��󿪼���  
	            ds.setMaxPoolSize(20);  
	            //��С������  
	            ds.setMinPoolSize(5);  
	            //���õȴ�ʱ��  
	            ds.setMaxIdleTime(50000);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return ds;
	    }  
	    //���ݿ����ӷ���  
	    public synchronized Connection getConn() throws Exception{   
	    	if (ds == null){
	    		ds = getDataSource();
	    	}
	    	 return ds.getConnection(); 
	    }  
}

