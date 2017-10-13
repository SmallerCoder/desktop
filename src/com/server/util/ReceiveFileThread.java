package com.server.util;

import java.io.DataInputStream;
import java.io.OutputStream;
import java.net.Socket;


import com.client.util.ReceiveFile;
import com.server.Server;

public class ReceiveFileThread extends Thread {

	private Socket socket;
	DataInputStream fis;

	public ReceiveFileThread(Socket socket,DataInputStream fis) {

		this.socket = socket;
		this.fis=fis;

	}

	@Override
	public synchronized void run() {

		OutputStream fos = null;

		try {
			System.out.println(1);
			System.out.println(socket.getInputStream());
			System.out.println(2);
			// ��ȡ������
			
			System.out.println(3);
			System.out.println(fis);
			// ��ȡ�ļ���
			 Server.fileName = fis.readUTF();
			if(Server.fileName.equals("over")){
				Server.fileName = null;
				return ;
			}
			System.out.println("0000");
			System.out.println(Server.fileName);
			System.out.println(4);
			// ��ȡ�����
//			DataOutputStream fos1 = new DataOutputStream(socket.getOutputStream());// д����Ϣ;
			System.out.println(5);
//			if ((JOptionPane.showOptionDialog(null, "��������ļ�" + fileName + "��", "��Ϣ", JOptionPane.YES_NO_OPTION, 0,
//					null, null, null)) == 0) {
				System.out.println("����ѡ������ļ���");


				// �����ļ�
				fos = new ReceiveFile().saveFile(Server.fileName);
				
				
				/*
				 * 
				 * ���Զ�̨�����Ƿ�����ȷ�����ļ�
				 * �ͻ�������
				 * 
				 * 
				 * 
				 */
				System.out.println(fos);
				
				/*
				 * ���ļ���д����
				 */
				int len = 0;

				byte[] bytes = new byte[1024];

				if (fis != null) {
					while ((len = fis.read(bytes)) != -1) {
						fos.write(bytes, 0, len);
					}
				}
				//JOptionPane.showMessageDialog(null, "�ļ�����ɹ���");
//				Client.isOver=true;

				if (fos != null)
					fos.close();
//			} else {
//				System.out.println("��ѡ���˾ܾ����ܣ�");
//				String message2 = "��ʾ���Է��Ѿܾ����ܣ�";
//				fos1.writeUTF(message2);
//			}
		} catch (Exception e) {
			e.printStackTrace();
			
			System.out.println("�����ļ�ʧ�ܣ�");
		}
	}
}
