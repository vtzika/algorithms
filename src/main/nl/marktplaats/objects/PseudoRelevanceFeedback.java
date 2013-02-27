package main.nl.marktplaats.objects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import main.nl.marktplaats.utils.SqlCommands;
import main.nl.marktplaats.utils.StringManipulation;

public class PseudoRelevanceFeedback {

	public String findBiggestCTRAndCreateNewQueries(List<Integer> cat_ids) throws Exception {
		SqlCommands sql = new SqlCommands();
		String newExtQuery = "";
		for(int id:cat_ids)
		{	HashMap<Long,Double>  adWithCTR = new HashMap<Long,Double>();
			adWithCTR = sql.selectHashMapLongDoubbleQuery("select ad_id,CTR from allL2AndCTR where l2="+id,"cas_ad_service");
			List<Long> maxCTRAds = getMaxCTRFromAds(adWithCTR);
			sql.insertQuery("insert into topCTRL2 values ("+id+","+maxCTRAds.get(1)+","+maxCTRAds.get(2)+","+maxCTRAds.get(3)+","+maxCTRAds.get(4)+","+maxCTRAds.get(5)+");","");
			String newQuery = calculateNewQueryFromFrequencies(maxCTRAds);
			sql.insertQuery("insert into extendedQueriesInL1Browse values ("+id+",'"+newQuery+"');","");
			newExtQuery+=newQuery;
		}
		return newExtQuery;
		
		
	}

	private String calculateNewQueryFromFrequencies(List<Long> maxCTRAds) throws Exception {
		String newQuery = "";
		SqlCommands sql = new SqlCommands();
		Attributes attribute = new Attributes();
		Category category = new Category();
		String allText = "";
		for(int i=1;i<=5; i++)
		{
			String titleDescr = sql.selectStringQuery("select title,description from ads where id="+maxCTRAds.get(i)+";","cas_ad_service");
			String attr = attribute.getCasAttributeStringByAdId(maxCTRAds.get(i));
			String cat = category.getCategoryNameByCatId(maxCTRAds.get(i));
			allText += titleDescr + " "+attr+" "+cat+" ";
		}
	
		newQuery = chooseMostFrequentTerms(allText);
		return newQuery;
	}

	private String chooseMostFrequentTerms(String t) throws Exception {
		StringManipulation sm = new StringManipulation();
		String text =  sm.sanitizeString(t);
		String[] terms = text.split(" ");
		HashMap<String,Integer> termFrequencies = new HashMap<String,Integer>();
		for(String term:terms)
		{
			int freq = 1;
			if(termFrequencies.containsKey(term))
			{
				freq += termFrequencies.get(term);
				termFrequencies.put(term, freq);
			}
			else
				termFrequencies.put(term, freq);
		}
		String newQuery = getMaxFrequentTerm(termFrequencies);
		
		return newQuery;
	}

	private String getMaxFrequentTerm(HashMap<String, Integer> termFrequencies) {
		String returned = "";
		TreeMap<String, Integer> sorted_map = new TreeMap();
		for (Map.Entry<String, Integer> pair : termFrequencies.entrySet()) {
			if(!(pair.getValue()==0))
			{
				sorted_map.put(pair.getKey(), pair.getValue());
			}
		}
		for (int i = 0; i <= 10; i++) {
			if(sorted_map.size()>0)
			{
				double max = sorted_map.firstEntry().getValue();
				 String maxKey = sorted_map.firstEntry().getKey();
				for (String key : sorted_map.keySet()) {
					if (sorted_map.get(key) > max) {
							max = sorted_map.get(key);
							maxKey = key;
						}

				}
				returned += " "+maxKey;
				sorted_map.remove(maxKey);
			}
			}
			return returned;
	}

	private List<Long> getMaxCTRFromAds(HashMap<Long, Double> adWithCTR) {
		List<Long> returned = new ArrayList<Long>();
		TreeMap<Long, Double> sorted_map = new TreeMap();
		for (Map.Entry<Long, Double> pair : adWithCTR.entrySet()) {
			if(!pair.getValue().isNaN())
			{
				sorted_map.put(pair.getKey(), pair.getValue());
			}
		}
		for (int i = 0; i <= 5; i++) {
			if(sorted_map.size()>0)
			{
				double max = sorted_map.firstEntry().getValue();
				Long maxKey = sorted_map.firstEntry().getKey();
				for (Long key : sorted_map.keySet()) {
					if (sorted_map.get(key) > max) {
							max = sorted_map.get(key);
							maxKey = key;
						}

				}
				System.out.println(maxKey);
				returned.add(maxKey);
				sorted_map.remove(maxKey);
			}
			}
			return returned;
	}

	public void createParameterFelesL2(List<Integer> cat_ids) throws Exception {
		for(int cat:cat_ids)
		{
			SqlCommands sql = new SqlCommands();
			String query = sql.selectStringQuery(" select query from searchInL2Keywords where l2="+cat+" ;","cas_ad_service");
			createParameterFile(cat, query);
			
		}
		
	}

	private void createParameterFile(int docNo, String query) {
		SqlCommands sql = new SqlCommands();
		//int l1 = docNo;
		Long l1 = sql.selectQuery("select distinct(l1) from allL2AndCTR where l2="+docNo+";","");
		String repositories = "";
		String outputFile = "/home/varvara/workspace/externalSources/ParameterFiles/L2aob/Unstemmed/EntireIndex/Search/l1/" + docNo;
		String inputText = "";
		String trecFormat = "\n <trecFormat>true</trecFormat>";
		String queryNumber ="<query>\n<text>"+query.trim()+"</text>\n<number>" + docNo + "</number>\n</query>";
		String l2Repository = "/home/varvara/workspace/externalSources/indri-5.3/repositoriesL1/"+l1;
		repositories = repositories + "\n <index>"+ l2Repository	+ "</index>";
		String baseline = "\n<baseline>tf.idf,k1:1.0,b:0.3</baseline>";
		inputText = "<parameters> \n " + repositories + "\n" + queryNumber+ baseline  + "\n" +"<count> 1000 </count>"+ "\n" + trecFormat;
		Writer output = null;
		File file = new File(outputFile);
		String finalText = inputText + "\n <fbDocs>5</fbDocs> \n <fbTerms>10</fbTerms>\n <fbMu>2500</fbMu> \n <fbOrigWeight>0.5</fbOrigWeight> \n </parameters>";
		try {
			output = new BufferedWriter(new FileWriter(file));
			output.write(finalText);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
