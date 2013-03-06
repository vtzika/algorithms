package main.nl.marktplaats.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.nl.marktplaats.utils.StringManipulation;


public class Classified {
	private DocNo docno;
	private Title title;
	private Description description;
	private Category category;
	private Price price;
	private Attributes attributes;
	
	public Classified(DocNo no,Title t, Description d, Category c, Price p, Attributes a) {
		this.docno=no;
		this.title = t;
		this.description = d;
		this.category = c;
		this.price = p;
		this.attributes = a;
	}

	public Classified() {
	}
	
	public Title getTitle() {
		return title;
	}

	public Description getDescription() {
		return description;
	}

	public Category getCategory() {
		return category;
	}
	
	public Attributes getAttributes()
	{
		return attributes;
	}

	public Price getPrice() {
		return price;
	}

	public void setTitle(Title t) {
		title = t;
	}

	public void setDescription(Description desc) {
		description = desc;

	}

	public void setPrice(Price p) {
		price = p;
	}

	public int getTotalNumberOfWords() {
		int titleCount = this.title.getTotalNumberOfWords();
		int categoryCount = this.category.getTotalNumberOfWords();
		int descriptionCount = this.description.getTotalNumberOfWords();

		return titleCount + categoryCount + descriptionCount;
	}

	public List<String> getWordsList() {
		List<String> words = new ArrayList<String>();
		for(String titleWords:title.getTitleWords())
		{
			words.add(titleWords);
		}
		for(String categoryWords:category.getCategoryWords())
		{
			words.add(categoryWords);
		}
		for(String descWords:description.getDescriptionWords())
		{
			words.add(descWords);
		}
		return words;
	}

	public void printClassifier() {
		for (String w : this.getWordsList()) {
			System.out.println(w);
		}

	}

	public Map<String, Integer> getWordsFrequency() {
		Map<String, Integer> wordsFrequency = new HashMap<String, Integer>();
		for (String word : this.getWordsList()) {
			wordsFrequency.put(word, this.getWordFrequency(word));
		}
		return wordsFrequency;
	}

	private int getWordFrequency(String word) {
		int count = 0;
		for (String classifiedsWord : this.getWordsList()) {
			if (classifiedsWord.toUpperCase().equals(word.toUpperCase()))
			{
				count++;
			}
		}
		return count;
	}

	public int getTotalWordsFrequency() {
		int count = 0;
		for (int freq : getWordsFrequency().values()) {
			count += freq;
		}
		return count;
	}

	public void removeStopWords() {
		StringManipulation segmentation=new StringManipulation();
		this.category.setCategoryWords(segmentation.removeStopWords(this.category.getCategoryWords()));
		this.title.setTitleWords(segmentation.removeStopWords(this.title.getTitleWords()));
		this.description.setDescriptionWords(segmentation.removeStopWords(this.description.getDescriptionWords()));
		this.attributes.setAttributesWords(segmentation.removeStopWords(this.attributes.getAttributesWords()));	
	}

	public void sanitize() {
		StringManipulation segmentation=new StringManipulation();
		this.category.setCategoryWords(segmentation.sanitizeWords(this.category.getCategoryWords()));
		this.title.setTitleWords(segmentation.sanitizeWords(this.title.getTitleWords()));
		this.description.setDescriptionWords(segmentation.sanitizeWords(this.description.getDescriptionWords()));
		this.attributes.setAttributesWords(segmentation.sanitizeWords(this.attributes.getAttributesWords()));
	}

	public DocNo getDocNo() {
		return this.docno;
	}
}
