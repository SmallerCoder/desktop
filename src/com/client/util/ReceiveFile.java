package com.client.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ReceiveFile extends JFrame{

	private static final long serialVersionUID = 1L;

		// 保存文件
		public OutputStream saveFile(String fileName) {
			
			OutputStream os = null;
			
			try {
//				//打开一个FileDialog,设置标题和模式（写文件）
//				FileDialog fd = new FileDialog(this, "保存文件", FileDialog.SAVE);
//				
//				// 对话框打开时显示默认的目录
//				fd.setDirectory("C:\\");
//				
//				fd.setFile(fileName);
//				
//				fd.setVisible(true);
//				
//				if (fd.getFile() != null) {
				
//					File myFile = new File("C:\\Users\\A1\\Desktop",fileName);
				File myFile = new File("E:\\",fileName);
					//将该文件发送到输出流中
					os = new FileOutputStream(myFile);
//				}
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(this, "文件保存发生错误，已停止");
			}
			return os;
		}
}
