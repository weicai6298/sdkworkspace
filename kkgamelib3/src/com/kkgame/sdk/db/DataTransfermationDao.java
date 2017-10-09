package com.kkgame.sdk.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.utils.CryptoUtil;



/**
 * 用户历史数据读写
 * 
 * @author wjy
 * 
 */
public class DataTransfermationDao {

    private static DataTransfermationDao mUDao;

    private DataTransfermationDao() {
    }

    private DataTransfermationDao(Context context) {
        this.mContext = context;
    }

    public static DataTransfermationDao getInstance(Context context) {
        if (mUDao == null) {
            mUDao = new DataTransfermationDao(context);
        }
        return mUDao;
    }

    private Context mContext;

    /**
     * 获取sqlite数据库实例
     * 
     * @return
     */
    public SQLiteDatabase getConnection() {
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = new OldAccountDbHelper(mContext)
                    .getWritableDatabase();
        } catch (Exception e) {
        }

        return sqLiteDatabase;
    }

    /**
     * 从数据库读取用户名列表
     * 
     * @return
     */
    public synchronized ArrayList<User> getUsers() {
        SQLiteDatabase database = getConnection();
        Cursor cursor = null;
        User user = null;
        ArrayList<User> users = new ArrayList<User>();
        try {
            String sql = new StringBuffer("select * from ").append(
                    OldAccountDbHelper.TABLE_NAME).toString();
            cursor = database.rawQuery(sql, new String[] {});
            while (cursor.moveToNext()) {
                user = new User();
                user.userName = CryptoUtil.decryptBASE64(cursor.getString(cursor
                        .getColumnIndex(OldAccountDbHelper.NAME)));
                user.password = CryptoUtil.decryptBASE64(cursor.getString(cursor
                        .getColumnIndex(OldAccountDbHelper.PWD)));
                
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != database) {
                database.close();
            }
        }
        return users;
    }

    /**
     * 从数据库读取密码
     * 
     * @param name
     *            用户名
     * @return
     */
    public synchronized String getPassword(String name) {
        SQLiteDatabase database = getConnection();
        Cursor cursor = null;
        String pwd = null;
        try {
            String sql = new StringBuffer("select * from ")
                    .append(OldAccountDbHelper.TABLE_NAME).append(" where ")
                    .append(OldAccountDbHelper.NAME).append(" = ? ").toString();

            cursor = database.rawQuery(sql,
                    new String[] { CryptoUtil.encryptBASE64(name) });
            if (cursor.moveToFirst()) {
                pwd = CryptoUtil.decryptBASE64(cursor.getString(cursor
                        .getColumnIndex(OldAccountDbHelper.PWD)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != database) {
                database.close();
            }
        }
        return pwd;
    }

    /**
     * 从数据库删除用户记录
     * 
     * @param name
     *            用户名
     * @return
     */
    public synchronized void removeUser(String name) {
        SQLiteDatabase database = getConnection();
        try {

            database.delete(OldAccountDbHelper.TABLE_NAME,
                    OldAccountDbHelper.NAME + "=?", new String[] { name });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != database) {
                database.close();
            }
        }
    }

    /**
     * 判断用户名在数据库中是否存在
     * 
     * @param name
     * @return
     */
    public synchronized boolean getUserStatus(String name) {
        SQLiteDatabase database = getConnection();
        Cursor cursor = null;
        boolean flag = false;
        try {
            String sql = new StringBuffer("select count(*) from ")
                    .append(OldAccountDbHelper.TABLE_NAME).append(" where ")
                    .append(OldAccountDbHelper.NAME).append(" = ").append("?")
                    .toString();

            cursor = database.rawQuery(sql,
                    new String[] { CryptoUtil.encryptBASE64(name) });
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != database) {
                database.close();
            }
        }
        return flag;
    }
}
