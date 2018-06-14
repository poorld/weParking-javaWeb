package com.teenyda.dao;

import com.teenyda.bean.Order;

public interface OrderDao {
	void GeneratingOrder(Order order);
	void updateOrder(Order order);
}
