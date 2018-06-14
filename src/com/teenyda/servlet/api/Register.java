package com.teenyda.servlet.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Url;

import com.teenyda.bean.SensitiveData;
import com.teenyda.bean.UserEntity;
import com.teenyda.common.ApiResponseObject;
import com.teenyda.service.UserEntityServiceImpl;
import com.teenyda.util.GetUserInfo;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

/**
 * 注册接口 Servlet implementation class Login
 */
@WebServlet("/register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Jedis jedis = new Jedis();
	private UserEntityServiceImpl userService = new UserEntityServiceImpl();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Register() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("注册");
		response.setContentType("text/json;charset=utf-8");
		PrintWriter pw = response.getWriter();
		
		JSONObject js = new JSONObject();
		// 获取数据
		ServletInputStream is = request.getInputStream();
		/*
		 * JSONObject js = GetUserInfo.inputStreamToJson(is);
		 * System.out.println(js.toString());
		 */
		// {"js_code":"001cN3Wh24kGbH0MxtZh2qk8Wh2cN3WE","encryptedData":"nuFaBtjnh/fOX7dhZouEVMRVmPGP6e7g88fvEWfYxguaocuAG+WrZAZ4lTTgT2YWt9VhOIXR4H9ZB4fW25nZTHUCumWA1s4wLlBXPTTpqo1wp2ZwpRJmK9O9HU0UssyG49tlJqPYx/R1JUfEiTUdGJJYzZlWDznWT11d1aYxn9H4fLcdBzpFJxmPNQiNhbnVHIPQDRto3YxyTKuYHKRyjXG59pUZ4wV7MwxTT/ZbXHr/8n4KGXuFMs4kEMD6C/Nvx0ZBdVq/0dYLb1H8vGsfoI9xsG28+2infjH+c25YyV0YKa3as0kf0ZsDq1ZBnsVurcy+75aM+3Nw1bZ61bFH6LqlKFStJqhuZpeLoi/tnWQwrKmhdXKz4mXpmWM/wiaDyWn3/HiKfLCZCDEJP7cLHi7Czc95yLIPWoPtJHpzKd2JUKMOkQuHFFZ6+aPoLaryjSc0faSDL3zljx+uwVpDxoRTqEOH+nmpqPXHNOZR7W0=","iv":"BrQr873cDbcpWOmeYMZPCA=="}
		SensitiveData sensitiveData = GetUserInfo.getSensitiveData(is);
		if (sensitiveData.getSessionKey() == null) {
			ApiResponseObject resp = new ApiResponseObject();
			resp.setErrorCode(-1);
			resp.setErrorMsg("获取参数失败");
			pw.write(js.fromObject(resp).toString());
		} else {
			System.out.println(sensitiveData);
			// 通过key 获取 value
			System.out.println(sensitiveData.getSessionKey());
			if (jedis.exists(sensitiveData.getSessionKey())){
				System.out.println("exists");
			}
			String sessionValue = jedis.get(sensitiveData.getSessionKey());
			String key = sessionValue.split("哒")[0]; // sessionKey哒openid
			String openid = sessionValue.split("哒")[1];// sessionKey哒openid
			boolean register = userService.isRegister(openid);
			if (register) {
				ApiResponseObject resp = new ApiResponseObject();
				resp.setErrorCode(0);
				resp.setErrorMsg("用户已注册");
				System.out.println("已注册");
				pw.write(js.fromObject(resp).toString());
			} else {
				ApiResponseObject resp = new ApiResponseObject();
				System.out.println("未注册");
				JSONObject decryptData = GetUserInfo.getDecryptData(sensitiveData.getEncryptedData(), key,
						sensitiveData.getIv());
				System.out.println(decryptData.toString());
				UserEntity entity = new UserEntity();
				entity.setNickName(decryptData.getString("nickName"));
				entity.setProvince(decryptData.getString("province"));
				entity.setCity(decryptData.getString("city"));
				entity.setOpenid(decryptData.getString("openId"));
				entity.setAvatarurl(decryptData.getString("avatarUrl"));
				Date date = new Date();
				String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				entity.setTime(time);
				userService.register(entity);
				resp.setErrorCode(1);
				resp.setErrorMsg("注册成功");
				pw.write(js.fromObject(resp).toString());
			}
		}

	}

}
