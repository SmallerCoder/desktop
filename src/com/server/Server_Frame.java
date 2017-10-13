package com.server;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.server.control.NewWatchFrame;
import com.server.util.GenerateDate;

public class Server_Frame extends JFrame {
	private static final long serialVersionUID = 1L;
	private static JPanel p1 = new JPanel(new GridLayout(1, 10)); // 定义一个中间容器，存放顶部9个按钮+1个标签
	private static JButton[] btn = new JButton[13];// 为13个按钮添加消息提示
	private static JLabel label = new JLabel("       此处显示时间"); // 显示时间的标签
	private static JPanel p2 = new JPanel();
	private static Box box = Box.createVerticalBox();
	private static JPanel p3 = new JPanel(new BorderLayout());
	private static JPanel p4 = new JPanel(new BorderLayout());// 存第1个文本框
	private static JTextArea textArea01 = new JTextArea(5, 40);// 定义第2个，第3个文本框
	private static JTextArea textArea02 = new JTextArea(3, 6);
	private static JTextArea textArea03 = new JTextArea(5, 55);
	private static Box box01 = Box.createVerticalBox();
	private static Box box02 = Box.createHorizontalBox();
	private static JButton bt01 = new JButton("发送至全体成员");// 定义两个放按钮
	private static JPanel p5 = new JPanel();
	private static JPanel p6 = new JPanel(new GridLayout());
	JComboBox<String> comboBox = new JComboBox<String>();
	private static String[] message = new String[] { "视频录像", "学生信息", "屏幕监控", "锁住客户机", "点名签到", "远程控制", "放大学生屏幕", "文件分发",
			"消息发送", "总开关", "", "", "设置" };// 设置按钮提示信息
	/*
	 * 存放52个按钮与其标识的Map数组 线程同步的
	 */
	public static Map<JButton, Integer> btMap = Collections.synchronizedMap(new HashMap<JButton, Integer>());
	/*
	 * 点击按钮的标识
	 */
	public static int clickedNum = 1;
	/*
	 * 放大权限
	 */
	public static boolean isLarge = false;
	/*
	 * 监控功能权限
	 */
	public static boolean isMonitor = false;

	public static boolean isDao = false;
	
	public static boolean isControl = false;

	public static String isSelected = null;
	/*
	 * getter\setter
	 */
	public static JButton[] bt = new JButton[52];

	public static boolean isMessage = false;

	public static boolean isSingleMessage = false;

	public static boolean isOK = false;

	private SystemTray tray;

	private TrayIcon trayIcon;

	/**
	 * @return the textArea03
	 */
	public static JTextArea getTextArea03() {
		return textArea03;
	}

	/**
	 * @param textArea03
	 *            the textArea03 to set
	 */
	public static void setTextArea03(JTextArea textArea03) {
		Server_Frame.textArea03 = textArea03;
	}

	public static JTextArea getTextArea01() {
		return textArea01;
	}

	public static void setTextArea01(JTextArea textArea01) {
		Server_Frame.textArea01 = textArea01;
	}

	// 初始化窗口函数
	public Server_Frame() throws Exception {
		// 设置窗体标题
		this.setTitle("远程桌面监控系统");
		// 设置窗体大小
		this.setSize(1331, 736);
		/*
		 * 设置窗口处于屏幕正中间
		 */
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		// 获取屏幕设备的宽度
		int width = (int) d.getWidth();// 1366
		// 获取屏幕设备的高度
		int height = (int) d.getHeight();// 768
		// 设置窗体处于正中间
		this.setLocation((width - 1331) / 2, (height - 736) / 2);

		/*
		 * 设置程序运行的图标
		 */

		Toolkit tk = Toolkit.getDefaultToolkit();

		Image img = tk.getImage(this.getClass().getResource("/images/0.png"));

		this.setIconImage(img);

		// 设置窗口关闭事件监听

		addWindowListener(new WindowAdapter() {

			// public void windowClosing(WindowEvent e) {
			//
			// // 将托盘图标添加到系统的托盘实例中
			//
			// try {
			//
			// tray.add(trayIcon);
			//
			// System.out.println("关闭");
			// //当前窗口关闭
			// Server_Frame.this.setVisible(false);
			//
			// } catch (AWTException e1) {
			//
			// e1.printStackTrace();
			//
			// }
			//
			// }
			//
		});

		/*
		 * 设置托盘
		 */

		if (SystemTray.isSupported()) {

			System.out.println("调 用tray");

			tray();

		}

		// 分别创建9个上标题按钮
		for (int i = 0; i < 9; i++) {
			btn[i] = new JButton(new Font(message[i], Font.ITALIC, 10).getName(),
					new ImageIcon(this.getClass().getResource("/images/" + i + ".png")));
			btn[i].setVerticalTextPosition(JButton.BOTTOM);
			btn[i].setHorizontalTextPosition(JButton.CENTER);
			btn[i].setIconTextGap(10);
			p1.add(btn[i]);
			btn[i].setToolTipText(message[i]);
		}
		p1.add(label);
		p2.setBorder(new TitledBorder(new EtchedBorder(), "用户列表"));
		/*
		 * 创建左边的4个按钮
		 */
		for (int i = 9; i < btn.length; i++) {
			btn[i] = new JButton(new ImageIcon(this.getClass().getResource("/images/" + i + ".png")));
			box.add(Box.createHorizontalStrut(1));
			box.add(btn[i]);
			btn[i].setToolTipText(message[i]);
		}

		/*
		 * 为p2添加滚动条 为52个按钮添加提示
		 */

		p2.setLayout(new GridLayout(13, 2));

		for (int i = 0; i < bt.length; i++) {
			bt[i] = new JButton((i + 1) + "号机", new ImageIcon(this.getClass().getResource("/images/huaji.png")));
			bt[i].setToolTipText((i + 1) + " 号 电 脑 ");
			bt[i].setVerticalTextPosition(JButton.BOTTOM);
			bt[i].setHorizontalTextPosition(JButton.CENTER);
			bt[i].setIconTextGap(90);
			p2.add(bt[i]);
		}

		/*
		 * 为p2添加滚动条
		 */
		JScrollPane js_p2 = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		js_p2.setViewportView(p2);
		p2.setPreferredSize(new Dimension(400, 2800));
		p2.revalidate();

		p3.add(js_p2, BorderLayout.CENTER);
		p3.add(box, BorderLayout.EAST);

		p4.setBorder(new TitledBorder(new EtchedBorder(), "信息管理"));

		textArea01.setLineWrap(true);
		textArea01.setWrapStyleWord(true);
		textArea01.setEditable(false);
		JScrollPane js_01 = new JScrollPane(textArea01);

		textArea02.setLineWrap(true);
		textArea02.setWrapStyleWord(true);
		JScrollPane js_02 = new JScrollPane(textArea02);

		comboBox.addItem("所有");
		for (int i = 1; i <= 52; i++) {
			comboBox.addItem(i + "号");
		}
		// 设置船体没有边缘
		// this.setUndecorated(true);
		box02.add(bt01);
		box02.add(Box.createHorizontalStrut(10));
		box02.add(comboBox);

		box01.add(js_02);
		box01.add(box02);

		/*
		 * 设置布局
		 */
		p4.add(js_01, BorderLayout.WEST);
		p4.add(box01, BorderLayout.EAST);

		p5.setBorder(new TitledBorder(new EtchedBorder(), "用户记录"));

		/*
		 * 文本框
		 */
		// 设置自动换行、自动换字功能
		textArea03.setLineWrap(true);
		textArea03.setWrapStyleWord(true);
		textArea03.setEditable(false);
		JScrollPane js_03 = new JScrollPane(textArea03);

		p5.add(js_03);

		// 存第p4,p5
		p6.add(p4);
		p6.add(p5);

		/*
		 * 给52个按钮做个映射 每个按钮序号对应从1~52
		 */
		for (int i = 0; i < bt.length; i++) {
			btMap.put(bt[i], i + 1);
			bt[i].setEnabled(false);
		}

		isSelected = "所有";
		/*
		 * 将三个容器添加到JFrame中
		 */
		this.add(p1, BorderLayout.NORTH);
		this.add(p3, BorderLayout.CENTER);
		this.add(p6, BorderLayout.SOUTH);
		componentListener();

		// 设置窗体显示出来
		this.setVisible(true);
		// 设置窗口不能修改大小
		this.setResizable(false);
		// 设置可以点击关闭
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void tray() {

		// 获得本操作系统托盘的实例

		tray = SystemTray.getSystemTray();

		// 显示在托盘中的图标

		ImageIcon icon = new ImageIcon(this.getClass().getResource("/images/1.png"));

		// 构造一个右键弹出式菜单

		PopupMenu pop = new PopupMenu();
		MenuItem file = new MenuItem("发送文件");
		MenuItem screen = new MenuItem("放大学生屏幕");
		final MenuItem monitor = new MenuItem("开启监控");
		MenuItem exit = new MenuItem("关闭服务");

		pop.add(monitor);
		pop.addSeparator();
		pop.add(file);
		pop.addSeparator();
		pop.add(screen);
		pop.addSeparator();
		pop.add(exit);
		pop.addSeparator();

		trayIcon = new TrayIcon(icon.getImage(), "实验室电子教学监控系统（服务端）", pop);

		// 这句很重要，没有会导致图片显示不出来

		trayIcon.setImageAutoSize(true);

		/*
		 * 点击右下角图标的事件
		 */
		trayIcon.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 2) {

					trayIcon.setImageAutoSize(true);
					Server_Frame.this.setVisible(true);

				}

			}

		});

		try {
			tray.add(trayIcon);
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		exit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				tray.remove(trayIcon);
				System.exit(0);

			}
		});
		file.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Server.fileFlag = true;

			}
		});
		screen.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				isLarge = true;
				JOptionPane.showMessageDialog(null, "放大功能已开启！");
				Server_Frame.getTextArea03().append(GenerateDate.getDate() + "放大功能已开启！\n");

			}
		});

		monitor.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (e.getActionCommand().equals("开启监控")) {
					isMonitor = true;
					Server_Frame.getTextArea03().append(GenerateDate.getDate() + "屏幕监控已开启！\n");
					JOptionPane.showMessageDialog(null, "屏幕监控已开启！");
					for (int i = 0; i < bt.length; i++) {
						bt[i].setEnabled(true);
					}
					monitor.setLabel("关闭监控");
				} else {
					Server_Frame.getTextArea03().append(GenerateDate.getDate() + "屏幕监控已关闭！\n");
					JOptionPane.showMessageDialog(null, "屏幕监控已关闭！");
					for (int i = 0; i < bt.length; i++) {
						bt[i].setEnabled(false);
					}
					monitor.setLabel("开启监控");
				}
			}
		});
	}

	public void componentListener() {
		
		btn[5].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 消息变量为true
				isControl = true;
				Server_Frame.getTextArea03().append("true");
			}
		});
		btn[4].addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// 消息变量为true
				isDao = true;
				Server_Frame.getTextArea03().append("true");
			}
		});
		/*
		 * 发送消息开关监听
		 */
		btn[8].addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// 消息变量为true
				isMessage = true;
				Server_Frame.getTextArea03().append("true");
			}
		});
		/*
		 * 最小化时隐藏该窗口
		 */

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowIconified(WindowEvent e) {
				super.windowIconified(e);
				Server_Frame.this.setVisible(false);
			}
		});

		this.addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
		/*
		 * 点击该按钮表示开启了监控功能
		 */
		btn[2].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				for (int i = 0; i < bt.length; i++) {
					bt[i].setEnabled(true);
				}
				isMonitor = true;
				Server_Frame.getTextArea03().append(GenerateDate.getDate() + "屏幕监控已开启！\n");
				JOptionPane.showMessageDialog(null, "屏幕监控已开启！");
			}
		});
		/*
		 * 涉及的变量为：isLarge
		 */
		btn[6].addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				isLarge = true;
				JOptionPane.showMessageDialog(null, "放大功能已开启！");
				Server_Frame.getTextArea03().append(GenerateDate.getDate() + "放大功能已开启！\n");
			}
		});

		btn[7].addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				/*
				 * 先开启监控功能才能发文件
				 */
				if (!isMonitor) {
					JOptionPane.showMessageDialog(null, "请先开启监控功能！么么哒！");
				} else if (Server.ipMap.size() <= 0) {
					JOptionPane.showMessageDialog(null, "分发文件必须至少有一台学生电脑连接上");
				} else {
					Server.fileFlag = true;
					Server_Frame.getTextArea03().append("true");
				}

			}
		});
		/*
		 * 52个按钮,如果放大功能允许
		 */

		for (int i = 0; i < bt.length; i++) {
			bt[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (isMonitor == true) {
						clickedNum = btMap.get(e.getSource());
						if (isLarge == true) {
							/*
							 * 获取按钮点击源头对应的序号1~52
							 */
							/*
							 * 如果该电脑未连接
							 */
							if (clickedNum > Server.ipMap.size()) {
								Server_Frame.getTextArea03()
										.append(GenerateDate.getDate() + clickedNum + " 号 电 脑 未 链 接 ！\n");
							} else {
								new NewWatchFrame();
							}
						} else {

							if (clickedNum > Server.ipMap.size()) {
								Server_Frame.getTextArea03()
										.append(GenerateDate.getDate() + clickedNum + " 号 电 脑 未 链 接 ！\n");
							} else {
								JOptionPane.showMessageDialog(null, "请先开启放大功能！么么哒！");
								Server_Frame.getTextArea03().append(GenerateDate.getDate() + "请先开启放大功能！\n");
							}
						}
					} else {
						JOptionPane.showMessageDialog(null, "请先开启监控功能！");
					}
				}
			});
		}
		/*
		 * 发送全体成员按钮
		 */
		bt01.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (isSelected.equals("所有")) {
					if (isMessage) {
						Server.text = textArea02.getText();
						isOK = true;
						textArea02.setText(null);
					} else {
						Server_Frame.getTextArea03().append(GenerateDate.getDate() + "请先开启消息发送功能！\n");
					}
				} else {
					if (isMessage) {
						if (Server.singleNum > Server.onLineNum) {
							JOptionPane.showMessageDialog(null, "该电脑已下线或者未连接");
						} else {
							Server.text = textArea02.getText();
							isSingleMessage = true;
							textArea02.setText(null);
						}

					} else {
						Server_Frame.getTextArea03().append(GenerateDate.getDate() + "请先开启消息发送功能！\n");
					}
				}

			}
		});
		// /*
		// * 发送按钮
		// */
		// bt02.addMouseListener(new MouseAdapter() {
		// @Override
		// public void mouseClicked(MouseEvent e) {
		// box02.add(comboBox);
		// }
		// });
		comboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// 获取选中的值
				isSelected = ((JComboBox<?>) e.getSource()).getSelectedItem().toString();
				if (isSelected != null && !(isSelected.equals("所有"))) {
					bt01.setText("    发送给" + isSelected + "       ");
					Server.singleNum = Integer.parseInt(isSelected.substring(0, isSelected.length() - 1));
					System.out.println(Server.singleNum);
				} else {
					bt01.setText("发送给全体成员");
				}

			}
		});
	}

}
