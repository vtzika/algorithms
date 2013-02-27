package main.nl.marktplaats.objects;

import main.nl.marktplaats.algorithm.ExtendQuery;

public class analyticsL1Extention extends ExtendQuery {

	public analyticsL1Extention(Long id, String qString) {
		super.setQueries(id, qString);
	}
	
	@Override
	public Query extendQuery(){
		
		Query query = super.getQuery();
		return query;
		
	}

}
