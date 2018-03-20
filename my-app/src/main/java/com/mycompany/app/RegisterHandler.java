package com.mycompany.app;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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

	//Legge til en treningsøkt
	public static void registerTreningsokt(int oktid, Date dato, int varighet, int form, int prestasjon, ArrayList<String> ovelser, String notat) {
		try {
			stmt = conn.createStatement();
			String sql = String.format("INSERT INTO `Treningsokt`(`OktID`, `Dato`, `Varighet`, `Form`, `Prestasjon`) VALUES ('%s', '%s', '%s', '%s', '%s' )", oktid, dato, varighet, form, prestasjon);
			String sql_notat = String.format("INSERT INTO `NotatForOkt`(`OktID`, `Beskrivelse`) VALUES ('%s', '%s')", oktid, notat);
			stmt.executeUpdate(sql);
			stmt.executeUpdate(sql_notat);
			for(int x=0; x < ovelser.size(); x++) {
				String sql1 = String.format("INSERT INTO `OvelseForOkt`(`Ovelsenavn`, `OktID`) VALUES ('%s', '%s')", ovelser.get(x), oktid);
				stmt.executeUpdate(sql1);
				}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Printer ut info om alle treningsøkter med OktID opp til og med n. Starter med OktID nr n og går nedover.
	public static void infoOmSisteOkter(int n) {
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM Treningsokt order by OktID DESC";
			Statement stmt2 = conn.createStatement();
			
			ResultSet result = stmt.executeQuery(sql);
			while(result.next()) {
				int oktID = result.getInt("OktID");
				Date dato = result.getDate("Dato");
				Time tidspunkt = result.getTime("Dato");
				int varighet = result.getInt("Varighet");
				String form = result.getString("Form");
				String prestasjon = result.getString("Prestasjon");
				
				String sql_notater = String.format("SELECT `Beskrivelse` FROM `NotatForOkt` WHERE `OktID`='%s'", oktID);
				
				ResultSet result_notat = stmt2.executeQuery(sql_notater);
				while(result_notat.next()) {
					//System.out.println(result_notat);
					String notat = result_notat.getString("Beskrivelse");
					System.out.println("Okt nr " + String.valueOf(oktID) + ": " + '\n' + "Dato: " + '\n' + String.valueOf(dato) + 
						"\nTidspunkt: " + String.valueOf(tidspunkt) + '\n' + "Varighet: " + String.valueOf(varighet) + '\n' + 
						"Form: " + form + '\n' + "Prestasjon: " + prestasjon + '\n' + 
						"Notat: " + notat + '\n' +  "----------------------------" + '\n' + '\n'
						);
				}
				
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//java.sql.Date currentDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		//registerTreningsokt(1, currentDate, 50, "Grei form", "Greit prestert");
		//registerTreningsokt(2, currentDate, 70, "Sykt bra form", "Sykt bra prestert");*/ 
		//ArrayList<String> testListe = new ArrayList<String>();
		//testListe.add("TestØvels1");
		//registerTreningsokt(1, currentDate, 40, 10, 8, testListe, "Kort og god økt.");
		infoOmSisteOkter(5);
		
	}

}
