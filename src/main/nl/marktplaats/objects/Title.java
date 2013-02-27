package main.nl.marktplaats.objects;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;

public class Title {
	private List<String> words;
	private String originalText;

	public Title(List<String> l) {
		words = l;
	}
	
	public Title(String[] s) {
		this.words = new ArrayList<String>();
		for (String string:s)
		{
			words.add(string);
		}
	}

	public Title() {
		// TODO Auto-generated constructor stub
	}

	public List<String> getTitleWords() {
		
		return this.words;
	}

	public void addWord(String word) {
		words.add(word);
	}

	public boolean containsWord(String w) {
		return words.contains(w);
	}
	
	public int getTotalNumberOfWords() {
		return this.words.size();
	}

	public String getTitleText() {
		return Joiner.on(" ").join(words);
	}

	public void setTitleWords(List<String> removeStopWords) {
		this.words=removeStopWords;
	}

	public String getOriginalText() {
		return originalText;
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
