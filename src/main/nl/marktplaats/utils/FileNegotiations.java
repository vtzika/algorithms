package main.nl.marktplaats.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class FileNegotiations {

	public static String getTxtLines(String classifiedFile) {
		String returnedlines = " ";
		try {
			FileInputStream fstream = new FileInputStream(classifiedFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				returnedlines += strLine;
			}
			in.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		return returnedlines;
	}

	public void createFile(String inputtext, String path) {

		try {
			FileWriter ryt = new FileWriter(path);
			BufferedWriter out = new BufferedWriter(ryt);
			out.write(inputtext);
			out.close();
			System.out.println(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getResultsFromTxtAndSaveInDB(String db) {
		String file = "/home/varvara/workspace/externalSources/Results/Voyager/SearchTopBlock/Unstemmed/EntireIndex/";
		SqlCommands sql = new SqlCommands();
		try {
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] words = strLine.split(" ");
				sql.insertQuery("insert into voyagerScore VALUES (" + words[0]
						+ "," + words[2] + "," + words[4] + "," + '0'
						+ ",'searchInEntire');", db);

			}
			in.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

	}

	public void getResultsFromCSVAndSaveInDB() {
		String file = "/home/varvara/Algorithms/ExperimentsNumbers/VipQueries.output.csv";
		try {
			// Open the file that is the first
			// command line parameter

			FileInputStream fstream = new FileInputStream(file);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				System.out.println(strLine);
				String[] words = strLine.split(",");
				this.saveCSVResults(words[0], words[1], words[2], words[4],
						words[3]);
				// Print the content on the console
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}

	private void saveCSVResults(String string, String string2, String string3,
			String string4, String string5) {
		try {
			String connectionURL = "jdbc:mysql://localhost:3306/cas_ad_service";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			String QueryString = "insert into tfIdfTon VALUES ('" + string
					+ "','" + string2 + "'," + string3 + "," + string4 + ","
					+ string5 + ");";
			System.out.println(QueryString);
			int updateQuery = statement.executeUpdate(QueryString);
			if (updateQuery != 0) {
				System.out.println("row is inserted.");
			}
			System.out.println(QueryString);
			statement.close();
			connection.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	}
