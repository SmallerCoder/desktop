package com.client;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class ShotImage {
	//��ȡ��װ��Ļ��ȡ��߶ȵ�Dimension����
	private Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	//����һ�ſհ׵Ļ��廭��
	BufferedImage screenshot;
	/*
	 * �߳�ͬ���Ľ�������
	 * ����ͼƬ����
	 */
	public synchronized BufferedImage snapShot() {
		
		try {
			//new Robot():�ڻ�����Ļ����ϵ�й���һ�� Robot ����
			/*
			 * public BufferedImage createScreenCapture(Rectangle screenRect)
			 * ������������Ļ�ж�ȡ�����ص�ͼ�񡣸�ͼ�񲻰�������ꡣ
			 * 
			 * Rectangle(int x, int y, int width, int height) 
			 *  ����һ���µ� Rectangle�������ϽǱ�ָ��Ϊ (x,y)�����Ⱥ͸߶���ͬ���Ĳ���ָ����
			 */
			screenshot = new Robot().createScreenCapture(
					new Rectangle(0, 0,
							(int) dimension.getWidth(), (int) dimension.getHeight()));
			//������һ����������
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return screenshot;
	}
}