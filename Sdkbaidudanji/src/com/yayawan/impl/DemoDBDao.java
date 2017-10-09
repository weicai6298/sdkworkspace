package com.yayawan.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DemoDBDao {

	private DemoDatabaseHelper dbHelper;
	private static DemoDBDao instance;
	private Context mContext = null;

	public void openConnection() {
		dbHelper = new DemoDatabaseHelper(mContext);
	}

	public void closeConnection() {
		dbHelper.close();
	}

	private DemoDBDao(Context context) {
		mContext = context;
	}

	public static synchronized DemoDBDao getInstance(Context context) {
		if (instance == null) {
			instance = new DemoDBDao(context);
		} 
		return instance;
	}

	public synchronized List<DemoRecordData> getAllRechargeRecords() {
		openConnection();
		List<DemoRecordData> dataList = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = dbHelper.getReadableDatabase();
			if(db != null){
				String selectSql = "select propsid, price, pnum from game_record where ptype = '1'";
				cursor = db.rawQuery(selectSql, null);
				dataList = new ArrayList<DemoRecordData>();
				if(cursor != null){
					while (cursor.moveToNext()) {
						DemoRecordData data = new DemoRecordData();
						data.setPropsId(cursor.getString(cursor.getColumnIndex("propsid")));
						data.setPrice(cursor.getString(cursor.getColumnIndex("price")));
						data.setRecordNum(cursor.getString(cursor.getColumnIndex("pnum")));
						dataList.add(data);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null)
				cursor.close();
			if(db != null)
				db.close();
			closeConnection();
		}

		return dataList;
	}

	public synchronized boolean queryNonRechargeRecord(String propsId) {
		openConnection();
		boolean flagRecharged = false;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = dbHelper.getReadableDatabase();
			if(db != null){
				String selectSql = "select propsid from game_record where propsid = ? and ptype = '0'";
				cursor = db.rawQuery(selectSql, new String[]{propsId});
				if(cursor != null){
					cursor.moveToFirst();
					if (cursor.getCount() > 0) {
						flagRecharged = true;
					} 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null)
				cursor.close();
			if(db != null)
				db.close();
			closeConnection();
		}

		return flagRecharged;
	}
	
	public synchronized void updateRechargeRecord(DemoRecordData data) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = null;
		Cursor cursor = null;
		openConnection();
		try {
			db = dbHelper.getWritableDatabase();
			if(db != null){
				db.beginTransaction();
				String selectSql = "select pnum from game_record where propsid = ?";
				cursor = db.rawQuery(selectSql, new String[]{data.getPropsId()});
				if(cursor != null){
					cursor.moveToFirst();
					if (cursor.getCount() > 0) {
						String pnum = cursor.getString(cursor.getColumnIndex("pnum"));
						long newPnum = Long.valueOf(pnum).longValue() + Long.valueOf(data.getRecordNum()).longValue();
						String updateSql = "update game_record set pnum = '" + newPnum + "' where propsid = '" + data.getPropsId() + "'";
						db.execSQL(updateSql);
					} else {
						String insertSql = "insert into game_record (propsid, price, ptype, pnum) values ('" + data.getPropsId() 
								+ "', '" + data.getPrice() + "', '"+ data.getType() + "', '" + data.getRecordNum() + "')";
						db.execSQL(insertSql);
					}
				}
				db.setTransactionSuccessful();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null)
				cursor.close();
			if(db != null){
				db.endTransaction();
				db.close();
				db = null;
			}
			closeConnection();
		}
	}
}
