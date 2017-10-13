package com.client.util;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**

 * 屏幕传输线程

 * @author syxChina

 *

 */

public class CaptureThread  extends Thread {

    public static final int DPS = 20;//设置的dps，未用

    public static final int THREAD_NUM = 5;//画面传输线程，未用

    private DataOutputStream dos;//管道

    private Robot robot;//robot

    public CaptureThread(DataOutputStream dos) {

        this.dos = dos; 

    }

     

    @Override

    public void run() {

        try {

            robot = new Robot();

        } catch (AWTException e1) {

            e1.printStackTrace();

        }

         

        final Toolkit tk = java.awt.Toolkit.getDefaultToolkit();

        final Dimension dm = tk.getScreenSize();

        final Rectangle rec = new Rectangle(dm);

        try {

            dos.writeDouble(dm.getHeight());

            dos.writeDouble(dm.getWidth());

            dos.flush();

        } catch (IOException e1) {

        	System.err.println("send screen size[%dx%d] error!");

            return;

        }

        while(true) {

            try {

                long begin = System.currentTimeMillis();

                byte[] data = createImage(rec);

                dos.writeInt(data.length);

                dos.write(data);

                dos.flush();

                long end = System.currentTimeMillis();

                System.err.println("time=%d,size=%d");

                if((end-begin) < 1000/DPS) {

                    Thread.sleep(1000/DPS - (end-begin));

                }

            } catch (Exception e) {


                return;

            } 

                 

        }

         

    }

 

    private byte[] createImage(Rectangle rec) throws IOException {

        BufferedImage bufImage = robot.createScreenCapture(rec);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(baos);

        encoder.encode(bufImage);

        return baos.toByteArray();

    }

}