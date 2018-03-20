package com.mycompany.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.mycompany.app.Connect;

public class RegisterHandler {
	
	static Connection conn = Connect.getConn();
	static Statement stmt;
	
	
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
	
	public static void registerFriOvelse(String Ovelsenavn, String beskrivelse) {
		try {
			stmt = conn.createStatement();
			String sql = String.format("INSERT INTO `FrivektsOvelse`(`Ovelsenavn`, `Beskrivelse`) VALUES ('%s','%s')", Ovelsenavn, beskrivelse);
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void main(String[] args) {
		System.out.println("Velkommen til Treningsdagboken!");
		System.out.println("Skriv inn navnet på apparatet: ");
		Scanner scanner = new Scanner(System.in);
		String apparat = scanner.nextLine();
		System.out.println("Beskrivelse: ");
		String beskrivelse = scanner.nextLine();
		registerApparat(19, apparat, beskrivelse);
		System.out.println("Du har satt inn: " + apparat + ", med beskrivelse: " + beskrivelse);
		
		
	}

}
