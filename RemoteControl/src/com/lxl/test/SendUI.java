package com.lxl.test;


import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;

public class SendUI {
	public static void main(String[] args) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				ServerSocket receiveOrder = null;
				ObjectInputStream ois = null;
				Socket socket = null;
				try {
					receiveOrder = new ServerSocket(8888);
					while (true) {
						socket = receiveOrder.accept();
						ois = new ObjectInputStream(socket.getInputStream());
						Object e = ois.readObject();
						// 处理指令
						MouseEvent me = null;
						MouseWheelEvent mwe = null;
						boolean isWheelEvent = true;
						me = (MouseEvent) e;
						Robot r = new Robot();
						r.mouseMove(me.getX(), me.getY());
						switch (me.getButton()) {
						case MouseEvent.BUTTON1:
							System.err.println("鼠标左键事件");
							break;
						case MouseEvent.BUTTON2:
							double dir = mwe.getWheelRotation();
							if (dir > 0) {
								System.err.println("滚轮向上上上上上上滚动");// 手的方向
							} else {
								System.err.println("滚轮向下下下下下下滚动");
							}
							break;
						case MouseEvent.BUTTON3:
							System.err.println("鼠标右键事件");
							break;
						default:
							System.err.println("执行");
						}
//						try {
//							mwe = (MouseWheelEvent) e;
//						} catch (ClassCastException e1) {
//							me = (MouseEvent) e;
//							isWheelEvent = false;
//						}
//						Robot r = new Robot();
//						if (isWheelEvent) {
//							double dir = mwe.getWheelRotation();
//							if (dir > 0) {
//								System.err.println("滚轮向上上上上上上滚动");// 手的方向
//							} else {
//								System.err.println("滚轮向下下下下下下滚动");
//							}
//						} else {
//							r.mouseMove(me.getX(), me.getY());// 获取鼠标位置，根据窗口大小，比例计算
//							switch (me.getButton()) {
//							case MouseEvent.BUTTON1:
//								System.err.println("鼠标左左左左左左左左键");
//								r.mousePress(InputEvent.BUTTON1_MASK);
//								r.mouseRelease(InputEvent.BUTTON1_MASK);
//								break;
//							case MouseEvent.BUTTON2:
//								System.err.println("鼠标中中中中中中中中键");
//								r.mousePress(InputEvent.BUTTON2_MASK);
//								r.mouseRelease(InputEvent.BUTTON2_MASK);
//								break;
//							case MouseEvent.BUTTON3:
//								System.err.println("鼠标右右右右右右右右键");
//								r.mousePress(InputEvent.BUTTON3_MASK);
//								r.mouseRelease(InputEvent.BUTTON3_MASK);
//								break;
//							}
//						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (AWTException e1) {
					e1.printStackTrace();
				}
			}
		}).start();
		new Thread(new Runnable() {
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			Robot robot = null;
			ImageIcon img = null;
			Socket socket = null;
			ObjectOutputStream oos = null;

			@Override
			public void run() {
				try {
					// 开启两个线程 1接收鼠标事件 2接受键盘事件
					while (true) {
						socket = new Socket("172.16.140.132", 9999);
						robot = new Robot();
						img = new ImageIcon(robot
								.createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight())));// 截取屏幕
						oos = new ObjectOutputStream(socket.getOutputStream());
						oos.writeObject(img);
						oos.flush();
					}
				} catch (AWTException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}
}
