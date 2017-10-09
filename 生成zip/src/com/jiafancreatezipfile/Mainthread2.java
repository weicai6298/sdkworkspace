package com.jiafancreatezipfile;

import java.io.File;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.List;

import javax.swing.JLabel;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Mainthread2 {

	private static String packagename;
	static String appid = null;

	public static void createZip(JLabel labe) {
		labe.setText("正在生成zip");
		String sdkpath = Myconstant.sdkpath;
		String delpath = Myconstant.apktoolpath + "\\" + sdkpath + "\\original";
		String delpath2 = Myconstant.apktoolpath + "\\" + sdkpath
				+ "\\apktool.yml";
		String delpath3 = Myconstant.apktoolpath + "\\" + sdkpath
				+ "\\AndroidManifest.xml";
		String zippath = Myconstant.apktoolpath + "\\" + sdkpath;
		String zippathtaget = Myconstant.apktoolpath + "\\" + sdkpath + ".zip";
		// F:\0azhangjiafan\apktoolfan\Sdk360\original
		// Fileutils.delFolder("F:\\0azhangjiafan\\apktoolfan\\Sdk360\\original");
		File file = new File(delpath);
		Fileutils.deleteDir2(file);
		Fileutils.delFile(delpath2);
		String read = Filetextutils.read(delpath3);
		// System.out.println(read);
		// if (zippathtaget.contains("QQ")) {

		SAXReader saxReader = new SAXReader();

		try {
			System.out.println("文件路径" + delpath3);
			saxReader.setEncoding("utf-8");
			Document document = saxReader.read(new File(delpath3));
			
			Element rootElement = document.getRootElement();
			Attribute rootattribute = rootElement.attribute("package");
			// 获取到包名
			packagename = rootattribute.getValue();
			System.out.println(packagename);
			Element application = rootElement.element("application");
			// application.remove(application.element("activity"));

			// 获取appid
			// String appid = "";

			List<Element> elements = application.elements("meta-data");
			// System.out.println(""+elements.size());
			for (int i = 0; i < elements.size(); i++) {

				String an_name = elements.get(i).attributeValue("name");
				if (an_name.equals("qqAppId")) {
					appid = elements.get(i).attributeValue("value");
					// elements.get(i).setAttributeValue("value", "123");
					if (appid.contains("string")) {
						appid = appid.replace("string", "");
					}
				}
				// System.out.println(an_name);

			}
			String docString = document.asXML();
			System.out.println(appid);
			read = document.asXML();
			// System .out.println(attribute.getValue());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// }

		int indexOf = read.indexOf("<activity");
		int indexOf2 = read.indexOf("activity>");
		System.out.println(indexOf + "++" + indexOf2);
		String substring = read.substring(indexOf, indexOf2 + 9);
		System.out.println(substring);
		String replace = read.replace(substring, "");
		System.out.println(replace);
		if (sdkpath.endsWith("SdkLenovo")) {
			replace = replace.replace("android.intent.action.MAIN", "string_action");
			replace = replace.replace("android.intent.category.LAUNCHER",
					"string_category");
		}

		if (packagename != null) {
			replace = replace
					.replaceFirst(packagename, "jiafantemppackagename");
			System.out.println("==================");
			System.out.println("第一次替换包名" + replace);
			System.out.println("==================");
			replace = replace.replaceAll(packagename, "string-package");
			System.out.println("==================");
			System.out.println("第二次次替换包名" + replace);
			System.out.println("==================");
			if (appid != null) {
				System.out.println("appid不为空" + appid);
				replace = replace.replaceAll(appid, "string_appid");
			}
			replace = replace.replaceAll("jiafantemppackagename", packagename);
			System.out.println("==================");
			System.out.println("第三次次替换包名" + replace);
			System.out.println("==================");
		}

		Filetextutils.write(delpath3, false, replace);
		// Fileutils.zip(zippath, Myconstant.fileroot);
		System.out.println(zippathtaget + "   " + zippath);
		labe.setText("开始压缩");
		Fileutils.zip(zippathtaget, zippath);
		labe.setText("成功生成zip");

		// 测试解析xml文件

		// System.out.println("得到的包名是：" + root.getAttribute("package"));
		// Element element = Xmlutils.getElement(root, "manifest");
		// String packagename = element.getAttribute("package");
		// System.out.println("得到的包名是："+element.toString());
		/*
		 * String id = XmlUtils.getElementValue(root, "id"); String nick =
		 * XmlUtils.getElementValue(root, "nick"); String email =
		 * XmlUtils.getElementValue(root, "email"); String gender =
		 * XmlUtils.getElementValue(root, "gender");
		 * 
		 * Element contactE = XmlUtils.getChildElement(root, "contact"); String
		 * address = XmlUtils.getElementValue(contactE, "address"); String
		 * postCode = XmlUtils.getElementValue(contactE, "post-code"); String
		 * telephone = XmlUtils.getElementValue(contactE, "telephone");
		 */
	}

	public static InputStream Str2Inputstr(String inStr) {
		try {
			// return new ByteArrayInputStream(inStr.getBytes());
			// return new ByteArrayInputStream(inStr.getBytes("UTF-8"));
			return new StringBufferInputStream(inStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
