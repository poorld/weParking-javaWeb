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
import com.teenyda.common.ApiResponseObject;
import com.teenyda.service.MarkersServiceImap;
import com.teenyda.service.OrderServiceImpl;
import com.teenyda.util.GetUserInfo;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

/**
 * ����ͣ���ӿ�
 * ΢�Ŷ�����������ӿ�ʱ��Ҫ���Ự�Ƿ���ڣ����������»�ȡ3rdsession
 * ���������
 * 		orderId ������id��
 * 		sessionkey	
 * 		spend	���ѽ��
 * 		lookid	��λ��id
 * Servlet implementation class EndParking
 */
@WebServlet("/endParking")
public class EndParking extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OrderServiceImpl orderService = new OrderServiceImpl();
	private MarkersServiceImap markerService = new MarkersServiceImap();
    private Jedis jedis = new Jedis();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EndParking() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * ����ͣ������Ҫ���ղ��� :
	 * orderId(����id)��sessionKey(�Զ����sessionkey),staytime(ͣ��ʱ��),spend(���ѽ��)
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("����ͣ��");
		response.setContentType("text/json;charset=utf-8");
		// TODO Auto-generated method stub
		PrintWriter pw = response.getWriter();
		JSONObject js = new JSONObject();
		ServletInputStream is = request.getInputStream();
		Map<String, String> endParkingInfo = GetUserInfo.getEndParkingInfo(is);
		if (endParkingInfo != null) {
			ApiResponseObject resp = new ApiResponseObject();
			Order order = new Order();
			String orderId = endParkingInfo.get("orderId");
			String spend = endParkingInfo.get("spend");
			String lookid = endParkingInfo.get("lookid");
			//��ӻ���
			String key = "lookid="+(Integer.parseInt(lookid)+1);
			jedis.set(key, "0");//����
			System.out.println("key="+key);
			System.out.println("����:"+jedis.get(key));
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (jedis.exists(key)) {
				System.out.println("����ʧ��");
				resp.setErrorCode(-1);
				resp.setErrorMsg("����ʧ��");
			} else {
				resp.setErrorCode(1);
				resp.setErrorMsg("�����ɹ�");
			}
			System.out.println("orderId"+orderId);
			System.out.println("spend"+spend);
			System.out.println("lookid"+lookid);

			order.setOrderId(orderId);
			order.setSpend(spend);
			//�޸Ķ���
			orderService.updateOrder(order);
			//�ĳ���״̬
			markerService.updateMarker(1, Integer.parseInt(lookid));
			pw.write(js.fromObject(resp).toString());
		}
		
	}

}
