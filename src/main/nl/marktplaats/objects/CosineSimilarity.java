package main.nl.marktplaats.objects;

import java.util.List;
import lemurproject.indri.QueryEnvironment;

public class CosineSimilarity {
	private Document doc1;
	private Document doc2;
	
	
	public CosineSimilarity(String db, long d1, long d2, long query, String dbTable, QueryEnvironment env,int system) throws Exception {
		
		doc1 = new Document(db,d1, query, dbTable, env,system);
		doc2 = new Document(db,d2, query, dbTable, env,system);
	}
	
	public Double calculateCosineSimilarity()
	{
		Double cosSimilarity = 0.0;
		List<Term> termsDoc1 = doc1.getTerms();
		List<Term> termsDoc2 = doc2.getTerms();
		Double sumSquaredWeightDoc1 = doc1.getSumSquaredWeight();
		Double sumSquaredWeightDoc2 = doc2.getSumSquaredWeight();
		Double St1it2i = getSumTermsWeightsForBothDocs();
		cosSimilarity = St1it2i /(sumSquaredWeightDoc1*sumSquaredWeightDoc2);
		return cosSimilarity;
	}
	private Double getSumTermsWeightsForBothDocs()
	{
		Double sum=0.0;
		for(Term d1Term:getDoc1().getTerms())
		{
			sum +=getDoc2().getTermWeight(d1Term.getStringTerm()) * d1Term.getWeight();
		}
		return sum;
	}

	
	public Document getDoc1()
	{
		return this.doc1;
	}
	public Document getDoc2()
	{
		return this.doc2;
	}
}
