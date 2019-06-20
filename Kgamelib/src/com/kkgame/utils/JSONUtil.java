package com.kkgame.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.kkgame.sdk.utils.Base64;

import android.app.Activity;

public class JSONUtil {

	/**
	 * 格式化订单扩展字段
	 * 
	 * @param wxt
	 * @param id
	 * @param name
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public static String formatExt(String ext, String game_id, String name,
			String uid) {

		// ext'#_yyw_{id,ui,name}

		StringBuffer buffer = new StringBuffer();

		buffer.append(ext).append("#_yyw_").append(game_id).append("|")
				.append(uid).append("|").append(name);

		String encode = "";
		try {
			encode = URLEncoder.encode(buffer.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encode;
	}

	/**
	 * 老版渠道登陆成功整合token，3。17，为兼容将只做token的转移
	 * 
	 * @param activity
	 * @param token
	 * @param name
	 * @return
	 */
	public static String formatToken(Activity activity, String token,
			String name) {

		return token;
	}

	public static String formatToken(Activity activity, String token,
			String uid, String username, String yy_username) {

		

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cp_token", "" + token);
			jsonObject.put("cp_uid", "" + uid);
			jsonObject.put("cp_username", "" + username);
			/*jsonObject.put("yy_username", "" + yy_username);
			jsonObject.put("appid", "" + DeviceUtil.getGameId(activity));
			jsonObject.put("union_id", "" + DeviceUtil.getUnionid(activity));*/
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// StringBuffer buffer = new StringBuffer();

		// buffer.append(token).append("@@").append(DeviceUtil.getUnionid(activity)).append("@@").append(name).append("@@").append(DeviceUtil.getGameId(activity));

		String encode = "";
		try {
			encode = URLEncoder.encode(Base64.encode(jsonObject.toString().getBytes()), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encode;
	}

}
