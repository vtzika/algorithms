package main.nl.marktplaats.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.FileDataSource;

import main.nl.marktplaats.objects.Category;
import main.nl.marktplaats.objects.Attributes;
import main.nl.marktplaats.objects.Classified;
import main.nl.marktplaats.objects.Description;
import main.nl.marktplaats.objects.DocNo;
import main.nl.marktplaats.objects.Price;
import main.nl.marktplaats.objects.Title;


public class ClassifiedParser {
	private Category category;
	private Title title;
	private Description description;
	private Price price;
	private Classified classified;
	private Attributes attributes;

	public ClassifiedParser() {
		category = new Category();
		title = new Title();
		description = new Description();
		price = new Price();
		classified = new Classified();
		attributes = new Attributes();
	}

	public void setClassified(String file) {
		String text = FileNegotiations.getTxtLinesAndReurnString(file);
		separateFields(text);
	}

	public List<String> separateWords(String text) {
		List<String> words = new ArrayList<String>();
		for (String fieldWord : text.split(" ")) {
			if (!fieldWord.contains("<"))
				words.add(new StringManipulation().sanitizeWord(fieldWord));
		}
		return words;
	}

	public Classified getClassified() {
		return classified;
	}

	public void separateFields(String text) {
		
		HashMap<String, String> textAndFields= new HashMap<String,String>();
		List<String> fields = new ArrayList<String>();
		fields.add("DOCNO");
		fields.add("TITLE");
		fields.add("MYDESCRIPTION");
		fields.add("MYCATEGORY");
		fields.add("PRICE");
		fields.add("ATTRIBUTES");

		textAndFields=separateAllFieldsFromText(text, fields);
		long docno = getIntOutOfString(textAndFields.get("DOCNO"));
		DocNo docNo=new DocNo(docno);
		
		Title  title = new Title(textAndFields.get("TITLE").split(" "));
		
		Description  description = new Description(textAndFields.get("MYDESCRIPTION"));
		
		Category  category = new Category(textAndFields.get("MYCATEGORY"));
		
		long pr = getIntOutOfString(textAndFields.get("PRICE"));
		Price price=new Price((int) pr);
		
		Attributes attributes = new Attributes(textAndFields.get("ATTRIBUTES"));
		classified = new Classified(docNo,title, description, category, price, attributes);
		classified.removeStopWords();
		classified.sanitize();
	}

	private HashMap<String, String> separateAllFieldsFromText(String text,List<String> fields) {
		HashMap<String, String> separatedTextAndFields = new HashMap<String, String>();
		for(String field:fields)
		{
			List<String> sepText = separateField(text,"</"+field+">");
			String fieldText = sepText.get(0);
			text = sepText.get(1);
			separatedTextAndFields.put(field, fieldText);
		}
		return separatedTextAndFields;
	}


	public List<String> separateField(String text, String fieldSeparator) {
		List<String> textParts = new ArrayList<String>();
		String[] separateFields = text.split(fieldSeparator);
		String textWithoutField =separateFields[0].replace(fieldSeparator, "");
		String bewFieldSeparator=fieldSeparator.replace("/", "");
		textWithoutField = textWithoutField.replace(bewFieldSeparator, "");
		textParts.add(textWithoutField);
		//System.out.println(separateFields[0]);
		//System.out.println(separateFields.length);
		textParts.add(separateFields[1]);
			
		return textParts;
	}
	
	private Long getIntOutOfString(String string)
	{
		long number =0;
		Matcher m = Pattern.compile("(?!=\\d\\.\\d\\.)([\\d.]+)").matcher(string);
		while (m.find()) {
			number = java.lang.Integer.parseInt(m.group(1));
		}
		return number;		
	}
}
