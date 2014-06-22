package dataPool;

import java.util.List;
import java.util.Map;

public class excuteThread extends Thread {
	private String file;
	public excuteThread(String file) {
		this.file = file;
	}
	
	@Override
	public void run() {
		DataPoolDAO db = new DataPoolDAO(file);
		try {
			List<Map<String, Object>> rs = db.querySql("select * from people");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
