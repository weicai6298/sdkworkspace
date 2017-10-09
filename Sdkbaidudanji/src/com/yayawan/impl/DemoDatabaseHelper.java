package com.yayawan.impl;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DemoDatabaseHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "demo_record.db";
	
	private static final int DB_VERSION = 1;
	
	
	/** 注：game_record表中ptype字段的值：为0表示非消费品，为1表示消费品  **/
	private String SQL_CREATE_TABLE_DEMO_RECORD = "CREATE TABLE IF NOT EXISTS game_record (" 
			+ "'_id' INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL," 
			+ "'propsid' TEXT  NOT NULL," 
			+ "'ptype' TEXT NOT NULL,"
			+ "'price' TEXT NOT NULL,"
			+ "'pnum' TEXT NOT NULL);";
	
	public DemoDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.beginTransaction();
		try {
			db.execSQL(SQL_CREATE_TABLE_DEMO_RECORD);
			db.setTransactionSuccessful();
		} catch (SQLException exception){
			exception.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}