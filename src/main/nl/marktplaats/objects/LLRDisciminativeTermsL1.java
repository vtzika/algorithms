package main.nl.marktplaats.objects;

import java.util.HashMap;
import java.util.List;
import main.nl.marktplaats.utils.*;
import lemurproject.indri.QueryEnvironment;
import main.nl.marktplaats.algorithm.ExtendQuery;
import main.nl.marktplaats.algorithm.LogLikelihoodRatioCalculator;

public class LLRDisciminativeTermsL1 extends ExtendQuery {

	public LLRDisciminativeTermsL1(Long id, String qString, Configuration c) {
		super.setQueries(id, qString, c);
	}

	@Override
	public Query extendQuery() throws Exception{
		return calcurateLLRForL1();		
	}
	
	private Query calcurateLLRForL1() throws Exception {
			SqlCommands sql = new SqlCommands();
			HashMapsManipulations hashMan = new HashMapsManipulations() ;
			List<Long> docs = getMaxCTRAdsFromL1();
			Configuration a = super.getConfiguration();
			LogLikelihoodRatioCalculator llr = new LogLikelihoodRatioCalculator();
			String results = llr.calculateLLRDocsList(docs, super.getConfiguration().getQueryEnvRepository());
			return new Query(super.getQuery().getQID(),results);
			//sql.insertQuery("insert into llrPseudoL1 values (3,\""+super.getQuery().getQString()+"\",\""+ results+"\");","");
			}


	private List<Long> getMaxCTRAdsFromL1() {
		SqlCommands sql = new SqlCommands();
		HashMapsManipulations hashMan = new HashMapsManipulations();
		HashMap<Long, Double> adsAndCTR = new HashMap<Long, Double>();
		for(int l2 : sql.selectListInt("select category_id from categories where parent_id="+super.getQuery().getQID()+";", "cas_ads"))
		{
			for(long ad : sql.selectListLong("select id from ads where category_id="+l2+";", "cas_ads"))
			{
				adsAndCTR.putAll(sql.selectHashMapLongDoubbleQuery("select id,CTR from allL2AndCTR where id="+ad+" and l2="+l2+";","cas_ads"));
			}
		}
		return hashMan.getMaxCTRFromAds(adsAndCTR);
		
	}

}
