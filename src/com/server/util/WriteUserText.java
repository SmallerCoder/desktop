package com.server.util;

import com.server.Server_Frame;

public class WriteUserText extends Thread {
	
	private String ip = null;

	/*
	 * ������
	 */
	public WriteUserText(String ip) {
		this.ip = ip;
				
	}

	/*
	 * �߳��� һ���߳�ʵ�ּ�¼һ����½�û�
	 */
	@Override
	public synchronized void run() {
		
		Server_Frame.getTextArea03().append(GenerateDate.getDate() + ip+"�û��ѵ�¼"+"\n");
		
	}
}
