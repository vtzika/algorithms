package main.nl.marktplaats.objects;

import lemurproject.indri.QueryEnvironment;

public class Weight {
	private String term;
	private double weight;

	public Weight(String t, QueryEnvironment env) throws Exception {
		this.term=t;
		weight = calculteWeight(env);
		
	}
	public double calculteWeight(QueryEnvironment env) throws Exception
	{
		if(env.documentCount(this.term.trim())==0)
			return (double) 0.0;
		
		else
		{
			double termWeight = Math.log((double)(double)env.documentCount() /(double)env.documentCount(this.term.trim()));
			return termWeight;
		}
		
		
	}
	
	public double getWeight()
	{
		return this.weight;
	}


}
