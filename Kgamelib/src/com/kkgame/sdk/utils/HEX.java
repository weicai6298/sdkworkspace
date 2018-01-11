package com.kkgame.sdk.utils;

public class HEX {

	public static String toHex(byte[] data) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			buffer.append(String.format("%02x", data[i]));
		}
		return buffer.toString();
	}

	public static byte[] toByte(String hexData) {
		if (hexData == null) {
			return null;
		}
		int len = hexData.length();
		if (len % 2 == 1) {
			return null;
		}
		int dataLength = len / 2;
		byte[] result = new byte[dataLength];
		for (int i = 0; i < dataLength; i++) {
			result[i] = (byte) Integer.parseInt(
					hexData.substring(i * 2, i * 2 + 2), 16);
		}
		return result;
	}
}
