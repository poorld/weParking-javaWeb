package com.teenyda.service;

import java.util.List;

import com.teenyda.bean.History;
import com.teenyda.dao.HistoryDaoImpl;

public class HistoryServiceImpl implements HistoryService{
	private HistoryDaoImpl hisDao = new HistoryDaoImpl();
	@Override
	public List<History> getRecords(String openid) {
		// TODO Auto-generated method stub
		return hisDao.getRecords(openid);
	}

}
