package com.teenyda.servlet.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teenyda.bean.Marker;
import com.teenyda.common.ApiResponseObject;
import com.teenyda.service.MarkersServiceImap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

/**
 * 车位锁接口
 * 每次滑动屏幕都会请求一次，实现车位锁刷新
 * Servlet implementation class Markers
 */
@WebServlet("/markers")
public class Markers extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MarkersServiceImap markerService = new MarkersServiceImap();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Markers() {
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
		System.out.println("gg");
		response.getWriter().append("Served at: ").append(request.getContextPath());
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/json;charset=utf-8");
		PrintWriter pw = response.getWriter();
		ApiResponseObject resp = new ApiResponseObject();
		JSONObject js = new JSONObject();
		List<Marker> markers = markerService.getMarkers();
		if (markers != null) {
			JsonConfig config = new JsonConfig();
			config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
			JSONArray json = JSONArray.fromObject(markers, config);
			//System.out.println(json);
			resp.setErrorCode(1);
			resp.setData(json.toString());
			pw.write(js.fromObject(resp).toString());
		}

	}

}
