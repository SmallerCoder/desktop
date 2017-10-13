package com.server.control;

import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

/**

 * 控制端

 * @author syxChina

 *

 */

public class RCClient  {

    private ClientUI clientUI ;

    private DataInputStream dis;

    private ObjectOutputStream oos;

    private Socket client;
    
    public static boolean flag ;

 

    /**

     * 连接被控制端

     * @param host

     * @param port

     * @return

     */

    public int connect(String host, int port) {

        int retCode = 0;

        try {
        	ServerSocket ss = new ServerSocket(port);

          while (true) {

          	System.err.println("Remote Control Server wait client...");

              Socket client = ss.accept();

              System.err.println("a client[%s:%d] connect!");

//            client = new Socket("172.25.240.3", 18080);


            oos = new ObjectOutputStream(client.getOutputStream());

            dis = new DataInputStream(client.getInputStream());

          }
        } catch (UnknownHostException e) {

            retCode = 1;

        } catch (IOException e) {

            retCode = 2;

        }

        return retCode;

    }

     

    /**

     * 显示图形界面

     * @throws Exception

     * @throws ClassNotFoundException

     */

    public void showClientUI() throws Exception, ClassNotFoundException {

        clientUI = new ClientUI(dis, oos);


        clientUI.updateSize(readServerSize());

        while(true) {

            long begin = System.currentTimeMillis();

            byte[] imageData = readBytes();

            clientUI.update(imageData);

            long end = System.currentTimeMillis();

        }

    }

    /**

     * 读被控制段发送来的数据

     * @return

     * @throws IOException

     * @throws ClassNotFoundException

     */

    public byte[] readBytes() throws IOException, ClassNotFoundException {

        int len = dis.readInt();

        byte[] data = new byte[len];

        dis.readFully(data);

        return data;

    }

    /**

     * 读被控制端分辨率

     * @return

     */

    public Dimension readServerSize() {

        double height = 100;

        double width = 100;

        try {

            height = dis.readDouble();

            width = dis.readDouble();

        } catch (IOException e) {


        }

        return new Dimension((int)width, (int)height);

    }

     

    public  boolean main() throws Exception {

//        String input = JOptionPane.showInputDialog("请输入要连接的服务器(192.168.0.2:18080):");

//        if(input == null) {
//
//            return;
//
//        }
//
//        Pattern pattern = Pattern.compile("(\\d+.\\d+.\\d+.\\d+):(\\d+)");
//
//        java.util.regex.Matcher m = pattern.matcher(input);
//
//        if(!m.matches()) {
//
//            return;
//
//        }
//
//        String host = m.group(1);
//
//        int port = Integer.parseInt(m.group(2));

        RCClient rcc = new RCClient();


//        rcc = new RCClient();


        int retCode = rcc.connect("127.0.0.1", 18080);//连接指定的被控制端

        if (retCode != 0) {

            return false;

        }

        try {

            rcc.showClientUI();

        } catch (Exception e) {


        }
		return flag;

    }

}