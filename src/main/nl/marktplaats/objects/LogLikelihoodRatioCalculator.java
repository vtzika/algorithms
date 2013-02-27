package main.nl.marktplaats.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

import javax.management.Query;


import lemurproject.indri.IndexEnvironment;
import lemurproject.indri.QueryEnvironment;
import main.nl.marktplaats.utils.HashMapsManipulations;


public class LogLikelihoodRatioCalculator {

	private Classified classified1;

	public LogLikelihoodRatioCalculator(Classified c1) {
		this.classified1 = c1;
	}

	public LogLikelihoodRatioCalculator() {
		// TODO Auto-generated constructor stub
	}

	public Float LLRCalculate(String term, HashMap<String, Long> corpora,
			QueryEnvironment env) throws Exception {
		

		Long a = corpora.get(term);
		long b = env.termCount(term);
		long c = getCorporaSumOfFreq(corpora) - a;
		long d = env.termCount() - c;
		return calculateLLR(a, b, c, d);
	}

	private long getCorporaSumOfFreq(HashMap<String, Long> corpora) {
		long totalFreq = 0;
		for (Entry<String, Long> entry : corpora.entrySet()) {
			totalFreq = totalFreq + entry.getValue();
		}
		return totalFreq;
	}

	private HashMap<String, Integer> getermsFequencies(String docText) {
		HashMap<String, Integer> termsFreq = new HashMap<String, Integer>();
		for (String term : docText.split(" ")) {
			if (termsFreq.containsKey(term)) {
				int freq = termsFreq.get(term) + 1;
				termsFreq.put(term, freq);
			} else {
				termsFreq.put(term, 1);
			}
		}
		return termsFreq;
	}

	// orderLLR(wordsLLR1);

	public String orderLLR(Map<String, Float> wordsLLR1) {
		System.out.println("size : "+wordsLLR1.size());
		String returned = "";
		System.out.println("NEW QUERY");
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
	
	
	
	public String calculateLLR(List<Long> docs,QueryEnvironment env) throws Exception {
		HashMapsManipulations hashMan = new HashMapsManipulations();
		HashMap<String,Long> termsFreq = hashMan.gatherTermsAndFrequencies(docs);
		HashMap<String,Float> llrResults = new HashMap<String, Float>();
		for (Entry<String,Long> term_freq:termsFreq.entrySet())
		{
			llrResults.put(term_freq.getKey().toString(),this.LLRCalculate(term_freq.getKey().toString(),termsFreq, env));
		}
		return hashMan.orderLLR(llrResults);
	}
	

}
