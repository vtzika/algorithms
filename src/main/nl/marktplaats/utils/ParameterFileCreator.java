package main.nl.marktplaats.utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ParameterFileCreator {
	Configuration configuration ;
	List<String> queryTerms;
	private String docNo;

	public ParameterFileCreator(Configuration c, String query, String docno) {
		this.configuration = c;
		queryTerms = new ArrayList<String>();
		this.docNo  = docno;
		for (String term:query.split(" ") )
		{
			queryTerms.add(term);
		}
	}

	public boolean createFile() {
		String query = takeQueryString();
		String repositories = "";
		String inputText = "";
		String trecFormat = "\n <trecFormat>true</trecFormat>";
		String queryNumber = query+"\n<number>" + docNo + "</number>\n</query>";
		for (String repository : configuration.getRepositories()) {
			repositories = repositories + "\n <index>"
					+ configuration.getRepositoryPath() + "/" + repository
					+ "</index>";
		}
		String baseline = null;
		String countString = null;
		inputText = "<parameters> \n " + repositories + "\n" + queryNumber
				+ baseline + "\n" + countString + "\n" + trecFormat;
		Writer output = null;
		File file = new File(configuration.getParameterFilesDirectory()+"/"+docNo);
		String finalText = inputText + "\n </parameters>";
		try {
			output = new BufferedWriter(new FileWriter(file));
			output.write(finalText);
			output.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private String takeQueryString() {
		String query = "<query> \n<text>";
		for (String term : queryTerms) {
			query += term + " ";
		}
		return query + "</text>\n";
	}

	public void runRetrievalModel() throws Throwable {
		Process process = new ProcessBuilder(
				configuration.getIndriPath()+"/runquery/IndriRunQuery",
				configuration.getParameterFilesDirectory()).start();
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		String line;

		System.out.println("Output of running is:");
	
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		//ResultFileReader fileReader = new ResultFileReader();
		//fileReader.readFile("/home/varvara/workspace/externalSources/indri/runquery/TfIdfResults/results");
	
	}

	public void setQueryTerm(String term) {
		this.queryTerms.add(term);
	}
}
