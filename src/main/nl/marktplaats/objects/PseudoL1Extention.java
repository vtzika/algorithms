package main.nl.marktplaats.objects;

import java.util.List;

import main.nl.marktplaats.algorithm.ExtendQuery;
import main.nl.marktplaats.utils.Configuration;
import main.nl.marktplaats.utils.SqlCommands;

public class PseudoL1Extention extends ExtendQuery {

	public PseudoL1Extention(Long id, String qString, Configuration c) {
		super.setQueries(id, qString, c);
	}

	@Override
	public Query extendQuery() throws Exception{
		
		PseudoRelevanceFeedback pseudo = new PseudoRelevanceFeedback();
		List<Integer> l2s = findAllL2InL1();
		String newQuery = pseudo.findBiggestCTRAndCreateNewQueries(l2s);
		Query query = new Query(super.getQuery().getQID(), newQuery );
		return query;
		
	}

	private List<Integer> findAllL2InL1() {
		SqlCommands sql = new SqlCommands();
		return sql.selectListInt("select distinct(category_id) from categories where category_id in (select category_id from ads where id=" +super.getQuery().getQID()+");", "cas_ad_service");
	}
}
