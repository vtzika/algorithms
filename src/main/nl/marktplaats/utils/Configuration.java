package main.nl.marktplaats.utils;

import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.List;
import lemurproject.indri.QueryEnvironment;
import main.nl.marktplaats.objects.Classified;
import main.nl.marktplaats.objects.Experiment;
import main.nl.marktplaats.objects.IndexedField;

public class Configuration {
	private int queryChoice ;
	private String voyagerQueriesTable;
	private String voyagerResultsTable;
	private String db;
	private String readTable;
	private String inputTable;
	private String system;
	private AobMethod aobMethod;
	private SearchEngine searchEngine;
	private QueryEnvironment queryEnvRepository;
	private String repositoryPath;
	private String readFile;
	private String inputFile;
	private Experiment experiment;
	private String voyagerRequest;
	private String postFixVoyagerRequest;
	private IndexedField indexField;
	private String voyagerResultsFolder;
	private String trecInputFolder;
	private String statisticsTable;
	private String inputMMRTable;
	private MMRMethod mmrMethod;
	private RetrievalMethod retrievalMethod;
	private String parameterFileDirectory;
	private String indriPath;
	private String VIPFolder;
	private String classifiedFile;
	private String indriResultsFolder;
	private String IndriResultsTable;
	private String pathToDisk;

	public void setDB(String database) {

		this.setDb(database);
	}

	public String getInputTable() {
		return inputTable;
	}

	public void setInputTable(String inputTable) {
		this.inputTable = inputTable;
	}

	public String getReadTable() {
		return readTable;
	}

	public void setReadTable(String readTable) {
		this.readTable = readTable;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public AobMethod getAobMethod() {
		return aobMethod;
	}

	public void setAobMethod(AobMethod aobMethod) {
		this.aobMethod = aobMethod;
	}

	public void setSearchEngine(SearchEngine se) {
		this.searchEngine = se;
	}

	public SearchEngine getSearchEngine() {
		return this.searchEngine;
	}

	public String getSystem() {
		return this.system;
	}

	public void setSystem(String s) {
		this.system = s;
	}

	public QueryEnvironment getQueryEnvRepository() {
		return queryEnvRepository;
	}

	public void setQueryEnvRepository(String rep) throws Exception {
		QueryEnvironmentManipulation envMan = new QueryEnvironmentManipulation();
		QueryEnvironment env = envMan.add(rep);
		this.queryEnvRepository = env;
		this.repositoryPath = rep;
	}

	public String getRepositoryPath() {
		return this.repositoryPath;
	}

	public String getReadFile() {
		return this.readFile;
	}

	public void setReadFile(String results) {
		this.readFile = results;
	}

	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public void setExperiment(Experiment exp) {
		this.experiment = exp;
	}

	public Experiment getExperiment() {
		return this.experiment;
	}

	public void setVoyagerRequest(String request) {
		this.voyagerRequest = request;
	}

	public String getVoyagerRequest() {
		return this.voyagerRequest;
	}

	public void setPostFixVoyagerRequest(String request) {
		this.postFixVoyagerRequest = request;
	}

	public String getPostFixVoyagerRequest() {
		return this.postFixVoyagerRequest;
	}

	public void setIndexField(IndexedField index) {
		this.indexField = index;
	}

	public IndexedField getIndexField() {
		return this.indexField;
	}

	public String getVoyagerResultsFolder() {
		return this.voyagerResultsFolder;
	}

	public void setVoyagerResultsFolder(String folder) {
		this.voyagerResultsFolder = folder;
	}

	public String getVoyagerResultsTable() {
		return this.voyagerResultsTable;
	}

	public void setVoyagerResultsTable(String table) {
		this.voyagerResultsTable = table;
	}

	public void setMMRTable(String mmrTable) {
		this.inputMMRTable = mmrTable;
	}

	public String getMMRTable() {
		return this.inputMMRTable;
	}

	private boolean checkIfHasStringValue(String fieldsName, String fieldsValue) {
		if (fieldsValue == null)
			System.out.println(fieldsName + " needs to be configured");
		else {
			System.out.println(fieldsName + " OK");
			return true;
		}
		return false;
	}

	public String getVoyagerQueriesTable() {
		return this.voyagerQueriesTable;
	}

	public void setVoyagerQueriesTable(String table) {
		this.voyagerQueriesTable = table;
	}

	public String getTrecInputFolder() {
		return this.trecInputFolder;
	}

	public void setTrecInputFolder(String f) {
		this.trecInputFolder = f;
	}

	public void setStatisticsTable(String s) {
		this.statisticsTable = s;
	}

	public String getStatisticsTable() {
		return this.statisticsTable;
	}

	public boolean checkVoyagerConfiguration() {
		boolean a = checkIfHasStringValue("DB ", this.db);
		boolean b = checkIfHasStringValue("ReadTable ", this.readTable);
		boolean c = checkIfHasStringValue("voyagerQueriesTable ",
				this.voyagerQueriesTable);
		boolean d = checkIfHasStringValue("IndexField ",
				this.indexField.toString());
		boolean e = checkIfHasStringValue("VoyagerResultsFolder ",
				this.voyagerResultsFolder);
		boolean f = checkIfHasStringValue("VoyagerResultsTable ",
				this.voyagerResultsTable);
		boolean g = checkIfHasStringValue("trecInputFolder ",
				this.trecInputFolder);
		boolean h = checkIfHasStringValue("statisticsTable  ",
				this.statisticsTable);

		if (a && b && c && d && e && f && g && h)
			return true;
		else
			return false;

	}

	public boolean checkAobConfiguration() {
		boolean a = checkIfHasStringValue("DB ", this.db);
		boolean b = checkIfHasStringValue("ReadTable ", this.readTable);
		boolean d = checkIfHasStringValue("InputTable ", this.inputTable);

		if (a && b && d)
			return true;
		else
			return false;
	}

	public boolean checkMMRConfiguration() {
		boolean a = checkIfHasStringValue("DB ", this.db);
		boolean b = checkIfHasStringValue("ReadTable ", this.readTable);
		boolean d = checkIfHasStringValue("inputMMRTable ", this.inputMMRTable);

		if (a && b && d)
			return true;
		else
			return false;
	}

	public MMRMethod getMmrMethod() {
		return mmrMethod;
	}

	public void setMmrMethod(MMRMethod method) {
		this.mmrMethod = method;
	}

	public boolean checkSynonymsConfiguration() {
		// TODO Auto-generated method stub
		boolean a = checkIfHasStringValue("DB ", this.db);
		boolean b = checkIfHasStringValue("ReadTable ", this.readTable);
		if (a && b)
			return true;
		else
			return false;
	}

	public boolean checkIndriConfiguration() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setRetrievalMethod(RetrievalMethod method) {
		this.retrievalMethod = method;
	}

	public RetrievalMethod getRetrievalMethod() {
		return this.retrievalMethod;
	}

	public String[] getRepositories() {
		FileNegotiations fileNego = new FileNegotiations();
		return fileNego.getListFiles(this.getRepositoryPath());
	}

	public String getParameterFilesDirectory() {
		return this.parameterFileDirectory;
	}

	public void setParameterFilesDirectory(String parameterFile) {
		this.parameterFileDirectory = parameterFile;
	}

	public String getIndriPath() {
		return this.indriPath;
	}

	public void setIndriPath(String path) {
		this.indriPath = path;
	}

	public String getSGMLFolder() {
		return this.VIPFolder;
	}

	public void setSGMLFolder(String folder) {
		this.VIPFolder = folder;
	}

	public void setClassifiedFile(String file) {
		this.classifiedFile = file;
	}
	public String getClassifiedFile() {
		return this.classifiedFile;
	}

	public String setClassifiedForQuerying(Classified classified) {
				// 1:all title words
				// 2:all description
				// 3:same words in title and description
				// 4:words in title or description
				// 5:words in the title and almost the same or greater Price
				// 6:words in the description and almost the same or greater Price
				// 7:same words in the description and title and almost the same or
				// greater Price
				// 8:words in title and in description and almost the same or greater
				// TODO Price
				// 9:words in the category or in attributes
				// 10:entire doc
				Queries q = new Queries(classified);
				int choice=this.queryChoice;
				String query1 = q.getStringQuery(classified.getTitle().getTitleWords());
				List<String> query1Terms =new ArrayList<String>();
				for(String term:query1.split(" "))
				{
					query1Terms.add(term);
				}
			
				String query2 = q.getStringQuery(classified.getDescription()
						.getDescriptionWords());
				List<String> query2Terms =new ArrayList<String>();
				for(String term:query2.split(" "))
				{
					query2Terms.add(term);
				}
				String query3 = q.getStringQuery(q.getSameWordsOfTwoLists(classified
						.getTitle().getTitleWords(), classified.getDescription().getDescriptionWords()));
				List<String> query3Terms = q.getSameWordsOfTwoLists(classified
						.getTitle().getTitleWords(), classified.getDescription().getDescriptionWords());
				
				String query4 = q.getStringQuery(q.getCombinedLists(classified
						.getTitle().getTitleWords(), classified.getDescription()
						.getDescriptionWords()));
				List<String> query4Terms =q.getCombinedLists(classified
						.getTitle().getTitleWords(), classified.getDescription()
						.getDescriptionWords());
				
				String query5 = q.combineQueries(newArrayList(q.queryGreaterPrice(),
						query1));
			
						
				String query6 = q.combineQueries(newArrayList(q.queryGreaterPrice(),
						query2));
				String query7 = q.combineQueries(newArrayList(q.queryGreaterPrice(),query3));
				String query8 = q.combineQueries(newArrayList(q.queryGreaterPrice(),
						query4));
				
				
				String query9 = q.getStringQuery(q.getCombinedLists(classified.getCategory().getCategoryWords(),classified.getAttributes().getAttributesWords())); 
				List<String> query9Terms =new ArrayList<String>();
				for(String term:query9.split(" "))
				{
					query9Terms.add(term);
				}
				
				String query10 = q.combineQueries(newArrayList(query9, query4));
				List<String> query10Terms =new ArrayList<String>();
				for(String term:query10.split(" "))
				{
					query10Terms.add(term);
				}
				
				
				//List<String> discrTerms = removeDuplicatesAndLengthSmall(query1Terms);
						
				//DescriptiveTerms descripTerms = new DescriptiveTerms(discrTerms);
				//String query11 = q.getStringQuery(descripTerms.choose10Terms());
				
				//List<String> query11Terms = descripTerms.choose10Terms();

				String query;
				List<String> queryTerms;
				switch (choice) {
				case 1:
					query = query1;
					queryTerms=query1Terms;
					break;
				case 2:
					query = query2;
					queryTerms=query2Terms;
					break;
				case 3:
					query = query3;
					queryTerms=query3Terms;
					break;
				case 4:
					query = query4;
					queryTerms=query4Terms;
					break;
				case 5:
					query = query5;
					break;
				case 6:
					query = query6;
					break;
				case 7:
					query = query7;
					break;
				case 8:
					query = query8;
					break;
				case 9:
					query = query9;
					queryTerms = query9Terms;
					break;
				case 10:
					query = query10;
					queryTerms = query10Terms;
					break;
				/*case 11:
					query = query11;
					queryTerms = query11Terms;
					break;
					*/
				default:
					throw new RuntimeException("Cannot create query with number " + choice);
				}
				return query;
	}

	public String getIndriResultsFolder() {
		return this.indriResultsFolder;
	}
	public void setIndriResultsFolder(String folder) {
		this.indriResultsFolder = folder;
	}

	public String getIndriScoresInputTable() {
		return this.IndriResultsTable;
	}
	public void setIndriScoresInputTable(String table) {
		this.IndriResultsTable = table;
	}

	public void setLocalPathToExternalDisk(String path) {
		this.pathToDisk = path;
	}
	public String getLocalPathToExternalDisk() {
		return this.pathToDisk;
	}
}
