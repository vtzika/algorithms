package main.nl.marktplaats.algorithm;

import main.nl.marktplaats.objects.Query;
import main.nl.marktplaats.objects.ResultsFile;
import main.nl.marktplaats.utils.Configuration;
import main.nl.marktplaats.utils.SqlCommands;

public abstract class ExtendQuery {
	
	Query origininalQuery;
	Query newQuery ;
	
	public ExtendQuery(Long id, String qString) {
		origininalQuery =  new Query(id, qString);;
		}
	public ExtendQuery()
	{
		
	}
	
	public Query getQuery() {
		return this.origininalQuery;
	}
	public void setQueries(Long id, String qString) {
		Query q = new Query();
		q.setQID(id);
		q.setQString(qString);
		this.origininalQuery = q;
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
		sql.insertQuery("insert into "+configuration.getInputTable()+" values("+this.getQuery().getQID()+",'"+this.getQuery().getQString()+"','"+this.getExtendedQuery().getQString()+"';", configuration.getDb());
	}
}
