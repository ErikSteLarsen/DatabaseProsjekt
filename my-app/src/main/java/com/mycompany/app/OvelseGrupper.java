package com.mycompany.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OvelseGrupper {
	
	Connection conn = Connect.getConn();
	Statement stmt;
	
	// lager øvelsegruppe ved å sette navnet på den. privatfunksjon for å finne høyeste gruppeid
	public boolean createGroup(String navn) {
		int id = findMaxId();
		if (id == -1) {
			return false;
		}
		try {
			stmt = conn.createStatement();
			String sql = String.format("INSERT INTO OvelseGruppe(GruppeID, Navn) VALUES ('%s','%s')", id+1, navn);
			stmt.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private int findMaxId() {
		int id;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM OvelseGruppe ORDER BY GruppeID DESC LIMIT 1";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			id = rs.getInt("GruppeID");
			return id;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	// returnerer gruppenavn som array
	public ArrayList<String> showGroups(){
		ArrayList<String>groups = new ArrayList<String>();
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM OvelseGruppe";
			ResultSet rs;
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				groups.add(rs.getString("Navn"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groups;
	}
	
	// finner gruppeid til gitt ovelsegruppe
	public int findGroupID(String navn) {
		int id = -1;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM OvelseGruppe";
			ResultSet rs;
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				if(navn == rs.getString("Navn")) {
					id = rs.getInt("GroupID");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public ArrayList<String> showOvelseInGroup(String navn){
		int id = findGroupID(navn);
		ArrayList<String> group = new ArrayList<String>();
		try {
			stmt = conn.createStatement();
			String sql = String.format("SELECT Ovelsenavn FROM OvelseIGruppe WHERE GruppeID='%s'", id);
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				group.add(rs.getString("Ovelsenavn"));
			}
			return group;
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	// lager relasjon i OvelseIGruppe med ovelsenavnet og navnet på ovelsegruppen. bruker privatfunksjoner for å sjekke at ovelsen eksisterer i databasen.
	public void createOvelseGruppe(String navn) {
		//Erik kan fikse pent. generell inntil videre
		System.out.println(apparatOvelseList().toString());
		System.out.println("Tast inn nummer på ovelsene du vil legge til i ovelsegruppen: ");
		System.out.println("Tast q hvis du er ferdig.");
		Scanner sc = new Scanner(System.in);
		
		List<String> chosenApparatOvelser = new ArrayList<String>();
		List<String> chosenFrivektsOvelser = new ArrayList<String>();
		List<Integer> apparatOvelseKeys = new ArrayList<Integer>();
		List<Integer> frivektsOvelseKeys = new ArrayList<Integer>();
		
		int i = sc.nextInt();
		chosenApparatOvelser.add(apparatOvelseList().get(i));
		
		for (String ovelse : chosenApparatOvelser) {
			String[] split = ovelse.split(":");
			apparatOvelseKeys.add(Integer.valueOf(split[0]));
		}
		
		for (String ovelse : chosenFrivektsOvelser) {
			String[] split = ovelse.split(":");
			frivektsOvelseKeys.add(Integer.valueOf(split[0]));
		}
		
		
	
	public List<String> apparatOvelseList(){
		List<String> apparatOvelse = new ArrayList<String>();
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM ApparatOvelse";
			ResultSet rs = stmt.executeQuery(sql);
			int i = 0;
			while(rs.next()) {
				String str = i + ":" + rs.getString("Ovelsenavn");
				apparatOvelse.add(str);
				i++;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return apparatOvelse;
	}
	
	public List<String> frivektsOvelseList(){
		List<String> frivektsOvelse = new ArrayList<String>();
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM Frivektsovelse";
			ResultSet rs = stmt.executeQuery(sql);
			int i = 0;
			while(rs.next()) {
				String str = i + ":" + rs.getString("Ovelsenavn");
				frivektsOvelse.add(str);
				i++;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return frivektsOvelse;
	}
	
	private boolean checkApparat(String ovelsenavn) {
		try {
			stmt = conn.createStatement();
			String sql = "SELECT Ovelsenavn FROM ApparatOvelse";
			ResultSet rs;
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				if(ovelsenavn == rs.getString("Ovelsenavn"));
				return true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean checkFrivekt(String ovelsenavn) {
		try {
			stmt = conn.createStatement();
			String sql = "SELECT Ovelsenavn FROM FrivektsOvelse";
			ResultSet rs;
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				if(ovelsenavn == rs.getString("Ovelsenavn"));
				return true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static void main(String[] args) {
		OvelseGrupper app = new OvelseGrupper();
		app.createOvelseGruppe("test");
	}
}
