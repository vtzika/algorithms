package main.nl.marktplaats;

import java.io.IOException;
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
import main.nl.marktplaats.objects.Voyager;
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
		configuration.setReadTable("queries");
		configuration.setInputTable("voyRequests");
		configuration.setSearchEngine(SearchEngine.Voyager);
		configuration.setExperiment(Experiment.Aob);
		configuration.setAobMethod(AobMethod.PseudoL2);
		configuration.setQueryEnvRepository("/home/varvara/workspace/repositories/repositoriesL1/");
		configuration.setReadFile("");
		configuration.setIndexField(IndexedField.Title);
		configuration.setVoyagerRequest("http://10.249.123.123:4242/query?Qy=");
		configuration.setPostFixVoyagerRequest("&Fl=AD_ID&Rk=1&Nr=1000&Sk=0&Hx=no");
		return configuration;
	}	
	
	public static void main(String[] args) throws Exception {
		Configuration configuration = createConfiguration();
		//runExperiment(configuration);
		voyagerExperiments(configuration);

	}
	private static void runExperiment(Configuration configuration) throws Exception {
		Experiment experiment = configuration.getExperiment();
		switch (experiment) {
		case Aob:
			aob(configuration);
			break;
		case TopSearch:
			System.out.println("TopSearch");
			break;
		case SimilarItems:
			aob(configuration);
			break;
		case Diversification:
			System.out.println("Diversification");
			break;
		case Synonyms:
			System.out.println("Synonyms");
			break;
		default:
			break;
		}
		
	}

	private static void voyagerExperiments(Configuration configuration) throws IOException {
		Voyager voyager = new Voyager(configuration);
		voyager.createQueryVoyagerQueryLanguage();
		voyager.runVoyagerQueries();
		voyager.saveVoyagerResultsToTable();
		voyager.createInputFiles();
		
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
			SqlCommands sql = new SqlCommands();
			HashMap<Long,String> queries = sql.selectHashMapQuery("select id,query from "+configuration.getReadTable()+";",configuration.getDb());
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
