package com.server.util;

import java.awt.FileDialog;

import javax.swing.JFrame;

public class SendFile extends JFrame{
	
	public static String name = null;

	private static final long serialVersionUID = 1L;
	
	public static String fileName = null;

		// ѡ����ļ�
		public String getFile() {
			//��һ��FileDialog,���ñ����ģʽ����ȡ�ļ���
			FileDialog fd = new FileDialog(this, "��ѡ��Ҫ����zpc���ļ�", FileDialog.LOAD);
			
			//���ô򿪶Ի���ĸ�Ŀ¼
			fd.setDirectory("C:\\");
			
			fd.setVisible(true);
			/*
			 * ���û�ѡ����ȡ����ť���򷵻�null
			 */
			if (fd.getFile() != null) {
				//��ȡ�ļ���ȫ�ļ���	
				fileName = fd.getDirectory()+fd.getFile();
				
				name = fd.getFile();
				
				System.out.println("��ѡ��� " + fileName);
			}
			//�����ļ�ȫ·��
			return fileName;
		}
}
