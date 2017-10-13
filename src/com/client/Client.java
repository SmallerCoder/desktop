package com.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.client.util.SendFile;
import com.client.util.ToolTip;
import com.client.util.GetFileInputStream;
import com.client.util.RCServer;
import com.client.util.SendFileThread;

public class Client {

	public static boolean flag = true;
	
	public static boolean hand = false;
	
	public static boolean isQuestion = false;
	
	public static boolean isDao = false;
	
	public static String IP = null;
	
	public static boolean isFile = false;

	/*
	 * �ļ�����ģ���м����
	 */
	public static char c = '1';
	public static boolean isOver = false;

	private static File tempFile = null;

	private SystemTray tray;

	private TrayIcon trayIcon;

	public Client() {

		if (SystemTray.isSupported()) {

			System.out.println("�� ��tray");

			tray();

		}

	}

	private void tray() {

		// ��ñ�����ϵͳ���̵�ʵ��

		tray = SystemTray.getSystemTray();

		// ��ʾ�������е�ͼ��

		ImageIcon icon = new ImageIcon(this.getClass().getResource("/images/huaji.png"));

		// ����һ���Ҽ�����ʽ�˵�

		PopupMenu pop = new PopupMenu();

		MenuItem handUp = new MenuItem("����");

		MenuItem question = new MenuItem("����");

		MenuItem dao = new MenuItem("ǩ��");
		
		MenuItem sendFile = new MenuItem("�ύ��ҵ");

		MenuItem exit = new MenuItem("�رշ���");
		
		pop.addSeparator();
		pop.add(sendFile);
		pop.addSeparator();
		pop.add(question);
		pop.addSeparator();
		pop.add(dao);
		pop.addSeparator();
		pop.add(handUp);
		pop.addSeparator();
		pop.add(exit);
		pop.addSeparator();
		trayIcon = new TrayIcon(icon.getImage(), "ʵ���ҵ��ӽ�ѧ���ϵͳ(ѧ����)", pop);

		// ������Ҫ��û�лᵼ��ͼƬ��ʾ������

		trayIcon.setImageAutoSize(true);

		/*
		 * ������½�ͼ����¼�
		 */
		trayIcon.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				// if (e.getClickCount() == 2) {
				//
				// tray.remove(trayIcon);
				//
				// }

			}

		});
		try {
			tray.add(trayIcon);
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		/*
		 * Ϊ�˵�������¼�
		 */
		exit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				tray.remove(trayIcon);
				tempFile.deleteOnExit();
				System.exit(0);
			}

		});
		dao.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				isDao = true;
			}
		});
		handUp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				hand = true;
			}
		});
		
		question.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				isQuestion = true;
			}
		});
		sendFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				isFile = true;
			}
		});
	}

	/*
	 * ��Ļ���������������
	 */
	private static void connectServer(Socket socket, int time) throws Exception {
		c = '1';
	 DataOutputStream os = null;
	 
//	 DataInputStream in = null;
	 SendFileThread s = null;
	 
	 InputStream ins = null;
		/*
		 * ������߳�
		 */
		Thread.sleep(2* 1000);
		try {
			os = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//�ڴ˿����������ʹ��ܣ���ѧ�������������Ϊ�ı����͸���ʦ
		if(hand){
			os.writeUTF("hand");
			hand = false;
		}else if(isQuestion){
			os.writeUTF("question");
			String message = JOptionPane.showInputDialog(null, "�������������⣡", "��������~~~",JOptionPane.QUESTION_MESSAGE);
			os.writeUTF(message);
			System.out.println(213345);
			isQuestion = false;
		}else if(isDao){
			os.writeUTF("dao");
			String message = JOptionPane.showInputDialog(null,
					"����������ѧ�ź�������", "��Ҫǩ��~~~",
					JOptionPane.QUESTION_MESSAGE,
					new ImageIcon("images/huaji.png"), null,
					"����������").toString();
			System.out.println(213);
			os.writeUTF(message);
			isDao = false;
			System.out.println(21332);
		}else if(isFile){
			os.writeUTF("file");
			ins = GetFileInputStream.getFileInputStream();
			System.err.println(ins);
			if(ins==null){
				os.writeUTF("over");
				isFile = false;
				JOptionPane.showMessageDialog(null, "����ʧ�ܣ�");
				return ;
			}
//			in = new DataInputStream(ins);
			s = new SendFileThread(socket,ins);
			s.start();
			
			s.join();
			
			isFile = false;
			SendFile.fileName = null;
			JOptionPane.showMessageDialog(null, "���ͳɹ���");
		}else{
			// �����ʱ����Ҫ
			if (socket != null) {
				ClientThread a = new ClientThread(socket);
				// �����߳��Ѿ�����������false
				System.out.println("1��isAlive" + a.isAlive());
				a.start();
				// Thread.sleep(2000);//�ټӵ�ϵͳ��ʱ��1�벻�У�����2��
				// �����߳��Ѿ�����
				System.out.println("2��isAlive" + a.isAlive());
			}
		}
	}

	/*
	 * ������
	 */
	public static void main(String[] args) {
		Socket socket = null;
		ReceiveFileThread b;
		DataInputStream fis = null;
		String mes = null;
		
		IP = JOptionPane.showInputDialog("�����������IP��ַ....", "127.0.0.1");
		int time = (int) (Math.random() * 3 + 3);
		tempFile = new File("E:/temp.txt");
		try {
			if (tempFile.exists()) {
				JOptionPane.showMessageDialog(null, "������������", "����",
						JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			} else {
				new Client();
				tempFile.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();

		}

		while (true) {
			try {
				System.out.println("6");
				socket = new Socket(IP, 12000);
				System.out.println(5242);
				connectServer(socket, time);
			} catch (IOException e) {
				System.out.println("02");
				/*
				 * �жϷ������Ƿ��������˻���һ���˻��Ƕ����
				 */

				// �����������ļ�
				while (c == '1') {
					System.out.println(1);
					try {
						socket = new Socket(IP, 10000);
						fis = new DataInputStream(socket.getInputStream());// �͸����ļ���ͬ���������ϴ��ļ��Ǵ�Socket�л�ȡ��I/O������

						mes = fis.readUTF();

						System.out.println(mes);

						System.out.println("fis===" + fis);
					} catch (UnknownHostException e2) {
						e2.printStackTrace();
					} catch (IOException e2) {
						System.out.println(4454545);
						JOptionPane.showMessageDialog(null, "�Բ��𣬷�����δ���ӣ�","ע��~~~",JOptionPane.ERROR_MESSAGE);
						tempFile.deleteOnExit();
						System.exit(0);
					} // ����д��Ŀ�����IP��ַ������һ�����ŵ�δ����������ռ�õĶ˿ں�
					System.out.println(socket);
					/*
					 * �����ļ������߳�
					 */
					System.out.println("212131");
					// isOver = false;

					switch (mes) {
					case "file":// �ļ�
						if (!isOver && c == '1') {
							System.out.println("211");
							try {
								b = new ReceiveFileThread(socket, fis);
								b.start();
								// �ȴ������߳����
								b.join();
								// �������ȡ����
								System.out.println(2);
								c = '0';
								socket.close();
							} catch (InterruptedException e1) {
								System.out.println("�ȱ������");
								e1.printStackTrace();
							} catch (UnknownHostException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						break;
					case "message":// ��Ϣ
						try {
							String message = fis.readUTF();
							// �ڴ�ʵ������
							ToolTip.main(message);
							c = '0';// ÿһ��caseһ��Ҫ�ı�״̬
							break;
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							System.out.println("������");
							tempFile.deleteOnExit();
							System.exit(0);
						}// �͸����ļ���ͬ���������ϴ��ļ��Ǵ�Socket�л�ȡ��I/O������
					case "single" :
						try {
							String message = fis.readUTF();
							// �ڴ�ʵ������
							ToolTip.main(message);
							c = '0';// ÿһ��caseһ��Ҫ�ı�״̬
							break;
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							System.out.println("������");
							tempFile.deleteOnExit();
							System.exit(0);
						}// �͸����ļ���ͬ���������ϴ��ļ��Ǵ�Socket�л�ȡ��I/O������
					case "s":
						try {
							RCServer.main();
							c = '0';
							break;
						} catch (Exception e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
					case "dao":
						try {
							isDao = true;

							// �����ֵ�쳣δ���
							// ���е���ǩ��̫����
							String message = JOptionPane.showInputDialog(null,
									"����������ѧ�ź�������", "��Ҫǩ��~~~",
									JOptionPane.QUESTION_MESSAGE,
									new ImageIcon("images/huaji.png"), null,
									"����������").toString();
							new DataOutputStream(socket.getOutputStream())
									.writeUTF(message);
							System.out.println("д���ɹ�");
							c = '0';
							isDao = false;
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							System.out.println(45);
							e1.printStackTrace();
						}
						break;
					case "1" :
						c = '0';
						break;
					default:
						break;
					}
				}
			} catch (Exception e) {
				System.out.println("hhe");
				e.printStackTrace();
			}
			if (flag == false) {
				System.out.println("�������ѹر�");
				tempFile.deleteOnExit();
				break;
			}
		}
	}
}