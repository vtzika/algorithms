package main.nl.marktplaats;

import java.util.HashMap;
import java.util.Map.Entry;

import main.nl.marktplaats.algorithm.ExtendQuery;
import main.nl.marktplaats.objects.analyticsL1Extention;
import main.nl.marktplaats.objects.analyticsL2Extention;
import main.nl.marktplaats.utils.AobMethod;
import main.nl.marktplaats.utils.Configuration;
import main.nl.marktplaats.utils.SearchEngine;
import main.nl.marktplaats.utils.SqlCommands;

public class Main {

	/**
	 * @param args
	 */
	private static Configuration createConfiguration() {
		Configuration configuration = new Configuration();
		configuration.setDB("aob");
		configuration.setReadTable("l2AnalyticsQueries");
		configuration.setInputTable("");
		configuration.setAobMethod(AobMethod.AnalyticsL1);
		configuration.setSearchEngine(SearchEngine.Voyager);
		return configuration;
	}	
	
	public static void main(String[] args) {
		Configuration configuaration = createConfiguration();
		aob(configuaration);

	}
	private static void aob(Configuration configuration) {
		try{
			configuration.getReadTable();
			SqlCommands sql = new SqlCommands();
			HashMap<Long,String> queries = sql.selectHashMapQuery("select l2,query from "+configuration.getReadTable()+";",configuration.getDb());
			for(Entry<Long, String> query: queries.entrySet())
			{	
				ExtendQuery newQuery = getExtendQueryType(configuration, query.getKey(), query.getValue());
				newQuery.saveResults(configuration);

			}
			}
		catch (Exception e) {
			System.out.println(e);
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
			newQuery = new analyticsL1Extention(id, qString);				
			break;
		case AnalyticsL2:
			newQuery = new analyticsL2Extention(id, qString);
			break;
		case PseudoL1:
			newQuery = new PseudoL1Extention(id, qString);
			break;
		case PseudoL2:
			newQuery = new PseudoL2Extention(id, qString);
			break;
		default:
			newQuery = new ExtendQuery() {};
			System.out.println("Please, give me a correct Aob method for extention :D !!!!!");
			break;
		}
		
		return newQuery;
	}
	

}
