package main.nl.marktplaats.objects;

import java.util.List;

import main.nl.marktplaats.utils.*;

import lemurproject.indri.QueryEnvironment;
import main.nl.marktplaats.algorithm.ExtendQuery;
import main.nl.marktplaats.algorithm.LogLikelihoodRatioCalculator;

public class LLRDisciminativeTermsL2 extends ExtendQuery {

	public LLRDisciminativeTermsL2(Long id, String qString, Configuration configuration) {
		super.setQueries(id, qString, configuration);
	}

	@Override
	public Query extendQuery() throws Exception{
		
		return calcurateLLRForL2();
		
	}
	
	private Query calcurateLLRForL2() throws Exception {
		SqlCommands sql = new SqlCommands();
		QueryEnvironment env = new QueryEnvironment();
		env.addIndex("/home/varvara/workspace/repositories/repositoriesL1/"+sql.selectIntQuery("select parent_id from categories where category_id="+super.getQuery().getQID()+";", "cas_ads")+"/");
		HashMapsManipulations hashMan = new HashMapsManipulations() ;
		List<Long> docs = hashMan.getMaxCTRFromAds(sql.selectHashMapLongDoubbleQuery("select id,CTR from allL2AndCTR where l2="+super.getQuery().getQID(),"cas_ads"));
		LogLikelihoodRatioCalculator llr = new LogLikelihoodRatioCalculator();
		String results = llr.calculateLLRDocsList(docs, env);
		System.out.println(results);
		return new Query(super.getQuery().getQID(),results);
		}

}
