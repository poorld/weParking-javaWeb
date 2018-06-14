package com.teenyda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.teenyda.bean.UserEntity;
import com.teenyda.util.DBUtil;

public class UserEntityDaoImpl implements UserEntityDao{

	@Override
	public boolean isRegister(String openId) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "select * from user where openid = ?";
		PreparedStatement state = null;
		ResultSet rs = null;
		try {
			state = conn.prepareStatement(sql);
			state.setString(1, openId);
			rs = state.executeQuery();
			if (rs.next()) {
				System.out.println("ÒÑ×¢²á");
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, state, conn);
		}
		return false;
	}

	@Override
	public UserEntity addUser(UserEntity entity) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "insert into user(nickname,province,city,openId,avatarUrl,time) values(?,?,?,?,?,?)";
		PreparedStatement state = null;
		try {
			state = conn.prepareStatement(sql);
			state.setString(1, entity.getNickName());
			state.setString(2, entity.getProvince());
			state.setString(3, entity.getCity());
			state.setString(4, entity.getOpenid());
			state.setString(5, entity.getAvatarurl());
			state.setTimestamp(6, Timestamp.valueOf(entity.getTime()));
			state.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<UserEntity> getAllUser() {
		// TODO Auto-generated method stub
		return null;
	}

}
