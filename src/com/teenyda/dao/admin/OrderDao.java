package com.teenyda.dao.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.teenyda.bean.Order;
import com.teenyda.bean.UserEntity;
import com.teenyda.util.DBUtil;

public class OrderDao {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public List<Order> getOrders(){
		List<Order> orders = new ArrayList<Order>();
		Connection conn = DBUtil.getConnection();
		String sql = "select * from order_info ORDER BY time desc";
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			prep = conn.prepareStatement(sql);
			rs = prep.executeQuery();
			while(rs.next()) {
				Order order = new Order();
				order.setOrderId(rs.getString("orderid"));
				order.setSpend(rs.getString("spend"));
				order.setTime(sdf.format(rs.getTimestamp("time")));
				orders.add(order);
			}
			return orders;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
