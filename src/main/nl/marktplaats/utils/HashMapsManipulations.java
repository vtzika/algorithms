package main.nl.marktplaats.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class HashMapsManipulations {

	
	
	public List<Long> getMaxCTRFromAds(HashMap<Long, Double> adWithCTR) {
		List<Long> returned = new ArrayList<Long>();
		TreeMap<Long, Double> sorted_map = new TreeMap();
		for (Map.Entry<Long, Double> pair : adWithCTR.entrySet()) {
			if(!pair.getValue().isNaN())
			{
				sorted_map.put(pair.getKey(), pair.getValue());
			}
		}
		for (int i = 0; i < 5; i++) {
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
				returned.add(maxKey);
				sorted_map.remove(maxKey);
			}
			}
			return returned;
	}
	
	public long getCorporaSumOfFreq(HashMap<String, Long> corpora) {
		long totalFreq = 0;
		for (Entry<String, Long> entry : corpora.entrySet()) {
			totalFreq = totalFreq + entry.getValue();
		}
		return totalFreq;
	}
	
	
	
	
	public HashMap<String, Integer> gatherTermsAndFrequencies(List<Long> docs) {
		HashMap<String, Integer> term_freq = new HashMap<String, Integer>();
		for(Long doc:docs)
		{	
				SqlCommands sql = new SqlCommands();
				String text = sql.selectStringQuery("select title,description from ads where id="+doc+";","cas_ads");
				//StringManipulation stringManipulation = new StringManipulation();
				//text = stringManipulation.sanitizeString(text);
				
				for(String t:text.split(" "))
				{
					String term = t.toLowerCase();
					if(term_freq.containsKey(term))
					{
						int freq = term_freq.get(term)+1;
						term_freq.put(term, freq);
					}
					else
						term_freq.put(term, 1);	
				}
			}
		return term_freq;
	}
	
	

	public String orderLLR(Map<String, Float> wordsLLR1) {
		String returned = "";
		TreeMap<String, Float> sorted_map = new TreeMap();
		for (Map.Entry<String, Float> pair : wordsLLR1.entrySet()) {
			if(!pair.getValue().isNaN())
			{
				sorted_map.put(pair.getKey(), pair.getValue());
			}
		}
		for (int i = 1; i <= 10; i++) {
			if(sorted_map.size()>0)
			{
				float max = sorted_map.firstEntry().getValue();
				String maxKey = sorted_map.firstEntry().getKey();
				
				for (String key : sorted_map.keySet()) {
					if (sorted_map.get(key) > max) {
							max = sorted_map.get(key);
							maxKey = key;
						}

				}
				returned+=maxKey+" ";
				sorted_map.remove(maxKey);
			}

			}
			return returned;
	}
	
	
	private TreeMap<Double,Long> orderHashMap(Map<Long, Double> unorderedHashMap) {
		TreeMap<Double, Long> sorted_map = new TreeMap<Double,Long>();
		for (Entry<Long, Double> pair : unorderedHashMap.entrySet()) {
			sorted_map.put(pair.getValue(),pair.getKey());
		}
		return sorted_map;

	}


	public int compare(Map.Entry<String, Float> e1, Map.Entry<String, Float> e2) {
		if (e1.getValue() < e2.getValue()) {
			return 1;
		} else if (e1.getValue() == e2.getValue()) {
			return 0;
		} else {
			return -1;
		}
	}

	public Float calculateLLR(Long a, long b, long c, long d) {
		float E1 = (float) (c * (a + b)) / (c + d);
		float E2 = (float) (d * (a + b)) / (c + d);
		float LLR = (float) (2 * ((a * Math.log(a / E1)) + (b * Math
				.log(b / E2))));

		return LLR;
	}

	public HashMap<String, Integer> gatherTermsAndFrequenciesByString(String query) {
	HashMap<String, Integer> term_freq = new HashMap<String, Integer>();
		for(String t:query.split(" "))
		{
			String term = t.toLowerCase();
			if(term_freq.containsKey(term))
			{
				int freq = term_freq.get(term)+1;
				term_freq.put(term, freq);				
			}
			else
				term_freq.put(term,  1);	
		}
		return term_freq;
	}
}
