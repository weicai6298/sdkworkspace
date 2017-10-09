package com.kkgame.sdk.db;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 数据库,保存历史用户名和密码
 * @author wjy
 *
 */
public class OldAccountDbHelper extends SQLiteOpenHelper{
	public static final int VERSION = 2;  
    public static final String mDbName =  OldSDBHelper.DB_DIR + "/com.yayawanhorizontal/" + "yywan.db";  
    public final static String TABLE_NAME = "yywanlogin";
    public final static String ID = "id";
    public final static String NAME = "yyname";
    public final static String PWD = "yypwd";
    
    
    public OldAccountDbHelper(Context context) {  
    	super(context, mDbName, null, VERSION);
    }

    public OldAccountDbHelper(Context context, int version){
    	super(context, mDbName, null, version);
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
//	    String sql = "Create table %s (%s integer primary key autoincrement,%s text, %s text);";
//        sql = String.format(sql, TABLE_NAME, ID, NAME, PWD);
//        db.execSQL(sql);
        
        
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
//	    String sql = "drop table if exists " + TABLE_NAME;
//		db.execSQL(sql);
//		onCreate(db);
	}
}
