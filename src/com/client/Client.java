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
	 * 文件发送模块中间变量
	 */
	public static char c = '1';
	public static boolean isOver = false;

	private static File tempFile = null;

	private SystemTray tray;

	private TrayIcon trayIcon;

	public Client() {

		if (SystemTray.isSupported()) {

			System.out.println("调 用tray");

			tray();

		}

	}

	private void tray() {

		// 获得本操作系统托盘的实例

		tray = SystemTray.getSystemTray();

		// 显示在托盘中的图标

		ImageIcon icon = new ImageIcon(this.getClass().getResource("/images/huaji.png"));

		// 构造一个右键弹出式菜单

		PopupMenu pop = new PopupMenu();

		MenuItem handUp = new MenuItem("举手");

		MenuItem question = new MenuItem("提问");

		MenuItem dao = new MenuItem("签到");
		
		MenuItem sendFile = new MenuItem("提交作业");

		MenuItem exit = new MenuItem("关闭服务");
		
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
		trayIcon = new TrayIcon(icon.getImage(), "实验室电子教学监控系统(学生端)", pop);

		// 这句很重要，没有会导致图片显示不出来

		trayIcon.setImageAutoSize(true);

		/*
		 * 点击右下角图标的事件
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
		 * 为菜单项添加事件
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
	 * 屏幕监控链接主机函数
	 */
	private static void connectServer(Socket socket, int time) throws Exception {
		c = '1';
	 DataOutputStream os = null;
	 
//	 DataInputStream in = null;
	 SendFileThread s = null;
	 
	 InputStream ins = null;
		/*
		 * 监控主线程
		 */
		Thread.sleep(2* 1000);
		try {
			os = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//在此可以增加提问功能，把学生输入的问题作为文本发送给老师
		if(hand){
			os.writeUTF("hand");
			hand = false;
		}else if(isQuestion){
			os.writeUTF("question");
			String message = JOptionPane.showInputDialog(null, "请输入您的问题！", "我有问题~~~",JOptionPane.QUESTION_MESSAGE);
			os.writeUTF(message);
			System.out.println(213345);
			isQuestion = false;
		}else if(isDao){
			os.writeUTF("dao");
			String message = JOptionPane.showInputDialog(null,
					"请输入您的学号和姓名！", "我要签到~~~",
					JOptionPane.QUESTION_MESSAGE,
					new ImageIcon("images/huaji.png"), null,
					"你想撩我吗？").toString();
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
				JOptionPane.showMessageDialog(null, "发送失败！");
				return ;
			}
//			in = new DataInputStream(ins);
			s = new SendFileThread(socket,ins);
			s.start();
			
			s.join();
			
			isFile = false;
			SendFile.fileName = null;
			JOptionPane.showMessageDialog(null, "发送成功！");
		}else{
			// 这个延时很重要
			if (socket != null) {
				ClientThread a = new ClientThread(socket);
				// 表明线程已经创建，返回false
				System.out.println("1、isAlive" + a.isAlive());
				a.start();
				// Thread.sleep(2000);//再加点系统延时，1秒不行，至少2秒
				// 测试线程已经死亡
				System.out.println("2、isAlive" + a.isAlive());
			}
		}
	}

	/*
	 * 主函数
	 */
	public static void main(String[] args) {
		Socket socket = null;
		ReceiveFileThread b;
		DataInputStream fis = null;
		String mes = null;
		
		IP = JOptionPane.showInputDialog("请输入服务器IP地址....", "127.0.0.1");
		int time = (int) (Math.random() * 3 + 3);
		tempFile = new File("E:/temp.txt");
		try {
			if (tempFile.exists()) {
				JOptionPane.showMessageDialog(null, "程序已在运行", "警告",
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
				 * 判断服务器是发给所有人还是一个人还是多个人
				 */

				// 服务器启动文件
				while (c == '1') {
					System.out.println(1);
					try {
						socket = new Socket(IP, 10000);
						fis = new DataInputStream(socket.getInputStream());// 和复制文件不同的是网络上传文件是从Socket中获取的I/O流对象

						mes = fis.readUTF();

						System.out.println(mes);

						System.out.println("fis===" + fis);
					} catch (UnknownHostException e2) {
						e2.printStackTrace();
					} catch (IOException e2) {
						System.out.println(4454545);
						JOptionPane.showMessageDialog(null, "对不起，服务器未连接！","注意~~~",JOptionPane.ERROR_MESSAGE);
						tempFile.deleteOnExit();
						System.exit(0);
					} // 这里写好目标机器IP地址和任意一个开放的未被其它程序占用的端口号
					System.out.println(socket);
					/*
					 * 启动文件接收线程
					 */
					System.out.println("212131");
					// isOver = false;

					switch (mes) {
					case "file":// 文件
						if (!isOver && c == '1') {
							System.out.println("211");
							try {
								b = new ReceiveFileThread(socket, fis);
								b.start();
								// 等待其他线程完成
								b.join();
								// 从网络获取数据
								System.out.println(2);
								c = '0';
								socket.close();
							} catch (InterruptedException e1) {
								System.out.println("腿被打断了");
								e1.printStackTrace();
							} catch (UnknownHostException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						break;
					case "message":// 消息
						try {
							String message = fis.readUTF();
							// 在此实现气泡
							ToolTip.main(message);
							c = '0';// 每一个case一定要改变状态
							break;
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							System.out.println("结束了");
							tempFile.deleteOnExit();
							System.exit(0);
						}// 和复制文件不同的是网络上传文件是从Socket中获取的I/O流对象
					case "single" :
						try {
							String message = fis.readUTF();
							// 在此实现气泡
							ToolTip.main(message);
							c = '0';// 每一个case一定要改变状态
							break;
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							System.out.println("结束了");
							tempFile.deleteOnExit();
							System.exit(0);
						}// 和复制文件不同的是网络上传文件是从Socket中获取的I/O流对象
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

							// 这里空值异常未解决
							// 还有点名签到太慢了
							String message = JOptionPane.showInputDialog(null,
									"请输入您的学号和姓名！", "我要签到~~~",
									JOptionPane.QUESTION_MESSAGE,
									new ImageIcon("images/huaji.png"), null,
									"你想撩我吗？").toString();
							new DataOutputStream(socket.getOutputStream())
									.writeUTF(message);
							System.out.println("写出成功");
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
				System.out.println("服务器已关闭");
				tempFile.deleteOnExit();
				break;
			}
		}
	}
}