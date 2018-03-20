package com.mycompany.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Date;
import java.util.ArrayList;
import java.sql.ResultSet;

import com.mycompany.app.Connect;

public class OvelseResultater {

	static Connection conn = Connect.getConn();
	static Statement stmt;
	
	
	public static ArrayList<Date> getOvelseTidspunkt() {
		ArrayList<Date> tidspunkt = new ArrayList<Date>();
		try {
			stmt = conn.createStatement();
			String sql = "SELECT Dato FROM Treningsokt";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				tidspunkt.add(rs.getDate("Dato"));
				//System.out.println(rs.getDate("Dato"));
			}
			return tidspunkt;
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
	}
	return null;
}
	public static void main(String[] args) {
		//ArrayList<Date> tidspunkt = new ArrayList<Date>();
		System.out.println(getOvelseTidspunkt());
	}
}