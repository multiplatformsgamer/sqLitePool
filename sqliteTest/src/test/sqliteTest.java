package test;

import java.util.List;
import java.util.Map;

import single.singleDAO;
import dataPool.DataPoolDAO;
import dataPool.excuteThread;

public class sqliteTest {

	public static void main(String[] args) throws Exception {
		String fileString = "testrrrrrrrrrrrr.db";
		
		long begin = System.currentTimeMillis();
		DataPoolDAO db = new DataPoolDAO(fileString);
		for (int i = 0; i < 10; i++) {
			List<Map<String, Object>> rs = db.querySql("select * from people");
		//	System.out.println(rs.size());
		}
		System.out.println(System.currentTimeMillis() - begin);
		
		long begin2 = System.currentTimeMillis();
		singleDAO singledb = new singleDAO(fileString,true);
		for (int i = 0; i < 10; i++) {
			List<Map<String, Object>> list = singledb.querySql("select * from people");
		//	System.out.println(list.size());
		}
		System.out.println(System.currentTimeMillis() - begin2);
		
		
		long begin3 = System.currentTimeMillis();
		excuteThread[] threads = new excuteThread[100];
		for(int i=0;i<10;i++){
			threads[i] = new excuteThread(fileString);
		}
		for(int i=0;i<10;i++){
			threads[i].start();
		}
		//此处统计无效，统计的是主线程的时间
		// System.out.println(System.currentTimeMillis() - begin3);
	}

}
