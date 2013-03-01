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
import java.sql.Statement;
import java.util.ArrayList;
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

	public void getResultsFromTxtAndSaveInDB(Configuration configuration) {
		SqlCommands sql = new SqlCommands();
		try {
			FileInputStream fstream = new FileInputStream(
					configuration.getReadFile());
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] words = strLine.split(" ");
				sql.insertQuery("insert into " + configuration.getInputTable()
						+ " VALUES (" + words[0] + "," + words[2] + ","
						+ words[4] + "," + '0' + ",'searchInEntire');",
						configuration.getDb());
			}
			in.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

	}

	public void getResultsFromCSVAndSaveInDB(Configuration configuration) {
		try {
			FileInputStream fstream = new FileInputStream(
					configuration.getReadFile());
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] words = strLine.split(",");
				this.saveCSVResults(words[0], words[1], words[2], words[4],
						words[3], configuration);
			}
			in.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	private void saveCSVResults(String string, String string2, String string3,
			String string4, String string5, Configuration configuration) {
		try {
			String connectionURL = "jdbc:mysql://localhost:3306/"
					+ configuration.getDb();
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			String QueryString = "insert into " + configuration.getInputTable()
					+ " VALUES ('" + string + "','" + string2 + "'," + string3
					+ "," + string4 + "," + string5 + ");";
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

	public List<String> getListFiles(String folder) {
		List<String> files = new ArrayList<String>();
		return files;
	}



}
