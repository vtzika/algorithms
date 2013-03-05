package main.nl.marktplaats.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StringManipulation {

	public String sanitizeString(String text) {
		if (text != null) {
			String[] words = text.split("[ |<|/|\\|\n|\t>]");
			String sanitizedString = "";
			for (String word : words) {

				if (!(this.isStopWord(word)) && !(word.trim().length() < 3)
						&& !(word.equals(null))) {
					// && !(segmentation.containsNumeric(word)) &&
					// !(word.trim().length()<3)
					sanitizedString = sanitizedString + " "
							+ this.sanitizeWord(word);
				}
				// else System.out.println(word);
			}
			// System.out.println(sanitizedString);
			return sanitizedString;
		} else
			return " ";
	}

	public String sanitizeWord(String word) {
		// TODO
		word.toLowerCase().trim();
		String sanitizedWord;
		sanitizedWord = word.replace("!", "");
		sanitizedWord = sanitizedWord.replace("*", " ");
		sanitizedWord = sanitizedWord.replace(",", " ");
		sanitizedWord = sanitizedWord.replace(".", " ");
		sanitizedWord = sanitizedWord.replace(":", " ");
		sanitizedWord = sanitizedWord.replace(";", " ");
		sanitizedWord = sanitizedWord.replace("|", " ");
		sanitizedWord = sanitizedWord.replace("~", " ");
		sanitizedWord = sanitizedWord.replace("=", " ");
		sanitizedWord = sanitizedWord.replace("~", " ");
		sanitizedWord = sanitizedWord.replace("/", " ");
		sanitizedWord = sanitizedWord.replace(")", " ");
		sanitizedWord = sanitizedWord.replace("(", " ");
		sanitizedWord = sanitizedWord.replace("-", " ");
		sanitizedWord = sanitizedWord.replace("]", " ");
		sanitizedWord = sanitizedWord.replace("[", " ");
		sanitizedWord = sanitizedWord.replace("@", " ");
		sanitizedWord = sanitizedWord.replace("\\", " ");
		sanitizedWord = sanitizedWord.replace("€", " ");
		sanitizedWord = sanitizedWord.replace("?", " ");
		sanitizedWord = sanitizedWord.replace("&", " ");
		sanitizedWord = sanitizedWord.replace("$", " ");
		sanitizedWord = sanitizedWord.replace("'", " ");
		sanitizedWord = sanitizedWord.replace("+", " ");
		sanitizedWord = sanitizedWord.replace("±", " ");
		sanitizedWord = sanitizedWord.replace("%", " ");
		sanitizedWord = sanitizedWord.replace("\"", " ");
		return sanitizedWord;
	}

	public List<String> removeStopWords(List<String> text) {
		String[] stopWords = DutchStopwords.getStopwords();
		List<String> textWithoutStopwords = new ArrayList<String>();
		for (String word : text) {
			boolean flagStop = false;
			for (String stopWord : stopWords) {
				if (word.toLowerCase().equals(stopWord.toLowerCase())) {
					flagStop = true;
					break;
				}
				// else if(containsOnlyDigits(word))
				// flagStop=true;
				// break;
			}
			if (flagStop == false) {
				textWithoutStopwords.add(word);
			}
		}
		return textWithoutStopwords;
	}

	private boolean containsOnlyDigits(String word) {
		boolean isDigit = true;
		char[] string = word.toCharArray();
		for (char c : string) {
			if (!Character.isDigit(c)) {
				isDigit = false;
				break;
			}
		}
		return isDigit;
	}

	public boolean isStopWord(String word) {
		// System.out.println(word);
		String[] stopWords = DutchStopwords.getStopwords();
		for (String stopWord : stopWords) {
			if (word.toLowerCase().equals(stopWord.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * public List<String> sanitizeAndStem(List<String> words) { List<String>
	 * sanitizedAndStemmedWords=new ArrayList<String>(); for(String word:words)
	 * { sanitizedAndStemmedWords.add(sanitizeAndStem(word)); } return
	 * sanitizedAndStemmedWords; }
	 */

	public List<String> sanitizeWords(List<String> words) {
		List<String> sanitizedWords = new ArrayList<String>();
		for (String word : words) {
			sanitizedWords.add(sanitizeWord(word));
		}
		return sanitizedWords;
	}

	public boolean containsNumeric(String sanitizedWord) {
		String clearString = " ";
		boolean flagHasLetters = false;
		flagHasLetters = false;
		for (char c : sanitizedWord.toCharArray()) {
			if (Character.isDigit(c))
				flagHasLetters = true;

		}
		return flagHasLetters;
	}

	public void RemoveDuplicates(String db) {
		SqlCommands sql = new SqlCommands();
		List<Long> ids = sql.selectListLong(
				"select distinct(query) from voyagerResultsReal;", db);

		for (Long i : ids) {
			System.out.println(i);
			List<Long> comparedResults = new ArrayList<Long>();
			List<Long> first = sql
					.selectListLong(
							"select doc from voyagerResultsReal where system like 'top4WeDiscrim' and query="
									+ i + " order by score desc limit 5;", db);
			List<Long> second = sql
					.selectListLong(
							"select doc from voyagerResultsReal where system like 'top10Discrim' and query="
									+ i + " order by score desc limit 5;", db);
			List<Long> third = sql
					.selectListLong(
							"select doc from voyagerResultsReal where system like 'top4DiscrimTitle' and query="
									+ i + " order by score desc limit 5;", db);
			List<Long> forth = sql.selectListLong(
					"select doc from voyagerResultsReal where system like 'top4Discrim' and query="
							+ i + " order by score desc limit 5;", db);

			List<Long> fifth = sql
					.selectListLong(
							"select doc from voyagerResultsReal where system like 'top10WeDiscrim' and query="
									+ i + " order by score desc limit 5;", db);
			comparedResults.addAll(forth);
			comparedResults.addAll(first);
			comparedResults.addAll(second);
			comparedResults.addAll(third);
			comparedResults.addAll(fifth);
			String insString = "";
			for (Long result : comparedResults) {
				insString += "(" + i + "," + result + "),";
			}
			sql.insertQuery("insert into tempQueryAndDoc VALUES " + insString
					+ "(0,0);", db);
		}

	}

	public void RemoveTonDuplicates(String db) {
		SqlCommands sql = new SqlCommands();
		List<Long> ids = sql.selectListLong("select distinct(keyword) from relevance;", db);
		for (Long i : ids) {
			List<Long> comparedResults = new ArrayList<Long>();

			List<Long> comparedResultsNew = new ArrayList<Long>();

			List<Long> previousResults = sql.selectListLong(
					"select doc from allResultsNEW where  query=" + i + ";", db);
			List<Long> TfIdfFromTon = sql.selectListLong(
					"select doc from tfIdfTon where sequence<=10 and query="
							+ i + ";", db);
			List<Long> TitleRandomFromTon = sql.selectListLong(
					"select doc from titleRandomTon where sequence<=10 and query="
							+ i + ";", db);
			List<Long> TitleDescRandomFromTon = sql.selectListLong(
					"select doc from titleDescRandomTon where sequence<=10 and query="
							+ i + ";", db);
			List<Long> TitleRPMFromTon = sql.selectListLong(
					"select doc from titleRpmTon where sequence<=10 and query="
							+ i + ";", db);
			List<Long> TitleDescRPMFromTon = sql.selectListLong(
					"select doc from titledescrRpm where sequence<=10 and query="
							+ i + ";", db);

			comparedResults.addAll(previousResults);

			for (Long doc : TfIdfFromTon) {
				if (comparedResults.contains(doc))
					System.out.print(doc);
				else {
					comparedResultsNew.add(doc);
					comparedResults.add(doc);
				}
			}
			for (Long doc2 : TitleRandomFromTon) {
				if (comparedResults.contains(doc2))
					System.out.print(doc2);
				else {
					comparedResultsNew.add(doc2);
					comparedResults.add(doc2);
				}
			}
			for (Long doc3 : TitleDescRandomFromTon) {
				if (comparedResults.contains(doc3))
					System.out.print(doc3);
				else {
					comparedResultsNew.add(doc3);
					comparedResults.add(doc3);
				}
			}

			for (Long doc4 : TitleRPMFromTon) {
				if (comparedResults.contains(doc4))
					System.out.print(doc4);
				else {
					comparedResultsNew.add(doc4);
					comparedResults.add(doc4);
				}
			}
			for (Long doc5 : TitleDescRPMFromTon) {
				if (comparedResults.contains(doc5))
					System.out.print(doc5);
				else {
					comparedResultsNew.add(doc5);

					comparedResults.add(doc5);
				}
			}
			for (Long result : comparedResultsNew)
				sql.insertQuery("insert into resultsForEvaluationTon VALUES (" + i
						+ ", " + result + ");", db);
		}
	}

	public String getStringSeparatedByCommas(String origQuery) {
		String[]  terms = origQuery.split(" ");
		String newQuery = terms[0];
		for(int i=1; i<terms.length;i++)
		{
			newQuery+=","+terms[i];
		}
		return newQuery;
	}
}
