package com.teenyda.util;

import java.util.UUID;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UUID uuid = UUID.randomUUID();
		String rd_session = uuid.toString();
		System.out.println(rd_session);
		//2c189777-8dac-4865-9b0e-648a9554cc34
		System.out.println(rd_session.length());
	}

}
