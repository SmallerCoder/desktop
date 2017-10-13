package com.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;

import java.io.DataOutputStream;

public class ClientThread extends Thread {
	private String imageFormat = "jpg";
	Socket socket = null;
	private DataOutputStream os = null;

	public ClientThread(Socket socket) {
			this.socket = socket;
			try {
				this.os = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public synchronized void run() {
			try {
				os.writeUTF("1");
				Client.hand = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.out.println("正在生成图像");
		// 获取屏幕画布
		BufferedImage image = new ShotImage().snapShot();
		/*
		 * 如果屏幕画布获取不到，则报出异常IoException
		 */
		if (image == null)
			System.out.println("图像获取不到！");
		// 使用支持给定格式的任意 ImageWriter 将一个图像写入 OutputStream。
		
		if(socket==null){
			System.out.println("sad");
		}
		try {
			ImageIO.write(image, imageFormat, socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("2");
			/*
			 * 对应Client的flag
			 */
			Client.flag=false;
			System.out.println("服务器已关闭23");
		}
		System.out.println("写出图像成功");
		try {
			socket.close();
			System.out.println("3、"+Thread.currentThread().isAlive());
			System.out.println("4、"+socket.isClosed());
			System.out.println("5、"+socket.isConnected());//关闭socket该方法并不会返回false
		} catch (IOException e) {
			/*
			 * 对应Client的flag
			 */
			Client.flag=false;
			System.out.println("服务器已关闭");
			e.printStackTrace();
		}
	}
}
