package main.nl.marktplaats.objects;

import java.util.ArrayList;
import java.util.List;

import lemurproject.indri.QueryEnvironment;
import main.nl.marktplaats.utils.SqlCommands;
import main.nl.marktplaats.utils.StringManipulation;

public class Document {
	private Double score;
	private List<Term> terms;
	String table;
	
public Document(String db,long d1, long query, String dbTable, QueryEnvironment env,int system) throws Exception {
	try{
		
		this.table = dbTable;
		setTerms(d1,env, db);
		setScore(d1,query,db,system);
	}
	catch (Exception e) {
		System.out.println(e);	}
	}

private void setScore(long d1, long query,String db, int system) {
	SqlCommands sql = new SqlCommands();
	this.score=sql.selectDoubleQuery("select score from "+this.table+" where system="+system+" and doc="+d1+" and query = "+query+" ;",db);
		
}

private void setTerms(long d1,QueryEnvironment env, String db) throws Exception {
	List<Term> allTerms = new ArrayList<Term>();
	SqlCommands sql = new SqlCommands();
	for(String titleDescrTerm:sql.selectStringQuery("select title, description, attr, category from vips where id="+d1+";", db).split(" |/|<|>"))
	{	StringManipulation segment = new StringManipulation();
		Term tdTerm = new Term(segment.sanitizeString(titleDescrTerm.toLowerCase()),env);
		allTerms.add(tdTerm);
	}
	this.terms = allTerms;
}

public List<Term> getTerms(){
	return this.terms;
}

public Double getSumSquaredWeight() {
	Double squaredSum = 0.0;
	for(Term term :terms)
	{
		squaredSum += Math.pow(term.getWeight(), 2);
	}
	return Math.sqrt(squaredSum);
}
public Double getScore() {
	return this.score;
}

public double getTermWeight(String stringTerm) {
	for(Term term:this.terms)
	{
		if(term.getStringTerm().equals(stringTerm))
		{
			return term.getWeight();
		}
	}
	return 0.0;
}
}
