package main.nl.marktplaats.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import main.nl.marktplaats.utils.*;

import lemurproject.indri.QueryEnvironment;
import main.nl.marktplaats.algorithm.ExtendQuery;

public class LLRDisciminativeTerms extends ExtendQuery {

	public void extend() {
	Query query = super.getQuery();
	
	//TODO extend LLR	
	}
	
	
	private void calcurateLLRForL1() throws Exception {
		SqlCommands sql = new SqlCommands();
			Query query = new Query();
			QueryEnvironment env = new QueryEnvironment();	
			int l1 = sql.selectIntQuery("select parent_id from categories where category_id="+query.getQString()+";", "cas_ad_service");
			env.addIndex("/home/varvara/workspace/externalSources/indri-5.3/repositoriesL1/"+l1);
			String table ="";
			HashMap<String,Float> llrResults = new HashMap<String,Float>();
			List<Long> docs = sql.selectListLong("select distinct(doc) from "+table+" where sequence<=5 and query="+query.getQString()+";","");
			HashMap<String,Long> termsFreq =gatherTermsAndFrequencies(docs);
			//getRelatedIds();
			for (Entry<String,Long> term_freq:termsFreq.entrySet())
			{
				llrResults.put(term_freq.getKey().toString(),this.LLRCalculate(term_freq.getKey().toString(),termsFreq, env));
			}
			String results = this.orderLLR(llrResults);
			sql.insertQuery("insert into llrPseudoL1 values (3,\""+query.getQString()+"\",\""+ results+"\");","");
		}
		
	
	
	private HashMap<String, Long> gatherTermsAndFrequencies(List<Long> docs) {
		HashMap<String, Long> term_freq = new HashMap<String, Long>();
		for(Long doc:docs)
			try {
				{
					SqlCommands sql = new SqlCommands();
					String text = sql.selectStringQuery("select title,description from ads where id="+doc+";","cas_ad_service");
					StringManipulation stringManipulation = new StringManipulation();
					text = stringManipulation.sanitizeString(text);
					for(String t:text.split(" "))
					{
						String term = t.toLowerCase();
						if(term_freq.containsKey(term))
						{
							Long freq = term_freq.get(term)+1;
							term_freq.put(term, freq);				
						}
						else
							term_freq.put(term, (long) 1);	
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return term_freq;
	}


	public Float LLRCalculate(String term, HashMap<String, Long> corpora,
			QueryEnvironment env) throws Exception {
		// SqlCommands sql = new SqlCommands();
		// String docText =
		// sql.selectQuery("select title,description from ads where id="+ad_id+";");
		// HashMap<String,Integer> termsFrequencies =
		// this.getermsFequencies(docText);

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

	private HashMap<String, Integer> getermsFequenciesFromString(String docText) {
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
	

}
