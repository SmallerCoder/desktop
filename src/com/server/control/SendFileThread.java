package com.server.control;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.server.util.SendFile;


public class SendFileThread extends Thread {
	private Socket socket;
	private InputStream in;
	public SendFileThread(Socket socket,InputStream in) {
		this.socket = socket;
		this.in = in;
	}

	@Override
	public void run() {
		DataOutputStream os;
		try {
			os = new DataOutputStream(socket.getOutputStream());
			// ����Ŀ�ĵ�����һ̨����,ͨ��Socket��ȡI/O��
			if (in != null) {
				try {
					// ʹ��UTF-8��������ļ���
					os.writeUTF(SendFile.name);
					// ÿ�������С
					byte[] b = new byte[1024];
					// ÿ�ζ����ʵ������
					int len = 0;
					// ѭ����ȡ����
					while ((len = in.read(b)) != -1) {
						os.write(b, 0, len);
					}

					// �������ȡ����
					DataInputStream fis = new DataInputStream(socket.getInputStream());
					// ʹ��UTF-8�����ȡ����
					String message = fis.readUTF();
					// ��ӡ��Ϣ
					System.out.println(message);

					if (os != null)
						os.close();
//				if (in != null)
//						in.close();
					socket.close();
				} catch (Exception e) {
					System.out.println("ò��zpc������,�Ժ��ٴ�����");
				}
			} else {
				os.writeUTF("over");
				System.out.println("�ļ���ȡʧ��(in = null)��");
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}
