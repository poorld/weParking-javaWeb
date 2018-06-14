package com.teenyda.service;

import com.teenyda.bean.UserEntity;
import com.teenyda.dao.UserEntityDaoImpl;

public class UserEntityServiceImpl implements UserEntityService{
	private UserEntityDaoImpl userDao = new UserEntityDaoImpl();
	
	@Override
	public boolean isRegister(String openId) {
		// TODO Auto-generated method stub
		return userDao.isRegister(openId);
	}

	@Override
	public void register(UserEntity user) {
		// TODO Auto-generated method stub
		userDao.addUser(user);
	}

}
