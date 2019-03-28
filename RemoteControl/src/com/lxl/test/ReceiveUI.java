package com.lxl.test;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ReceiveUI {
	final String configPath = "src/com/lxl/config/config.properties";
	static Socket socket = null;
	static Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	static JLabel label = new JLabel();
	static JFrame controllerUI = new JFrame();
	static {
		controllerUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controllerUI.setSize(d.getSize());
		label.setSize(d.getSize());
		controllerUI.add(label);
		controllerUI.setVisible(true);
	}

	public static void main(String[] args) {

		label.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				System.out.println(e.toString()); // 滚轮事件
				if (socket != null) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							ObjectOutputStream oos = null;
							Socket order = null;
							try {
								order = new Socket("192.168.31.135", 8888);
								oos = new ObjectOutputStream(order.getOutputStream());
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
		});
		label.addMouseListener(new MouseAdapter() {
			private void sendOrder(MouseEvent e) { //
				System.out.println(e.toString());
				if (socket != null) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							ObjectOutputStream oos = null;
							Socket order = null;
							try {
								order = new Socket("192.168.31.135", 8888);
								oos = new ObjectOutputStream(order.getOutputStream());
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
			public void mouseClicked(MouseEvent e) { // 单击事件
				sendOrder(e);
			}

			@Override
			public void mouseDragged(MouseEvent e) { // 点击拖动事件
				sendOrder(e);
			}

			@Override
			public void mouseExited(MouseEvent e) { // 离开组件事件
				sendOrder(e);
			}

			@Override
			public void mouseMoved(MouseEvent e) { // 移动事件
				sendOrder(e);
			}

		});
/**
		controllerUI.addKeyListener(new KeyAdapter() {
		创建另一个线程，监听键盘事件。
			private void sendOrder(KeyEvent e) {
				System.out.println(e.toString());
				if (socket != null) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							ObjectOutputStream oos = null;
							try {
								oos = new ObjectOutputStream(socket.getOutputStream());
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
			public void keyPressed(KeyEvent e) {
				// 按下事件
				sendOrder(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// 释放事件
				sendOrder(e);
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// 键入事件（按下，再释放）
				sendOrder(e);
			}
		});
		*/
		try {

			ServerSocket receive = new ServerSocket(9999);
			ImageIcon img = null;
			ObjectInputStream ois = null;
			while (true) {
				socket = receive.accept();
				ois = new ObjectInputStream(socket.getInputStream());
				img = (ImageIcon) ois.readObject();
				label.setIcon(img);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
