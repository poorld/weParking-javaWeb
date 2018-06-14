package com.teenyda.dao;

import java.util.List;

import com.teenyda.bean.UserEntity;

public interface UserEntityDao {
	//是否注册
	public boolean isRegister(String openId);
	//添加用户
	public UserEntity addUser(UserEntity entity);
	public List<UserEntity> getAllUser();
}
