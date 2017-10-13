package com.server;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.server.control.NewWatchFrame;
import com.server.util.GenerateDate;
import com.server.util.ReceiveFileThread;

public class ServerThread extends Thread {

	public Socket socket;
	public int flag;
	private String ip;
	private DataInputStream in = null;
	private ReceiveFileThread RC = null;

	public ServerThread(Socket socket, int flag, String ip) {
		try {
			this.socket = socket;
			this.flag = flag;
			this.ip = ip;
			this.in = new DataInputStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void run() {

		System.out.println(this.getName());

		try {
			System.out.println(13346);
			String s = in.readUTF();
			System.err.println(s + "232");
			if (s.equals("hand")) {
				if (Server.ipMap2.get(ip) != null) {
					Server_Frame.getTextArea01().append(
							GenerateDate.getDate() + " : "
									+ Server.ipMap2.get(ip) + "    同学举手！"
									+ "\n");
				} else {
					Server_Frame.getTextArea01().append(
							GenerateDate.getDate() + " : " + flag + "号同学举手！"
									+ "\n");
				}
				System.out.println("12321432");
			} else if (s.equals("1")) {

			} else if (s.equals("file")) {
				RC = new ReceiveFileThread(socket, in);

				RC.start();

				RC.join();
				System.err.println(Server.fileName);
				if (Server.ipMap2.get(ip) != null && Server.fileName != null) {
					Server_Frame.getTextArea01().append(
							GenerateDate.getDate() + " : 已接收       "
									+ Server.ipMap2.get(ip) + "同学作业！" + "\n");
				} else if (Server.fileName != null) {
					Server_Frame.getTextArea01().append(
							GenerateDate.getDate() + " : 已接收       " + flag
									+ "号同学作业！" + "\n");
				}
			} else if (s.equals("question")) {
				System.out.println(s+"1");
				s = in.readUTF();
				System.out.println(s);
				if (Server.ipMap2.get(ip) != null) {
					Server_Frame.getTextArea01().append(
							GenerateDate.getDate() + " : "
									+ Server.ipMap2.get(ip) + "同学提问：" + s + "\n");
				} else {
					Server_Frame.getTextArea01().append(
							GenerateDate.getDate() + " : " + flag + "号同学提问：  "
									+ s +"\n");
				}

			} else if(s.equals("dao")){
				System.out.println(s+"1");
				s = in.readUTF();
				System.out.println(s);
				if (Server.ipMap2.get(ip) != null) {
					Server_Frame.getTextArea01().append(
							GenerateDate.getDate() + " : "
									+ Server.ipMap2.get(ip) + "  同学已签到！" + "\n");
				} else {
					Server_Frame.getTextArea01().append(
							GenerateDate.getDate() + " : " + s + "  已签到！"
									+ "\n");
					Server.ipMap2.put(ip, s);
				}
			}
			BufferedImage image = ImageIO.read(socket.getInputStream());

			// 测试
			System.out.println("正在读取图像");
			// 测试
			if (socket.getInputStream() == null) {
				System.out.println("输出流为空！");
			}
			// 测试
			if (socket == null) {
				System.out.println("socket为空1");
			}
			// 测试
			if (socket == null) {
				System.out.println("socket为空2");
			}
			// 测试
			if (image == null) {
				System.out.println("图像为空");
			}
			// 测试
			if (socket.getInputStream() == null) {
				System.out.println("输入流为空！");
			}
			// 测试
			System.out.println("成功收到图像");
			if (image != null) {
				if (Server_Frame.isMonitor == true) {
					// size数量，往第1个按钮写图片
					Server_Frame.bt[flag - 1].setIcon(new ImageIcon(image
							.getScaledInstance(290, 180,
									Image.SCALE_AREA_AVERAGING)));
					Server_Frame.bt[flag - 1].setText(ip + "   " + flag + "号机");
					Server_Frame.bt[flag - 1].setIconTextGap(10);
					/*
					 * 显示查看那个按钮（电脑）的图像
					 */
					if (Server_Frame.clickedNum == flag&&Server_Frame.isLarge) {

						Server_Frame.bt[flag - 1].setIcon(new ImageIcon(image));

						NewWatchFrame.label.setIcon(Server_Frame.bt[flag - 1]
								.getIcon());
						/*
						 * 这里有一个小小的bug,那个如果点击第一个时会出现被放大了
						 */
					}
				}
			}
			// 测试
			System.out.println("成功写入按钮");
			socket.close();
			System.out.println("3、" + Thread.currentThread().isAlive());
			System.out.println("4、" + socket.isClosed());
			System.out.println("5、" + socket.isConnected());// 关闭socket该方法并不会返回false
		} catch (IOException e) {

			// 这里有待测试多台机情况
			/*
			 * 步骤： 1、打开服务器、打开多个客户端 2、关闭其中的一台，再发送文件
			 * 
			 * 3、再关闭一台，再发送文件
			 */
			if (Server.count == 0) {
				Server.count++;
			} else {
				System.out.println("千" + Server.count);
				Server.count++;
				System.out.println("后" + Server.count);
			}
			
			/*
			 * 如果还没有签到，则显示成几号同学，如果签到了，则显示姓名
			 */
			if(Server.ipMap2.get(ip)!=null){
				Server_Frame.getTextArea03().append(
						GenerateDate.getDate() + Server.ipMap2.get(ip) + "同学已下线" + "\n");
			}else{
				Server_Frame.getTextArea03().append(
						GenerateDate.getDate() + flag + "号电脑已下线" + "\n");
			}

			Server.onLineNum--;
			System.out.println("服务器已关闭!");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println(213);
			e.printStackTrace();
		}
	}
}
