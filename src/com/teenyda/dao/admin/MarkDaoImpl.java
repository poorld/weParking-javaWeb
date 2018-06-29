package com.teenyda.dao.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.teenyda.util.DBUtil;

public class MarkDaoImpl implements MarkDao{
	private static final int available = 1;
	private static final String img_available = "/images/idle.png";
	
	private static final int disabled = 2;
	private static final String img_disabled = "/images/not.png";

	@Override
	public void available() {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "update markers set iconPath = '"+img_available+"'";
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement(sql);
			prep.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(null, prep, conn);
		}
	}

	@Override
	public void disabled() {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "update markers set iconPath = '"+img_disabled+"'";
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement(sql);
			prep.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(null, prep, conn);
		}
	}

	@Override
	public void online(int lookid, int state) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String iconPath = "";
		if (state == available) {
			iconPath = img_available;
		}
		if (state == disabled) {
			iconPath = img_disabled;
		}
		String sql = "update markers set iconPath = '"+iconPath+"' where id = ?" ;
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement(sql);
			prep.setInt(1, lookid);
			System.out.println(prep.executeUpdate()>0?"修改图标成功":"修改状态失败");
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
		String sql = "update markers set iconPath = '"+img_available+"' where id = ?";
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement(sql);
			prep.setInt(1, lookid);
			System.out.println(prep.executeUpdate()>0?"修改图标成功":"修改状态失败");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(null, prep, conn);
		}
	}

	@Override
	public void disabledleByLookId(int lookid) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "update markers set iconPath = '"+img_disabled+"' where id = ?";
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement(sql);
			prep.setInt(1, lookid);
			System.out.println(prep.executeUpdate()>0?"修改图标成功":"修改状态失败");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(null, prep, conn);
		}
	}
	
	

}
