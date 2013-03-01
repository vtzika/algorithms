package main.nl.marktplaats.utils;

import lemurproject.indri.QueryEnvironment;


public class Configuration {
	private String db;
	private String readTable;
	private String inputTable;
	private String system;
	private AobMethod aobMethod;
	private SearchEngine searchEngine;
	private QueryEnvironment queryEnvRepository;
	private String repositoryPath;
	
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

}
