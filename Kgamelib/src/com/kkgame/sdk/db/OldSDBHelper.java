package com.kkgame.sdk.db;
import java.io.File;
import android.os.Environment;
/**
 * 外部数据库,
*/
public class OldSDBHelper {
	
	public static final String DB_DIR = Environment.getExternalStorageDirectory().getPath()
			+ File.separator + "hpcDbData";
	static {
		while(! Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		File dbFolder = new File(DB_DIR);
		if (!dbFolder.exists()){
			//dbFolder.mkdirs();
		}
	}
}
