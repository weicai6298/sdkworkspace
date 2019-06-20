package com.kkgame.sdk.db;

import java.util.ArrayList;
import java.util.Date;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.utils.CryptoUtil;
import com.kkgame.utils.PermissionUtils;
import com.kkgame.utils.Sputils;
import com.kkgame.utils.Yayalog;


/**
 * 用户历史数据读写
 * 
 * @author wjy
 * 
 */
public class UserDao {

	private static UserDao mUDao;

	private UserDao() {
	}

	private UserDao(Context context) {
		this.mContext = context;
	}

	public static UserDao getInstance(Context context) {
		
		
		
		
		
		
		if (mUDao == null) {
			mUDao = new UserDao(context);
		}
		return mUDao;
	}

	private Context mContext;

	
	//把本地sp存到数据库中
	public void synSpuser(){
		
		
		if ((PermissionUtils.checkPermission(mContext,
				Manifest.permission.READ_EXTERNAL_STORAGE) && PermissionUtils
				.checkPermission(mContext,
						Manifest.permission.WRITE_EXTERNAL_STORAGE))){
			
			String username = Sputils.getSPstring("username", "k", mContext);
			String password = Sputils.getSPstring("password", "k", mContext);
			if (!username.equals("k")) {
				if (!getUserStatus(username)) {
					Yayalog.loger("账号密码写入数据库"+username+password);
					writeUser(username, password, "",1);
					Yayalog.loger("清空sp临时账号");
					Sputils.putSPstring("username", "k", mContext);
				}
				
			}
			
			
		}
		
		
	}
	
	
	/**
	 * 获取sqlite数据库实例
	 * 
	 * @return
	 */
	public SQLiteDatabase getConnection() {
		SQLiteDatabase sqLiteDatabase = null;
		try {
			sqLiteDatabase = new AccountDbHelper(mContext)
					.getWritableDatabase();
		} catch (Exception e) {
		}

		return sqLiteDatabase;
	}

	/**
	 * 将当前用户信息保存到数据库 如果数据库中有该用户信息,更新用户密码和登录时间
	 * 
	 * @param user
	 */
	public synchronized void writeUser(String name, String password,
			String secret) {
		synSpuser();
		// 检查有没有sd卡读写权限
		if (!(PermissionUtils.checkPermission(mContext,
				Manifest.permission.READ_EXTERNAL_STORAGE) && PermissionUtils
				.checkPermission(mContext,
						Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
			Yayalog.loger("sd卡没有权限，将账号密码存sp中");
			Sputils.putSPstring("username", name, mContext);
			Sputils.putSPstring("password", password, mContext);

		} else {

			
			
			
			if (TextUtils.isEmpty(name)) {
				return;
			}
			SQLiteDatabase database = getConnection();
			String sql = null;
			try {

				if (getUserStatus(name)) {

					if (TextUtils.isEmpty(secret)) {
						secret = getSecret(name);
					}
					if (TextUtils.isEmpty(password)) {
						password = getPassword(password);
					}

					removeUser(name);
					writeUser(name, password, secret);

				} else {

					if (TextUtils.isEmpty(secret)) {

						sql = new StringBuffer("insert into ")
								.append(AccountDbHelper.TABLE_NAME).append("(")
								.append(AccountDbHelper.NAME).append(", ")
								.append(AccountDbHelper.PWD).append(", ")

								.append(AccountDbHelper.TIME)
								.append(") values (?,?,?)").toString();

						Object[] bindArgs = { CryptoUtil.encryptBASE64(name),
								CryptoUtil.encryptBASE64(password), new Date() };
						System.out.println(sql + bindArgs[2]);
						database.execSQL(sql, bindArgs);

					} else {
						sql = new StringBuffer("insert into ")
								.append(AccountDbHelper.TABLE_NAME).append("(")
								.append(AccountDbHelper.NAME).append(", ")
								.append(AccountDbHelper.PWD).append(", ")
								.append(AccountDbHelper.SECRET).append(", ")
								.append(AccountDbHelper.TIME)
								.append(") values (?,?,?,?)").toString();

						Object[] bindArgs = { CryptoUtil.encryptBASE64(name),
								CryptoUtil.encryptBASE64(password),
								CryptoUtil.encryptBASE64(secret), new Date() };
						// System.out.println(sql+bindArgs[2]);
						database.execSQL(sql, bindArgs);
					}
					// System.out.println("");

				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != database) {
					database.close();
				}
			}
		}
	}

	/**
	 * 将当前用户信息保存到数据库 如果数据库中有该用户信息,更新用户密码和登录时间
	 * 
	 * @param user
	 */
	public synchronized void writeUser(String name, String password,
			String secret,int type) {
		
		// 检查有没有sd卡读写权限
	
			
			
		Yayalog.loger("数据库存账号密码"+name+password);
			if (TextUtils.isEmpty(name)) {
				return;
			}
			SQLiteDatabase database = getConnection();
			String sql = null;
			try {

				if (getUserStatus(name)) {

					if (TextUtils.isEmpty(secret)) {
						secret = getSecret(name);
					}
					if (TextUtils.isEmpty(password)) {
						password = getPassword(password);
					}

					removeUser(name);
					writeUser(name, password, secret);

				} else {

					if (TextUtils.isEmpty(secret)) {

						sql = new StringBuffer("insert into ")
								.append(AccountDbHelper.TABLE_NAME).append("(")
								.append(AccountDbHelper.NAME).append(", ")
								.append(AccountDbHelper.PWD).append(", ")

								.append(AccountDbHelper.TIME)
								.append(") values (?,?,?)").toString();

						Object[] bindArgs = { CryptoUtil.encryptBASE64(name),
								CryptoUtil.encryptBASE64(password), new Date() };
						System.out.println(sql + bindArgs[2]);
						database.execSQL(sql, bindArgs);

					} else {
						sql = new StringBuffer("insert into ")
								.append(AccountDbHelper.TABLE_NAME).append("(")
								.append(AccountDbHelper.NAME).append(", ")
								.append(AccountDbHelper.PWD).append(", ")
								.append(AccountDbHelper.SECRET).append(", ")
								.append(AccountDbHelper.TIME)
								.append(") values (?,?,?,?)").toString();

						Object[] bindArgs = { CryptoUtil.encryptBASE64(name),
								CryptoUtil.encryptBASE64(password),
								CryptoUtil.encryptBASE64(secret), new Date() };
						// System.out.println(sql+bindArgs[2]);
						database.execSQL(sql, bindArgs);
					}
					// System.out.println("");

				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != database) {
					database.close();
				}
			}
		
	}
	
	/**
	 * 将当前用户信息保存到数据库 如果数据库中有该用户信息,更新用户密码和登录时间
	 * 
	 * @param user
	 */
	public synchronized void writeUsers(ArrayList<User> users) {
		SQLiteDatabase database = getConnection();
		String sql = null;
		try {
			for (User user : users) {
				if (getUserStatus(user.userName)) {

					String password = getPassword(user.userName);
					String secret = getSecret(user.userName);

					removeUser(user.userName);

					writeUser(user.userName, password, secret);

				} else {
					sql = new StringBuffer("insert into ")
							.append(AccountDbHelper.TABLE_NAME).append("(")
							.append(AccountDbHelper.NAME).append(", ")
							.append(AccountDbHelper.PWD).append(", ")
							.append(AccountDbHelper.SECRET).append(", ")
							.append(AccountDbHelper.TIME)
							.append(") values (?,?,?,?)").toString();

					Object[] bindArgs = {
							CryptoUtil.encryptBASE64(user.userName),
							CryptoUtil.encryptBASE64(user.password),
							CryptoUtil.encryptBASE64(user.secret), new Date() };
					database.execSQL(sql, bindArgs);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}

	/**
	 * 从数据库读取用户名列表
	 * 
	 * @return
	 */
	public synchronized ArrayList<String> getUsers() {
		
		synSpuser();
		
		ArrayList<String> names = new ArrayList<String>();
		if (!(PermissionUtils.checkPermission(mContext,
				Manifest.permission.READ_EXTERNAL_STORAGE) && PermissionUtils
				.checkPermission(mContext,
						Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
			Yayalog.loger("sd卡没有权限，将返回sp中的账号");
			String username = Sputils.getSPstring("username", "k", mContext);
			if (!username.equals("k")) {
				names.add(username);
			}

		} else {
			SQLiteDatabase database = getConnection();
			Cursor cursor = null;

			try {
				String sql = new StringBuffer("select ")
						.append(AccountDbHelper.NAME).append(" from ")
						.append(AccountDbHelper.TABLE_NAME)
						.append(" order by ").append(AccountDbHelper.ID)
						.append(" desc ").toString();

				cursor = database.rawQuery(sql, new String[] {});

				while (cursor.moveToNext()) {
					String decryptBASE64 = CryptoUtil.decryptBASE64(cursor
							.getString(0));
					if (TextUtils.isEmpty(decryptBASE64)) {

					} else {
						names.add(decryptBASE64);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != database) {
					database.close();
				}
			}
		}

		return names;
	}

	/**
	 * 从数据库读取密码
	 * 
	 * @param name
	 *            用户名
	 * @return
	 */
	public synchronized String getPassword(String name) {

		if (!(PermissionUtils.checkPermission(mContext,
				Manifest.permission.READ_EXTERNAL_STORAGE) && PermissionUtils
				.checkPermission(mContext,
						Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
			Yayalog.loger("sd卡没有权限，将返回sp中的密码");
			String password = Sputils.getSPstring("password", "k", mContext);
			return password;

		} else {
		
			SQLiteDatabase database = getConnection();
			Cursor cursor = null;
			String pwd = null;
			try {
				String sql = new StringBuffer("select * from ")
						.append(AccountDbHelper.TABLE_NAME).append(" where ")
						.append(AccountDbHelper.NAME).append(" = ? ")
						.toString();

				cursor = database.rawQuery(sql,
						new String[] { CryptoUtil.encryptBASE64(name) });
				if (cursor.moveToFirst()) {
					pwd = CryptoUtil.decryptBASE64(cursor.getString(cursor
							.getColumnIndex(AccountDbHelper.PWD)));
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
	}

	/**
	 * 从数据库读取key
	 * 
	 * @param name
	 *            用户名
	 * @return
	 */
	public synchronized String getSecret(String name) {
		SQLiteDatabase database = getConnection();
		Cursor cursor = null;
		String secret = null;
		try {
			String sql = new StringBuffer("select * from ")
					.append(AccountDbHelper.TABLE_NAME).append(" where ")
					.append(AccountDbHelper.NAME).append(" = ? ").toString();

			cursor = database.rawQuery(sql,
					new String[] { CryptoUtil.encryptBASE64(name) });
			if (cursor.moveToFirst()) {
				secret = CryptoUtil.decryptBASE64(cursor.getString(cursor
						.getColumnIndex(AccountDbHelper.SECRET)));
			}
		} catch (Exception e) {

		} finally {
			if (null != database) {
				database.close();
			}
		}
		return secret;
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
		String sql;
		try {

			database.delete(AccountDbHelper.TABLE_NAME, AccountDbHelper.NAME
					+ "=?", new String[] { CryptoUtil.encryptBASE64(name) });

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
					.append(AccountDbHelper.TABLE_NAME).append(" where ")
					.append(AccountDbHelper.NAME).append(" = ").append("?")
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

	/**
	 * 从数据库添加新列
	 * 
	 * @param name
	 *            用户名
	 * @return
	 */
	public synchronized void upDateclume() {
		
		
		
		
		SQLiteDatabase database = getConnection();
		String sql;
		try {

			if (!checkColumnExist1(database, AccountDbHelper.TABLE_NAME,
					AccountDbHelper.SECRET)) {

				sql = "ALTER TABLE " + AccountDbHelper.TABLE_NAME
						+ "  ADD COLUMN " + AccountDbHelper.SECRET
						+ " char(20)";
				database.execSQL(sql);
				Log.e("更改数据库", "我改了");

				// System.out.println("我改了数据库哦");
			} else {
				Log.e("更改数据库", "我没有改");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
		
		
		

		
	}

	/**
	 * 方法1：检查某表列是否存在
	 * 
	 * @param db
	 * @param tableName
	 *            表名
	 * @param columnName
	 *            列名
	 * @return
	 */
	private boolean checkColumnExist1(SQLiteDatabase db, String tableName,
			String columnName) {
		boolean result = false;
		Cursor cursor = null;

		try {
			// 查询一行
			cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0",
					null);
			result = cursor != null && cursor.getColumnIndex(columnName) != -1;
		} catch (Exception e) {
			// Log.e(TAG,"checkColumnExists1..." + e.getMessage()) ;
		} finally {
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}

		return result;
	}

}
