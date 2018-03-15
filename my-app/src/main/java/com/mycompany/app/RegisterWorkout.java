package com.mycompany.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.mycompany.app.Connect;

public class RegisterWorkout {
	
	Connection conn = Connect.getConn();
	Statement stmt;
	
	
	public void registerApparat(String navn, String beskrivelse) {
		try {
			stmt = conn.createStatement();
			String sql = String.format("INSERT INTO `Apparat`(`ApparatID`, `Navn`, `Beskrivelse`) VALUES (null,'%s','%s')", navn, beskrivelse);
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
	}

}
