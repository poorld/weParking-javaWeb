package com.teenyda.service;

import java.util.List;

import com.teenyda.bean.History;

public interface HistoryService {
	List<History> getRecords(String openid);
}
