package com.mycompany.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.Date;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.util.Calendar;

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
			}
			return tidspunkt;
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
	}
	return null;
}
	
	
	public static ArrayList<String> showResults(String ovelse, Date startDate, Date endDate){
		ArrayList<String> ovelser = new ArrayList<String>();
		ArrayList<Date> datoer = getOvelseTidspunkt();
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM Ovelse";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ovelser.add(rs.getString("Ovelsenavn"));
				System.out.println(ovelser);
				}
			if(ovelser.contains(ovelse)) {
				
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		ArrayList<Date> tidspunkt = getOvelseTidspunkt();
		 Date date1 = tidspunkt.get(0);
		 Date date2 = tidspunkt.get(1);
		 System.out.println(date2);
	}
}