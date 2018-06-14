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
	private static final String defaultData = "111111";
	private static final String look_query_close_state = "AB12003";//锁住
	private static final String look_query_open_state = "AB13004";//没锁
	
	private static final String look_query_state = "AB12003";//查询命令
	private static final String look_close_state = "AB10001";//锁命令
	private static final String look_open_state = "AB11002";//开锁命令
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
			jedis.set("lookid=1", "1");
			/*String state = checkState();
			if (state.equals(look_open_state)) {
				res.setState(look_open_state);
				res.setMsg("以执行 开锁");
				JSONObject fromObject = js.fromObject(res);
				System.out.println(fromObject.toString());
				pw.write(fromObject.toString());
			}else {
				res.setState("error");
				res.setMsg("开锁失败");
				JSONObject fromObject = js.fromObject(res);
				System.out.println(fromObject.toString());
				pw.write(fromObject.toString());
			}*/
			
		}
		//执行 锁
		if (command.equals(command_closeLook)) {
			jedis.set("lookid=1", "0");
			/*String state = checkState();
			if (state.equals(look_close_state)) {
				res.setState(look_close_state);
				res.setMsg("以执行 锁住状态");
				JSONObject fromObject = js.fromObject(res);
				System.out.println(fromObject.toString());
				pw.write(fromObject.toString());
			}else {
				res.setState("error");
				res.setMsg("锁失败");
				JSONObject fromObject = js.fromObject(res);
				System.out.println(fromObject.toString());
				pw.write(fromObject.toString());
			}*/
		}
		//查询
		if (command.equals(command_queryLook)) {
			jedis.set("lookid=1", "2");
		/*
			String state = checkState();
			if (state.equals(look_query_close_state)) {
				res.setMsg("当前锁状态:关");
				res.setState(look_query_close_state);
				JSONObject jo = js.fromObject(res);
				pw.write(jo.toString());
			}
			if (state.equals(look_query_open_state)) {
				res.setMsg("当前锁状态:开");
				res.setState(look_query_open_state);
				JSONObject jo = js.fromObject(res);
				pw.write(jo.toString());
			}*/
		}
		
		/*if (command.equals(command_queryLook)) {
			jedis.set("lookid=1", "2");
		}*/
	}
	
	/*private String checkState() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (jedis.exists("state")) {
			String state = jedis.get("state");
			System.out.println("state="+state);
			return state;
		}
		return "";
	}
	*/
	
	

}
