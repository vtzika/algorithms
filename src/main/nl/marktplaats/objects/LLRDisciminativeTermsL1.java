package main.nl.marktplaats.objects;

import java.util.List;
import main.nl.marktplaats.utils.*;
import lemurproject.indri.QueryEnvironment;
import main.nl.marktplaats.algorithm.ExtendQuery;
import main.nl.marktplaats.algorithm.LogLikelihoodRatioCalculator;

public class LLRDisciminativeTermsL1 extends ExtendQuery {

	@Override
	public Query extendQuery() throws Exception{
		
		return calcurateLLRForL1();
		
	}
	
	
	private Query calcurateLLRForL1() throws Exception {
			SqlCommands sql = new SqlCommands();
			QueryEnvironmentManipulation qenvManipulation = new QueryEnvironmentManipulation();
			QueryEnvironment env = qenvManipulation.add("/home/varvara/workspace/externalSources/indri-5.3/repositoriesEntire");
			HashMapsManipulations hashMan = new HashMapsManipulations() ;
			List<Long> docs = hashMan.getMaxCTRFromAds(sql.selectHashMapLongDoubbleQuery("select ad_id,CTR from allL2AndCTR where l1="+super.getQuery().getQID(),"cas_ad_service"));
			LogLikelihoodRatioCalculator llr = new LogLikelihoodRatioCalculator();
			String results = llr.calculateLLR(docs, env);
			return new Query(super.getQuery().getQID(),results);
			//sql.insertQuery("insert into llrPseudoL1 values (3,\""+super.getQuery().getQString()+"\",\""+ results+"\");","");
		}

}
