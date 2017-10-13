package com.server.util;

import java.io.DataInputStream;
import java.io.OutputStream;
import java.net.Socket;


import com.client.util.ReceiveFile;
import com.server.Server;

public class ReceiveFileThread extends Thread {

	private Socket socket;
	DataInputStream fis;

	public ReceiveFileThread(Socket socket,DataInputStream fis) {

		this.socket = socket;
		this.fis=fis;

	}

	@Override
	public synchronized void run() {

		OutputStream fos = null;

		try {
			System.out.println(1);
			System.out.println(socket.getInputStream());
			System.out.println(2);
			// 获取输入流
			
			System.out.println(3);
			System.out.println(fis);
			// 获取文件名
			 Server.fileName = fis.readUTF();
			if(Server.fileName.equals("over")){
				Server.fileName = null;
				return ;
			}
			System.out.println("0000");
			System.out.println(Server.fileName);
			System.out.println(4);
			// 获取输出流
//			DataOutputStream fos1 = new DataOutputStream(socket.getOutputStream());// 写回信息;
			System.out.println(5);
//			if ((JOptionPane.showOptionDialog(null, "您想接受文件" + fileName + "吗？", "消息", JOptionPane.YES_NO_OPTION, 0,
//					null, null, null)) == 0) {
				System.out.println("我已选择接受文件！");


				// 保存文件
				fos = new ReceiveFile().saveFile(Server.fileName);
				
				
				/*
				 * 
				 * 测试多台机器是否能正确接收文件
				 * 客户端问题
				 * 
				 * 
				 * 
				 */
				System.out.println(fos);
				
				/*
				 * 往文件里写东西
				 */
				int len = 0;

				byte[] bytes = new byte[1024];

				if (fis != null) {
					while ((len = fis.read(bytes)) != -1) {
						fos.write(bytes, 0, len);
					}
				}
				//JOptionPane.showMessageDialog(null, "文件保存成功！");
//				Client.isOver=true;

				if (fos != null)
					fos.close();
//			} else {
//				System.out.println("我选择了拒绝接受！");
//				String message2 = "提示：对方已拒绝接受！";
//				fos1.writeUTF(message2);
//			}
		} catch (Exception e) {
			e.printStackTrace();
			
			System.out.println("接受文件失败！");
		}
	}
}
