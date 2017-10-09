package com.yayawan.impl;

public class Logutils {

	public static boolean canlog=false;
	
	public static void sys(String message){
		if (canlog) {
			System.out.println(message);
		}
		
	}
}
