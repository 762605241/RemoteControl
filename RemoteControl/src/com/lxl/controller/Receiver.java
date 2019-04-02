package com.lxl.controller;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;

/**
 * 远程控制
 * 
 * 界面接收端线程方法
 * 
 * 任务：1、接受界面；1、发送鼠标，键盘事件
 * 
 * @author lxl
 *
 */
public class Receiver implements Runnable {

	static Socket socket = null;
	// 获取屏幕尺寸
	static Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	// 界面显示（显示传送过来的界面图片）
	static JLabel label = new JLabel();
	static JFrame controllerUI = new JFrame();
	static {
		controllerUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controllerUI.setSize(d.getSize());
		label.setSize(d.getSize());
		controllerUI.add(label);
		controllerUI.setVisible(true);

	}

	@Override
	public void run() {
		/**
		 * 界面接收
		 */
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println("UI界面接收服务器启动");
					ServerSocket receive = new ServerSocket(9999);
					ImageIcon img = null;
					ObjectInputStream ois = null;
					while (true) {
						socket = receive.accept();
						ois = new ObjectInputStream(socket.getInputStream());
						img = (ImageIcon) ois.readObject();
						label.setSize(new Dimension(img.getIconWidth(), img.getIconHeight()));
						label.setIcon(img);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}).start();
		/**
		 * 鼠标键盘事件
		 * 
		 * 发送
		 */
		new Thread(new Runnable() {
			private void sendAction(InputEvent e) {
				if (socket != null) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							ObjectOutputStream oos = null;
							Socket order = null;
							try {
								order = new Socket("192.168.31.135", 8888);
								oos = new ObjectOutputStream(order.getOutputStream());
								// 创建鼠标事件
								oos.writeObject(e);
								oos.flush();
							} catch (IOException e1) {
								e1.printStackTrace();
							} finally {
								if (oos != null) {
									try {
										oos.close();
									} catch (IOException e1) {
										e1.printStackTrace();
									}
								}
							}
						}
					}).start();
				}
			}

			@Override
			public void run() {
				controllerUI.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						sendAction(e);
					}

					@Override
					public void keyTyped(KeyEvent e) {
						sendAction(e);
					}
				});
				label.addMouseWheelListener(new MouseAdapter() {
					@Override
					public void mouseWheelMoved(MouseWheelEvent e) {
						sendAction(e);
					}
				});
				label.addMouseMotionListener(new MouseAdapter() {
					@Override
					public void mouseMoved(MouseEvent e) {
						sendAction(e);
					}

					@Override
					public void mouseDragged(MouseEvent e) {
						sendAction(e);
					}
				});
				label.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						sendAction(e);
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						sendAction(e);
					}

					@Override
					public void mouseClicked(MouseEvent e) {
						sendAction(e);
					}

					@Override
					public void mouseDragged(MouseEvent e) {
						sendAction(e);
					}
				});
			}
		}).start();
	}

	public static void main(String[] args) {
		new Thread(new Receiver()).start();
	}
}
