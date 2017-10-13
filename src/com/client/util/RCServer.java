package com.client.util;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.client.Client;

/**

 * 服务器（被控制端）

 * @author syxChina

 *

 */

public class RCServer {

    private static RCServer rcs = new RCServer();

 

 

    public static void main() throws Exception {

        System.err.println("start Remote Control Server...");

        rcs.startServer(18080);

    }

 

    /**

     * 根据特定端口启动服务器

     * @param port

     * @throws Exception

     */

    public void startServer(int port) throws Exception {

    	System.err.println("run server in port:%d");

//        ServerSocket ss = new ServerSocket(port);
//
        while (true) {
//
//        	System.err.println("Remote Control Server wait client...");
//
//            Socket client = ss.accept();
//
//            System.err.println("a client[%s:%d] connect!");

    		Socket client = new Socket(Client.IP, 18080);

            InputStream in = client.getInputStream();

            ObjectInputStream ois = new ObjectInputStream(in);

             

            OutputStream os = client.getOutputStream();

            DataOutputStream dos = new DataOutputStream(os);

            System.err.println("socket open stream ok!");

             

            ControlThread cont = new ControlThread(ois);

            cont.start();//启动控制线程

            CaptureThread capt = new CaptureThread(dos);

            capt.start();//启动屏幕传输线程

        }

    }

 

    public int stopServer() {

        return 0;

    }

}