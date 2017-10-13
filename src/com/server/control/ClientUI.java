package com.server.control;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.server.Server;

/**

 * 控制端界面

 * @author syxChina

 *

 */

public class ClientUI extends JFrame implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DataInputStream dis;//接受被控制端发来的图片

    private ObjectOutputStream oos;//发送控制事件

    private JLabel backImage;//此本版使用一个JLable显示图片

 

    public ClientUI(DataInputStream dis, ObjectOutputStream oos) {

        this();

        this.dis = dis;

        this.oos = oos;

    }

 

    /**

     * 根据图片数据更新控制端界面

     * @param imageData

     */

    public void update(byte[] imageData) {

        ImageIcon image = new ImageIcon(imageData);

        backImage.setIcon(image);

        this.repaint();

    }

 

    public void updateSize(Dimension client) {

        Dimension clientSize = getScreenSize();

        double  width = 0, height = 0;

        if (clientSize.getWidth() >= client.getWidth()) {

            width = client.getWidth()+60;

        } else {

            width = clientSize.getWidth();

        }

        if((clientSize.getHeight()-client.getHeight()) > 0) {

            height = client.getHeight() + 60;

        } else {

            height = clientSize.getHeight();

        }

        setSize((int)width, (int)height);

    }

 

    private ClientUI() {

    	
    	this.addWindowListener(new WindowAdapter() {

			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				Server.sf.setVisible(true);
				RCClient.flag = true;
			}
		});
        setDefaultCloseOperation(3 );

        setSize(1050, 800);

        backImage = new JLabel();

        JPanel pane = new JPanel();

        JScrollPane scrollPane = new JScrollPane(pane);

        pane.setLayout(new FlowLayout());

        pane.add(backImage);

        add(scrollPane);

         

        addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {

                sendEventObject(e);

            }

            public void keyReleased(KeyEvent e) {

                sendEventObject(e);

            }

            public void keyTyped(KeyEvent e) {

            }

        });

 

        addMouseWheelListener(new MouseWheelListener() {

            public void mouseWheelMoved(MouseWheelEvent e) {

                sendEventObject(e);

            }

        });

        backImage.addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent e) {

                sendEventObject(e);

            }

            public void mouseMoved(MouseEvent e) {

                if(Math.random()>0.8) 

                    sendEventObject(e);

            }

        });

        backImage.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {

                sendEventObject(e);

            }

            public void mouseReleased(MouseEvent e) {

                sendEventObject(e);

            }

        });

        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

 

    /**

     * 发送事件

     * @param event

     */
   
    private void sendEventObject(java.awt.event.InputEvent event)  {

        try {

            oos.writeObject(event);

        } catch (Exception ef) {

            ef.printStackTrace();

        }

 

    }

 

    public Dimension getScreenSize() {

        return Toolkit.getDefaultToolkit().getScreenSize();

    }

}