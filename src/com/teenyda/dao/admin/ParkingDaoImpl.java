package com.teenyda.dao.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.teenyda.bean.Look;
import com.teenyda.util.DBUtil;

public class ParkingDaoImpl implements ParkingDao{

	@Override
	public List<Look> getParkings() {
		// TODO Auto-generated method stub
		List<Look> looks = new ArrayList<Look>();
		Connection conn = DBUtil.getConnection();
		String sql = "select * from look";
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			prep = conn.prepareStatement(sql);
			rs = prep.executeQuery();
			while(rs.next()) {
				Look look = new Look();
				look.setLookid(rs.getInt("lookid"));
				look.setState(rs.getInt("state"));
				look.setLongitude(rs.getString("longitude"));
				look.setLatitude(rs.getString("latitude"));
				look.setAddress(rs.getString("address"));
				looks.add(look);
			}
			return looks;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, prep, conn);
		}
		return null;
	}

	@Override
	public boolean updateParking(Look look) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "update look set state = ? , longitude = ? , latitude = ? , address = ? where lookid = ?";
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement(sql);
			prep.setInt(1, look.getState());
			prep.setString(2, look.getLongitude());
			prep.setString(3, look.getLatitude());
			prep.setString(4, look.getAddress());
			prep.setInt(5, look.getLookid());
			int result = prep.executeUpdate();
			if (result > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, prep, conn);
		}
		return false;
	}

	@Override
	public int addParking(Look look) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "insert into look(state,longitude,latitude,address) values(?,?,?,?)";
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			prep.setInt(1, look.getState());
			prep.setString(2, look.getLongitude());
			prep.setString(3, look.getLatitude());
			prep.setString(4, look.getAddress());
			int result = prep.executeUpdate();
			ResultSet rs = prep.getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1);
			}else {
				return 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, prep, conn);
		}
		return 0;
	}

	@Override
	public void remove(int lookid) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "delete from look where lookid = ?";
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement(sql);
			prep.setInt(1, lookid);
			prep.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, prep, conn);
		}
	}

	


}
