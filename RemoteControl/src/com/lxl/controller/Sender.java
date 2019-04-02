package com.lxl.controller;

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
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;

public class Sender implements Runnable {

	@Override
	public void run() {
		/**
		 * 发送界面图片
		 */
		new Thread(new Runnable() {

			@Override
			public void run() {
				Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
				Robot robot = null;
				ImageIcon img = null;
				Socket socket = null;
				ObjectOutputStream oos = null;
				try {
					// 开启两个线程 1接收鼠标事件 2接受键盘事件
					while (true) {
						// socket = new Socket("172.16.140.132", 9999);
						socket = new Socket("192.168.1.145", 9999);
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
		/**
		 * 接收鼠标指令，及键盘指令
		 */
		new Thread(new Runnable() {
			@Override
			public void run() {
				ServerSocket receiveOrder = null;
				ObjectInputStream ois = null;
				Socket socket = null;
				try {
					Robot r = new Robot();
					System.out.println("鼠标指令接收服务器启动");
					receiveOrder = new ServerSocket(8888);
					InputEvent e = null;
					MouseWheelEvent mwe = null;
					MouseEvent me = null;
					int count = 0;

					int num = 0;
					int button = 0;

					while (true) {
						socket = receiveOrder.accept();
						ois = new ObjectInputStream(socket.getInputStream());
						e = (InputEvent) ois.readObject();
						// 判断滚轮滑动事件，鼠标事件
						if (e instanceof MouseWheelEvent) {
							mwe = (MouseWheelEvent) e;
							// 获取滚轮的滚动次数（分正负）
							count = mwe.getWheelRotation();
							// 移动鼠标指针
							r.mouseMove(mwe.getX(), mwe.getY());
							// 模拟鼠标滚轮滚动
							r.mouseWheel(count);
						} else if (e instanceof MouseEvent) {
							me = (MouseEvent) e;
							// 实时移动鼠标位置（需进行改进，控制端和被控制端位置不精确）
							r.mouseMove(me.getX(), me.getY());
							/**
							 * 更具事件ID判断
							 * 
							 * 如需更多事件需在发送指令端进行监听
							 */
							switch (me.getID()) {
							case MouseEvent.MOUSE_DRAGGED:
								switch (me.getButton()) {
								case MouseEvent.BUTTON1:// 左键
									button = InputEvent.BUTTON1_MASK;// 设置为左键
									/**
									 * 不用MouseEvent.BUTTON1的原因，因为后面的Robot模拟操作所需的
									 * 参数不对应，需转换。 以下同此处。
									 */
									break;
								case MouseEvent.BUTTON2:
									button = InputEvent.BUTTON2_MASK;
									break;
								case MouseEvent.BUTTON3:
									button = InputEvent.BUTTON3_MASK;
									break;
								}
								// 模拟操作
								r.mousePress(button);
								r.mouseMove(me.getX(), me.getY());
								break;
							case MouseEvent.MOUSE_RELEASED:
								r.mouseRelease(button);
								break;
							case MouseEvent.MOUSE_PRESSED:
								switch (me.getButton()) {
								case MouseEvent.BUTTON1:
									button = InputEvent.BUTTON1_MASK;
									break;
								case MouseEvent.BUTTON2:
									button = InputEvent.BUTTON2_MASK;
									break;
								case MouseEvent.BUTTON3:
									button = InputEvent.BUTTON3_MASK;
									break;
								}
								// 判断是否为双击事件
								num = me.getClickCount();
								if (num == 2) {
									// 双击事件多按下一次
									r.mousePress(button);
								}
								r.mousePress(button);
							}
						} else {
							/**
							 * 此处为键盘事件接收
							 * 
							 * 与鼠标事件类似，使用Robot类模拟点击事件
							 */
						}
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
	}

	public static void main(String[] args) {
		new Thread(new Sender()).start();
	}
}
