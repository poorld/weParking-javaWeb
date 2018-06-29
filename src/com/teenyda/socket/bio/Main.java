package com.teenyda.socket.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.servlet.http.HttpServlet;

/**
* @author  作者 E-mail: 
* @date 创建时间：2018年6月2日 下午9:14:25 
* @version 1.0 
* @parameter  
* @since  
* @return  
*/
public class Main extends HttpServlet{
	MyThread thread ;

	public void init() throws javax.servlet.ServletException {
		// TODO Auto-generated method stub
		thread = new MyThread();
		thread.start();
	}
	
	class MyThread extends Thread {
		ServerSocket serverSocket;
		@Override
		public void run() {
			try {
				serverSocket = new ServerSocket(9527);
				while (true) {
					System.out.println("监听端口:9527");
					System.out.println("等待连接");
					Socket socket = serverSocket.accept();
					System.out.println(socket.getInetAddress().getHostAddress()+"连接成功");
					Server server = new Server(socket);
//					MySocketServer adminSocket = new MySocketServer(socket);
					new Thread(server).start();
					//new Thread(adminSocket).start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		//thread.interrupt();
	}
	

}
