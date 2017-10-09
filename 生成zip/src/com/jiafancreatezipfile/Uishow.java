package com.jiafancreatezipfile;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Uishow extends JFrame {
	JTextField tx_sdkworkspace;// 定义文本框组件
	JTextField tx_sdkpath;// 定义密码框组件
	JTextField tx_japktoolpath;// 定义密码框组件
	JLabel jLabel1, jLabel2,jLabel3,jLabel4;
	JPanel jp1, jp2, jp3,jp4;
	JButton but_fanbianyi, but_tozip,but_root;

	// 创建按钮
	public Uishow() {
		
		tx_sdkworkspace = new JTextField(20);
		tx_sdkpath = new JTextField(20);
		tx_japktoolpath = new JTextField(20);
		jLabel1 = new JLabel("sdkworkspace");
		jLabel2 = new JLabel("sdkpath");
		jLabel3 = new JLabel("apktoolpath");
		jLabel4 = new JLabel("编译");
		but_fanbianyi = new JButton("反编译apk");
		but_tozip = new JButton("生成zip");
		but_root = new JButton("打开zip目录");
		jp1 = new JPanel();
		jp2 = new JPanel();
		jp4 = new JPanel();
		jp3 = new JPanel();
		tx_sdkworkspace.setText(Myconstant.sdkworkspace);
		tx_sdkpath.setText(Myconstant.sdkpath);
		tx_japktoolpath.setText(Myconstant.apktoolpath);
	
		// 设置布局
		this.setLayout(new GridLayout(5, 1));
		
		
		jp1.add(jLabel1);
		jp1.add(tx_sdkworkspace);// 第一块面板添加用户名和文本框

		jp2.add(jLabel2);
		jp2.add(tx_sdkpath);// 第二块面板添加密码和密码输入框

		jp4.add(jLabel3);
		jp4.add(tx_japktoolpath);// 第二块面板添加密码和密码输入框
		
		jp3.add(but_fanbianyi);
		jp3.add(but_tozip); // 第三块面板添加确认和取消
		jp3.add(but_root);

		// jp3.setLayout(new FlowLayout());
		// 　　//因为JPanel默认布局方式为FlowLayout，所以可以注销这段代码.
		this.add(jp1);
		this.add(jp2);
		this.add(jp4);
		this.add(jLabel4);
		
		this.add(jp3); // 将三块面板添加到登陆框上面
		// 设置显示
		this.setSize(600, 300);
		// this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setTitle("编译zip包");
		

	}

}
