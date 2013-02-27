package main.nl.marktplaats.objects;

import main.nl.marktplaats.algorithm.ExtendQuery;

public class analyticsL2Extention extends ExtendQuery {
	public analyticsL2Extention(Long id, String qString) {
		super.setQueries(id, qString);
		//super.setExtendedQuery(qString);
	}

	@Override
	public Query extendQuery(){
		
		Query query = super.getQuery();
		return query;
		
	}

}
