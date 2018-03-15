package com.mycompany.app;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connect {

	public static Connection getConn() {
		try {	
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://mysql.stud.ntnu.no/eriksla_databaseprosjekt112?useSSL=false";
			String user = "eriksla_1";
			String pw = "databasemedsvenn";
			
			//connection to database
			Connection conn = DriverManager.getConnection(url, user, pw);
			return conn;
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		Connection conn = Connect.getConn();
	}
	
}
