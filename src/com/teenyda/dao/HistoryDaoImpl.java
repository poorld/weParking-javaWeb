package com.teenyda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.teenyda.bean.History;
import com.teenyda.util.DBUtil;

public class HistoryDaoImpl implements HistoryDao{
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public List<History> getRecords(String openid) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		ResultSet rs = null;
		String sql = "select o.spend,m.title,o.starttime from order_info o LEFT JOIN markers m ON o.lookid = m.id where openid = ? GROUP BY time DESC";
		PreparedStatement prep = null;
		List<History> records = new ArrayList<History>();
		try {
			prep = conn.prepareStatement(sql);
			prep.setString(1, openid);
			rs = prep.executeQuery();
			while(rs.next()) {
				History hi = new History();
				String spend = rs.getString("spend");
				String title = rs.getString("title");
				String time = sdf.format(rs.getTimestamp("starttime"));
				hi.setMoney(spend);
				hi.setAddress(title);
				hi.setTime(time);
				records.add(hi);
			}
			return records;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, prep, conn);
		}
		return null;
	}

}
