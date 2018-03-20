package com.mycompany.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import com.mycompany.app.Connect;

public class MainProgram {
	
	static Connection conn = Connect.getConn();
	static Statement stmt;
	
	// -------------------------------------------------------------------------------------------------------------------------
	
	public static void registerApparat(int apparatID, String navn, String beskrivelse) {
		try {
			stmt = conn.createStatement();
			String sql = String.format("INSERT INTO `Apparat`(`ApparatID`, `Navn`, `Beskrivelse`) VALUES ('%d','%s','%s')", apparatID, navn, beskrivelse);
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// -------------------------------------------------------------------------------------------------------------------------
	
	public static void registerApparatOvelse(String Ovelsenavn, double antallKilo, int antallSett) {
		try {
			stmt = conn.createStatement();
			String sql = String.format("INSERT INTO `ApparatOvelse`(`Ovelsenavn`, `Antall_Sett`, `Antall_Kilo`) VALUES ('%s','%s','%s')", Ovelsenavn, antallKilo, antallSett);
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// -------------------------------------------------------------------------------------------------------------------------
	
	public static void registerFriOvelse(String Ovelsenavn, String beskrivelse) {
		try {
			stmt = conn.createStatement();
			String sql = String.format("INSERT INTO `Ovelse`(`Ovelsenavn`) VALUES ('%s')", Ovelsenavn);
			stmt.executeUpdate(sql);
			stmt = conn.createStatement();
			String sql2 = String.format("INSERT INTO `FrivektsOvelse`(`Ovelsenavn`, `Tekstlig_beskrivelse`) VALUES ('%s','%s')", Ovelsenavn, beskrivelse);
			stmt.executeUpdate(sql2);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// -------------------------------------------------------------------------------------------------------------------------

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
	
	// -------------------------------------------------------------------------------------------------------------------------
	
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
	
	// -------------------------------------------------------------------------------------------------------------------------
	
	// Bare en kjapp metode for å sjekke om en string er integer.
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    //Hvis det er en int
	    return true;
	}
	
	
	// IS DOUBLE FUNCTION
	public static boolean isDouble(String s) {
	    try { 
	        Double.parseDouble(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    //Hvis det er en double
	    return true;
	}
	
	// -------------------------------------------------------------------------------------------------------------------------
	
	
	// HER STARTER MAIN -------------------------------------------------------------------------------------------------------------------------
	// HER STARTER MAIN -------------------------------------------------------------------------------------------------------------------------
	// HER STARTER MAIN -------------------------------------------------------------------------------------------------------------------------
	// HER STARTER MAIN -------------------------------------------------------------------------------------------------------------------------
	// HER STARTER MAIN -------------------------------------------------------------------------------------------------------------------------
	
	public static void main(String[] args) throws IOException {
		// Mainen for kjøring av programmet.
		Scanner scanner = new Scanner(System.in);
		
		//Intro start
		System.out.println("Velkommen til Treningsdagboken!");
		System.out.println("Under vil du få opp ulike alternativer til hva du kan gjøre.");
		System.out.println("Når du skal velge alternativ svarer du med tall.\n");
		//Intro slutt
		
		//While loop som får programmet til å kjøre "evig".
		
		while (true) {
			System.out.println("-----------------------------------------------");
			System.out.println("Hva vil du gjøre?");
			System.out.println("-----------------------------------------------");
			System.out.println("1 - Registrere apparat");
			System.out.println("2 - Registrere ovelse");
			System.out.println("3 - Registrere treningsokt");
			System.out.println("4 - Se info om x antall siste treningsøkter");
			System.out.println("5 - Se resultatlogg fra et gitt tidsintervall");
			System.out.println("6 - Lage ovelsesgruppe / se grupper");
			System.out.println("7 - Se prosentokning på et apparat");
			System.out.println("Skriv 'avslutt' for å gå tilbake hit.");
			System.out.println("-----------------------------------------------");
			
			System.out.println("Velg et alternativ (int, 1-7): ");
			int valgtAlternativ = 8;
			
			try {
				String valgtAlternativString = scanner.nextLine();
				valgtAlternativ = Integer.parseInt(valgtAlternativString);
			}	catch (Exception e){
				continue;
			}
			
			// -------------------------------------------------------------------------------------------------------------------------
			
			// Her starter alle alternativene du kan velge og gå videre inn på.
			// 1 - Registrere apparat
			if (valgtAlternativ == 1) { 
				System.out.println("Registrere apparat:\n");
			
				int apparatID = 0;
				System.out.println("Skriv inn apparatID (int):");
				String apparatIDstring = scanner.nextLine();
				while (isInteger(apparatIDstring) == false) {
					System.out.println("Skriv inn apparatID (int):");
					apparatIDstring = scanner.nextLine();
				}
				apparatID = Integer.parseInt(apparatIDstring);
				
				System.out.println("Skriv inn apparatnavn (string):");
				String apparatnavn = scanner.nextLine();
				
				System.out.println("Skriv inn en beskrivelse (string):");
				String beskrivelse = scanner.nextLine();
				
				registerApparat(apparatID, apparatnavn, beskrivelse);
			}
			
			// -------------------------------------------------------------------------------------------------------------------------
			// 2 - Registrere ovelse
			else if (valgtAlternativ == 2) { 
				// Velge type ovelse 
				System.out.println("Registrere ovelse:\n");
				System.out.println("Hvilken type ovelse vil du registrere?\n1 - Frivektsovelse\n2 - Apparatovelse\n");
				
				
				int ovelsevalg = 3;
				while ((ovelsevalg != 1) || (ovelsevalg != 1)) {
						try {
							System.out.println("Velg enten 1 eller 2 (int): ");
							String ovelsevalgString = scanner.nextLine();
							ovelsevalg = Integer.parseInt(ovelsevalgString);
						}	catch (Exception e){
							continue;
						}
				}
				
				if (ovelsevalg == 1) {
					System.out.println("\nFrivektsovelse:");
					System.out.println("Vennligst skriv inn navnet på ovelsen: ");
					String ovelsenavn = scanner.nextLine();
					System.out.println("Vennligst skriv inn en beskrivelse på ovelsen: ");
					String ovelsebeskrivelse = scanner.nextLine();
					
					registerFriOvelse(ovelsenavn, ovelsebeskrivelse);
					System.out.println("Frivektsovelsen ble registrert.");
					
				}
				
				else if (ovelsevalg == 2) {
					System.out.println("\nApparatovelse:");
					System.out.println("Vennligst skriv inn navnet på ovelsen: ");
					String ovelsenavn = scanner.nextLine();
					
					
					// ovelsekilo med sjekk for riktig input -----------------
					double ovelsekilo = 0;
					System.out.println("Vennligst skriv inn antall kilo: ");
					String ovelsekiloString = scanner.nextLine();
				
					while (isDouble(ovelsekiloString) == false) {
						System.out.println("Vennligst skriv inn antall kilo: ");
						ovelsekiloString = scanner.nextLine();
					}
					ovelsekilo = Double.parseDouble(ovelsekiloString);
					// ovelsekilo ferdig med sjekk for riktig input -----------
						
					// ----------------
					int antallsett = 0;
					System.out.println("Vennligst skriv inn antall sett: ");
					String antallsettString = scanner.nextLine();
					
					while (isInteger(antallsettString) == false) {
						System.out.println("Vennligst skriv inn antall kilo: ");
						antallsettString = scanner.nextLine();
					}
					antallsett = Integer.parseInt(antallsettString);
					
					// ----------------
					
					registerApparatOvelse(ovelsenavn, ovelsekilo, antallsett);
					System.out.println("Apparatovelsen ble registrert.");
					
				}	
				
			}
			
			// -------------------------------------------------------------------------------------------------------------------------
			// 3 - Registrere treningsokt
			else if (valgtAlternativ == 3) { System.out.println("Registrere treningsokt:\n");

				int oktID = 0;
				System.out.println("Vennligst skriv inn ID for denne treningsokten (int):");
				String oktIDstring = scanner.nextLine();
				
				while (isInteger(oktIDstring) == false) {
					System.out.println("Vennligst skriv inn ID for denne treningsokten (int):");
					oktIDstring = scanner.nextLine();
				}
				oktID = Integer.parseInt(oktIDstring);
				//oktID OKAY ---------------------------------------
				
				System.out.println("Vennligst skriv inn dato og tidspunkt for okten: FORMAT: 'yyyy-MM-dd HH:mm:ss'\n");
				String datetimeobjekt = scanner.nextLine();
				
				SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
				
				java.sql.Date dt =  ft.parse(datetimeobjekt);
				
				// SQL ??? UTIL???
				
				// PROBLEMER OVER HER, HVA FAEN DATE 
				// HELT MONGO
				
				
				
				
				
				
				
				// ferdig med 'dt' datetime, kan være feil her  ---------------------------------------
				// start med varighet				 ---------------------------------------
				int varighet = 0;
				System.out.println("Vennligst skriv inn en varighet for okten (int):");
				String varighetString = scanner.nextLine();
				
				while (isInteger(varighetString) == false) {
					System.out.println("Vennligst skriv inn en varighet for okten (int):");
					varighetString = scanner.nextLine();
				}
				varighet = Integer.parseInt(varighetString);
				
				// ferdig med varighet 				---------------------------------------	
				// Start med form som en int 		---------------------------------------
				
				int form = 0;
				System.out.println("Skriv inn hvor bra formen var denne okten (int, 1-10):");
				String formString = scanner.nextLine();
				
				while (isInteger(formString) == false) {
					System.out.println("Skriv inn hvor bra formen var denne okten (int, 1-10):");
					formString = scanner.nextLine();
				}
				form = Integer.parseInt(formString);
				
				// ferdig med form som en int		 ---------------------------------------
				// start med prestasjon som en int	 ---------------------------------------
				
				int prestasjon = 0;
				System.out.println("Hvor bra prestasjonen var denne okten (int, 1-10):");
				String prestasjonString = scanner.nextLine();
				
				while (isInteger(prestasjonString) == false) {
					System.out.println("Hvor bra prestasjonen var denne okten (int, 1-10):");
					prestasjonString = scanner.nextLine();
				}
				prestasjon = Integer.parseInt(prestasjonString);
				
				// liste med ovelser  				---------------------------------------
				//  	vi må kanskje sjekke at vi ikke legger til noen ovelser som ikke finnes.
	
				System.out.println("Legg til ovelser du gjorde i okten, FORMAT='ovelsenavn, ovelsenavn, ovelsenavn':\n");
				String ovelserString = scanner.nextLine();
				
				List<String> ovelserlist = Arrays.asList(ovelserString.split("\\s*,\\s*"));
				ArrayList<String> arraylistOvelser = new ArrayList();
				for (int i = 0; i < ovelserlist.size(); i++) { 
					arraylistOvelser.add(ovelserlist.get(i));
				}
				
				
				// notat	 add			 				---------------------------------------
				//  									---------------------------------------
				
				System.out.println("Skriv et notat om okten:\n");
				String notat = scanner.nextLine();
				
			
			registerTreningsokt(oktID, dt, varighet, form, prestasjon, arraylistOvelser, notat);
				
			}
			
			// -------------------------------------------------------------------------------------------------------------------------
			// 4 - Se info om x antall siste treningsøkter
			else if (valgtAlternativ == 4) { System.out.println("Se info om x antall siste treningsøkter:\n");
				
				int antallOkter = 0;
				System.out.println("Antall okter du vil ha info om (int):");
				String antallOkterString = scanner.nextLine();
				
				while (isInteger(antallOkterString) == false) {
					System.out.println("Antall okter du vil ha info om (int):");
					antallOkterString = scanner.nextLine();
				}
				antallOkter = Integer.parseInt(antallOkterString);
				
				infoOmSisteOkter(antallOkter);
			
			}
			
			
			// KOMMET NED HIT TIRSDAG, egentlig ganske fornøyd men fuck datetime
				
			// -------------------------------------------------------------------------------------------------------------------------
			// 5 - Se resultatlogg fra et gitt tidsintervall
			else if (valgtAlternativ == 5) { 	System.out.println("Se resultatlogg fra et gitt tidsintervall:\n");
			
			}
			
			// -------------------------------------------------------------------------------------------------------------------------
			// 6 - Lage ovelsesgruppe / se grupper
			else if (valgtAlternativ == 6) { System.out.println("Lage ovelsesgruppe / se grupper:\n");
					
			}
				
			// -------------------------------------------------------------------------------------------------------------------------
			// 7 - Se prosentokning på et apparat
			else if (valgtAlternativ == 7) { System.out.println("Se prosentokning på et apparat:\n");
				
			}
			
			else { System.out.println("Feil input, prøv igjen."); }
			
			}
		
			// -------------------------------------------------------------------------------------------------------------------------
		
		
		
	}

			
}
		
		
		
