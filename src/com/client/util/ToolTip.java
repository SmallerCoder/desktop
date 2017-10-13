package com.client.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.border.EtchedBorder;
 
/**
 * @author http://cping1982.blog.51cto.com/601635/129810
 */
public class ToolTip {
 
    // ������ʾ��
    private int _width = 300;
 
    // ������ʾ��
    private int _height = 100;
 
    // �趨ѭ���Ĳ���
    private int _step = 30;
 
    // ÿ��ʱ��
    private int _stepTime = 40;
 
    // ��ʾʱ��
    private int _displayTime = 10000;
 
    // Ŀǰ�����������ʾ����
    private int _countOfToolTip = 0;
 
    // ��ǰ���������
    private int _maxToolTip = 0;
 
    // ����Ļ����ʾ�����������ʾ����
    private int _maxToolTipSceen;
 
    // ����
    private Font _font;
 
    // �߿���ɫ
    private Color _bgColor;
 
    // ������ɫ
    private Color _border;
 
    // ��Ϣ��ɫ
    private Color _messageColor;
 
    // ��ֵ�趨
    int _gap;
 
    // �Ƿ�Ҫ��������jre1.5���ϰ汾����ִ�У�
    boolean _useTop = true;
 
    /**
     * ���캯������ʼ��Ĭ��������ʾ����
     *
     */
    public ToolTip() {
        // �趨����
        _font = new Font("����", 0, 12);
        // �趨�߿���ɫ
        _bgColor = new Color(255, 255, 225);
        _border = Color.BLACK;
        _messageColor = Color.BLACK;
        _useTop = true;
        // ͨ�����÷�����ǿ�ƻ�֪�Ƿ�֧���Զ������ö�
        try {
            JWindow.class.getMethod("setAlwaysOnTop",
                    new Class[] { Boolean.class });
        } catch (Exception e) {
            _useTop = false;
        }
 
    }
 
    /**
     * �ع�JWindow������ʾ��һ������ʾ��
     *
     */
    class ToolTipSingle extends JWindow {
        private static final long serialVersionUID = 1L;
 
        private JLabel _iconLabel = new JLabel();
 
        private JTextArea _message = new JTextArea();
 
        public ToolTipSingle() {
            initComponents();
        }
 
        private void initComponents() {
            setSize(_width, _height);
            _message.setFont(getMessageFont());
            JPanel externalPanel = new JPanel(new BorderLayout(1, 1));
            externalPanel.setBackground(_bgColor);
            // ͨ���趨ˮƽ�봹ֱ��ֵ����ڲ����
            JPanel innerPanel = new JPanel(new BorderLayout(getGap(), getGap()));
            innerPanel.setBackground(_bgColor);
            _message.setBackground(_bgColor);
            _message.setMargin(new Insets(4, 4, 4, 4));
            _message.setLineWrap(true);
            _message.setWrapStyleWord(true);
            // ��������ָ����������Ӱ��ɫ�����̸��񻯱߿�
            EtchedBorder etchedBorder = (EtchedBorder) BorderFactory
                    .createEtchedBorder();
            // �趨�ⲿ������ݱ߿�Ϊ�绯Ч��
            externalPanel.setBorder(etchedBorder);
            // �����ڲ����
            externalPanel.add(innerPanel);
            _message.setForeground(getMessageColor());
            innerPanel.add(_iconLabel, BorderLayout.WEST);
            innerPanel.add(_message, BorderLayout.CENTER);
            getContentPane().add(externalPanel);
        }
 
        /**
         * ������ʼ
         *
         */
        public void animate() {
            new Animation(this).start();
        }
 
    }
 
    /**
     * ���ദ�򶯻�����
     *
     */
    class Animation extends Thread {
 
        ToolTipSingle _single;
 
        public Animation(ToolTipSingle single) {
            this._single = single;
        }
 
        /**
         * ���ö���Ч�����ƶ���������
         *
         * @param posx
         * @param startY
         * @param endY
         * @throws InterruptedException
         */
        private void animateVertically(int posx, int startY, int endY)
                throws InterruptedException {
            _single.setLocation(posx, startY);
            if (endY < startY) {
                for (int i = startY; i > endY; i -= _step) {
                    _single.setLocation(posx, i);
                    Thread.sleep(_stepTime);
                }
            } else {
                for (int i = startY; i < endY; i += _step) {
                    _single.setLocation(posx, i);
                    Thread.sleep(_stepTime);
                }
            }
            _single.setLocation(posx, endY);
        }
 
        /**
         * ��ʼ��������
         */
        public void run() {
            try {
                boolean animate = true;
                GraphicsEnvironment ge = GraphicsEnvironment
                        .getLocalGraphicsEnvironment();
                Rectangle screenRect = ge.getMaximumWindowBounds();
                int screenHeight = (int) screenRect.height;
                int startYPosition;
                int stopYPosition;
                if (screenRect.y > 0) {
                    animate = false;
                }
                _maxToolTipSceen = screenHeight / _height;
                int posx = (int) screenRect.width - _width - 1;
                _single.setLocation(posx, screenHeight);
                _single.setVisible(true);
                if (_useTop) {
                    _single.setAlwaysOnTop(true);
                }
                if (animate) {
                    startYPosition = screenHeight;
                    stopYPosition = startYPosition - _height - 1;
                    if (_countOfToolTip > 0) {
                        stopYPosition = stopYPosition
                                - (_maxToolTip % _maxToolTipSceen * _height);
                    } else {
                        _maxToolTip = 0;
                    }
                } else {
                    startYPosition = screenRect.y - _height;
                    stopYPosition = screenRect.y;
 
                    if (_countOfToolTip > 0) {
                        stopYPosition = stopYPosition
                                + (_maxToolTip % _maxToolTipSceen * _height);
                    } else {
                        _maxToolTip = 0;
                    }
                }
 
                _countOfToolTip++;
                _maxToolTip++;
 
                animateVertically(posx, startYPosition, stopYPosition);
                Thread.sleep(_displayTime);
                animateVertically(posx, stopYPosition, startYPosition);
 
                _countOfToolTip--;
                _single.setVisible(false);
                _single.dispose();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
 
    /**
     * �趨��ʾ��ͼƬ����Ϣ
     *
     * @param icon
     * @param msg
     */
    public void setToolTip(Icon icon, String msg) {
        ToolTipSingle single = new ToolTipSingle();
        if (icon != null) {
            single._iconLabel.setIcon(icon);
        }
        single._message.setText(msg);
        single.animate();
    }
 
    /**
     * �趨��ʾ����Ϣ
     *
     * @param msg
     */
    public void setToolTip(String msg) {
        setToolTip(null, msg);
    }
 
    /**
     * ��õ�ǰ��Ϣ����
     *
     * @return
     */
    public Font getMessageFont() {
        return _font;
    }
 
    /**
     * ���õ�ǰ��Ϣ����
     *
     * @param font
     */
    public void setMessageFont(Font font) {
        _font = font;
    }
 
    /**
     * ��ñ߿���ɫ
     *
     * @return
     */
    public Color getBorderColor() {
        return _border;
    }
 
    /**
     * ���ñ߿���ɫ
     *
     * @param _bgColor
     */
    public void setBorderColor(Color borderColor) {
        this._border = borderColor;
    }
 
    /**
     * �����ʾʱ��
     *
     * @return
     */
    public int getDisplayTime() {
        return _displayTime;
    }
 
    /**
     * ������ʾʱ��
     *
     * @param displayTime
     */
    public void setDisplayTime(int displayTime) {
        this._displayTime = displayTime;
    }
 
    /**
     * ��ò�ֵ
     *
     * @return
     */
    public int getGap() {
        return _gap;
    }
 
    /**
     * �趨��ֵ
     *
     * @param gap
     */
    public void setGap(int gap) {
        this._gap = gap;
    }
 
    /**
     * �����Ϣ��ɫ
     *
     * @return
     */
    public Color getMessageColor() {
        return _messageColor;
    }
 
    /**
     * �趨��Ϣ��ɫ
     *
     * @param messageColor
     */
    public void setMessageColor(Color messageColor) {
        this._messageColor = messageColor;
    }
 
    /**
     * ���ѭ������
     *
     * @return
     */
    public int getStep() {
        return _step;
    }
 
    /**
     * �趨ѭ������
     *
     * @param _step
     */
    public void setStep(int _step) {
        this._step = _step;
    }
 
    public int getStepTime() {
        return _stepTime;
    }
 
    public void setStepTime(int _stepTime) {
        this._stepTime = _stepTime;
    }
 
    public Color getBackgroundColor() {
        return _bgColor;
    }
 
    public void setBackgroundColor(Color bgColor) {
        this._bgColor = bgColor;
    }
 
    public int getHeight() {
        return _height;
    }
 
    public void setHeight(int height) {
        this._height = height;
    }
 
    public int getWidth() {
        return _width;
    }
 
    public void setWidth(int width) {
        this._width = width;
    }
 
    public static void main(String text) {
 
        ToolTip tip = new ToolTip();
        tip.setToolTip(new ImageIcon("images/huaji.png"),
                text);
 
    }
 
}
