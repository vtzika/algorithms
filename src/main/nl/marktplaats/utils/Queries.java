package main.nl.marktplaats.utils;

import java.util.ArrayList;
import java.util.List;

import main.nl.marktplaats.objects.Classified;

public class Queries {
	private Classified classified = new Classified();

	public Queries(Classified c) {
		classified = c;
	}

	public String getStringQuery(List<String> words) {
		String query = "#combine(";
		for (String w : words) {
			query += w + " ";
		}
		return query + ")";
	}

	public String getNumericQuery(String word, int value) {
		return "TODO";
	}

	public String queryGreaterPrice() {
		return "#greater(price " + this.classified.getPrice().getPrice() + " )";

	}

	public String combineQueries(List<String> queries) {
		String combinedQuery = "";
		for (String query : queries) {

			combinedQuery += query + "";
		}
		return combinedQuery;
	}

	public List<String> getSameWordsOfTwoLists(List<String> l1, List<String> l2) {
		List<String> combinedList = new ArrayList<String>();
		for (String l1Word : l1) {
			if (l2.contains(l1Word))
				combinedList.add(l1Word);
		}
		return combinedList;
	}

	public List<String> getCombinedLists(List<String> l1, List<String> l2) {
		List<String> combinedList = l1;
		for (String l2Word : l2) {
			combinedList.add(l2Word);
		}
		return combinedList;
	}

	public Classified getClassified() {
		return this.classified;
	}
}
