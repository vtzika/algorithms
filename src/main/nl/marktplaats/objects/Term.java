package main.nl.marktplaats.objects;
import lemurproject.indri.QueryEnvironment;

public class Term {	
	String term;
	Weight weight;

public Term(String t, QueryEnvironment env) throws Exception {
		this.term = t;
		
		weight = new Weight(t,env);
		//System.out.println(this.term);
		//System.out.println(this.weight.calculteWeight(env));
}



public double getWeight() {
	return weight.getWeight();
}

public String getStringTerm() {
	return term;
}

}