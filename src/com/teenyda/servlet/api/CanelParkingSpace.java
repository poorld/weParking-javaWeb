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

import net.sf.json.JSONObject;

/**
 * Servlet implementation class CanelParkingSpace
 */
@WebServlet("/canelParkingSpace")
public class CanelParkingSpace extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int Available = 1;//可以使用
	private static final int Unavailable = 2;//不可使用
	private MarkersServiceImap markerService = new MarkersServiceImap();   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CanelParkingSpace() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/json;charset=utf-8");
		PrintWriter pw = response.getWriter();
		JSONObject js = new JSONObject();
		ServletInputStream is = request.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
		StringBuilder sb = new StringBuilder();
		String line = "";
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		JSONObject fromObject = js.fromObject(sb.toString());
		ApiResponseObject resp = new ApiResponseObject();
		if (fromObject.has("temID")) {
			String lookid = fromObject.getString("temID");
			markerService.updateMarker(Available, Integer.parseInt(lookid));
			resp.setErrorCode(1);
			resp.setErrorMsg("已取消车位");
			pw.write(js.fromObject(resp).toString());
		}
	}

}
