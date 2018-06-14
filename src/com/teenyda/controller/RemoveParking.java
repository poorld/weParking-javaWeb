package com.teenyda.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teenyda.service.admin.ParkingService;

/**
 * Servlet implementation class RemoveParking
 */
@WebServlet("/removeParking")
public class RemoveParking extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ParkingService service = new ParkingService();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoveParking() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("ÄãºÃ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String lookid = request.getParameter("lookid");
		System.out.println(lookid);
		service.removeParking(Integer.parseInt(lookid));
	}

}
