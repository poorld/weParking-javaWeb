package com.teenyda.dao.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.teenyda.util.DBUtil;

public class LookDaoImpl implements LookDao{
	@Override
	public void available() {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "update look set state = 1";
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement(sql);
			System.out.println(prep.executeUpdate()>0?"ÐÞ¸Ä×´Ì¬Îªavailable":"ÐÞ¸Ä×´Ì¬ÎªavailableÊ§°Ü");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, prep, conn);
		}
	}

	@Override
	public void disabled() {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "update look set state = 2";
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement(sql);
			System.out.println(prep.executeUpdate()>0?"ÐÞ¸Ä×´Ì¬Îªdisabled":"ÐÞ¸Ä×´Ì¬ÎªdisabledÊ§°Ü");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, prep, conn);
		}
	}

	@Override
	public void offline() {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "update look set state = 3";
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement(sql);
			prep.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, prep, conn);
		}
	}

	@Override
	public void offlineByLookId(int lookid) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "update look set state = 3 where lookid=?";
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

	@Override
	public void onlineByLookId(int lookid,int state) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "update look set state = ? where lookid=?";
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement(sql);
			prep.setInt(1, state);
			prep.setInt(2, lookid);
			System.out.println(prep.executeUpdate()>0?"ÐÞ¸Ä×´Ì¬³É¹¦":"ÐÞ¸Ä×´Ì¬Ê§°Ü");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, prep, conn);
		}
	}

	@Override
	public void availableByLookId(int lookid) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "update look set state = 1 where lookid = ?";
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement(sql);
			prep.setInt(1, lookid);
			System.out.println(prep.executeUpdate()>0?"ÐÞ¸Ä×´Ì¬Îªavailable":"ÐÞ¸Ä×´Ì¬ÎªavailableÊ§°Ü");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, prep, conn);
		}
	}

	@Override
	public void disabledByLookId(int lookid) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "update look set state = 2 where lookid = ?";
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement(sql);
			prep.setInt(1, lookid);
			System.out.println(prep.executeUpdate()>0?"ÐÞ¸Ä×´Ì¬Îªavailable":"ÐÞ¸Ä×´Ì¬ÎªavailableÊ§°Ü");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, prep, conn);
		}
	}

}
