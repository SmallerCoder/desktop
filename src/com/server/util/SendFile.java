package com.server.util;

import java.awt.FileDialog;

import javax.swing.JFrame;

public class SendFile extends JFrame{
	
	public static String name = null;

	private static final long serialVersionUID = 1L;
	
	public static String fileName = null;

		// 选择打开文件
		public String getFile() {
			//打开一个FileDialog,设置标题和模式（读取文件）
			FileDialog fd = new FileDialog(this, "请选择要传给zpc的文件", FileDialog.LOAD);
			
			//设置打开对话框的根目录
			fd.setDirectory("C:\\");
			
			fd.setVisible(true);
			/*
			 * 当用户选择了取消按钮，则返回null
			 */
			if (fd.getFile() != null) {
				//获取文件的全文件名	
				fileName = fd.getDirectory()+fd.getFile();
				
				name = fd.getFile();
				
				System.out.println("已选择打开 " + fileName);
			}
			//返回文件全路径
			return fileName;
		}
}
