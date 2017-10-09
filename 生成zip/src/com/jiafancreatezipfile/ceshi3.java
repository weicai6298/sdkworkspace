package com.jiafancreatezipfile;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ceshi3 {
	
	public static String file1;

	public static String getFile1() {
		return file1;
	}

	public static void setFile1(String file1) {
		ceshi3.file1 = file1;
	}

	public static void main(String[] args) {

		SAXReader saxReader = new SAXReader();

		try {
			Document document = saxReader.read(new File(
					"F:\\0azhangjiafan\\f临时文件\\AndroidManifest.xml"));
			Element rootElement = document.getRootElement();
			Attribute rootattribute = rootElement.attribute("package");
			// 获取到包名
			String pagename = rootattribute.getValue();
			System.out.println(pagename);
			Element application = rootElement.element("application");
			application.remove(application.element("activity"));

			// 获取appid
			String appid = "";

			List<Element> elements = application.elements("meta-data");
			// System.out.println(""+elements.size());
			for (int i = 0; i < elements.size(); i++) {

				String an_name = elements.get(i).attributeValue("name");
				if (an_name.equals("qqAppId")) {
					appid = elements.get(i).attributeValue("value");
					//elements.get(i).setAttributeValue("value", "123");
				}
				// System.out.println(an_name);

			}
			String docString = document.asXML();
			System.out.println(appid);
			System.out.println(document.asXML());
			// System .out.println(attribute.getValue());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void reUmengchanel(String xmlpath) {
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(new File(
					"F:\\0azhangjiafan\\f临时文件\\AndroidManifest.xml"));
			Element rootElement = document.getRootElement();
			Attribute rootattribute = rootElement.attribute("package");
			// 获取到包名
			String pagename = rootattribute.getValue();
			System.out.println(pagename);
			Element application = rootElement.element("application");
			application.remove(application.element("activity"));

			// 获取appid
			String appid = "";

			List<Element> elements = application.elements("meta-data");
			// System.out.println(""+elements.size());
			for (int i = 0; i < elements.size(); i++) {

				String an_name = elements.get(i).attributeValue("name");
				if (an_name.equals("UMENG_CHANNEL")) {
					appid = elements.get(i).attributeValue("value");
					elements.get(i).setAttributeValue("value", "123");
				}
				// System.out.println(an_name);

			}
			String docString = document.asXML();
			System.out.println(appid);
			System.out.println(document.asXML());
			// System .out.println(attribute.getValue());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
