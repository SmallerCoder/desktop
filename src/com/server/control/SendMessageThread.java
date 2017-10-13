package com.server.control;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SendMessageThread  extends Thread{
	
	Socket socket = null;
	String text = null;
	DataOutputStream ds = null;
	
	public SendMessageThread(Socket socket,String text,DataOutputStream ds) {
		this.text = text;
		this.socket = socket;
		this.ds = ds;
	}
	
	
	@Override
	public  void run() {
		
		try {
			ds.writeUTF(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
