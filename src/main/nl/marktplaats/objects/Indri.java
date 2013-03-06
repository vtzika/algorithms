package main.nl.marktplaats.objects;

import java.util.HashMap;

import main.nl.marktplaats.utils.ClassifiedParser;
import main.nl.marktplaats.utils.Configuration;
import main.nl.marktplaats.utils.FileNegotiations;
import main.nl.marktplaats.utils.RetrievalMethod;
import main.nl.marktplaats.utils.SqlCommands;
import main.nl.marktplaats.utils.Statistics;
public class Indri {

	private Configuration configuration;

	public Indri(Configuration configuration) {
		this.configuration = configuration;
	}

	public HashMap<String, String> createQueryFromSGMLIndriQueryLanguage() {
		HashMap<String, String> queries = new HashMap<String, String>();
		FileNegotiations fileNego = new FileNegotiations();
		System.out.println("Reading VIPs of folder : "+ fileNego.getListFiles(configuration.getSGMLFolder()));
		for (String visitedClassified:fileNego.getListFiles(configuration.getSGMLFolder()))
		{	
			configuration.setClassifiedFile(configuration.getSGMLFolder()+visitedClassified);
			queries.put(visitedClassified, createQuery(configuration));
		}
		return queries;
	}
	
	private String createQuery(Configuration configuration2) {
		ClassifiedParser classifiedParser = new ClassifiedParser();
		classifiedParser.setClassified(configuration.getClassifiedFile());
		String query = configuration.setClassifiedForQuerying(classifiedParser.getClassified());
		return query;
	}

	public HashMap<String, String> createQueryFromTableIndriQueryLanguage() {
		SqlCommands sql = new SqlCommands();
		System.out.println("Reading queries of table "+configuration.getReadTable());
		HashMap<String, String> queries = sql.selectHashMapStringStringQuery("select doc,query from "+configuration.getReadTable()+";", configuration.getDb());
		return queries;
	}
	public void runIndriQueries() {
		// TODO Auto-generated method stub
		RetrievalMethod retrievalMethod = this.configuration.getRetrievalMethod();
		switch(retrievalMethod){
		case Okapi:
			System.out.println("Okapi");
			break;
		case Tfidf:
			System.out.println("TfIdf");
			break;
		default :
			break;
		}
	}

	public void saveIndriResultsToTable() {
		FileNegotiations f = new FileNegotiations(); 
		String file = configuration.getIndriResultsFolder();
		for (String query:f.getListFiles(file))
		{	
			f.getResultsFromTxtAndSaveInDB(file+"/"+query,configuration.getIndriScoresInputTable(), configuration.getDb());
		}
	}

	public void gatherStatistics() {
		Statistics stat = new Statistics(configuration);
		stat.gatherStatistics();
	}

}
