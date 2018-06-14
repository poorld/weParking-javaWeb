package com.teenyda.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teenyda.bean.Look;
import com.teenyda.bean.Order;
import com.teenyda.bean.UserEntity;
import com.teenyda.dao.admin.OrderDao;
import com.teenyda.dao.admin.UserDao;
import com.teenyda.service.admin.ParkingService;

/**
 * Servlet implementation class ParkingController
 */
@WebServlet("/admin")
public class ParkingController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ParkingService service = new ParkingService();   
    private UserDao userDao = new UserDao();
    private OrderDao orderDao = new OrderDao();
 

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		PrintWriter pw = response.getWriter();
		List<Look> parkings = service.getParkings();
		List<UserEntity> users = userDao.getUser();
		List<Order> orders = orderDao.getOrders();
		System.out.println(parkings);
		request.setAttribute("parkings", parkings);
		request.setAttribute("users", users);
		/*request.setAttribute("orders", orders);*/
		request.getRequestDispatcher("/admin.jsp").forward(request,response); 
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
