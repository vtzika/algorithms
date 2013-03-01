package main.nl.marktplaats.algorithm;

import lemurproject.indri.QueryEnvironment;
import main.nl.marktplaats.objects.Query;
import main.nl.marktplaats.utils.Configuration;
import main.nl.marktplaats.utils.SqlCommands;

public abstract class ExtendQuery {
	
	Query origininalQuery;
	Query newQuery ;
	Configuration configuration;
	
	public ExtendQuery(Long id, String qString, Configuration config) {
		origininalQuery =  new Query(id, qString);
		this.configuration = config;
		}
	public ExtendQuery()
	{
		
	}
	
	public Query getQuery() {
		return this.origininalQuery;
	}
	public void setQueries(Long id, String qString, Configuration c) {
		Query q = new Query();
		q.setQID(id);
		q.setQString(qString);
		this.origininalQuery = q;
		this.configuration = c;
	}

	public Query extendQuery() throws Exception
	{
		return this.newQuery;
	}
	public Query getExtendedQuery() throws Exception
	{
		this.newQuery = extendQuery();
		return this.newQuery;
	}
	public void setExtendedQuery(Query extendQuery) {
		this.newQuery = extendQuery;
		
	}
	public void saveResults(Configuration configuration) throws Exception {
		SqlCommands sql = new SqlCommands();
		sql.insertQuery("insert into "+configuration.getInputTable()+" values("+this.getQuery().getQID()+",'"+this.getExtendedQuery().getQString()+"');", configuration.getDb());
	}
	public Configuration getConfiguration() {
		return this.configuration;
	}
}
