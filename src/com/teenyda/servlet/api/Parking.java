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
 * 立刻停车接口 参数： lookid （车锁id） sessionKey
 * 
 * Servlet implementation class Parking
 */
@WebServlet("/parking")
public class Parking extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OrderServiceImpl orderService = new OrderServiceImpl();
	private Jedis jedis = new Jedis();
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
	 *      response) 需要的参数lookid（车锁编号）, sessionKey(自定义的sessionkey)
	 *      js_code(用来获取用户openid) 需要生成的参数： orderid（订单id）
	 *      
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
		// session有效
		if (jedis.exists(sessionkey)) {
			// 添加缓存
			String key = "lookNumber=" + (Integer.parseInt(lookid) + 1);
			jedis.set(key, down_unlocking);// 开锁
			System.out.println("key=" + key);
			System.out.println("命令:" + jedis.get(key));
			while(true){
				if (jedis.exists(key)){
					String result = jedis.get(key);
					if (result.equals("true") || result.equals("false")){
						break;
					}
				}
			}
			if (jedis.get(key).equals("false")) {
				ApiResponseObject resp = new ApiResponseObject();
				resp.setErrorCode(-1);
				resp.setErrorMsg("开锁失败");
				pw.write(js.fromObject(resp).toString());
				System.out.println("开锁失败");
			} else {
				ApiResponseObject resp = new ApiResponseObject();
				// 获取openid
				String openid = rdSession.split("哒")[1];
				System.out.println("openid=" + openid);
				Order order = new Order();
				order.setOpenId(openid);
				order.setLookId(lookid);
				String orderId = orderService.addOrder(order);
				System.out.println("订单id" + orderId);
				OrderId oid = new OrderId();
				oid.setOrderid(orderId);
				
				resp.setErrorCode(1);
				resp.setErrorMsg("开锁成功，生成订单");
				resp.setData(oid);
				
				String json = js.fromObject(resp).toString();
				System.out.println(json);
				pw.write(json);
				// 返回订单id
				// orderId2018052409053680400
			}
		} else {
			// session无效
			ApiResponseObject resp = new ApiResponseObject();
			resp.setErrorCode(-1);
			resp.setErrorMsg("sessionKey无效");
			pw.write(js.fromObject(resp).toString());
		}
		
		jedis.del("lookNumber=1");
	}

}
