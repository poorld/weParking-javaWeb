package com.teenyda.service;

import com.teenyda.bean.UserEntity;

public interface UserEntityService {
	public boolean isRegister(String openId);
	
	public void register(UserEntity user);
	
}
