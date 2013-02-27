package main.nl.marktplaats.objects;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Category {
	private List<String> words=new ArrayList<String>();
	private HashMap<Long, String> categoryIdAndCategoryRepresentation;

	public Category(List<String> l) {
		words=l;
	}

	public Category() {
		setCategoryName();
	}

	public Category(String string) {
		String[] w=string.split(" ");
		for(String word:w)
		{
			this.words.add(word);
		}
	}

	public List<String> getCategoryWords() {
		return this.words;
	}

	public void addWord(String word) {
		words.add(word);
		
	}
	public void printContents()
	{
		for(String word:words)
		{
			System.out.println(word);
		}
	}

	public boolean containsWord(String w) {
		return words.contains(w);
	}

	public int getTotalNumberOfWords() {
		return this.words.size();
	}

	public void setCategoryWords(List<String> removeStopWords) {
		this.words=removeStopWords;
	}
	
	private void setCategoryName()
	{
		HashMap<Long, String> catIdAndName = new HashMap<Long, String>();

		ResultSet rs;
		try {
			String connectionURL = "jdbc:mysql://localhost:3306/cas_ad_service";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			// Add description,price,category_id + not implemented attributes
			String QueryString = "select category_id, parent_path,category_name from categories; ";
			//System.out.println(QueryString);
			rs = statement.executeQuery(QueryString);
			while (rs.next()) {
				catIdAndName.put(rs.getLong(1), rs.getString(2) + ' '
						+ rs.getString(3));
			}
			rs.close();
			statement.close();
			connection.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.categoryIdAndCategoryRepresentation = catIdAndName;
	}
	public String getCategoryNameByCatId(Long catId)
	{
		return this.categoryIdAndCategoryRepresentation.get(catId);
	}

	

}
