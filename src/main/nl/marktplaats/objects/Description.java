package main.nl.marktplaats.objects;

import java.util.List;
import java.util.ArrayList;

public class Description {
	private List<String> words = new ArrayList<String>();
	private String originalText;

	public Description(List<String> l) {
		words = l;
	}

	public Description() {
	}

	public Description(String string) {
		String[] w = string.split(" ");
		for(String word:w)
		{
			words.add(word);
		}
	}

	public void addWord(String word) {
		words.add(word);
	}

	public List<String> getDescriptionWords() {
		return words;
	}

	public boolean containsWord(String w) {
		return words.contains(w);
	}
	
	public void printContents()
	{
		for(String word:words)
		{
			System.out.println(word);
		}
	}

	public int getTotalNumberOfWords() {
		return this.words.size();
	}

	public void setDescriptionWords(List<String> removeStopWords) {
		this.words=removeStopWords;
		}


	public String getOriginalText() {
		return this.originalText;
	}
	public void setOriginalText(String text)
	{
		text.trim();
		String[] splitedWords = text.split(" ");
		String returnedText="";
		for(String word:splitedWords)
		{
			returnedText=returnedText+" "+sanitizeWord(word);
		}
		this.originalText=returnedText;
	}
	public String sanitizeWord(String word) {
		String sanitizedWord;
		sanitizedWord = word.replace("'s", "is");
		sanitizedWord = sanitizedWord.replace("'", "");
		sanitizedWord = sanitizedWord.replace(";", "");
		return sanitizedWord;
	}

}
