package com.teenyda.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teenyda.bean.Response;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

/**
 * 与SocketServer通信
 * Servlet implementation class Socket
 */
@WebServlet("/socket")
public class Socket extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    private boolean update = false;
    private static final String command_openLook = "openlook";
	private static final String command_closeLook = "closelook";
	private static final String command_queryLook = "querylook";
	/**
	 * 下行数据 开锁
	 */
	private static final String down_unlocking = "AB11002";
	/**
	 * 下行数据 锁
	 */
	private static final String down_lockup = "AB10001";
	/**
	 * 下行数据 查询
	 */
	private static final String down_query = "AB12003";
	private Jedis jedis = new Jedis();
	private JSONObject js = new  JSONObject();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Socket() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter pw = response.getWriter();
		String command = request.getParameter("command");
		System.out.println(command);
		/*Response res = new Response();*/
		//执行开锁
		if (command.equals(command_openLook)) {
			jedis.set("lookNumber=1", down_unlocking);
		}
		//执行 锁
		if (command.equals(command_closeLook)) {
			jedis.set("lookNumber=1", down_lockup);
		}
		//查询
		if (command.equals(command_queryLook)) {
			jedis.set("lookNumber=1", down_query);
		}
	}
	
	

}
