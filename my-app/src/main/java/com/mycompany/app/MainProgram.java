package com.mycompany.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

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
	
	// -------------------------------------------------------------------------------------------------------------------------
	
	public static void registerFriOvelse(String Ovelsenavn, String beskrivelse) {
		try {
			stmt = conn.createStatement();
			String sql1 = String.format("INSERT INTO `Ovelse`(`Ovelsenavn`) VALUES ('%s')", Ovelsenavn);
			stmt.executeUpdate(sql1);
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
	public static void registerTreningsokt(int oktid, String dato, int varighet, int form, int prestasjon, ArrayList<String> friOvelser, Map<String,ArrayList<Integer>> apparatOvelser, String notat) {
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
	
	// -------------------------------------------------------------------------------------------------------------------------
	
	//Printer ut info om alle treningsøkter med OktID opp til og med n. Starter med OktID nr n og går nedover.
	public static void infoOmSisteOkter(int n) {
		try {
			stmt = conn.createStatement();
			String sql = String.format("SELECT * FROM Treningsokt order by OktID DESC LIMIT " + n);
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
				String notat = "Ingenting";
				while(result_notat.next()) {
					notat = result_notat.getString("Beskrivelse");
				}
				System.out.println("-----------------------------------------------");
				System.out.println("Okt nr " + String.valueOf(oktID) + ": " + '\n' + "Dato: " + '\n' + String.valueOf(dato) + 
					"\nTidspunkt: " + String.valueOf(tidspunkt) + '\n' + "Varighet: " + String.valueOf(varighet) + '\n' + 
					"Form: " + form + '\n' + "Prestasjon: " + prestasjon + '\n' + 
					"Notat: " + notat + '\n'
					);
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
	
	
	public static List<String> frivektsOvelseListErik(){
		List<String> frivektsOvelse = new ArrayList<String>();
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM FrivektsOvelse";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String str = rs.getString("Ovelsenavn");
				frivektsOvelse.add(str);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return frivektsOvelse;
	}

	public static List<String> apparatOvelseListErik(){
		List<String> apparatOvelse = new ArrayList<String>();
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM ApparatOvelse";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String str = rs.getString("Ovelsenavn");
				apparatOvelse.add(str);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return apparatOvelse;
	}
	
	
	//Printer ut info om alle treningsøkter med OktID opp til og med n. Starter med OktID nr n og går nedover.
	public static void getProsentOkning(int oktid, String apparatOvelse) {
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
	
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	
	
	public static ArrayList<String> showResults(String ovelse, java.util.Date date2, java.util.Date date3){
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
				if((date.after(date2) && date.before(date3))||date.equals(date2)||date.equals(date3)) {
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
		System.out.println("-----------------------------------------------");
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
			
			String valgtAlternativString = scanner.nextLine();
			try {
				
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
				try {
					stmt = conn.createStatement();
					String sql = "SELECT ApparatID FROM Apparat order by ApparatID DESC";
					ResultSet result = stmt.executeQuery(sql);
					if (result.next()) {
						apparatID = result.getInt(1) + 1;
					}
					else {
						apparatID = 1;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.out.println("-----------------------------------------------");
				System.out.println("Skriv inn apparatnavn (string):");
				String apparatnavn = scanner.nextLine();
				System.out.println("-----------------------------------------------");
				
				System.out.println("Skriv inn en beskrivelse (string):");
				String beskrivelse = scanner.nextLine();
				System.out.println("-----------------------------------------------");
				
				registerApparat(apparatID, apparatnavn, beskrivelse);
			}
			
			// -------------------------------------------------------------------------------------------------------------------------
			// 2 - Registrere ovelse
			else if (valgtAlternativ == 2) { 
				// Velge type ovelse 
				System.out.println("Registrere ovelse:\n");
				System.out.println("Hvilken type ovelse vil du registrere?\n1 - Frivektsovelse\n2 - Apparatovelse\n");
				
				String ovelsevalgString = scanner.nextLine();
				int ovelsevalg = Integer.parseInt(ovelsevalgString);
				
				while ((ovelsevalg != 1) || (ovelsevalg != 2)) {
					while (isInteger(ovelsevalgString) == false) {
						System.out.println("-----------------------------------------------");
						System.out.println("Velg enten 1 eller 2 (int):");
						ovelsevalgString = scanner.nextLine();
					}
					ovelsevalg = Integer.parseInt(ovelsevalgString);
					break;
					
				}
				
				if (ovelsevalg == 1) {
					System.out.println("-----------------------------------------------");
					System.out.println("\nFrivektsovelse:");
					System.out.println("Vennligst skriv inn navnet på ovelsen: ");
					String ovelsenavn = scanner.nextLine();
					System.out.println("-----------------------------------------------");
					System.out.println("Vennligst skriv inn en beskrivelse på ovelsen: ");
					String ovelsebeskrivelse = scanner.nextLine();
					
					registerFriOvelse(ovelsenavn, ovelsebeskrivelse);
					System.out.println("Frivektsovelsen ble registrert.");
					
				}
				
				else if (ovelsevalg == 2) {
					System.out.println("-----------------------------------------------");
					System.out.println("\nApparatovelse:");
					System.out.println("Vennligst skriv inn navnet på ovelsen: ");
					String ovelsenavn = scanner.nextLine();
					
					
					// ----------------
					registerApparatOvelse(ovelsenavn);
					System.out.println("Apparatovelsen ble registrert.");
				}
				else {
					System.out.println("Du har valgt noe annet enn 1 eller 2, går tilbake til start.");
				}
			}
			
			// -------------------------------------------------------------------------------------------------------------------------
			// 3 - Registrere treningsokt
			else if (valgtAlternativ == 3) { System.out.println("Registrere treningsokt:\n");

				int oktID = 0;
				try {
					stmt = conn.createStatement();
					String sql = "SELECT OktID FROM Treningsokt order by OktID DESC";
					ResultSet result = stmt.executeQuery(sql);
					if (result.next()) {
						oktID = result.getInt(1) + 1;
					}
					else {
						oktID = 1;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				//oktID OKAY ---------------------------------------
				System.out.println("-----------------------------------------------");
				System.out.println("Vennligst skriv inn dato og tidspunkt for okten: (FORMAT: 'yyyy-MM-dd HH:mm:ss')");
				String datetimeobjekt = scanner.nextLine();
				
				// start med varighet				 ---------------------------------------
				int varighet = 0;
				System.out.println("-----------------------------------------------");
				System.out.println("Vennligst skriv inn en varighet for okten (int):");
				String varighetString = scanner.nextLine();
				
				while (isInteger(varighetString) == false) {
					System.out.println("-----------------------------------------------");
					System.out.println("Vennligst skriv inn en varighet for okten (int):");
					varighetString = scanner.nextLine();
				}
				varighet = Integer.parseInt(varighetString);
				
				// ferdig med varighet 				---------------------------------------	
				// Start med form som en int 		---------------------------------------
				
				int form = 0;
				System.out.println("-----------------------------------------------");
				System.out.println("Skriv inn hvor bra formen var denne okten (int, 1-10):");
				String formString = scanner.nextLine();
				
				while (isInteger(formString) == false) {
					System.out.println("-----------------------------------------------");
					System.out.println("Skriv inn hvor bra formen var denne okten (int, 1-10):");
					formString = scanner.nextLine();
				}
				form = Integer.parseInt(formString);
				
				// ferdig med form som en int		 ---------------------------------------
				// start med prestasjon som en int	 ---------------------------------------
				
				int prestasjon = 0;
				System.out.println("-----------------------------------------------");
				System.out.println("Hvor bra prestasjonen var denne okten (int, 1-10):");
				String prestasjonString = scanner.nextLine();
				
				while (isInteger(prestasjonString) == false) {
					System.out.println("-----------------------------------------------");
					System.out.println("Hvor bra prestasjonen var denne okten (int, 1-10):");
					prestasjonString = scanner.nextLine();
				}
				prestasjon = Integer.parseInt(prestasjonString);
				
				// liste med ovelser  				---------------------------------------
				//  	vi må kanskje sjekke at vi ikke legger til noen ovelser som ikke finnes.
				
				// Sjekke type ovelse en skal gjøre
				int ovelsevalg = 1;
				
				Map<String,ArrayList<Integer>> mapting = new HashMap();
				ArrayList<Integer> vektsettListe = new ArrayList();
				ArrayList<String> friOvelser = new ArrayList();

				while ((ovelsevalg == 1) || (ovelsevalg == 2) || (ovelsevalg == 3)) {
					System.out.println("-----------------------------------------------");
					System.out.println("Hvilken type ovelse vil du legge til?\n1 - Frivektsovelse\n2 - Apparatovelse\n3 - Ferdig med å legge til ovelser");
					String ovelsevalgString = scanner.nextLine();
					while (isInteger(ovelsevalgString) == false) {
						System.out.println("-----------------------------------------------");
						System.out.println("Feil input, prøv igjen.");
						ovelsevalgString = scanner.nextLine();
					}
					ovelsevalg = Integer.parseInt(ovelsevalgString);

					
					if (ovelsevalg == 1) {
						System.out.println("-----------------------------------------------");
						System.out.println("Legg til en frivektsovelse.");
						System.out.println("Valgmuligheter (skriv stringen):");
						List<String> frivektsOvelser = frivektsOvelseListErik();
						if (frivektsOvelser.isEmpty()) {
							System.out.println("Du har ikke lagt til noen ovelser.");
							break;
						}
						for (String frivektsovelse : frivektsOvelser) {
							System.out.println(frivektsovelse);
						}
						String ovelseAdd = scanner.nextLine();
						friOvelser.add(ovelseAdd);
					}
					else if (ovelsevalg == 2) {
						System.out.println("-----------------------------------------------");
						System.out.println("Legg til en apparatovelse.");
						System.out.println("Dette er dine valgmuligheter:");
						//Oversikt over alle ovelsene.
						List<String> apparatOvelser = apparatOvelseListErik();
						if (apparatOvelser.isEmpty()) {
							System.out.println("Du har ikke lagt til noen ovelser.");
							break;
						}
						for (String apparatOvelse : apparatOvelser) {
							System.out.println(apparatOvelse);
						}
						String ovelseAdd1 = scanner.nextLine();

						//Legge til vekt
						System.out.println("-----------------------------------------------");
						System.out.println("Legg til vekt på denne ovelsen (int):");
						String vektintString = scanner.nextLine();
						while (isInteger(vektintString) == false) {
							System.out.println("-----------------------------------------------");
							System.out.println("Feil input, prøv igjen.");
							vektintString = scanner.nextLine();
						}
						int vektint = Integer.parseInt(vektintString);
						
						//Legge til sett
						System.out.println("-----------------------------------------------");
						System.out.println("Legg til antall sett på denne ovelsen (int):");
						String settintString = scanner.nextLine();
						while (isInteger(settintString) == false) {
							System.out.println("Feil input, prøv igjen.");
							settintString = scanner.nextLine();
						}
						int settint = Integer.parseInt(settintString);
						vektsettListe.add(vektint);
						vektsettListe.add(settint);
						
						mapting.put(ovelseAdd1, vektsettListe);
					}
					else if (ovelsevalg == 3) {
						System.out.println("-----------------------------------------------");
						System.out.println("Du har valgt å avslutte ovelsevalget.");
						break;
					}
				}
				
				// notat	 add		---------------------------------------
				//				---------------------------------------
				
				System.out.println("-----------------------------------------------");
				System.out.println("Skriv et notat om okten:\n");
				String notat = scanner.nextLine();
			
				registerTreningsokt(oktID, datetimeobjekt, varighet, form, prestasjon, friOvelser, mapting, notat);
			}
			
			// -------------------------------------------------------------------------------------------------------------------------
			// 4 - Se info om x antall siste treningsøkter
			else if (valgtAlternativ == 4) { 
				System.out.println("-----------------------------------------------");
				System.out.println("Se info om x antall siste treningsøkter:");

				int antallOkter = 0;
				System.out.println("-----------------------------------------------");
				System.out.println("Antall okter du vil ha info om (int):");
				String antallOkterString = scanner.nextLine();
				
				while (isInteger(antallOkterString) == false) {
					System.out.println("-----------------------------------------------");
					System.out.println("Antall okter du vil ha info om (int):");
					antallOkterString = scanner.nextLine();
				}
				antallOkter = Integer.parseInt(antallOkterString);
				
				infoOmSisteOkter(antallOkter);
			
			}
				
			// -------------------------------------------------------------------------------------------------------------------------
			// 5 - Se resultatlogg fra et gitt tidsintervall
			else if (valgtAlternativ == 5) { 
				System.out.println("-----------------------------------------------");
				System.out.println("Se resultatlogg fra et gitt tidsintervall:\n");
				System.out.println("Tilgjengelige ovelser:");
				System.out.println(apparatOvelseListErik());
				
				System.out.println("Skriv inn ovelse:");
				String ovelse = scanner.next();
				System.out.println("Enter the Date : (format: yyyy/mm/dd )");
				String startDate = scanner.next();
				System.out.println("Enter the Date : (format: yyyy/mm/dd )");
				String endDate = scanner.next();
				java.util.Date date2 = null;
				java.util.Date date3 = null;
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
			
			// -------------------------------------------------------------------------------------------------------------------------
			// 6 - Lage ovelsesgruppe / se grupper
			else if (valgtAlternativ == 6) {
				System.out.println("-----------------------------------------------");
				System.out.println("Lage ovelsesgruppe / se grupper:");
				System.out.println("-----------------------------------------------");
				System.out.println("Velg alternativ 1 eller 2:\n1 - Lage ovelsesgruppe\n2 - Vise en ovelsesgruppe");
				String gruppevalgString = scanner.nextLine();
				int gruppevalg = 0;
				
				OvelseGrupper app = new OvelseGrupper();
				
				while ((gruppevalg != 1) || (gruppevalg != 2)) {
					while (isInteger(gruppevalgString) == false) {
						System.out.println("-----------------------------------------------");
						System.out.println("Velg alternativ 1 eller 2:\n1 - Lage ovelsesgruppe\n2 - Vise en ovelsesgruppe");
						gruppevalgString = scanner.nextLine();
					}
					gruppevalg = Integer.parseInt(gruppevalgString);
					
					if (gruppevalg == 1) {
						System.out.println("Tast inn navn på øvelsesgruppen: ");
						String navn = scanner.nextLine();
						app.createOvelseGruppe(navn);
						break;
						
					}
					else if (gruppevalg == 2) {
						System.out.println("Dette er alle tilgjengelige ovelsesgrupper:");
						System.out.println(app.showGroups().toString());
						System.out.println("-----------------------------------------------");
						System.out.println("Velg en av gruppene du vil vise (skriv navnet):");
						String valgavgruppe = scanner.nextLine();
						System.out.println("Ovelsene i gruppen du valgte er: " + app.showOvelseInGroup(valgavgruppe));
						break;
					}
					
				}

			}
				
			// -------------------------------------------------------------------------------------------------------------------------
			// 7 - Se prosentokning på et apparat
			else if (valgtAlternativ == 7) { 
				System.out.println("-----------------------------------------------");
				System.out.println("Se prosentokning på et apparat:");
				System.out.println("Du må velge en treningsokt og apparat for aa sammenligne med okten foer den valgte.");
				System.out.println("Her er en oversikt over oktene med ID og Dato:");
				try {
					stmt = conn.createStatement();
					String sql = "SELECT * FROM Treningsokt order by OktID DESC";
					ResultSet rs = stmt.executeQuery(sql);
					System.out.println("-----------------------------------------------");
					while(rs.next()) {
						System.out.println("ID: " + rs.getInt("OktID") + " Dato: " + rs.getDate("Dato").toString());
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				int valgtOktID = 0;
				System.out.println("-----------------------------------------------");
				System.out.println("Velg OktID:");
				String valgtOktIDString = scanner.nextLine();
				while (isInteger(valgtOktIDString) == false) {
					System.out.println("-----------------------------------------------");
					System.out.println("Velg OktID:");
					valgtOktIDString = scanner.nextLine();
				}
				valgtOktID = Integer.parseInt(valgtOktIDString);
				
				System.out.println("Oversikt over apparat:");
				try {
					stmt = conn.createStatement();
					String sql2 = String.format("SELECT * FROM ApparatOvelseForOkt WHERE OktID = '%s'", valgtOktID);
					ResultSet rs2 = stmt.executeQuery(sql2);
					ArrayList<String> apparatListe = new ArrayList();
					while(rs2.next()) {
						apparatListe.add(rs2.getString("Ovelsenavn"));
					}
					for (String x : apparatListe) {
						System.out.println(x);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				System.out.println("-----------------------------------------------");
				System.out.println("Velg apparat:");
				String valgtApparat = scanner.nextLine();

				getProsentOkning(valgtOktID, valgtApparat);

			}
			
			else { 
				System.out.println("-----------------------------------------------");
				System.out.println("Feil input, prøv igjen."); }
			
			}
		
			// -------------------------------------------------------------------------------------------------------------------------
		
		
		
	}

			
}
		
		
		
