package com.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class GetFileInputStream {
	
	public static String fileName;
	
	public static InputStream  getFileInputStream(){
		// 进行文件的读取.数据源
		SendFile open = new SendFile();
		// 获取全文件名(全路径)
		fileName = open.getFile();
		// 构造一个File对象
		File myFile = new File(fileName);
		// 输入流
		InputStream in = null;

		try {
			// 获取文件的输入流
			in = new FileInputStream(myFile);

		} catch (FileNotFoundException e1) {
			System.out.println("未选中文件！");
		}
		return in;
	}
}
