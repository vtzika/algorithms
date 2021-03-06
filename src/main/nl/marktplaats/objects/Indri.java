package main.nl.marktplaats.objects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map.Entry;

import main.nl.marktplaats.utils.ClassifiedParser;
import main.nl.marktplaats.utils.Configuration;
import main.nl.marktplaats.utils.FileNegotiations;
import main.nl.marktplaats.utils.ParameterFileCreator;
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
		System.out.println("Reading VIPs of folder : "+ configuration.getSGMLFolder());
		for (String visitedClassified:fileNego.getListFiles(configuration.getSGMLFolder()))
		{	
			configuration.setClassifiedFile(configuration.getSGMLFolder()+"/"+visitedClassified);
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
		HashMap<String, String> queries = sql.selectHashMapStringStringQuery("select doc,query from "+configuration.getReadTable()+" where experiment="+configuration.getSystem()+";", configuration.getDb());
		return queries;
	}
	public void runIndriQueries() throws IOException {
		System.out.println("Running Queries.....");
		FileNegotiations fileNeg = new FileNegotiations();
		for(String paramFile : fileNeg.getListFiles(configuration.getParameterFilesDirectory()))
		{
			String results = "";
			Process process = new ProcessBuilder(configuration.getIndriPath()+"runquery/IndriRunQuery", configuration.getParameterFilesDirectory()+"/"+paramFile).start();
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			while ((br.readLine()) != null) {
				results+=br.readLine()+"\n";
			}
			fileNeg.createFile(results, configuration.getIndriResultsFolder()+"/"+paramFile);
		}
		System.out.println("Files with results are saved on : "+configuration.getIndriResultsFolder());
		}

	public void saveIndriResultsToTable() {
		System.out.println("Saving Results on table:"+configuration.getIndriScoresInputTable());
		FileNegotiations f = new FileNegotiations(); 
		String file = configuration.getIndriResultsFolder();
		for (String query:f.getListFiles(file))
		{	
			System.out.println(query);
			f.getResultsFromTxtAndSaveInDB(file+"/"+query,configuration.getIndriScoresInputTable(), configuration.getDb(), ""+configuration.getQueryChoice(), configuration.getSystem());
		}
		System.out.println("Results saved on : "+configuration.getIndriScoresInputTable());
	}

	public void gatherStatistics() {
		boolean a = configuration.checkIfHasStringValue("Statistics table : ",configuration.getStatisticsTable());
		if(a)
		{
			Statistics stat = new Statistics(configuration);
			stat.gatherStatistics();
		}
	}

	public void createParameterFiles(HashMap<String, String> queries) {
		ParameterFileCreator parameterFile;
		System.out.println("Creating Parameter Files....");
		for(Entry<String, String> docNoAndQueries : queries.entrySet())
		{
			String docno = docNoAndQueries.getKey().replaceAll(".sgml", "");
			parameterFile = new ParameterFileCreator(configuration, docNoAndQueries.getValue(), docno );
			parameterFile.createFile();
		}
		System.out.println("Parameter files are saved on : "+configuration.getParameterFilesDirectory());		
	}

}
