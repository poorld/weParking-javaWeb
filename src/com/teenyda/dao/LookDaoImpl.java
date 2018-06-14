package com.teenyda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.teenyda.util.DBUtil;

public class LookDaoImpl implements LookDao{

	@Override
	public int updateState(int lookId, int state) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "update look set state = ? where lookid = ?";
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement(sql);
			prep.setInt(1, state);
			prep.setInt(2, lookId);
			int execute = prep.executeUpdate();
			if (execute == 0){
				return -1;
			}
			return state;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, prep, conn);
		}
		return state;
		
		
	}

	@Override
	public int isInUse(int lookId) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "select * from look where lookid = ?";
		PreparedStatement prep = null;
		ResultSet rs = null;
		int state = 2;
		try {
			prep = conn.prepareStatement(sql);
			prep.setInt(1, lookId);
			rs = prep.executeQuery();
			if (rs.next()){
				state = rs.getInt("state");
			}
			return state;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, prep, conn);
		}
		return -1;
	}

}
