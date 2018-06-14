package com.teenyda.servlet.api;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teenyda.common.ApiResponseObject;
import com.teenyda.service.UserEntityServiceImpl;
import com.teenyda.util.GetUserInfo;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

/**
 * 微信小程序登录接口 请求参数： js_code 返回参数：随机生成的uuid -> 3rdsession
 * 把3rdsession存入Redis缓存，失效时间：7200秒 Servlet implementation class CheckLogin
 */
@WebServlet("/onLogin")
public class AppLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Jedis jedis = new Jedis();
	private static final int sessionTime = 7200;
	private UserEntityServiceImpl userService = new UserEntityServiceImpl();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AppLogin() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response) 微信小程序登录只需传js_code即可
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		ServletInputStream is = request.getInputStream();
		Map<String, String> infos = GetUserInfo.loginInWXservice(is);// 获取sessionKey与openId
		ApiResponseObject resp = new ApiResponseObject();
		JSONObject js = new JSONObject();
		if (infos != null) {
			resp.setErrorCode(1);
			resp.setErrorMsg("login success");
			// 查看是否注册
			// 生成3rd_session
			String rd_session = UUID.randomUUID().toString();
			resp.setData(new String(rd_session));
			// 以rd_sessioni为key，sessionKey+openid 为value存入redis
			String value = infos.get("sessionKey") + "哒" + infos.get("openid");
			// jedis.set(rd_session, value);
			jedis.setex(rd_session, sessionTime, value);
			System.out.println("rd_sessionKey=" + rd_session);
			System.out.println("rd_sessionValue=" + value);
			System.out.println("----------------------------");
			System.out.println("登录");
			System.out.println("----------------------------");
			System.out.println("sessionKey\t" + rd_session);
			System.out.println("sessionValue\t" + value);
			System.out.println("----------------------------");
			JSONObject json = js.fromObject(resp);
			response.getWriter().write(json.toString());
		} else {
			resp.setErrorCode(-1);
			resp.setErrorMsg("获取js_code失败");
			JSONObject json = js.fromObject(resp);
			response.getWriter().write(json.toString());
			System.out.println(json.toString());
		}
	}

}
