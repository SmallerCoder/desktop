package com.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class GetFileInputStream {
	
	public static String fileName;
	
	public static InputStream  getFileInputStream(){
		// �����ļ��Ķ�ȡ.����Դ
		SendFile open = new SendFile();
		// ��ȡȫ�ļ���(ȫ·��)
		fileName = open.getFile();
		// ����һ��File����
		File myFile = new File(fileName);
		// ������
		InputStream in = null;

		try {
			// ��ȡ�ļ���������
			in = new FileInputStream(myFile);

		} catch (FileNotFoundException e1) {
			System.out.println("δѡ���ļ���");
		}
		return in;
	}
}
