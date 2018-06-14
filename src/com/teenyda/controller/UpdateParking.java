package com.teenyda.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teenyda.bean.Look;
import com.teenyda.service.admin.ParkingService;
import com.teenyda.util.GetUserInfo;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class UpdateParking
 */
@WebServlet("/updateParking")
public class UpdateParking extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ParkingService service = new ParkingService();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateParking() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Look look = new Look();
		ServletInputStream is = request.getInputStream();
		JSONObject js = GetUserInfo.inputStreamToJson(is);
		String id = js.getString("id");
		String state = js.getString("state");
		String longitude = js.getString("longitude");
		String latitude = js.getString("latitude");
		String address = js.getString("address");
		look.setLookid(Integer.parseInt(id));
		if (state.equals("true")) {
			look.setState(1);
		}else {
			look.setState(2);
		}
		look.setLongitude(longitude);
		look.setLatitude(latitude);
		look.setAddress(address);
		System.out.println(look);
		boolean result = service.updateParking(look);

	}

}
