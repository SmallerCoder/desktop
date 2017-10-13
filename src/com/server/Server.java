package com.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import com.server.control.RCClient;
import com.server.control.ReadDaoMessageThread;
import com.server.control.SendFileThread;
import com.server.control.SendMessageThread;
import com.server.util.GetFileInputStream;
import com.server.util.SendFile;
import com.server.util.WriteUserText;

public class Server {

	public static Map<String, Integer> ipMap = Collections
			.synchronizedMap(new HashMap<String, Integer>());
	public static Map<String, Socket> ipMap1 = Collections
			.synchronizedMap(new HashMap<String, Socket>());
	/*
	 * 签到
	 */
	public static Map<String, String> ipMap2 = Collections
			.synchronizedMap(new HashMap<String, String>());
	/*
	 * 服务器初始化窗口
	 */
	public static Server_Frame sf = null;
	/*
	 * 每个客户机链接对应的Map标识
	 */
	public static int flag = 0;
	public static int count = 0;

	public static String fileName = null;

	/*
	 * 实际在线人数
	 */

	public static int onLineNum = 0;

	public static int singleNum = 0;

	public static boolean fileFlag = false;
	public static String text = null;

	public static void util(Socket socket, String ip) {

		if (ipMap.containsKey(ip)) {

			flag = ipMap.get(ip);// 取出IP对应的序号
			/*
			 * 如果监控功能没有开启，我肯定是不放行的，但是链接依旧保存
			 */
			ServerThread a = new ServerThread(socket, flag, ip);
			System.out.println("1、isAlive" + a.isAlive());
			a.start();
			System.out.println("2、isAlive" + a.isAlive());
		} else {

			ipMap.put(ip, (ipMap.size()) + 1);// 第一台机器对应1

			onLineNum++;

			flag = ipMap.size();// 第一台机器输出1
			/*
			 * 开启日志记录线程
			 */
			new WriteUserText(ip).start();

			ServerThread a = new ServerThread(socket, flag, ip);
			System.out.println("1、isAlive" + a.isAlive());
			a.start();
			System.out.println("2、isAlive" + a.isAlive());
		}
	}

	public static boolean mainListener(ServerSocket server) {
		// 服务器端的Socket
		Socket socket = null;
		try {
			socket = server.accept();
			String ip = socket.getInetAddress().getHostAddress();
			util(socket, ip);
			/*
			 * if (ipMap.size() > 52) {
			 * 
			 * break;// 应该拒绝链接 }
			 */

			// 如果点击了发送文件按钮，则传输图片的功能应该被取消掉
			if (fileFlag/* 按钮被点击 */) {
				server.close();
				return false;
			}
			if (Server_Frame.isDao/* 按钮被点击 */) {
				server.close();
				return false;
			}
			if (Server_Frame.isOK/* 消息按钮被点击 */) {
				server.close();
				return false;
			}
			if (Server_Frame.isControl/* 按钮被点击 */) {
				server.close();
				return false;
			}
			if (Server_Frame.isSingleMessage/* 消息按钮被点击 */) {
				server.close();
				return false;
			}
		} catch (IOException e) {
			System.out.println("1");
			e.printStackTrace();
		}
		return true;
	}

	public static void sendFile(ServerSocket server) {
		// 此处要循环监听所有客户端
		Socket socket = null;
		InputStream in = null;
		DataOutputStream os = null;
		System.out.println("count=" + count);
		System.out.println("size=" + ipMap.size());
		/*
		 * 暂时还没有解决in为空的情况，即老师取消发送文件
		 */
		in = GetFileInputStream.getFileInputStream();
		while (true) {
			try {
				socket = server.accept();
				/*
				 * 获取输入流，输出流，告诉客户端我是要发文件
				 */
				os = new DataOutputStream(socket.getOutputStream());

				os.writeUTF("file");// 到最后在关闭

				String ip = socket.getInetAddress().getHostAddress();
				if (ipMap1.containsKey(ip)) {
					in = new FileInputStream(new File(SendFile.fileName));
					System.out.println(4122);
					SendFileThread b = new SendFileThread(socket, in);
					System.out.println(77);
					b.start();
					try {
						b.join();
						count++;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(78);
					System.out.println("count=" + count);
					System.out.println("size=" + ipMap.size());
				} else {
					System.out.println(5453);
					ipMap1.put(ip, socket);
					in = new FileInputStream(new File(SendFile.fileName));
					SendFileThread b = new SendFileThread(socket, in);

					System.out.println(777);
					System.out.println(in);
					b.start();
					try {
						b.join();
						count++;
						System.out.println(count);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(7875);
					System.out.println("count=" + count);
					System.out.println("size=" + ipMap.size());
				}
				System.out.println(4552);
			} catch (IOException e1) {
				System.out.println("233");
			}

			if (ipMap.size() == count) {
				System.out.println(12312);
				JOptionPane.showMessageDialog(null, "文件发送完成！");
				count = 0;
				if (in != null)
					try {
						in.close();
					} catch (IOException e) {
						System.out.println(11);
						e.printStackTrace();
					}
				if (os != null)
					try {
						os.close();
						//
						server.close();
					} catch (IOException e) {
						System.out.println(11);
						e.printStackTrace();
					}
				break;
			}
		}
	}

	/*
	 * 签到
	 */

	public static void sendMessage(String type) {

		ServerSocket server = null;
		Socket socket = null;
		DataOutputStream os = null;
		DataInputStream in = null;
		ReadDaoMessageThread rdm = null;

		SendMessageThread smd = null;
		try {
			server = new ServerSocket(10000);

			while (true) {
				socket = server.accept();
				os = new DataOutputStream(socket.getOutputStream());
				String ip = socket.getInetAddress().getHostAddress();
				if (Server_Frame.clickedNum == ipMap.get(ip)
						&& type.equals("s")/* 是否还要判断数字是否大于map大小 */) {
					os.writeUTF(type);// 到最后在关闭
					if (type.equals("s")) {
						break;
					}

				} else if (!type.equals("s")) {
					if (type.equals("single") && ipMap.get(ip) == singleNum) {
						os.writeUTF(type);// 到最后在关闭
						smd = new SendMessageThread(socket, text, os);
						System.out.println(7734);
						smd.start();
						try {
							smd.join();
							System.out.println("count======"+count);
							break;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else if (type.equals("message")) {
						os.writeUTF(type);
					} else if (type.equals("dao")) {
						os.writeUTF(type);
					} else {
						os.writeUTF("1");
						break;
					}

					if (ipMap1.containsKey(ip)) {
						System.out.println(4122);

						if (type.equals("dao")) {
							in = new DataInputStream(socket.getInputStream());
							rdm = new ReadDaoMessageThread(socket, in, ip);
							rdm.start();
							try {
								rdm.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} else if (type.equals("message")) {
							smd = new SendMessageThread(socket, text, os);
							System.out.println(77);
							smd.start();
							try {
								smd.join();

							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} else if (type.equals("single")
								&& ipMap.get(ip) == singleNum) {

						}
						count++;
						System.out.println(78);
						System.out.println("count=" + count);
						System.out.println("size=" + ipMap.size());
					} else {
						System.out.println(5453);
						ipMap1.put(ip, socket);
						if (type.equals("dao")) {
							in = new DataInputStream(socket.getInputStream());
							rdm = new ReadDaoMessageThread(socket, in, ip);
							rdm.start();
							try {
								rdm.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} else if (type.equals("message")) {
							smd = new SendMessageThread(socket, text, os);
							System.out.println(77);
							smd.start();
							try {
								smd.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} else if (type.equals("single")
								&& ipMap.get(ip) == singleNum) {
							smd = new SendMessageThread(socket, text, os);
							System.out.println(7734);
							smd.start();
							try {
								smd.join();
								break;
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						count++;
						System.out.println(7875);
						System.out.println("count=" + count);
						System.out.println("size=" + ipMap.size());
					}
					System.out.println(4552);
					System.out.println(212);
					System.out.println(21);
				}

				if (ipMap.size() == count) {
					System.out.println(12312);
					JOptionPane.showMessageDialog(null, "发送完成！");
					count = 0;
					if (os != null)
						try {
							os.close();
						} catch (IOException e) {
							System.out.println(11);
							e.printStackTrace();
						}
					server.close();
					break;
				}

			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println(text);

	}

	public static void main(String[] args) {
		// 创建一个服务器端ServerSocket，端口号为12000
		ServerSocket server;
		DataOutputStream os = null;
		try {
			sf = new Server_Frame();
			server = new ServerSocket(12000);
			/*
			 * 采用死循环，不断监听来自客户端的链接
			 */
			while (true) {
				System.out.println(23);
				boolean isTrue = mainListener(server);
				// 已经关闭
				if (!isTrue) {
					if (fileFlag) {
						System.out.println(1);
						server = new ServerSocket(10000);
						sendFile(server);
						// server.close();
						// 因为是本机，所以提示被占用
						fileFlag = false;
						server = new ServerSocket(12000);
					} else if (Server_Frame.isOK) {
						System.out.println(11);
						sendMessage("message");
						System.out.println(2332);
						Server_Frame.isOK = false;
						server = new ServerSocket(12000);
					} else if (Server_Frame.isDao) {
						System.out.println(11);
						sendMessage("dao");
						System.out.println(2332);
						Server_Frame.isDao = false;
						server = new ServerSocket(12000);
					} else if (Server_Frame.isSingleMessage) {
						System.out.println(113);
						sendMessage("single");
						System.out.println(2344532);
						Server_Frame.isSingleMessage = false;
						server = new ServerSocket(12000);
					} else if (Server_Frame.isControl) {
						System.out.println(113);
						sendMessage("s");
						new RCClient().main();
						System.out.println(2344532);
						Server_Frame.isControl = false;
						server = new ServerSocket(12000);
					}
				}

				/*
				 * if 远程操控按钮点击了，......
				 */
			}
		} catch (IOException e) {
			System.out.println(12121);
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(121);
			// try {
			// server = new ServerSocket(12000);
			// } catch (IOException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			e.printStackTrace();
		}
	}
}
