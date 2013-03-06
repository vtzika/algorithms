package main.nl.marktplaats.algorithm;

import java.util.HashMap;
import java.util.List;

import main.nl.marktplaats.utils.SqlCommands;

public class LateDataFussion {

	
	
	public void lateDataFussion() {
		SqlCommands sql = new SqlCommands();
		//HashMap<Long,Long> allPair = getHashMapWithDocAndScore("select query,doc from allResults;");
		List<Long> vips = sql.selectListLong("select distinct(query) from allResults;","Results");
		for(Long query:vips)
		{
			//double newScore=0;
			for (Long doc:sql.selectListLong("select distinct(doc) from allResults where query="+query+";","Results"))
			{
				//HashMap<Long, Double> titleResults = getHashMapWithDocAndScore("select doc,score from titleTfidf where query="+query+" ;");
				//HashMap<Long, Double> descResults = getHashMapWithDocAndScore("select doc,score from descTfidf where query="+query+" ;");
				List<Double> tfidfResults = sql.selectListDouble("select mapWeihtAndAttr from System1Tfidf where query="+query+" and doc="+doc+";");
				List<Double> lmResults = sql.selectListDouble("select mapWeihtAndAttr from SystemLM where query="+query+" and doc="+doc+";");
				List<Double> okapiResults = sql.selectListDouble("select mapWeihtAndAttr from SystemOkapi where query="+query+" and doc="+doc+";");
				List<Double> allResults = tfidfResults;
				allResults.addAll(okapiResults);
				allResults.addAll(lmResults);
				int totalResults = tfidfResults.size()+lmResults.size()+okapiResults.size();
				Double maxScore = getMaxFromList(allResults);
				Double minScore =  getMinFromList(allResults);
				Double sumScore =  getSUMFromList(allResults);
				Double mnzScore = sumScore*totalResults ;
				Double anzScore = sumScore/totalResults ;
				sql.insertQuery("insert into wldfAttr values ("+query+", "+doc+", "+maxScore+", "+minScore+", "+sumScore+", "+mnzScore+", "+anzScore+");","Results");
		}
			
		}
		
}

	private double getResultsFromHash(int t, int l, int o,
			HashMap<Integer, Double> tfidf, HashMap<Integer, Double> lm,
			HashMap<Integer, Double> okapi) {
		double result = 0.0;
		if(tfidf.containsKey(t))
		{
			result+=tfidf.get(t);
		}
		if(lm.containsKey(l))
		{
			result+=lm.get(l);
		}
		if(okapi.containsKey(o))
		{
			result+=okapi.get(o);
		}
		
			
		return result/3;
	}

	private Double getSUMFromList(List<Double> allResults) {
		double total=0.0;
		for(Double score : allResults)
		{
			total+=score;
		}
		return total;
	}

	private Double getMinFromList(List<Double> allResults) {
		double min = allResults.get(0);
		for(Double score : allResults)
		{
			if(score<min)
			{
				min = score;
			}
		}
		return min;
	}

	private Double getMaxFromList(List<Double> allResults) {
		double max = allResults.get(0);
		for(Double score : allResults)
		{
			if(score>max)
			{
				max=score;
			}
		}
		return max;
	}

}
