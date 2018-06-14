package com.teenyda.dao;

import java.util.List;

import com.teenyda.bean.History;

public interface HistoryDao {
	List<History> getRecords(String openid);
}
