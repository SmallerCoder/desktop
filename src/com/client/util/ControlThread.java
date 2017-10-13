package com.client.util;

import java.awt.AWTException;
import java.awt.Event;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.ObjectInputStream;

/**

 * 控制线程

 * @author syxChina

 *

 */

public class ControlThread extends Thread {

    private ObjectInputStream ois;

    private Robot robot;

    public ControlThread(ObjectInputStream ois) {

        this.ois = ois;

    }

 

    @Override

    public void run() {

        try {

            robot = new Robot();

        } catch (AWTException e1) {

        }

        while (true) {

            try {

                Object event = ois.readObject();

                InputEvent e = (InputEvent) event;

                actionEvent(e);

            } catch (Exception e) {


                return;

            } 

        }

    }

     

    private void actionEvent(InputEvent e) throws Exception {

        if (e instanceof java.awt.event.KeyEvent) {

            KeyEvent ke = (KeyEvent) e;

            int type = ke.getID();

            if (type == java.awt.Event.KEY_PRESS) {

                robot.keyPress(ke.getKeyCode());

            }

            if (type == java.awt.Event.KEY_RELEASE) {

                robot.keyRelease(ke.getKeyCode());

            }

 

        }

        if (e instanceof java.awt.event.MouseEvent) {

            MouseEvent me = (MouseEvent) e;

            int type = e.getID();

            if (type == java.awt.Event.MOUSE_DOWN) {

                robot.mousePress(getMouseClick(me.getButton()));

            }else if (type == java.awt.Event.MOUSE_UP) {

                robot.mouseRelease(getMouseClick(me.getButton()));

            }else if (type == java.awt.Event.MOUSE_MOVE) {

                robot.mouseMove(me.getX(), me.getY());

            } else if(type == Event.MOUSE_DRAG) {

                robot.mouseMove(me.getX(), me.getY());

            }

 

        }

 

    }

 

    /**

     * 根据发送事的Mouse事件对象，转变为通用的Mouse按键代码

     * @param button

     * @return

     */

    private int getMouseClick(int button) {

        if (button == MouseEvent.BUTTON1) {

            return InputEvent.BUTTON1_MASK;

        }

        if (button == MouseEvent.BUTTON2) {

            return InputEvent.BUTTON2_MASK;

        }

        if (button == MouseEvent.BUTTON3) {

            return InputEvent.BUTTON3_MASK;

        }

        return -1;

    }

}