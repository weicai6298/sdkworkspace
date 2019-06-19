package com.kkgame.sdk.utils;

import java.text.DecimalFormat;

public class TextUtil {


	/**
	 * 将字节 按照大小转换成 M K B
	 * 
	 * @param contentLength
	 * @return
	 */
	public static String getContentLengthValue(long contentLength) {
		// M K B
		DecimalFormat formate = new DecimalFormat("###.00");
		String sizeValue = "";
		if (contentLength > 1024 * 1024) {
			double size = contentLength / (1024 * 1024.00);
			sizeValue = formate.format(size) + "M";
		} else if (contentLength > 1024) {
			double size = contentLength / (1024.00);
			sizeValue = formate.format(size) + "K";
		} else {
			sizeValue = contentLength + "B";
		}
		return sizeValue;
	}
}
