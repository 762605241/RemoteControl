package com.lxl.test;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
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
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
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
						img = new ImageIcon(
								robot.createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight())));// 截取屏幕
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
