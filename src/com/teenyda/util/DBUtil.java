package com.teenyda.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.mysql.jdbc.PreparedStatement;

public class DBUtil {
	private static Connection connection=null;
	private static PreparedStatement statement=null;
	private static String url="";
	private static String driver="";
	private static String usename="";
	private static String password="";
	private static Properties properties=null;;
	private static InputStream inputStream=null;
	static
	{
		properties=new Properties();
		inputStream=DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
		try {
			properties.load(inputStream);
			driver=properties.getProperty("jdbc.driverClassName");
			url=properties.getProperty("jdbc.url");
			usename=properties.getProperty("jdbc.usename");
			password=properties.getProperty("jdbc.password");
			inputStream=null;
			Class.forName(driver);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection()
	{
		try {
			connection=DriverManager.getConnection(url,usename,password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
	public static void close()
	{
		try {
			statement.close();
			connection.close();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void close(ResultSet set,Statement statement,Connection connection)
	{
		if(set!=null)
		{
			try {
				set.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(statement!=null)
		{
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(connection!=null)
		{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
}
