package com.teenyda.service;

import com.teenyda.bean.Order;

public interface OrderService {
	String addOrder(Order order);
	void updateOrder(Order order);
}
