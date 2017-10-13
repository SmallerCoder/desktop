package com.client;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class ShotImage {
	//获取封装屏幕宽度、高度的Dimension对象
	private Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	//定义一张空白的缓冲画布
	BufferedImage screenshot;
	/*
	 * 线程同步的截屏方法
	 * 返回图片画布
	 */
	public synchronized BufferedImage snapShot() {
		
		try {
			//new Robot():在基本屏幕坐标系中构造一个 Robot 对象
			/*
			 * public BufferedImage createScreenCapture(Rectangle screenRect)
			 * 创建包含从屏幕中读取的像素的图像。该图像不包括鼠标光标。
			 * 
			 * Rectangle(int x, int y, int width, int height) 
			 *  构造一个新的 Rectangle，其左上角被指定为 (x,y)，其宽度和高度由同名的参数指定。
			 */
			screenshot = new Robot().createScreenCapture(
					new Rectangle(0, 0,
							(int) dimension.getWidth(), (int) dimension.getHeight()));
			//创建了一个矩形区域
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return screenshot;
	}
}