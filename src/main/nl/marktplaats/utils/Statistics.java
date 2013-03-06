package main.nl.marktplaats.utils;

import java.util.HashMap;
import java.util.Map.Entry;


public class Statistics {
	private Configuration configuration;
	
	public Statistics(Configuration c)
	{
		this.configuration = c;
	}

	public void gatherStatistics() {
		SqlCommands sql = new SqlCommands();
		String db = configuration.getDb();
		String table = configuration.getVoyagerResultsTable();
		for(int query:sql.selectListInt("select distinct(query) from "+table+" ;",db))
		{			
			double top5 =getTopXresults(table, 5, query, db, "CTR");
			double top10 =getTopXresults(table, 10, query, db, "CTR");
			double top20 =getTopXresults(table, 20, query, db, "CTR");
			double top100 =getTopXresults(table, 100, query, db, "CTR");

			double top5RPM =getTopXresults(table, 5, query, db, "RPM");
			double top10RPM =getTopXresults(table, 10, query, db, "RPM");
			double top20RPM =getTopXresults(table, 20, query, db, "RPM");
			double top100RPM =getTopXresults(table, 100, query, db, "RPM");
			sql.insertQuery("insert into "+configuration.getStatisticsTable()+" values ("+query +", "+top5+ ", "+top10+", "+top20+","+top100+",'CTR')", db);
			sql.insertQuery("insert into "+configuration.getStatisticsTable()+" values ("+query +", "+top5RPM+ ", "+top10RPM+", "+top20RPM+","+top100RPM+",'RPM')", db);
		}
	}

	private static double getTopXresults(String table, int i, int query, String db, String CTRorRPM ) {
		SqlCommands sql = new SqlCommands();
		double topX = 0.0;
		HashMap<Long, Double> top4Results = sql.selectHashMapLongDoubbleQuery("select docs,"+CTRorRPM+" from "+table+" where query="+query+" order by sequence limit "+i+";", db); 
		for(Entry<Long,Double> entry : top4Results.entrySet())
		{
			topX += entry.getValue();
		}
		return topX/i;
	}
}
