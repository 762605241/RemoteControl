package com.lxl.ui;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MainUI extends JFrame {

	// 远程主机ID标识
	private JTextField remoteID;
	// 本机ID标识
	private JLabel localID;
	// 随机验证码
	private JLabel code;
	// 刷新按钮
	private JButton refresh;
	// 连接按钮
	private JButton connect;
	// 设置按钮
	private JButton set;

	final String configPath = "src/com/lxl/config/config.properties";
	private JTextField passwd;

	public JTextField getPasswd() {
		return passwd;
	}

	public void setPasswd(JTextField passwd) {
		this.passwd = passwd;
	}

	// 初始化主界面
	public MainUI() {
		getContentPane().setLayout(null);

		this.setTitle("远程控制");

		this.setSize(500, 300);

		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel lblid = new JLabel("\u672C\u673AID");
		lblid.setBounds(39, 45, 86, 15);
		getContentPane().add(lblid);

		JLabel lblid_1 = new JLabel("\u8FDC\u7A0B\u4E3B\u673AID");
		lblid_1.setBounds(39, 85, 86, 15);
		getContentPane().add(lblid_1);

		localID = new JLabel("123456789");
		localID.setBounds(191, 45, 120, 15);
		getContentPane().add(localID);

		remoteID = new JTextField();
		remoteID.setBounds(191, 82, 120, 21);
		getContentPane().add(remoteID);
		remoteID.setColumns(10);

		JLabel label_1 = new JLabel("\u9A8C\u8BC1\u7801");
		label_1.setBounds(39, 159, 54, 15);
		getContentPane().add(label_1);

		code = new JLabel("999999");
		code.setBounds(191, 159, 72, 15);
		getContentPane().add(code);

		refresh = new JButton("\u5237\u65B0");
		refresh.setBounds(331, 155, 93, 23);
		getContentPane().add(refresh);

		connect = new JButton("\u8FDE\u63A5");
		connect.setBounds(62, 208, 93, 23);
		getContentPane().add(connect);

		JButton set = new JButton("\u8BBE\u7F6E");
		set.setBounds(253, 208, 93, 23);
		getContentPane().add(set);
		
		passwd = new JTextField();
		passwd.setBounds(191, 113, 120, 21);
		getContentPane().add(passwd);
		passwd.setColumns(10);
		
		JLabel label = new JLabel("密码");
		label.setBounds(39, 116, 54, 15);
		getContentPane().add(label);

		// 初始化页面数据
		initMainUIData();
		// 绑定控件事件
		bind();
	}

	private static void startLocalServer() {
		try {
			ServerSocket server = new ServerSocket(8888);
			Socket socket = null;
			System.out.println("服务器启动");
			while (true) {
				socket = server.accept();
				System.out.println("已连接" + socket.getInetAddress().getHostAddress());
				ServerThread clientService = new ServerThread(socket);
				clientService.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void bind() {
		// 关闭事件
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}

			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
			}
		});
		// 验证码刷新按钮事件
		refresh.addActionListener(refreshListener());
		// 连接事件
		connect.addActionListener(connectListener());
	}

	private ActionListener connectListener() {
		ActionListener a = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tryToConnect();
			}

		};
		return a;
	}

	private void tryToConnect() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		Robot robot = null;
		ImageIcon img = null;
		Socket socket = null;
		try {
			socket = new Socket(getRemoteID().getText(), 8888);
			robot = new Robot();
			while (true) {
				img = new ImageIcon(robot.createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight())));// 截取屏幕
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(img);
				oos.flush();
			}
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	
//		// 1、获取验证码、设置密码、用户名及密码
//		String strCode = code.getText();
//		File f = new File(configPath);
//		String setPassword = null;
//		Properties prop = new Properties();
//		InputStream is = null;
//		try {
//			is = new BufferedInputStream(new FileInputStream(f));
//			prop.load(is);
//			setPassword = prop.getProperty("setPassword");
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (is != null) {
//				try {
//					is.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		String strRemoteID = remoteID.getText();
//		String pw = null;
//		BufferedWriter bw = null;
//		Socket s = null;
//		if (strRemoteID != null && !strRemoteID.equals("")) {
//			pw = (setPassword != null && !setPassword.equals("")) ? setPassword : strCode;
//			// 启动客户端 尝试连接
//			try {
//				s = new Socket(strRemoteID, 8888);
//				bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
//				bw.write("confirm:" + pw);
//				bw.flush();
//			} catch (UnknownHostException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				if (bw != null) {
//					try {
//						bw.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//				if (s != null) {
//					try {
//						s.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
	}

	private ActionListener refreshListener() {
		ActionListener a = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getCode().setText(getRandomCode());
			}
		};
		return a;
	}

	private void initMainUIData() {


		File f = new File(configPath);
		Properties prop = new Properties();
		InputStream is = null;
		OutputStream os = null;
		String localID = null;
		try {
			if (!f.exists() || f.length() == 0) {
				f.createNewFile();
				os = new BufferedOutputStream(new FileOutputStream(f));
				localID = InetAddress.getLocalHost().getHostAddress();
				prop.setProperty("localHostID", localID);
				// 根据ip生成特定切唯一的ID
				getLocalID().setText(localID);

				prop.setProperty("randomCode",
						"a,b,c,d,e,f,g,h,i,g,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,1,2,3,4,5,6,7,8,9,0");
				prop.store(os, new Date().toString());
				prop.setProperty("code", getRandomCode());
				prop.store(os, new Date().toString());
				getCode().setText(getRandomCode());
			} else {
				is = new BufferedInputStream(new FileInputStream(f));
				prop.load(is);
				localID = prop.getProperty("localHostID");
				getLocalID().setText(localID);
				getCode().setText(getRandomCode());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getRandomCode() {
		File f = new File(configPath);
		Properties prop = new Properties();
		InputStream is = null;
		OutputStream os = null;
		StringBuilder sb = new StringBuilder();
		try {
			is = new BufferedInputStream(new FileInputStream(f));
			prop.load(is);
			String[] codes = prop.getProperty("randomCode").split(",");
			int i = 0;
			for (; i < 6; i++) {
				sb.append(codes[(int) (Math.random() * 62)]);
			}
			os = new BufferedOutputStream(new FileOutputStream(f));
			prop.setProperty("code", sb.toString());
			prop.store(os, new Date().toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	public JTextField getRemoteID() {
		return remoteID;
	}

	public void setRemoteID(JTextField remoteID) {
		this.remoteID = remoteID;
	}

	public JLabel getLocalID() {
		return localID;
	}

	public void setLocalID(JLabel localID) {
		this.localID = localID;
	}

	public JLabel getCode() {
		return code;
	}

	public void setCode(JLabel code) {
		this.code = code;
	}

	public JButton getRefresh() {
		return refresh;
	}

	public void setRefresh(JButton refresh) {
		this.refresh = refresh;
	}

	public JButton getConnect() {
		return connect;
	}

	public void setConnect(JButton connect) {
		this.connect = connect;
	}

	public JButton getSet() {
		return set;
	}

	public void setSet(JButton set) {
		this.set = set;
	}

	public static void main(String[] args) {
		MainUI mainUI = new MainUI();
		mainUI.setVisible(true);
		// 开启本机服务器
		startLocalServer();
	}
}
