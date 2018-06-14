package com.teenyda.servlet.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teenyda.common.ApiResponseObject;
import com.teenyda.service.MarkersServiceImap;
import com.teenyda.util.GetUserInfo;

import jdk.nashorn.internal.parser.JSONParser;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

/**
 * ѡ��ó�λ���߸�����λ Servlet implementation class ChooseParking
 */
@WebServlet("/chooseParking")
public class ChooseParking extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int Available = 1;// ����ʹ��
	private static final int Unavailable = 2;// ����ʹ��
	private Jedis jedis = new Jedis();
	private MarkersServiceImap markerService = new MarkersServiceImap();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChooseParking() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		this.doPost(request, response);
	}

	/**
	 * ��Ҫ�Ĳ���lookid��������ţ�, sessionKey(�Զ����sessionkey)
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/json;charset=utf-8");
		PrintWriter pw = response.getWriter();
		System.out.println("ChooseParkingѡ��λ");
		ServletInputStream is = request.getInputStream();
		JSONObject js = GetUserInfo.inputStreamToJson(is);
		if (js.has("sessionKey")) {
			String sessionKey = js.getString("sessionKey");
			if (jedis.exists(sessionKey)) {
				// ���ĳ�λ
				if (js.has("temID") && js.has("markerId")) {
					String temID = js.getString("temID");
					String markerId = js.getString("markerId");
					System.out.println("���ĳ�λ");
					ApiResponseObject resp = new ApiResponseObject();
					markerService.updateMarker(Available, Integer.parseInt(temID));// �ɳ�λ
					markerService.updateMarker(Unavailable, Integer.parseInt(markerId));// �³�λ
					resp.setErrorCode(1);
					resp.setErrorMsg("������λ�ɹ�");
					String json = js.fromObject(resp).toString();
					System.out.println(json);
					pw.write(json);
				} else if (js.has("markerId") && !js.has("temID")){
					// ѡ��λ
					System.out.println("ѡ��λ");
					String markerId = js.getString("markerId");
					ApiResponseObject resp = new ApiResponseObject();
					System.out.println("markerId" + markerId);
					System.out.println("���³���id" + markerId);
					markerService.updateMarker(Unavailable, Integer.parseInt(markerId));
					resp.setErrorCode(1);
					resp.setErrorMsg("ѡ��λ�ɹ�");
					String json = js.fromObject(resp).toString();
					pw.write(json);
					System.out.println(json);
				}
			}else {
				ApiResponseObject resp = new ApiResponseObject();
				resp.setErrorCode(-1);
				resp.setErrorMsg("sessionkey�Թ�ʱ");
				String json = js.fromObject(resp).toString();
				pw.write(json);
				System.out.println(json);
			}
		}else {
			ApiResponseObject resp = new ApiResponseObject();
			resp.setErrorCode(-1);
			resp.setErrorMsg("��sessionkey");
			String json = js.fromObject(resp).toString();
			pw.write(json);
			System.out.println(json);
		}
	}

}
