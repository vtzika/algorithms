package main.nl.marktplaats.utils;

import java.util.ArrayList;
import java.util.List;

public class StringManipulation {

	public String sanitizeString(String text) {
		if (text!=null)
		{
		String[] words = text.split("[ |<|/|\\|\n|\t>]");
		String sanitizedString = "";
		for (String word : words) {

			if (!(this.isStopWord(word))&& !(word.trim().length()<3) && !(word.equals(null)))
			{
				//&& !(segmentation.containsNumeric(word)) && !(word.trim().length()<3)
				sanitizedString = sanitizedString + " "
						+ this.sanitizeWord(word);
			}
			//else System.out.println(word);
		}
		//System.out.println(sanitizedString);
		return sanitizedString;
		}
		else return " ";
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
			for (String word :text) {
				boolean flagStop = false;
				for (String stopWord : stopWords) {
				if(word.toLowerCase().equals(stopWord.toLowerCase())) {
					flagStop = true;
					break;
				}
				//else if(containsOnlyDigits(word))
				//	flagStop=true;
				//	break;
			}
			if(flagStop == false) {
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
		//System.out.println(word);
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
		for(char c:sanitizedWord.toCharArray())
		{
			if(Character.isDigit(c))
				flagHasLetters=true;
			
		} 
		return flagHasLetters;}


}
