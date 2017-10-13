package com.server.control;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.server.util.SendFile;


public class SendFileThread extends Thread {
	private Socket socket;
	private InputStream in;
	public SendFileThread(Socket socket,InputStream in) {
		this.socket = socket;
		this.in = in;
	}

	@Override
	public void run() {
		DataOutputStream os;
		try {
			os = new DataOutputStream(socket.getOutputStream());
			// 数据目的地是另一台主机,通过Socket获取I/O流
			if (in != null) {
				try {
					// 使用UTF-8编码输出文件名
					os.writeUTF(SendFile.name);
					// 每次输出大小
					byte[] b = new byte[1024];
					// 每次读入的实际数据
					int len = 0;
					// 循环读取数据
					while ((len = in.read(b)) != -1) {
						os.write(b, 0, len);
					}

					// 从网络获取数据
					DataInputStream fis = new DataInputStream(socket.getInputStream());
					// 使用UTF-8编码读取数据
					String message = fis.readUTF();
					// 打印消息
					System.out.println(message);

					if (os != null)
						os.close();
//				if (in != null)
//						in.close();
					socket.close();
				} catch (Exception e) {
					System.out.println("貌似zpc不在线,稍后再传！！");
				}
			} else {
				os.writeUTF("over");
				System.out.println("文件读取失败(in = null)！");
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}
