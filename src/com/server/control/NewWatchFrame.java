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
 * �´�����
 */
public class NewWatchFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public static JLabel label = new JLabel();

	public NewWatchFrame() {
		/*
		 * ���ô����С����Ļһ����
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
				Server_Frame.getTextArea03().append(GenerateDate.getDate() + "�Ŵ����ѹرգ�\n");
			}

			/*
			 * �Ӵ��ڿ���ʱ��������Ҫ�ر�
			 */
			@Override
			public void windowOpened(WindowEvent e) {
				Server.sf.setVisible(false);
			}
		});

		// �����Ӵ��ڹرո����ڲ��ر�
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.setVisible(true);
	}
}
