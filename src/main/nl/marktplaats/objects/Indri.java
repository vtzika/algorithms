package main.nl.marktplaats.objects;

import main.nl.marktplaats.utils.Configuration;
import main.nl.marktplaats.utils.RetrievalMethod;

public class Indri {

	private Configuration configuration;

	public Indri(Configuration configuration) {
		this.configuration = configuration;
	}

	public void createQueryIndriQueryLanguage() {
		// TODO Auto-generated method stub
		
	}

	public void runIndriQueries() {
		// TODO Auto-generated method stub
		RetrievalMethod retrievalMethod = this.configuration.getRetrievalMethod();
		switch(retrievalMethod){
		case Okapi:
			System.out.println("Okapi");
			break;
		case Tfidf:
			System.out.println("TfIdf");
			break;
		default :
			break;
		}
		
	}

	public void saveIndriResultsToTable() {
		// TODO Auto-generated method stub
		
	}

	public void gatherStatistics() {
		// TODO Auto-generated method stub
		
	}

}
