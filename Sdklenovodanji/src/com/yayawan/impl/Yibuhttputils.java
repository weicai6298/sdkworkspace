package com.yayawan.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public abstract class Yibuhttputils {

	private String url;
	private String str;
	private int requestcode;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				faile("网络问题", msg.what + "");
				break;
			case 200:
				sucee(returnLine);

				break;
			case 201:

				sucee(returnLine);

				break;
			case 400:

				faile(returnLine, msg.what + "");

				break;

			default:
				faile(returnLine, msg.what + "");
				break;
			}
		};
	};
	private static HttpURLConnection connection;
	private static String returnLine;
	private static BufferedReader reader;

	public Yibuhttputils() {

	}

	public synchronized void runHttp(final String url, final String str,
			final String method, final String token) {

		new Thread() {

			public void run() {
				int sendRequest = SendRequest(url, str, method, token);
				Log.e("返回的responsecode", sendRequest + "");
				Message message = new Message();
				message.what = sendRequest;
				handler.sendMessage(message);
			};
		}.start();
	}

	private static int SendRequest(String adress_Http, String strJson,
			String method, String token) {

		returnLine = "";

		try {

		//	System.out.println("**************开始http通讯**************");
		//	System.out.println("**************调用的接口地址为**************"
		//			+ adress_Http);

		//	System.out.println("**************请求发送的数据为**************" + strJson
		//			+ "******发送的token是*****" + token);
			if (method.equals("GET")||method.equals("DELETE")) {
				int code = get(adress_Http, token,method);
				return code;
			} else {

				URL my_url = new URL(adress_Http);

				connection = (HttpURLConnection) my_url.openConnection();
				if (!token.equals("")) {
					connection.setRequestProperty("Authorization", "Token "
							+ token);
				}

				connection.setRequestMethod(method);
				connection.setDoOutput(true);
				connection.setDoInput(true);

				connection.setUseCaches(false);

				connection.setInstanceFollowRedirects(true);
				connection.setRequestProperty("Content-Type",
						"application/json");
				connection.connect();

				DataOutputStream out = new DataOutputStream(
						connection.getOutputStream());

				byte[] content = strJson.getBytes("utf-8");

				out.write(content, 0, content.length);
				out.flush();
				out.close(); // flush and close

			}

			int responseCode = connection.getResponseCode();
		//	System.out.println("++++++++返回的状态吗+++++++++" + responseCode);
			// connection.getErrorStream();
			if (responseCode == 400) {
				reader = new BufferedReader(new InputStreamReader(
						connection.getErrorStream(), "utf-8"));
			} else {
				reader = new BufferedReader(new InputStreamReader(
						connection.getInputStream(), "utf-8"));
			}

			// StringBuilder builder = new StringBuilder();

			String line = "";

		//	System.out.println("Contents of post request start");

			while ((line = reader.readLine()) != null) {
				// line = new String(line.getBytes(), "utf-8");
				returnLine += line;

				//System.out.println(line);

			}

		//	System.out.println("Contents of post request ends");

			reader.close();
			connection.disconnect();
			//System.out.println("返回的状态吗" + connection.getResponseCode()
			//		+ "========返回的结果的为========" + returnLine);

			return connection.getResponseCode();
			// returnLine = Chulifanhuidata(returnLine, connection);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 100;

	}

	public static int get(String url1, String token,String method) {
		HttpURLConnection conn = null;
		try {
			// String url1 = Myconstants.restfulbaseurl + "/restful/drivers/";
			// String url1="http://192.168.1.109/zouzhe/servlet/ceshi";
			URL url = new URL(url1);
			// 1.得到HttpURLConnection实例化对象
			conn = (HttpURLConnection) url.openConnection();
			// 2.设置请求信息（请求方式... ...）
			// 设置请求方式和响应时间
			conn.setRequestMethod(method);
			// conn.setRequestProperty("encoding","UTF-8"); //可以指定编码
			conn.setConnectTimeout(5000);
			if (!token.equals("")) {
				conn.setRequestProperty("Authorization", "Token " + token);
			}

			// conn.addRequestProperty("Authorization", "Token" +
			// Myconstants.token);
			// 不使用缓存
			conn.setUseCaches(false);

			int responseCode = conn.getResponseCode();
			System.out.println("++++++++返回的状态吗+++++++++" + responseCode);
			// connection.getErrorStream();
			if (responseCode == 400) {
				reader = new BufferedReader(new InputStreamReader(
						conn.getErrorStream(), "utf-8"));
			} else {
				reader = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "utf-8"));
			}

			// StringBuilder builder = new StringBuilder();

			String line = "";

			System.out.println("Contents of post request start");

			while ((line = reader.readLine()) != null) {
				// line = new String(line.getBytes(), "utf-8");
				returnLine += line;

				//System.out.println(line);

			}

			System.out.println("Contents of post request ends");

			reader.close();
			conn.disconnect();
		//	System.out.println("返回的状态吗" + conn.getResponseCode()
			//		+ "========返回的结果的为========" + returnLine);
			return conn.getResponseCode();
		} catch (Exception e) {
			e.printStackTrace();
			return 100;
		} finally {
			// 4.释放资源
			if (conn != null) {
				// 关闭连接 即设置 http.keepAlive = false;
				conn.disconnect();
			}
		}
	}

	public abstract void sucee(String str);

	public abstract void faile(String err, String rescode);

}
