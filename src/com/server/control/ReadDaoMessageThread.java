package com.server.control;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import com.server.Server;
import com.server.Server_Frame;
import com.server.util.GenerateDate;

public class ReadDaoMessageThread  extends Thread{
	
	Socket socket = null;
	String text = null;
	DataInputStream in = null;
	String s = null;
	String ip = null;
	
	public ReadDaoMessageThread(Socket socket,DataInputStream in,String ip) {
		this.socket = socket;
		this.in = in;
		this.ip =ip;
	}
	
	
	@Override
	public  void run() {
		
		try {
			s = in.readUTF();
			Server_Frame.getTextArea01().append(GenerateDate.getDate() + " : "+s+"ÒÑÇ©µ½\n");
			Server.ipMap2.put(ip, s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
