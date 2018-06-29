package com.teenyda.socket.nio;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class Start extends HttpServlet{

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		new Thread(new Reactor2()).start();
	}
}
