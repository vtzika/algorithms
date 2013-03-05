package main.nl.marktplaats.objects;

import lemurproject.indri.QueryEnvironment;
import main.nl.marktplaats.utils.Configuration;

public class CosineSimilarity {
	private Document doc1;
	private Document doc2;
	
	
	public CosineSimilarity(String db, long d1, long d2, long query, String dbTable, QueryEnvironment env) throws Exception {
		
		doc1 = new Document(db,d1, query, dbTable, env);
		doc2 = new Document(db,d2, query, dbTable, env);
	}
	
	public CosineSimilarity(Configuration configuration) {
		// TODO Auto-generated constructor stub
	}

	public Double calculateCosineSimilarity()
	{
		Double cosSimilarity = 0.0;
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
