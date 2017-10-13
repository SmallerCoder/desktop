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
		System.out.println("��������ͼ��");
		// ��ȡ��Ļ����
		BufferedImage image = new ShotImage().snapShot();
		/*
		 * �����Ļ������ȡ�������򱨳��쳣IoException
		 */
		if (image == null)
			System.out.println("ͼ���ȡ������");
		// ʹ��֧�ָ�����ʽ������ ImageWriter ��һ��ͼ��д�� OutputStream��
		
		if(socket==null){
			System.out.println("sad");
		}
		try {
			ImageIO.write(image, imageFormat, socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("2");
			/*
			 * ��ӦClient��flag
			 */
			Client.flag=false;
			System.out.println("�������ѹر�23");
		}
		System.out.println("д��ͼ��ɹ�");
		try {
			socket.close();
			System.out.println("3��"+Thread.currentThread().isAlive());
			System.out.println("4��"+socket.isClosed());
			System.out.println("5��"+socket.isConnected());//�ر�socket�÷��������᷵��false
		} catch (IOException e) {
			/*
			 * ��ӦClient��flag
			 */
			Client.flag=false;
			System.out.println("�������ѹر�");
			e.printStackTrace();
		}
	}
}
