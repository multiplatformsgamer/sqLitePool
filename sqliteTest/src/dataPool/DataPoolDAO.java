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
	        try {//加载驱动  
	            ds.setDriverClass(driverClass);  
	            //数据库连接  
	            ds.setJdbcUrl(url);  
	           
	            //初始化开几个连接  
	            ds.setInitialPoolSize(10);  
	            //最大开几个  
	            ds.setMaxPoolSize(20);  
	            //最小开几个  
	            ds.setMinPoolSize(5);  
	            //设置等待时间  
	            ds.setMaxIdleTime(50000);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return ds;
	    }  
	    //数据库连接方法  
	    public synchronized Connection getConn() throws Exception{   
	    	if (ds == null){
	    		ds = getDataSource();
	    	}
	    	 return ds.getConnection(); 
	    }  
}

