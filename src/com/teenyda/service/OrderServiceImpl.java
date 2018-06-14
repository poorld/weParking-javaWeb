package com.teenyda.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.junit.Test;

import com.teenyda.bean.Order;
import com.teenyda.bean.Record;
import com.teenyda.dao.OrderDaoImpl;
import com.teenyda.dao.RecordDaoImpl;

public class OrderServiceImpl implements OrderService{
	private OrderDaoImpl orderDao = new OrderDaoImpl();
	private RecordDaoImpl recordDao = new RecordDaoImpl();
	
	@Override
	public String addOrder(Order order) {
		// TODO Auto-generated method stub
		// 生成订单id
		String orderid=new SimpleDateFormat("yyyyMMddHHMMss").format(new Date());
		orderid+=Math.round(Math.random()*100000+1);
		System.out.println("生成订单："+orderid);
		String starttime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		order.setOrderId(orderid);//订单id
		order.setOrderState(0);//设置为待支付
		order.setStartTime(starttime);//时间
		order.setTime(starttime);
		//生成订单
		orderDao.GeneratingOrder(order);
		//生成个人停车记录表
		Record record = new Record();
		record.setOpenid(order.getOpenId());
		record.setOrderid(orderid);
		recordDao.addRecord(record);
		return orderid;
	}
	

	@Override
	public void updateOrder(Order order) {
		// TODO Auto-generated method stub
		String endtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		order.setOrderState(1);//支付
		order.setEndTime(endtime);
		orderDao.updateOrder(order);
	}
	
	
	
	@Test
	public void test(){
		/*String date=new SimpleDateFormat("yyyyMMddHHMMSS").format(new java.util.Date());
		String orderId = UUID.randomUUID().toString().replace("-", "");
		System.out.println(orderId);
		System.out.println(Math.round(Math.random()*100000+1) );*/
		String date=new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
		date+=Math.round(Math.random()*100000+1);
		System.out.println(date);
		String starttime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println(starttime);
	}




	

}
