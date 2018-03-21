package com.mycompany.app;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	

	public static void registerApparatOvelse(String ovelsenavn) {
		try {
			stmt = conn.createStatement();
			String sql1 = String.format("INSERT INTO `Ovelse`(`Ovelsenavn`) VALUES ('%s')", ovelsenavn);
			String sql = String.format("INSERT INTO `ApparatOvelse`(`Ovelsenavn`) VALUES ('%s')", ovelsenavn);
			stmt.executeUpdate(sql1);
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
	public static void registerTreningsokt(int oktid, Date dato, int varighet, int form, int prestasjon, ArrayList<String> friOvelser, Map<String,ArrayList<Integer>> apparatOvelser, String notat) {
		try {
			stmt = conn.createStatement();
			String sql = String.format("INSERT INTO `Treningsokt`(`OktID`, `Dato`, `Varighet`, `Form`, `Prestasjon`) VALUES ('%s', '%s', '%s', '%s', '%s' )", oktid, dato, varighet, form, prestasjon);
			String sql_notat = String.format("INSERT INTO `NotatForOkt`(`OktID`, `Beskrivelse`) VALUES ('%s', '%s')", oktid, notat);
			stmt.executeUpdate(sql);
			stmt.executeUpdate(sql_notat);
			
			Statement stmt2 = conn.createStatement();
			//Går gjennom friøvelsene i input-listen, og legger inn øvelsene i økten
			for(int x=0; x < friOvelser.size(); x++) {
				String sql1 = String.format("INSERT INTO `FriOvelseForOkt`(`Ovelsenavn`, `OktID`) VALUES ('%s', '%s')", friOvelser.get(x), oktid);
				stmt2.executeUpdate(sql1);
				}
			
			Statement stmt3 = conn.createStatement();
			//Går gjennom apparatøvelse-dictionairyen vi får inn, og legger til øvelsene + vekt og sets i økten
			for(int x=0; x < apparatOvelser.size(); x++) {
				System.out.println(apparatOvelser);
				System.out.println("Øvelse: " + apparatOvelser.keySet().toArray()[x]);
				System.out.println("Vekt: " + apparatOvelser.get(apparatOvelser.keySet().toArray()[x]).toArray()[0]);
				System.out.println("Antall sett: " + apparatOvelser.get(apparatOvelser.keySet().toArray()[x]).toArray()[1]);
				String sql2 = String.format("INSERT INTO `ApparatOvelseForOkt`(`Ovelsenavn`, `OktID`, `Vekt`, `Sett`) VALUES ('%s', '%s', '%s', '%s')", apparatOvelser.keySet().toArray()[x], oktid, apparatOvelser.get(apparatOvelser.keySet().toArray()[x]).toArray()[0], apparatOvelser.get(apparatOvelser.keySet().toArray()[x]).toArray()[1]);
				//System.out.println(sql2);
				stmt3.executeUpdate(sql2);
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
		
		java.sql.Date currentDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		//registerTreningsokt(1, currentDate, 50, "Grei form", "Greit prestert");
		//registerTreningsokt(2, currentDate, 70, "Sykt bra form", "Sykt bra prestert");*/ 
		ArrayList<String> testListe = new ArrayList<String>();
		testListe.add("TestØvels1");
		//registerTreningsokt(1, currentDate, 40, 10, 8, testListe, "Kort og god økt.");
		//infoOmSisteOkter(5);
		ArrayList<Integer> vektSettTest = new ArrayList<Integer>();
		vektSettTest.add(50);
		vektSettTest.add(5);
		
		ArrayList<Integer> vektSettTest2 = new ArrayList<Integer>();
		vektSettTest2.add(88);
		vektSettTest2.add(8);
		//registerApparatOvelse("Deadlift");
		Map<String,ArrayList<Integer>> testListe2 = new HashMap<String, ArrayList<Integer>>();
		testListe2.put("Benkpress", vektSettTest);
		testListe2.put("Deadlift", vektSettTest2);
		registerTreningsokt(16, currentDate, 111, 10, 10, testListe, testListe2, "Shit nå funker ting bra");
	}

}
