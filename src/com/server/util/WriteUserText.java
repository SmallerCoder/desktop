package com.server.util;

import com.server.Server_Frame;

public class WriteUserText extends Thread {
	
	private String ip = null;

	/*
	 * 构造器
	 */
	public WriteUserText(String ip) {
		this.ip = ip;
				
	}

	/*
	 * 线程体 一个线程实现记录一个登陆用户
	 */
	@Override
	public synchronized void run() {
		
		Server_Frame.getTextArea03().append(GenerateDate.getDate() + ip+"用户已登录"+"\n");
		
	}
}
