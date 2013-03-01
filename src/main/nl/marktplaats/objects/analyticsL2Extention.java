package main.nl.marktplaats.objects;

import main.nl.marktplaats.algorithm.ExtendQuery;
import main.nl.marktplaats.utils.Configuration;

public class analyticsL2Extention extends ExtendQuery {
	public analyticsL2Extention(Long id, String qString, Configuration c) {
		super.setQueries(id, qString, c);
	}

	@Override
	public Query extendQuery(){
		
		Query query = super.getQuery();
		return query;
		
	}

}
