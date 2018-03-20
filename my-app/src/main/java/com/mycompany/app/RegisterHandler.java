package com.mycompany.app;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.mycompany.app.Connect;

public class RegisterHandler {
	
	static Connection conn = Connect.getConn();
	static Statement stmt;
	Connection conn_2 = Connect.getConn();
	Statement stmt_2;
	int number;
	
	public int getLastPrimaryKey() {
		try {
			stmt_2 = conn_2.createStatement();
			String sql = "SELECT * FROM Apparat ORDER BY ApparatID DESC LIMIT 1";
			ResultSet rs;
			rs = stmt_2.executeQuery(sql);
			rs.next();
			number = rs.getInt("ApparatID");
			return number;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void registerApparat(int apparatID, String navn, String beskrivelse) {
		try {
			stmt = conn.createStatement();
			String sql = String.format("INSERT INTO `Apparat`(`ApparatID`, `Navn`, `Beskrivelse`) VALUES ('%s','%s','%s')", apparatID, navn, beskrivelse);
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void registerApparatOvelse(String ovelsenavn, double antallKilo, int antallSett) {
		try {
			stmt = conn.createStatement();
			String sql = String.format("INSERT INTO `ApparatOvelse`(`Ovelsenavn`, `Antall_Sett`, `Antall_Kilo`) VALUES ('%s','%s','%s')", ovelsenavn, antallKilo, antallSett);
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void registerFriOvelse(String ovelsenavn, String beskrivelse) {
		try {
			stmt = conn.createStatement();
			String sql = String.format("INSERT INTO `Frivektsovelse`(`Ovelsenavn`, `Beskrivelse`) VALUES ('%s','%s')", ovelsenavn, beskrivelse);
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void main(String[] args) {
		RegisterHandler handler = new RegisterHandler();
		System.out.println(handler.getLastPrimaryKey());
		
	}

}
