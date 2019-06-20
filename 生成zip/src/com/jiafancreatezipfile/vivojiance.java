package com.jiafancreatezipfile;

import java.io.File;

public class vivojiance{
//	public static String filepath="F:\\0azhangjiafan\\jiafanjar\\apkide123\\ApkIDE\\Work\\com.yl.zsbb.vivo";
	public static String filepath= "I:\\F\\apkIDE\\ApkIDE\\Work\\com.hulian.hzsh";
	public static void main(String[] args) {
		/*String read = Filetextutils.read("F:\\0azhangjiafan\\fsdkwork1\\Sdkxxzhushou\\AndroidManifest.xml");
		System.out.println(read);
		Filetextutils.write("F:\\0azhangjiafan\\fsdkwork1\\Sdkxxzhushou\\AndroidManifest1.xml", false, read);*/
		checkfile(filepath);
	}
//https://h5cqllyx.jiulingwan.com/webserver/07073/android/login.php?uid=3518138306526142854&username=13711676298&token=f717da1812fae62566f7841ec6204c20

	public static void checkfile(String path){
		//System.out.println("+++++++++"+path);
		File file = new File(path);
		if (file.isDirectory()) {
			//file.list()
			File[] listFiles = file.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				File file2 = new File(listFiles[i].getAbsolutePath());
				
				if (file2.getAbsolutePath().toString().contains("Alipay")) {
					System.out.println(listFiles[i].getAbsolutePath());
				}
				if (file2.getAbsolutePath().toString().contains("alipay")) {
					System.out.println(listFiles[i].getAbsolutePath());
				}
				if (file2.getAbsolutePath().toString().contains("weixin")) {
					System.out.println(listFiles[i].getAbsolutePath());
				}
				if (file2.getAbsolutePath().toString().contains("Weixin")) {
					System.out.println(listFiles[i].getAbsolutePath());
				}
				if (file2.getAbsolutePath().toString().contains("WX")) {
					System.out.println(listFiles[i].getAbsolutePath());
				}			
				//System.out.println(file2.getAbsolutePath());
				if (file2.isDirectory()) {
					//System.out.println(file2.getAbsolutePath());
					checkfile(listFiles[i].getAbsolutePath());
				}else {
					String read = Filetextutils.read(listFiles[i].getAbsolutePath());
					if (read.toString().contains("Alipay")) {
						System.out.println("Alipay"+listFiles[i].getAbsolutePath());
					}
					if (read.toString().contains("alipay")) {
						System.out.println("alipay"+listFiles[i].getAbsolutePath());
					}
					if (read.toString().contains("weixin")) {
						System.out.println("weixin"+listFiles[i].getAbsolutePath());
					}
					if (read.toString().contains("Weixin")) {
						System.out.println("Weixin"+listFiles[i].getAbsolutePath());
					}
					if (read.toString().contains("WX")) {
						//System.out.println(listFiles[i].getAbsolutePath());
					}
				}
			}
		}else {
			System.out.println("不是文件");
		}
	}
}
