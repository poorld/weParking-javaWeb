package com.teenyda.servlet.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teenyda.bean.AppSession;
import com.teenyda.common.ApiResponseObject;
import com.teenyda.util.GetUserInfo;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

/**
 * 检查session是否有效
 * 请求参数：sessionkey
 * 返回参数：
 * 	SessionExpired 无效
 * 	SessionEffectiveness 有效
 * Servlet implementation class CheckLogin
 */
@WebServlet("/checkSession")
public class CheckSession extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Jedis jedis = new Jedis();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckSession() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter pw = response.getWriter();
		JSONObject js = new JSONObject();
		System.out.println("checkSession");
		ServletInputStream is = request.getInputStream();
		String sessionKey = GetUserInfo.getAppSessionKey(is);
		ApiResponseObject resp = new ApiResponseObject();
		if (!jedis.exists(sessionKey)) {
			resp.setErrorCode(-1);
			resp.setErrorMsg("SessionExpired");
			System.out.println("SessionExpired");
			pw.write(js.fromObject(resp).toString());//会话无效
		} else {
			resp.setErrorCode(1);
			resp.setErrorMsg("SessionEffectiveness");
			System.out.println("SessionEffectiveness");
			pw.write(js.fromObject(resp).toString());//会话有效
		}
		
	}

}
