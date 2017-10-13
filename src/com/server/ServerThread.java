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
									+ Server.ipMap2.get(ip) + "    ͬѧ���֣�"
									+ "\n");
				} else {
					Server_Frame.getTextArea01().append(
							GenerateDate.getDate() + " : " + flag + "��ͬѧ���֣�"
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
							GenerateDate.getDate() + " : �ѽ���       "
									+ Server.ipMap2.get(ip) + "ͬѧ��ҵ��" + "\n");
				} else if (Server.fileName != null) {
					Server_Frame.getTextArea01().append(
							GenerateDate.getDate() + " : �ѽ���       " + flag
									+ "��ͬѧ��ҵ��" + "\n");
				}
			} else if (s.equals("question")) {
				System.out.println(s+"1");
				s = in.readUTF();
				System.out.println(s);
				if (Server.ipMap2.get(ip) != null) {
					Server_Frame.getTextArea01().append(
							GenerateDate.getDate() + " : "
									+ Server.ipMap2.get(ip) + "ͬѧ���ʣ�" + s + "\n");
				} else {
					Server_Frame.getTextArea01().append(
							GenerateDate.getDate() + " : " + flag + "��ͬѧ���ʣ�  "
									+ s +"\n");
				}

			} else if(s.equals("dao")){
				System.out.println(s+"1");
				s = in.readUTF();
				System.out.println(s);
				if (Server.ipMap2.get(ip) != null) {
					Server_Frame.getTextArea01().append(
							GenerateDate.getDate() + " : "
									+ Server.ipMap2.get(ip) + "  ͬѧ��ǩ����" + "\n");
				} else {
					Server_Frame.getTextArea01().append(
							GenerateDate.getDate() + " : " + s + "  ��ǩ����"
									+ "\n");
					Server.ipMap2.put(ip, s);
				}
			}
			BufferedImage image = ImageIO.read(socket.getInputStream());

			// ����
			System.out.println("���ڶ�ȡͼ��");
			// ����
			if (socket.getInputStream() == null) {
				System.out.println("�����Ϊ�գ�");
			}
			// ����
			if (socket == null) {
				System.out.println("socketΪ��1");
			}
			// ����
			if (socket == null) {
				System.out.println("socketΪ��2");
			}
			// ����
			if (image == null) {
				System.out.println("ͼ��Ϊ��");
			}
			// ����
			if (socket.getInputStream() == null) {
				System.out.println("������Ϊ�գ�");
			}
			// ����
			System.out.println("�ɹ��յ�ͼ��");
			if (image != null) {
				if (Server_Frame.isMonitor == true) {
					// size����������1����ťдͼƬ
					Server_Frame.bt[flag - 1].setIcon(new ImageIcon(image
							.getScaledInstance(290, 180,
									Image.SCALE_AREA_AVERAGING)));
					Server_Frame.bt[flag - 1].setText(ip + "   " + flag + "�Ż�");
					Server_Frame.bt[flag - 1].setIconTextGap(10);
					/*
					 * ��ʾ�鿴�Ǹ���ť�����ԣ���ͼ��
					 */
					if (Server_Frame.clickedNum == flag&&Server_Frame.isLarge) {

						Server_Frame.bt[flag - 1].setIcon(new ImageIcon(image));

						NewWatchFrame.label.setIcon(Server_Frame.bt[flag - 1]
								.getIcon());
						/*
						 * ������һ��СС��bug,�Ǹ���������һ��ʱ����ֱ��Ŵ���
						 */
					}
				}
			}
			// ����
			System.out.println("�ɹ�д�밴ť");
			socket.close();
			System.out.println("3��" + Thread.currentThread().isAlive());
			System.out.println("4��" + socket.isClosed());
			System.out.println("5��" + socket.isConnected());// �ر�socket�÷��������᷵��false
		} catch (IOException e) {

			// �����д����Զ�̨�����
			/*
			 * ���裺 1���򿪷��������򿪶���ͻ��� 2���ر����е�һ̨���ٷ����ļ�
			 * 
			 * 3���ٹر�һ̨���ٷ����ļ�
			 */
			if (Server.count == 0) {
				Server.count++;
			} else {
				System.out.println("ǧ" + Server.count);
				Server.count++;
				System.out.println("��" + Server.count);
			}
			
			/*
			 * �����û��ǩ��������ʾ�ɼ���ͬѧ�����ǩ���ˣ�����ʾ����
			 */
			if(Server.ipMap2.get(ip)!=null){
				Server_Frame.getTextArea03().append(
						GenerateDate.getDate() + Server.ipMap2.get(ip) + "ͬѧ������" + "\n");
			}else{
				Server_Frame.getTextArea03().append(
						GenerateDate.getDate() + flag + "�ŵ���������" + "\n");
			}

			Server.onLineNum--;
			System.out.println("�������ѹر�!");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println(213);
			e.printStackTrace();
		}
	}
}
