package main.nl.marktplaats.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import main.nl.marktplaats.configuration;
import main.nl.marktplaats.utils.*;

import lemurproject.indri.QueryEnvironment;
import main.nl.marktplaats.algorithm.ExtendQuery;

public class LLRDisciminativeTermsL2 extends ExtendQuery {

	@Override
	public Query extendQuery() throws Exception{
		
		return calcurateLLRForL2();
		
	}
	
	private Query calcurateLLRForL2() throws Exception {
		SqlCommands sql = new SqlCommands();
		QueryEnvironmentManipulation qenvManipulation = new QueryEnvironmentManipulation();
		QueryEnvironment env = qenvManipulation.add("/home/varvara/workspace/externalSources/indri-5.3/repositoriesL1/"+sql.selectIntQuery("select parent_id from categories where category_id="+super.getQuery().getQString()+";", "cas_ad_service"));
		HashMapsManipulations hashMan = new HashMapsManipulations() ;
		List<Long> docs = hashMan.getMaxCTRFromAds(sql.selectHashMapLongDoubbleQuery("select ad_id,CTR from allL2AndCTR where l2="+super.getQuery().getQID(),"cas_ad_service"));
		LogLikelihoodRatioCalculator llr = new LogLikelihoodRatioCalculator();
		String results = llr.calculateLLR(docs, env);
		return new Query(super.getQuery().getQID(),results);
		//sql.insertQuery("insert into llrPseudoL2 values (3,\""+super.getQuery().getQString()+"\",\""+ results+"\");","");
		}
}
