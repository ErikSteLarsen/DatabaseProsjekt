package com.mycompany.app;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
			String sql1 = String.format("INSERT INTO `Ovelse`(`Ovelsenavn`) VALUES ('%s')", Ovelsenavn);
			String sql = String.format("INSERT INTO `FrivektsOvelse`(`Ovelsenavn`, `Tekstlig_beskrivelse`) VALUES ('%s','%s')", Ovelsenavn, beskrivelse);
			stmt.executeUpdate(sql1);
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
			String sql = String.format("SELECT * FROM Treningsokt WHERE OktID<= '%s' order by OktID DESC", n);
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
	
	
	
	//Printer ut info om alle treningsøkter med OktID opp til og med n. Starter med OktID nr n og går nedover.
	public static void getApparatOvelseOkter(int oktid, String apparatOvelse) {
		try {
			stmt = conn.createStatement();
			String sql = String.format("SELECT * FROM ApparatOvelseForOkt WHERE Ovelsenavn ='%s' AND (OktID='%s' or OktID<'%s') order by OktID ASC", apparatOvelse, oktid, oktid);
			//Statement stmt2 = conn.createStatement();
			String prevOvelsenavn = null;
			int prevOktID = 0;
			int prevVekt = 0;
			int prevSett = 0;
			
			String ovelsenavn = null;
			int oktID;
			int vekt = 0;
			int sett = 0;
			
			
			ResultSet result = stmt.executeQuery(sql);
			while(result.next()) {
				if(result.getInt("OktID") ==oktid && result.getRow()==0) {
					System.out.println("Dette var den første treningsøkten");
					break;
				}
				System.out.println("Ovelsenavn, loop: " + result.getString("Ovelsenavn"));
				if(result.getInt("OktID")==oktid) {
					ovelsenavn = result.getString("Ovelsenavn");
					oktID = result.getInt("OktID");
					vekt = result.getInt("Vekt");
					sett = result.getInt("Sett");
					break;
				}					
				else {
					prevOvelsenavn = result.getString("Ovelsenavn");
					prevOktID = result.getInt("OktID");
					prevVekt = result.getInt("Vekt");
					prevSett = result.getInt("Sett");
				}
			}
			//Gir vektfremgangen i prosent
			double vektFremgang = ((((double)vekt/(double)prevVekt)-1)*100); 
			//Gir settfremgangen i prosent
			double settFremgang = ((((double)sett/(double)prevSett)-1)*100);
			
			System.out.println("Ovelse: " + ovelsenavn + "\nOktID: " + oktid + "\nVekt: " + vekt + "\nSett: " + sett);	
			System.out.println("Forrige ovelse: " + prevOvelsenavn + "\nForrige OktID: " + prevOktID + "\nForrige vekt: " + prevVekt + "\nForrige sett: " + prevSett);
			System.out.println("Fremgang, antall kilo: " + vektFremgang + "%" + "\nFremgang, antall sett: " + settFremgang +"%");
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
		//registerFriOvelse("TestØvels1", "Kul øvelse");
		ArrayList<String> testListe = new ArrayList<String>();
		testListe.add("TestØvels1");
		//registerTreningsokt(1, currentDate, 40, 10, 8, testListe, "Kort og god økt.");
		//infoOmSisteOkter(5);
		ArrayList<Integer> vektSettTest = new ArrayList<Integer>();
		vektSettTest.add(55);
		vektSettTest.add(5);
		
		ArrayList<Integer> vektSettTest2 = new ArrayList<Integer>();
		vektSettTest2.add(77);
		vektSettTest2.add(7);
		//registerApparatOvelse("Deadlift");
		Map<String,ArrayList<Integer>> testListe2 = new HashMap<String, ArrayList<Integer>>();
		testListe2.put("Benkpress", vektSettTest);
		testListe2.put("Deadlift", vektSettTest2);
		registerTreningsokt(14, currentDate, 111, 4, 4, testListe, testListe2, "Funger da!!");
		getApparatOvelseOkter(14, "Benkpress");
	}

}
