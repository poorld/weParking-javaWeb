package com.teenyda.servlet.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teenyda.bean.History;
import com.teenyda.common.ApiResponseObject;
import com.teenyda.service.HistoryServiceImpl;
import com.teenyda.util.GetUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import redis.clients.jedis.Jedis;

/**
 * 历史账单
 * Servlet implementation class Record
 */
@WebServlet("/record")
public class Record extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Jedis jedis = new Jedis();
    private HistoryServiceImpl hiService = new HistoryServiceImpl();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Record() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("record");
		response.setContentType("text/json;charset=utf-8");
		PrintWriter pw = response.getWriter();
		
		ServletInputStream is = request.getInputStream();
		JSONObject js = GetUserInfo.inputStreamToJson(is);
		if (js.has("sessionKey")) {
			ApiResponseObject resp = new ApiResponseObject();
			String sessionKey = js.getString("sessionKey");
			String session = jedis.get(sessionKey);
			String openid = session.split("哒")[1];
			System.out.println("sessionKey="+sessionKey);
			System.out.println("openid="+openid);
			List<History> records = hiService.getRecords(openid);
			System.out.println(records);
			JsonConfig config = new JsonConfig();
			config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
			//list转json
			JSONArray json = JSONArray.fromObject(records, config);
			resp.setErrorCode(1);
			resp.setData(json);
			pw.write(js.fromObject(resp).toString());
		} else {
			pw.write("waiting");
		}
	}

}
