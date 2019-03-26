package com.lxl.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ServerThread extends Thread {
	final String configPath = "src/com/lxl/config/config.properties";
	Socket socket = null;
	static Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	static JLabel label = new JLabel();
	static JFrame controllerUI = new JFrame();
	static {
		controllerUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controllerUI.setSize(d.getSize());
		controllerUI.add(label);
		controllerUI.setVisible(true);
	}
	public ServerThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		
		ImageIcon img = null;
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			img = (ImageIcon) ois.readObject();
			label.setIcon(img);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}

//
// String setPassword = null;
// String code = null;
// String pw = null;
// Properties prop = new Properties();
// File f = new File(configPath);
// InputStream is = null;
// BufferedWriter bw = null;
// try {
// br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
// if ((str = br.readLine()) != null) {
// is = new BufferedInputStream(new FileInputStream(f));
// prop.load(is);
// setPassword = prop.getProperty("setPassword");
// code = prop.getProperty("code");
// pw = (setPassword == null || setPassword.equals("")) ? code : setPassword;
// if (str.substring(0, str.indexOf(":")).equals("confirm")) {
// str = str.substring(str.indexOf("confirm"));
// if (pw.equals(str)) {
// bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
// bw.write("confirmSucceed");
// System.out.println("被控制端验证成功");
// bw.flush();
// } else {
//
// }
// } else {
//
// }
//
// }
// } catch (IOException e) {
// e.printStackTrace();
// } finally {
// if (bw != null) {
// try {
// bw.close();
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
//
// if (is != null) {
// try {
// is.close();
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
// if (br != null) {
// try {
// br.close();
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
// if (socket != null) {
// try {
// socket.close();
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
// }
