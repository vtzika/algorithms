package main.nl.marktplaats.objects;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import main.nl.marktplaats.utils.Configuration;
import main.nl.marktplaats.utils.FileNegotiations;
import main.nl.marktplaats.utils.SqlCommands;

public class Results {

	public void readVoyagerResults(Configuration configuration) {
		try {
			FileInputStream fstream = new FileInputStream(
					configuration.getReadFile());
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] words = strLine.split(" ");
				if (words[0].equals("Rk")) {
					// TODO save them in a table
				}

			}
			in.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

	}

	public void combinationOfScores(String db, String table, String s1,
			String s2, String resultedTable) {
		SqlCommands sql = new SqlCommands();
		List<Long> queries = sql.selectListLong("select distinct query from "
				+ table + ";", db);
		double newScore = 0;
		for (Long query : queries) {
			HashMap<Long, Double> titleResults = getHashMapWithDocAndScore(
					"select doc," + s1 + " from " + table + " where query="
							+ query + " ;", db);
			HashMap<Long, Double> descResults = getHashMapWithDocAndScore(
					"select doc," + s2 + " from " + table + " where query="
							+ query + " ;", db);
			for (Long key : titleResults.keySet()) {
				if (descResults.containsKey(key))
					newScore = 3 * titleResults.get(key) + descResults.get(key);
				else
					newScore = 3 * titleResults.get(key);
				sql.insertQuery("insert into " + resultedTable + " values ("
						+ query + ", " + key + ", " + newScore + ");", db);
			}
			for (Long key : descResults.keySet()) {
				if (titleResults.containsKey(key)) {
				} else {
					newScore = descResults.get(key);
					sql.insertQuery("insert into " + resultedTable + "values ("
							+ query + ", " + key + ", " + newScore + ");", db);
				}
			}
		}
	}

	private HashMap<Long, Double> getHashMapWithDocAndScore(String query,
			String db) {
		HashMap<Long, Double> results = new HashMap<Long, Double>();
		ResultSet rs;
		try {
			String connectionURL = "jdbc:mysql://localhost:3306/" + db;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				results.put(rs.getLong(1), rs.getDouble(2));
			}
			rs.close();
			statement.close();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return results;
	}

	public void createInputForTrec(String pathFolder, String db, String system,
			String table) {
		String path = pathFolder + system + ".txt";
		ResultSet rs;
		String inputtext = "";
		SqlCommands sql = new SqlCommands();
		List<Long> queries = sql.selectListLong("select distinct(query) from "
				+ table + " where system like '" + system + "';", db);
		for (Long query : queries) {
			try {
				String connectionURL = "jdbc:mysql://localhost:3306/" + db;
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				Connection connection = DriverManager.getConnection(
						connectionURL, "root", "qwe123");
				Statement statement = connection.createStatement();
				String QueryString = "select distinct query,docs,score,sequence from "
						+ table
						+ " where  query="
						+ query
						+ " and  system like '"
						+ system
						+ "' and sequence<100 order by sequence;";
				rs = statement.executeQuery(QueryString);
				while (rs.next()) {
					inputtext = inputtext + rs.getInt(1) + " Q0 "
							+ rs.getLong(2) + " " + rs.getInt(4) + " "
							+ rs.getDouble(3) + " indri\n";
				}
				rs.close();
				statement.close();
				connection.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		new FileNegotiations().createFile(inputtext, new String(path));
	}

	public void createGroundTruthForTrec() {
		String path = "/home/varvara/workspace/externalSources/trecResults/groundTruth/150SearchKeywordsVoyager85FQ.txt";
		ResultSet rs;
		String inputtext = "";
		try {
			String connectionURL = "jdbc:mysql://localhost:3306/150SearchKeywords";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			// Add description,price,category_id + not implemented attributes
			String QueryString = "select * from newGroundTruth;";
			rs = statement.executeQuery(QueryString);
			while (rs.next()) {
				inputtext = inputtext + rs.getInt(1) + " 0 " + rs.getLong(2)
						+ " " + rs.getInt(3) + "\n";
			}
			System.out.println(inputtext);
			rs.close();
			statement.close();
			connection.close();
			new FileNegotiations().createFile(inputtext, new String(path));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void readLogs(String db) {
		String file = "";
		SqlCommands sql = new SqlCommands();
		try {
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			String insQuery = "";
			int countL = 0;
			int countI = 0;
			while ((strLine = br.readLine()) != null) {
				// System.out.println(strLine);
				String[] words = strLine.split(",");
				String ip = "";
				String luckyNo = "";
				String agent = "";
				String vip = "";
				for (String word : words) {
					if (word.contains("ad_id[")) {
						vip += word;
					}
					if (word.contains("user_ip[")) {
						ip += word;
					} else if (word.contains("luckynumber[")) {
						luckyNo += word;
					} else if (word.contains("user_agent[")) {
						agent += word;
					}
				}
				insQuery += "('" + vip + "','" + ip + "','" + luckyNo + "','"
						+ agent + "'),";
				countL++;
				if (countL == 1000) {
					countI++;
					countL = 0;
					sql.insertQuery("insert into allLogs VALUES " + insQuery
							+ "('0','0','0','0');", db);
					insQuery = "";
					System.out.println(countI);
				}
			}

			in.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

	}

}
