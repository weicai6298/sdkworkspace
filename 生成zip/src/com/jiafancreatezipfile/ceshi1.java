package com.jiafancreatezipfile;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.crypto.Data;

public class ceshi1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			//企业
		String uidtemp=System.currentTimeMillis()+"kk";
		String uid=uidtemp.substring(4, uidtemp.length())+new Random().nextInt(10);
		System.out.println(uid);   
	}

}
