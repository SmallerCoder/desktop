package com.server.control;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.server.Server;
import com.server.Server_Frame;
import com.server.util.GenerateDate;

/*
 * 新窗体类
 */
public class NewWatchFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public static JLabel label = new JLabel();

	public NewWatchFrame() {
		/*
		 * 设置窗体大小跟屏幕一样大
		 */
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

		int screenWidth = (int) dimension.getWidth();

		int screenHeight = (int) dimension.getHeight();

		this.setSize(screenWidth, screenHeight);

		this.add(label);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				Server.sf.setVisible(true);
				Server_Frame.clickedNum = -1;
				Server_Frame.isLarge = false;
				Server_Frame.getTextArea03()
						.append(GenerateDate.getDate() + "clickNum = " + Server_Frame.clickedNum + "\n");
				Server_Frame.getTextArea03().append(GenerateDate.getDate() + "放大功能已关闭！\n");
			}

			/*
			 * 子窗口开启时，主窗口要关闭
			 */
			@Override
			public void windowOpened(WindowEvent e) {
				Server.sf.setVisible(false);
			}
		});

		// 设置子窗口关闭父窗口不关闭
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.setVisible(true);
	}
}
