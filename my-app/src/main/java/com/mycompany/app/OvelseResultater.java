package com.mycompany.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.sql.ResultSet;


import com.mycompany.app.Connect;

public class OvelseResultater {

	static Connection conn = Connect.getConn();
	static Statement stmt;
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	
	
	public static ArrayList<String> showResults(String ovelse, Date startDate, Date endDate){
		ArrayList<String> ovelser = new ArrayList<String>();
		ArrayList<Date> dates = new ArrayList<Date>();
		Set<Integer> oktID = new TreeSet<Integer>();
		ArrayList<Integer> form = new ArrayList<Integer>();
		ArrayList<Integer> prestasjon = new ArrayList<Integer>();
		ArrayList<Integer> varighet = new ArrayList<Integer>();
		ArrayList<Integer> vekt = new ArrayList<Integer>();
		ArrayList<Integer> sett = new ArrayList<Integer>();
		
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM ApparatOvelse";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {	
				ovelser.add(rs.getString("Ovelsenavn"));
				}
			if(ovelser.contains(ovelse)) {
			System.out.println(ovelse);
			}
			}catch (SQLException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}	
	
		try {
			stmt = conn.createStatement();
			String sql2 = "SELECT * FROM Treningsokt";
			ResultSet rs2 = stmt.executeQuery(sql2);
			ArrayList<Date> validDates = new ArrayList<Date>();
			while(rs2.next()) {
				dates.add(rs2.getDate("Dato"));			
				
				}
			for(Date date: dates) {
				if((date.after(startDate) && date.before(endDate))||date.equals(startDate)||date.equals(endDate)) {
					validDates.add(date);
				}
			}
			for(int i = 0; i < validDates.size(); i++) {
				Date trainingDate = validDates.get(i);
				String sql3 = String.format("SELECT OktID FROM Treningsokt WHERE Dato = '%s'", trainingDate);
				ResultSet rs3 = stmt.executeQuery(sql3);
				while(rs3.next()) {
					int id = rs3.getInt("OktID");
					oktID.add(id);
				}
			}
			if(ovelser.contains(ovelse)) {
				for(int i: oktID) {
				String sql5 = String.format("SELECT * FROM ApparatOvelseForOkt WHERE OktID = '%s' AND Ovelsenavn = '%s'",i, ovelse);
				ResultSet rs5 = stmt.executeQuery(sql5);
				while(rs5.next()) {
					vekt.add(rs5.getInt("Vekt"));
					sett.add(rs5.getInt("Sett"));
				}
				}
				for(int j = 0; j< vekt.size(); j++) {
					System.out.println("Du trente med " + vekt.get(j) + " kg og hadde " + sett.get(j) + " sett.");
			}
			}
			else {
				System.out.println("Ovelsen eksisterer ikke");
			}
				
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
		
		
	}
	@Override
	public String toString() {
        return dateFormat.format(this);
    }
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Skriv inn ovelse:");
		String ovelse = scanner.next();
		System.out.println("Enter the Date : (format: yyyy/mm/dd )");
		String startDate = scanner.next();
		System.out.println("Enter the Date : (format: yyyy/mm/dd )");
		String endDate = scanner.next();
		scanner.close();
		Date date2 = null;
		Date date3 = null;
		try {
		    //Parsing the String
		    date2 = dateFormat.parse(startDate);
		    date3 = dateFormat.parse(endDate);
		} catch (ParseException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		//System.out.println(date2);
		showResults(ovelse,date2,date3);
		 
	}
}