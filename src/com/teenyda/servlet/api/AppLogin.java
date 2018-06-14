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
 * ΢��С�����¼�ӿ� ��������� js_code ���ز�����������ɵ�uuid -> 3rdsession
 * ��3rdsession����Redis���棬ʧЧʱ�䣺7200�� Servlet implementation class CheckLogin
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
	 *      response) ΢��С�����¼ֻ�贫js_code����
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		ServletInputStream is = request.getInputStream();
		Map<String, String> infos = GetUserInfo.loginInWXservice(is);// ��ȡsessionKey��openId
		ApiResponseObject resp = new ApiResponseObject();
		JSONObject js = new JSONObject();
		if (infos != null) {
			resp.setErrorCode(1);
			resp.setErrorMsg("login success");
			// �鿴�Ƿ�ע��
			// ����3rd_session
			String rd_session = UUID.randomUUID().toString();
			resp.setData(new String(rd_session));
			// ��rd_sessioniΪkey��sessionKey+openid Ϊvalue����redis
			String value = infos.get("sessionKey") + "��" + infos.get("openid");
			// jedis.set(rd_session, value);
			jedis.setex(rd_session, sessionTime, value);
			System.out.println("rd_sessionKey=" + rd_session);
			System.out.println("rd_sessionValue=" + value);
			System.out.println("----------------------------");
			System.out.println("��¼");
			System.out.println("----------------------------");
			System.out.println("sessionKey\t" + rd_session);
			System.out.println("sessionValue\t" + value);
			System.out.println("----------------------------");
			JSONObject json = js.fromObject(resp);
			response.getWriter().write(json.toString());
		} else {
			resp.setErrorCode(-1);
			resp.setErrorMsg("��ȡjs_codeʧ��");
			JSONObject json = js.fromObject(resp);
			response.getWriter().write(json.toString());
			System.out.println(json.toString());
		}
	}

}
