package com.teenyda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.teenyda.bean.Record;
import com.teenyda.util.DBUtil;

public class RecordDaoImpl implements RecordDao{

	@Override
	public void addRecord(Record record) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		PreparedStatement prep = null;
		String sql = "insert into record(openid,orderid) values (?,?)";
		try {
			prep = conn.prepareStatement(sql);
			prep.setString(1, record.getOpenid());
			prep.setString(2, record.getOrderid());
			prep.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, prep, conn);
		}
	}

}
