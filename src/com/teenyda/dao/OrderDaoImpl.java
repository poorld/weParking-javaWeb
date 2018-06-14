package com.teenyda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.teenyda.bean.Order;
import com.teenyda.util.DBUtil;

public class OrderDaoImpl implements OrderDao{

	@Override
	public void GeneratingOrder(Order order) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		PreparedStatement prep = null;
		String sql = "insert into order_info(orderid,openid,lookid,orderstate,starttime,time) values (?,?,?,?,?,?)";
		try {
			prep = conn.prepareStatement(sql);
			prep.setString(1, order.getOrderId());
			prep.setString(2, order.getOpenId());
			prep.setString(3, order.getLookId());
			prep.setInt(4, order.getOrderState());
			prep.setTimestamp(5, Timestamp.valueOf(order.getStartTime()));
			prep.setTimestamp(6, Timestamp.valueOf(order.getTime()));
			prep.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, prep, conn);
		}
		
	}

	@Override
	public void updateOrder(Order order) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		PreparedStatement prep = null;
		String sql = "update order_info set orderstate = ? , endtime = ? , spend = ? where orderid = ? ";
		try {
			prep = conn.prepareStatement(sql);
			prep.setInt(1, order.getOrderState());
			prep.setTimestamp(2, Timestamp.valueOf(order.getEndTime()));
			prep.setString(3, order.getSpend());
			prep.setString(4, order.getOrderId());
			prep.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, prep, conn);
		}
	}

}
