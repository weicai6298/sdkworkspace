package com.kkgame.sdk.utils;

public class GoogleAuthTest {

	/**
	 * 随机生成一个key值
	 */
	public static void genSecretTest() {
		String secret = GoogleAuthenticator.generateSecretKey();

		//System.out.println("Secret key is " + secret);
	}

	public static void authTest(long code) {
		// enter the code shown on device. Edit this and run it fast before the
		// code expires!
		// long code = 205263;
		long t = System.currentTimeMillis();
		GoogleAuthenticator ga = new GoogleAuthenticator();
		ga.setWindowSize(5); // should give 5 * 30 seconds of grace...
		boolean r = ga.check_code("QVJJGVFHH5YZHUPI", code, t);
		//System.out.println("Check code = " + r);
	}
}
