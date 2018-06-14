package com.teenyda.servlet.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teenyda.bean.Order;
import com.teenyda.bean.OrderId;
import com.teenyda.common.ApiResponseObject;
import com.teenyda.service.OrderServiceImpl;
import com.teenyda.util.GetUserInfo;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

/**
 * ����ͣ���ӿ� ������ lookid ������id�� sessionKey
 * 
 * Servlet implementation class Parking
 */
@WebServlet("/parking")
public class Parking extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OrderServiceImpl orderService = new OrderServiceImpl();
	private Jedis jedis = new Jedis();

	public Parking() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response) ��Ҫ�Ĳ���lookid��������ţ�, sessionKey(�Զ����sessionkey)
	 *      js_code(������ȡ�û�openid) ��Ҫ���ɵĲ����� orderid������id��
	 *      ,starttime(��ʼʱ��)��time������ʱ�䣩
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/json;charset=utf-8");
		PrintWriter pw = response.getWriter();
		JSONObject js = new JSONObject();
		ServletInputStream is = request.getInputStream();
		Map<String, String> parkingInfo = GetUserInfo.getParkingInfo(is);
		String sessionkey = parkingInfo.get("sessionkey");
		String lookid = parkingInfo.get("lookid");
		System.out.println("sessionkey" + sessionkey);
		System.out.println("lookid" + lookid);
		String rdSession = jedis.get(sessionkey);
		// session��Ч
		if (jedis.exists(sessionkey)) {
			// ��ӻ���
			String key = "lookid=" + (Integer.parseInt(lookid) + 1);
			jedis.set(key, "1");// ����
			System.out.println("key=" + key);
			System.out.println("����:" + jedis.get(key));
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (jedis.exists(key)) {
				ApiResponseObject resp = new ApiResponseObject();
				resp.setErrorCode(-1);
				resp.setErrorMsg("����ʧ��");
				pw.write(js.fromObject(resp).toString());
				System.out.println("����ʧ��");
			} else {
				ApiResponseObject resp = new ApiResponseObject();
				// ��ȡopenid
				String openid = rdSession.split("��")[1];
				System.out.println("openid=" + openid);
				Order order = new Order();
				order.setOpenId(openid);
				order.setLookId(lookid);
				String orderId = orderService.addOrder(order);
				System.out.println("����id" + orderId);
				OrderId oid = new OrderId();
				oid.setOrderid(orderId);
				resp.setErrorCode(1);
				resp.setErrorMsg("�����ɹ������ɶ���");
				resp.setData(oid);
				String json = js.fromObject(resp).toString();
				System.out.println(json);
				pw.write(json);
				// ���ض���id
				// orderId2018052409053680400
			}
		} else {
			// session��Ч
			ApiResponseObject resp = new ApiResponseObject();
			resp.setErrorCode(-1);
			resp.setErrorMsg("sessionKey��Ч");
			pw.write(js.fromObject(resp).toString());
		}
	}

}
