package com.teenyda.dao;

import java.util.List;

import com.teenyda.bean.UserEntity;

public interface UserEntityDao {
	//�Ƿ�ע��
	public boolean isRegister(String openId);
	//����û�
	public UserEntity addUser(UserEntity entity);
	public List<UserEntity> getAllUser();
}
