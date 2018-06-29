package com.teenyda.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teenyda.service.admin.StateServiceImpl;

/**
 * ��̨һ���޸�
 * һ������
 * һ��������
 * һ������
 * Servlet implementation class ModifyState
 */
@WebServlet("/modifyState")
public class ModifyState extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//available һ������
	private static final String available = "available";
	
	//disabled һ��������
	private static final String disabled = "disabled";
	
	//offline һ������
	private static final String offline = "offline";
	
	
	
	private StateServiceImpl stateService = new StateServiceImpl();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModifyState() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		String action = request.getParameter("action");
		if (action.equals(available)) {
			stateService.allAvailable();
		}else if (action.equals(disabled)) {
			stateService.allNnavailable();
		}else if (action.equals(offline)) {
			stateService.allOffline();
		}
	}

}
