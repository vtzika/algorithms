package main.nl.marktplaats.objects;

import java.util.ArrayList;
import java.util.List;

import main.nl.marktplaats.algorithm.ExtendQuery;
import main.nl.marktplaats.utils.Configuration;

public class PseudoL2Extention extends ExtendQuery {

	public PseudoL2Extention(Long id, String qString, Configuration c) {
		super.setQueries(id, qString, c);
	}

	@Override
	public Query extendQuery() throws Exception{

		PseudoRelevanceFeedback pseudo = new PseudoRelevanceFeedback();
		List<Integer> l2 = new ArrayList<Integer>();
		l2.add((int) super.getQuery().getQID().longValue());
		String newQuery = pseudo.findBiggestCTRAndCreateNewQueries(l2);
		Query query = new Query(super.getQuery().getQID(), newQuery );
		return query;
		
	}
}
