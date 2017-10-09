package com.jiafancreatezipfile;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Mainthread {

	private static Uishow uishow;

	public static void main(String[] args) {
		
		uishow = new Uishow();
		uishow.but_fanbianyi.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println(uishow.tx_sdkworkspace.getText());
				Myconstant.apktoolpath=uishow.tx_japktoolpath.getText();
				Myconstant.sdkpath=uishow.tx_sdkpath.getText();
				Myconstant.sdkworkspace=uishow.tx_sdkworkspace.getText();
				//System.out.println(Myconstant.sdkworkspace);
				Cmd.apktooljieya(Myconstant.sdkpath);
			}
		});
		uishow.but_tozip.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Mainthread2.createZip(uishow.jLabel4);
			}
		});
		
		uishow.but_root.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Cmd.startApkroot();
			}
		});
		
		//String sdkpath=Myconstant.sdkpath;
		//Cmd.apktooljieya(Myconstant.sdkpath);
		//String delpath3=Myconstant.fileroot+sdkpath+"\\AndroidManifest.xml";
		//String read = Filetextutils.read(delpath3);
		//Filetextutils.write("\\\\192.168.1.246\\packsdk\\sdk\\AndroidManifest.xml", false, read);
	/*	String cunfangdizhi="F:/0azhangjiafan";
		String read = Filetextutils.read("F:/0azhangjiafan/jiafanjar/apkide/ApkIDE/Work/com.andgame.airfight.mzyw/AndroidManifest.xml");
		System.out.println(read);
		int indexOf = read.indexOf("<activity");
		int indexOf2 = read.indexOf("activity>");
		System.out.println(indexOf+"++"+indexOf2);
		String substring = read.substring(indexOf, indexOf2+9);
		System.out.println(substring);
		String replace = read.replace(substring, "");
		System.out.println(replace);*/
	}
	
	public void reumengXML(String xmlpath){
		
		
	}
}
