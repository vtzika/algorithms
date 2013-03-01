package main.nl.marktplaats.utils;

import java.io.File;

import lemurproject.indri.QueryEnvironment;

public class QueryEnvironmentManipulation {

	public QueryEnvironment add(String path) throws Exception {
		QueryEnvironment env = new QueryEnvironment();
		File repository=new File(path);
		for( String rep:repository.list())
		{
			System.out.println(path+rep);
			env.addIndex(path + rep);
		}
		return env;
		
	}

}
