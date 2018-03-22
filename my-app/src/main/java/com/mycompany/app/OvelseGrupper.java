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
				if(navn.equals(rs.getString("Navn"))) {
					id = rs.getInt("GruppeID");
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
		List<String> apparatOvelseKeys = new ArrayList<String>();
		List<String> frivektsOvelseKeys = new ArrayList<String>();
		
		int i = sc.nextInt();
		chosenApparatOvelser.add(apparatOvelseList().get(i));
		
		for (String ovelse : chosenApparatOvelser) {
			String[] split = ovelse.split(":");
			apparatOvelseKeys.add(split[1]);
		}
		
		for (String ovelse : chosenFrivektsOvelser) {
			String[] split = ovelse.split(":");
			frivektsOvelseKeys.add(split[1]);
		}
		
		
		try {
			String sql = String.format("INSERT INTO OvelseGruppe(Navn) VALUES ('%s')", navn);
			PreparedStatement stmt_1 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt_1.executeUpdate();
			
			ResultSet rs = stmt_1.getGeneratedKeys();
			//System.out.println(rs.getInt("GruppeID"));
			rs.next();
			int id = rs.getInt(1);
			System.out.println("ID " + id);
			
			
			for (String key : apparatOvelseKeys) {
				String sql_2 = String.format("INSERT INTO OvelseIGruppe(GruppeID, Ovelsenavn) VALUES ('%s', '%s')", id, key);
				PreparedStatement stmt_2 = conn.prepareStatement(sql_2);
				stmt_2.executeUpdate();
			}
			
			
			for (String key : frivektsOvelseKeys) {
				String sql_2 = String.format("INSERT INTO OvelseIGruppe(GruppeID, Ovelsenavn) VALUES ('%s', '%s')", id, key);
				PreparedStatement stmt_2 = conn.prepareStatement(sql_2);
				stmt_2.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("BRUH");
		sc.close();
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
	
	/* private funksjoner for å sjekke om øvelser eksisterer. men brukes ikke.
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
*/

	
	public static void main(String[] args) {
		OvelseGrupper app = new OvelseGrupper();
		System.out.println(app.showGroups());
	}
}
