package main.nl.marktplaats;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import lemurproject.indri.QueryEnvironment;
import main.nl.marktplaats.algorithm.ExtendQuery;
import main.nl.marktplaats.algorithm.LogLikelihoodRatioCalculator;
import main.nl.marktplaats.objects.LLRDisciminativeTermsL1;
import main.nl.marktplaats.objects.LLRDisciminativeTermsL2;
import main.nl.marktplaats.objects.PseudoL1Extention;
import main.nl.marktplaats.objects.PseudoL2Extention;
import main.nl.marktplaats.objects.Query;
import main.nl.marktplaats.objects.analyticsL1Extention;
import main.nl.marktplaats.objects.analyticsL2Extention;
import main.nl.marktplaats.utils.AobMethod;
import main.nl.marktplaats.utils.Configuration;
import main.nl.marktplaats.utils.HashMapsManipulations;
import main.nl.marktplaats.utils.QueryEnvironmentManipulation;
import main.nl.marktplaats.utils.SearchEngine;
import main.nl.marktplaats.utils.SqlCommands;

public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	private static Configuration createConfiguration() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setDB("tests");
		configuration.setReadTable("l2Queries");
		configuration.setInputTable("l2ExtQueries");
		configuration.setSearchEngine(SearchEngine.Voyager);
		configuration.setAobMethod(AobMethod.llrL1);
		configuration.setQueryEnvRepository("/home/varvara/workspace/repositories/repositoriesEntireDoc/");
		return configuration;
	}	
	
	public static void main(String[] args) throws Exception {
		Configuration configuration = createConfiguration();
		aob(configuration);

	}
	private static void llr(Configuration configuration) throws Exception {
		SqlCommands sql = new SqlCommands();
		HashMap<Long, String> docsQueries = sql.selectHashMapLongStringQuery("select doc,query from queries",configuration.getDb());
		LogLikelihoodRatioCalculator llr = new LogLikelihoodRatioCalculator();
		for(Entry<Long,String> docQuery : docsQueries.entrySet())
		{
			String results = llr.calculateLLRForDoc(docQuery, configuration.getQueryEnvRepository());
			System.out.println("QUERY:"+docQuery.getValue()+" \n NEW QUERY: "+results);
			//sql.insertQuery("insert into llrQueries values("+docQuery.getKey()+",'"+results+"')", configuration.getDb());
		}
		System.out.println("What Do you want to with the new Queries?");
	}

	private static void aob(Configuration configuration) throws Exception {
			configuration.getReadTable();
			SqlCommands sql = new SqlCommands();
			HashMap<Long,String> queries = sql.selectHashMapQuery("select l2,query from "+configuration.getReadTable()+";",configuration.getDb());
			for(Entry<Long, String> query: queries.entrySet())
			{	
				ExtendQuery newQuery = getExtendQueryType(configuration, query.getKey(), query.getValue());
				newQuery.saveResults(configuration);
			}
	}

	private static ExtendQuery getExtendQueryType(Configuration configuration, Long id, String qString) throws Exception {
		ExtendQuery newQuery;
		if(configuration.getAobMethod()==null)
		{
			System.out.println("Please, give me Aob method for extention :D !!!!!");
			return null;
		}
		switch (configuration.getAobMethod()) {
		case AnalyticsL1:
			newQuery = new analyticsL1Extention(id, qString, configuration);				
			break;
		case AnalyticsL2:
			newQuery = new analyticsL2Extention(id, qString, configuration);
			break;
		case PseudoL1:
			newQuery = new PseudoL1Extention(id, qString, configuration);
			break;
		case PseudoL2:
			newQuery = new PseudoL2Extention(id, qString, configuration);
			break;
		case llrL1:
			newQuery = new LLRDisciminativeTermsL1(id, qString, configuration);
			break;
		case llrL2:
			newQuery = new LLRDisciminativeTermsL2(id, qString, configuration);
			break;
		default:
			newQuery = new ExtendQuery() {};
			System.out.println("Please, give me a correct Aob method for extention :D !!!!!");
			break;
		}
		
		return newQuery;
	}
	

}
