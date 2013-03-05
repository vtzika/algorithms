package main.nl.marktplaats.objects;

import java.util.ArrayList;
import java.util.List;

import main.nl.marktplaats.algorithm.ExtendQuery;
import main.nl.marktplaats.utils.Configuration;
import main.nl.marktplaats.utils.SqlCommands;

public class PseudoL2Extention extends ExtendQuery {

	public PseudoL2Extention(Long id, String qString, Configuration c) {
		super.setQueries(id, qString, c);
	}

	@Override
	public Query extendQuery() throws Exception{
		PseudoRelevanceFeedback pseudo = new PseudoRelevanceFeedback();
		List<Integer> l2 = new ArrayList<Integer>();
		SqlCommands sql = new SqlCommands();
		l2.add(sql.selectIntQuery("select category_id from ads where id="+(int) super.getQuery().getQID().longValue()+";", "cas_ad_service"));
		String newQuery = pseudo.findBiggestCTRAndCreateNewQueries(l2);
		Query query = new Query(super.getQuery().getQID(), newQuery );
		return query;
		
	}
}
