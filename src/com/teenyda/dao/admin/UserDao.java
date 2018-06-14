package com.teenyda.dao.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.teenyda.bean.UserEntity;
import com.teenyda.util.DBUtil;

public class UserDao {
	public List<UserEntity> getUser() {
		List<UserEntity> users = new ArrayList<UserEntity>();
		Connection conn = DBUtil.getConnection();
		String sql = "select * from user";
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			prep = conn.prepareStatement(sql);
			rs = prep.executeQuery();
			while( rs.next()) {
				UserEntity user = new UserEntity();
				user.setAvatarurl(rs.getString("avatarurl"));
				user.setNickName(rs.getString("nickname"));
				user.setProvince(rs.getString("province"));
				user.setCity(rs.getString("city"));
				users.add(user);
			}
			return users;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, prep, conn);
		}
		return null;
	}
}
