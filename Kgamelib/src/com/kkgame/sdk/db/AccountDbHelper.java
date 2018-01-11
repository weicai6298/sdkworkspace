package com.kkgame.sdk.db;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库,保存历史用户名和密码
 * 
 * @author wjy
 * 
 */
public class AccountDbHelper extends SQLiteOpenHelper {
	//我設置的是四
	public static final int VERSION = 2;
	public static final String mDbName = SDBHelper.DB_DIR + File.separator
			+ "userdate.db";
	//数据库名字/storage/emulated/0/yayaUserData/com.yayawan.sdk.account.db/userdate.db

	public final static String TABLE_NAME = "userinfo";// 表名
	public final static String ID = "_id";
	public final static String NAME = "name";
	public final static String PWD = "password";
	public final static String TIME = "time";

	public final static String SECRET = "secret";

	public AccountDbHelper(Context context) {
		
		super(context, mDbName, null, VERSION);
		//System.out.println("数据库名字"+mDbName);
	}

	public AccountDbHelper(Context context, int version) {
		super(context, mDbName, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table %s (%s integer primary key autoincrement,%s text, %s text, %s current_timestamp);";
		sql = String.format(sql, TABLE_NAME, ID, NAME, PWD, TIME);
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// String sql = "drop table if exists " + TABLE_NAME;

		String sql = "ALTER TABLE " + TABLE_NAME + "  ADD COLUMN " + SECRET
				+ " char(20)";
		
		//System.out.println("执行的sql语句"+sql);
		
		db.execSQL(sql);
		
		//onCreate(db);
	}
}
