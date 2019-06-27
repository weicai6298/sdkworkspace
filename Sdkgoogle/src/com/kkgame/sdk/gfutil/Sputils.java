package com.kkgame.sdk.gfutil;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.kkgame.sdk.bean.Userkey;
import com.kkgame.utils.myBase64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Sputils {

	private static Editor ed;

	public static boolean putSPstring(String key, String value, Context mcontext) {
		SharedPreferences sp = mcontext.getSharedPreferences("common",
				Context.MODE_PRIVATE);

		ed = sp.edit();
		ed.putString("dd"+key, value);
		boolean flag = ed.commit();
		return flag;
	}

	public static boolean removeSPstring(String key,Context mcontext) {
		SharedPreferences sp = mcontext.getSharedPreferences("common",
				Context.MODE_PRIVATE);
		ed = sp.edit();
		ed.remove("dd"+key);
		boolean flag = ed.commit();
		return flag;
	}
	/**
	 * 查询某个key是否已经存在
	 *
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean contains(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences("common",
				Context.MODE_PRIVATE);
		return sp.contains("dd"+key);
	}


	public static boolean putSPint(String key, int value, Context mcontext) {
		SharedPreferences sp = mcontext.getSharedPreferences("common",
				Context.MODE_PRIVATE);

		ed = sp.edit();

		ed.putInt("dd"+key, value);
		boolean flag = ed.commit();
		return flag;
	}

	public static int getSPint(String key, int defValue, Context mcontext) {
		SharedPreferences sp = mcontext.getSharedPreferences("common",
				Context.MODE_PRIVATE);

		ed = sp.edit();
		int k = sp.getInt("dd"+key, defValue);
		return k;
	}

	public static String getSPstring(String key, String defValue,
			Context mcontext) {
		SharedPreferences sp = mcontext.getSharedPreferences("common",
				Context.MODE_PRIVATE);

		ed = sp.edit();
		String k = sp.getString("dd"+key, defValue);
		return k;
	}

	public static <T> Boolean putObject(String key, T object, Context mcontext) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(3000);
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 将Product对象放到OutputStream中
		// 将Product对象转换成byte数组，并将其进行base64编码
		String newWord = new String(myBase64.encode(baos.toByteArray()));

		// 将编码后的字符串写到base64.xml文件中
		SharedPreferences sp = mcontext.getSharedPreferences("common",
				Context.MODE_PRIVATE);

		ed = sp.edit();
		ed.putString(key, newWord);
		boolean flag = ed.commit();
		return flag;
	}
	
	public <T> String ObjectToString(T object){
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream(3000);
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		// 将Product对象放到OutputStream中
		// 将Product对象转换成byte数组，并将其进行base64编码
		String newWord = new String(myBase64.encode(baos.toByteArray()));
		return newWord;
		
	}
	
	public <T> T StringToObject(String k){
		try {
			// 对Base64格式的字符串进行解码
			byte[] base64Bytes = myBase64.decode(k);
			ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			// 从ObjectInputStream中读取Product对象
			T addWord = (T) ois.readObject();
			return addWord;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public static <T> T getObject(String key, Context mcontext) {
		SharedPreferences sp = mcontext.getSharedPreferences("common",
				Context.MODE_PRIVATE);

		ed = sp.edit();
		String k = sp.getString(key, null);
		try {
			// 对Base64格式的字符串进行解码
			byte[] base64Bytes = myBase64.decode(k);
			ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			// 从ObjectInputStream中读取Product对象
			T addWord = (T) ois.readObject();
			return addWord;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	/**
	 * 保存key值
	 * 
	 * @param username
	 * @param key
	 * @param mContext
	 * @return 是否保存成功
	 */
	public static Boolean saveUserkey(String username, String key,
			Context mContext) {

		ArrayList<Userkey> userkeys = getObject("secretkey", mContext);
		if (userkeys == null) {
			userkeys = new ArrayList<Userkey>();
			Userkey userkey = new Userkey();
			userkey.setUsername(username);
			userkey.setUserkey(key);
			userkeys.add(userkey);
			Boolean putObject = putObject("secretkey", userkeys, mContext);
			return putObject;
		} else {
			for (int i = 0; i < userkeys.size(); i++) {

				if (userkeys.get(i).getUsername().equals(username)) {
					return true;
				}
			}
			Userkey userkey = new Userkey();
			userkey.setUsername(username);
			userkey.setUserkey(key);
			userkeys.add(userkey);
			Boolean putObject = putObject("secretkey", userkeys, mContext);
			return putObject;
		}
	}

	/**
	 * 用username获取秘钥
	 * 
	 * @param username
	 * @param mContext
	 * @return
	 */
	public static String getSecretkey(String username, Context mContext) {
		ArrayList<Userkey> userkeys = getObject("secretkey", mContext);
		if (userkeys != null) {
			for (int i = 0; i < userkeys.size(); i++) {
				if (userkeys.get(i).getUsername().equals(username)) {
					return userkeys.get(i).getUserkey();
				}
			}
			return null;
		} else {
			return null;
		}
	}

}
