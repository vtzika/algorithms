package main.nl.marktplaats.utils;

import lemurproject.indri.QueryEnvironment;
import main.nl.marktplaats.objects.Experiment;
import main.nl.marktplaats.objects.IndexedField;


public class Configuration {
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
	public SearchEngine getSearchEngine()
	{
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
	
	public Experiment getExperiment( ) {
		return this.experiment;
	}
	public void setVoyagerRequest(String request) {
		this.voyagerRequest = request; 
	}
	public String getVoyagerRequest()
	{
		return this.voyagerRequest;
	}
	
	public void setPostFixVoyagerRequest(String request) {
		this.postFixVoyagerRequest = request; 
	}
	public String getPostFixVoyagerRequest()
	{
		return this.postFixVoyagerRequest;
	}
	public void setIndexField(IndexedField index) {
		this.indexField = index;
	}
	public IndexedField getIndexField()
	{
		return this.indexField;
	}
	public String getVoyagerResultsFolder() {
		return this.voyagerResultsFolder;
	}
	public void setVoyagerResultsFolder(String folder)
	{
		this.voyagerResultsFolder = folder;
	}
	public String getVoyagerResultsTable() {
		return this.voyagerResultsTable;
	}
	public void setVoyagerResultsTable(String table) {
		this.voyagerResultsTable = table;
	}	
	
	public void setInputMMRTable(String mmrTable)
	{
		this.inputMMRTable = mmrTable;
	}
	public String getMMRTable()
	{
		return this.getMMRTable();
	}
	private boolean checkIfHasStringValue(String fieldsName, String fieldsValue) {
		if(fieldsValue==null)
			System.out.println(fieldsName+" needs to be configured");
		else
		{
			System.out.println(fieldsName+" OK");
			return true;
		}
		return false;
	}
	public String getVoyagerQueriesTable() {
		return this.voyagerQueriesTable;
	}
	
	public void setVoyagerQueriesTable(String table)
	{
		this.voyagerQueriesTable = table;
	}
	public String getTrecInputFolder() {
		return this.trecInputFolder;
	}

	public void setTrecInputFolder(String f) {
		this.trecInputFolder= f;
	}
	public void setStatisticsTable(String s) {
		this.statisticsTable = s;
	}
	public String getStatisticsTable()
	{
		return this.statisticsTable;
	}
	public boolean checkVoyagerConfiguration() {
		boolean a = checkIfHasStringValue("DB ",this.db);
		boolean b = checkIfHasStringValue("ReadTable ",this.readTable);
		boolean c = checkIfHasStringValue("voyagerQueriesTable ",this.voyagerQueriesTable);
		boolean d = checkIfHasStringValue("IndexField ",this.indexField.toString());
		boolean e = checkIfHasStringValue("VoyagerResultsFolder ",this.voyagerResultsFolder);
		boolean f = checkIfHasStringValue("VoyagerResultsTable ",this.voyagerResultsTable);
		boolean g = checkIfHasStringValue("trecInputFolder ",this.trecInputFolder);
		boolean h = checkIfHasStringValue("statisticsTable  ",this.statisticsTable);
		
		if(a && b && c && d && e && f && g && h)
			return true;
		else return false;
		
	}
	public boolean checkAobConfiguration() {
		boolean a = checkIfHasStringValue("DB ",this.db);
		boolean b = checkIfHasStringValue("ReadTable ",this.readTable);
		boolean d = checkIfHasStringValue("InputTable ",this.inputTable);
		
		if(a && b && d)
			return true;
		else return false;
	}
	public boolean checkMMRConfiguration() {
		boolean a = checkIfHasStringValue("DB ",this.db);
		boolean b = checkIfHasStringValue("ReadTable ",this.readTable);
		boolean d = checkIfHasStringValue("inputMMRTable ",this.inputMMRTable);
		
		if(a && b && d)
			return true;
		else return false;
	}
}
