package com.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;


import com.client.util.ReceiveFile;

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
			String fileName = fis.readUTF();
			if(fileName.equals("over")){
				return ;
			}
			System.out.println("0000");
			System.out.println(fileName);
			System.out.println(4);
			// ��ȡ�����
			DataOutputStream fos1 = new DataOutputStream(socket.getOutputStream());// д����Ϣ;
			System.out.println(5);
//			if ((JOptionPane.showOptionDialog(null, "��������ļ�" + fileName + "��", "��Ϣ", JOptionPane.YES_NO_OPTION, 0,
//					null, null, null)) == 0) {
				System.out.println("����ѡ������ļ���");
				String message1 = "�������ѽ��գ�";

				fos1.writeUTF(message1);

				// �����ļ�
				fos = new ReceiveFile().saveFile(fileName);
				
				
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
				if (fis != null)
					fis.close();

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
