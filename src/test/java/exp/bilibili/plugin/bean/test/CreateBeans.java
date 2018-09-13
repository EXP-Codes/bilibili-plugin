package exp.bilibili.plugin.bean.test;

import java.sql.Connection;

import exp.bilibili.plugin.cache.ActivityMgr;
import exp.libs.warp.db.sql.SqliteUtils;

public class CreateBeans {

	public static void main(String[] args) {
		Connection conn = SqliteUtils.getConn(ActivityMgr.DS);
		SqliteUtils.createBeanFromDB(conn, 
				"exp.bilibili.plugin.bean.ldm", 
				"./src/main/java/exp/bilibili/plugin/bean/ldm", 
				new String[] { "T_ACTIVITY" }
		);
		SqliteUtils.close(conn);
	}
	
}
