package com.teenyda.dao;
/**
 * 车位锁信息表修改
 * @author Administrator
 *
 */
public interface LookDao {
	/**
	 * 返回使用状态
	 */
	int updateState(int lookId,int state);
	/**
	 * 查询使用状态
	 * 1-空位 2-已使用 3-维护
	 * @param lookId
	 * @return
	 */
	int isInUse(int lookId);
}
