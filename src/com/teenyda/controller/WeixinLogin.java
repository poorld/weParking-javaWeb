package com.teenyda.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teenyda.bean.Success;
import com.teenyda.bean.User;
import com.teenyda.util.GetOpenid;

import net.sf.json.JSONObject;

/**
 * 测试登录，保留
 * Servlet implementation class WeixinLogin
 */
@WebServlet("/gg")
public class WeixinLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WeixinLogin() {
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
		// TODO Auto-generated method stub
		//doGet(request, response);
		response.setContentType("text/html;charset=utf-8");
		ServletInputStream is = request.getInputStream();
		User user = getUser(is);
		System.out.println("appid:"+user.getAppid());
		System.out.println("sercet:"+user.getSecret());
		System.out.println("js_code:"+user.getJs_code());
		System.out.println("encryptedData:"+user.getEncryptedData());
		System.out.println("iv:"+user.getIv());
		String path = "https://api.weixin.qq.com/sns/jscode2session?Appid="+user.getAppid()+
					"&secret="+user.getSecret()+"&js_code="+user.getJs_code()+"&grant_type=authorization_code";
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5000);
		InputStream inputStream = conn.getInputStream();
		StringBuffer status = new StringBuffer();
		//BufferedReader buf = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
		Success success = getSessionKey(inputStream);
		System.out.println("openid:"+success.getOpenid());
		System.out.println("session_key:"+success.getSession_key());
		//{"session_key":"oZpdgmHlutXLGU4LmxOTUw==","expires_in":7200,"openid":"oam4D0QY-X57_xbv27L_w9G4fBjA"}
		//生成3rd_session
		//UUID uuid = UUID.randomUUID();
		JSONObject jsonObject = new GetOpenid().getUserInfo(user.getEncryptedData(),success.getSession_key(), user.getIv());
		String json = jsonObject.toString();//获取解密后的数据
		System.out.println(json);
		PrintWriter pw = response.getWriter();
		pw.write(json);
		pw.flush();
		pw.close();
		/**
		 * {"country":"China",
		 * "watermark":{"appid":"wx2f7904a87e445037","timestamp":1526475146},
		 * "gender":1,
		 * "province":"Guangxi",
		 * "city":"Nanning",
		 * "avatarUrl":"https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83epDwZ9fM2m5Fs7S12f6g7XYp46WMBbwsWoGr1T1vcMscQXYmOEtMcvRUMYrH6DbjY1Wfx8rRuLdJA/132",
		 * "openId":"oam4D0QY-X57_xbv27L_w9G4fBjA",
		 * "nickName":"露小哒",
		 * "language":"zh_CN"
		 * }
		 */
	}
	
	protected User getUser(InputStream is){
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = br.readLine()) != null){
				sb.append(line);
			}
			System.out.println(sb.toString());
			//{"appid":"wx2f7904a87e445037","secret":"4d04ab6b3e59fe18a712388a85942845","js_code":"0115dFMO1T6ru317ygPO1hwFMO15dFMb","grant_type":"authorization_code"}
			JSONObject json = new JSONObject();
			JSONObject object = json.fromObject(sb.toString());
			User bean = (User) object.toBean(object, User.class);
			return bean;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	protected Success getSessionKey(InputStream is){
		JSONObject json = new JSONObject();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String line = "";
			StringBuilder status = new StringBuilder();
			while((line = br.readLine()) != null){
				status.append(line);
			}
			System.out.println(status.toString());
			JSONObject fromObject = json.fromObject(status.toString());
			Success success = (Success) fromObject.toBean(fromObject, Success.class);
			return success;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (br!= null){
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
		
	}

}
