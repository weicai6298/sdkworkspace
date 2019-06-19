package com.kkgame.sdk.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

	/**
	 * 读取字节数组
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream)
			throws IOException {
		byte[] data;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 边读边写
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, length);
		}
		outStream.flush();
		outStream.close();
		inputStream.close();
		data = outStream.toByteArray();
		return data;
	}

	/**
	 * 读取字符串
	 * 
	 * @param inputStream
	 * @param charSet
	 *            字符编码集
	 * @return
	 * @throws IOException
	 */
	public static String readInputStream(InputStream inputStream, String charSet)
			throws IOException {
		byte[] data = readInputStream(inputStream);
		return new String(data, charSet);
	}

	/**
	 * 默认用UTF-8解析字符串
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static String readInputStreamUTF8(InputStream inputStream)
			throws IOException {
		byte[] data = readInputStream(inputStream);
		return new String(data, "UTF-8");
	}
}
