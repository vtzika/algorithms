package main.nl.marktplaats.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SqlCommands {

	public void insertQuery(String query, String db) {
		try {
			String connectionURL = "jdbc:mysql://localhost:3306/" + db;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			statement.close();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean checkIfhasValue(String query, String db) {
		Boolean flag = false;
		try {
			String connectionURL = "jdbc:mysql://localhost:3306/" + db;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			if (rs.next())
				flag = true;
			statement.close();
			connection.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return flag;
	}

	public Long selectQuery(String query, String db) {
		Long result = (long) 0;
		try {
			ResultSet rs = null;
			String connectionURL = "jdbc:mysql://localhost:3306/" + db;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				result = rs.getLong(1);
			}
			statement.close();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public String selectCatQuery(String query, String db) {
		String result = "";
		try {
			ResultSet rs = null;
			String connectionURL = "jdbc:mysql://localhost:3306/" + db;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				result = rs.getString(1);
			}
			statement.close();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public String selectStringQuery(String query, String db) {
		String result = "";
		try {
			ResultSet rs = null;
			String connectionURL = "jdbc:mysql://localhost:3306/" + db;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				result = rs.getString(1) + " " + rs.getString(2) + " ";
			}
			statement.close();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public HashMap<Long, String> selectHashMapQuery(String query, String db) {
		HashMap<Long, String> vipsQueries = new HashMap<Long, String>();
		try {
			ResultSet rs = null;
			String connectionURL = "jdbc:mysql://localhost:3306/" + db;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				vipsQueries.put(rs.getLong(1), rs.getString(2));
			}
			statement.close();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return vipsQueries;
	}

	public HashMap<Long, Double> selectHashMapLongDoubbleQuery(String query,
			String db) {
		HashMap<Long, Double> vipsQueries = new HashMap<Long, Double>();
		try {
			ResultSet rs = null;
			String connectionURL = "jdbc:mysql://localhost:3306/" + db;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				vipsQueries.put(rs.getLong(1), rs.getDouble(2));
			}
			statement.close();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return vipsQueries;
	}

	public Double selectDoubleQuery(String query, String db) {
		Double result = 0.0;
		try {
			ResultSet rs = null;
			String connectionURL = "jdbc:mysql://localhost:3306/" + db;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				result = rs.getDouble(1);
			}
			statement.close();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;

	}

	public List<Integer> selectListInt(String query, String db) {
		List<Integer> results = new ArrayList<Integer>();
		try {
			ResultSet rs = null;
			String connectionURL = "jdbc:mysql://localhost:3306/" + db;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				results.add(rs.getInt(1));
			}
			statement.close();
			connection.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return results;
	}

	public List<Long> selectListLong(String query, String db) {
		List<Long> results = new ArrayList<Long>();
		try {
			ResultSet rs = null;
			String connectionURL = "jdbc:mysql://localhost:3306/" + db;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				results.add(rs.getLong(1));
			}
			statement.close();
			connection.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return results;
	}

	public HashMap<HashMap<Long, Long>, Double> selectNestedHashMapQuery(
			String query, String db) {
		HashMap<Long, Long> firstHM = new HashMap<Long, Long>();
		HashMap<HashMap<Long, Long>, Double> vipsQueries = new HashMap<HashMap<Long, Long>, Double>();
		try {
			ResultSet rs = null;
			String connectionURL = "jdbc:mysql://localhost:3306/" + db;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				firstHM.put(rs.getLong(1), rs.getLong(2));
				vipsQueries.put(firstHM, rs.getDouble(3));
			}
			statement.close();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return vipsQueries;
	}

	public HashMap<Long, Long> selectHashMapLongLongQuery(String query,
			String db) {
		HashMap<Long, Long> vipsQueries = new HashMap<Long, Long>();
		try {
			ResultSet rs = null;
			String connectionURL = "jdbc:mysql://localhost:3306/" + db;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				vipsQueries.put(rs.getLong(1), rs.getLong(2));
			}
			statement.close();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return vipsQueries;
	}

	public HashMap<Integer, Double> selectHashMapIntDoubleQuery(String query,
			String db) {
		HashMap<Integer, Double> vipsQueries = new HashMap<Integer, Double>();
		try {
			ResultSet rs = null;
			String connectionURL = "jdbc:mysql://localhost:3306/" + db;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				vipsQueries.put(rs.getInt(1), rs.getDouble(2));
			}
			statement.close();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return vipsQueries;
	}

	public double[] selectDoubleArray(int arraySize, String query, String db) {
		double[] results = new double[arraySize];
		try {
			ResultSet rs = null;
			String connectionURL = "jdbc:mysql://localhost:3306/" + db;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(query);
			int count = 0;
			while (rs.next()) {

				results[count] = rs.getDouble(1);
				count++;
			}
			statement.close();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return results;
	}

	public int selectIntQuery(String query, String db) {
		int result = 0;
		try {
			ResultSet rs = null;
			String connectionURL = "jdbc:mysql://localhost:3306/" + db;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				result = rs.getInt(1);
			}
			statement.close();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public List<String> selectListString(String query, String db) {
		List<String> results = new ArrayList<String>();
		try {
			ResultSet rs = null;
			String connectionURL = "jdbc:mysql://localhost:3306/" + db;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				results.add(rs.getString(1));

			}
			statement.close();
			connection.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return results;
	}

	public HashMap<Long, String> selectHashMapLongStringQuery(String query,
			String db) {
		HashMap<Long, String> vipsQueries = new HashMap<Long, String>();
		try {
			ResultSet rs = null;
			String connectionURL = "jdbc:mysql://localhost:3306/" + db;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				vipsQueries.put(rs.getLong(1), rs.getString(2));
			}
			statement.close();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return vipsQueries;
	}

	public List<Double> selectListDouble(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public HashMap<String, String> selectHashMapStringStringQuery(
			String query, String db) {
		HashMap<String, String> vipsQueries = new HashMap<String, String>();
		try {
			ResultSet rs = null;
			String connectionURL = "jdbc:mysql://localhost:3306/" + db;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				vipsQueries.put(rs.getString(1), rs.getString(2));
			}
			statement.close();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return vipsQueries;
	}

}
