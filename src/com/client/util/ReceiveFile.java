package com.client.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ReceiveFile extends JFrame{

	private static final long serialVersionUID = 1L;

		// �����ļ�
		public OutputStream saveFile(String fileName) {
			
			OutputStream os = null;
			
			try {
//				//��һ��FileDialog,���ñ����ģʽ��д�ļ���
//				FileDialog fd = new FileDialog(this, "�����ļ�", FileDialog.SAVE);
//				
//				// �Ի����ʱ��ʾĬ�ϵ�Ŀ¼
//				fd.setDirectory("C:\\");
//				
//				fd.setFile(fileName);
//				
//				fd.setVisible(true);
//				
//				if (fd.getFile() != null) {
				
//					File myFile = new File("C:\\Users\\A1\\Desktop",fileName);
				File myFile = new File("E:\\",fileName);
					//�����ļ����͵��������
					os = new FileOutputStream(myFile);
//				}
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(this, "�ļ����淢��������ֹͣ");
			}
			return os;
		}
}
