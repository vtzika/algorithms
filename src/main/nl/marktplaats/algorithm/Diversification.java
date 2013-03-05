package main.nl.marktplaats.algorithm;
//TO DO:Needs Cleaning

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import lemurproject.indri.QueryEnvironment;
import main.nl.marktplaats.objects.CosineSimilarity;
import main.nl.marktplaats.objects.Document;
import main.nl.marktplaats.utils.Configuration;
import main.nl.marktplaats.utils.SqlCommands;

public class Diversification {
	private static String db ;
	private static String system; 
	private static String table;
	private static QueryEnvironment env ;

	public Diversification(Configuration configuration ) throws Exception
	{
		db = configuration.getDb();
		table = configuration.getReadTable();
		env = configuration.getQueryEnvRepository();
	}
	
	public static void diversificationSimple() throws Exception
	{
		SqlCommands sql = new SqlCommands();
		for(int query:sql.selectListInt("select distinct(query) from "+table+" where query not in (select distinct(query) from MMR );",db))
		{
			List<Long> ids = sql.selectListLong("select distinct(doc) from "+table+" where  query="+query+" and doc not in (select doc from MMR where query="+query+") limit 100;",db);
			for(int i=0; i<=ids.size()-2;i++)
			{
				HashMap<Long,Double> cos_sims = sql.selectHashMapLongDoubbleQuery("select doc2,similarity from cosSim where doc1="+ids.get(i)+";", db);
				for(int j=i+1;j<=ids.size()-1; j++)
				{
				double c_s = 0.0;
				if(cos_sims.containsKey(ids.get(j)))
				{
				 c_s = cos_sims.get(ids.get(j));
				}
				else
				{
					CosineSimilarity cs = new CosineSimilarity(db, ids.get(i), ids.get(j),query, table, env);
					c_s = cs.calculateCosineSimilarity();
					cos_sims.put(ids.get(j),c_s);
					sql.insertQuery("insert into cosSim Values("+query+","+ids.get(i)+","+ ids.get(j)+","+cs.getDoc1().getScore()+","+c_s+");",db);
					
				}
				}
				Document doc1 = new Document(db,ids.get(i), query, table, env);
				double maxCosineSimilarity = getMaxCs(cos_sims) ;
				double mmr = 0.3 * doc1.getScore() - (1 - 0.3) * maxCosineSimilarity;
				sql.insertQuery("insert into MMR Values("+query+","+ids.get(i)+","+mmr+","+system+");",db);
				
			}
		} 
	}


	//This technique check the similarity of one add with all the previous results 
	public static void alternativeDiversification() throws Exception
	{
		SqlCommands sql = new SqlCommands();
		for(int query:sql.selectListInt("select distinct(query) from "+table+" where system="+system+" and query not in (select distinct(query) from altMMR where system="+system+");",db))
		{
			List<Long> ids = sql.selectListLong("select distinct(doc) from "+table+" where system="+system+" and query="+query+" and doc not in (select doc from altMMR where system="+system+" and  query="+query+") limit 100;",db);
			double score = sql.selectDoubleQuery("select score from "+table+" where query="+query+" and doc="+ids.get(0)+";",db);
			sql.insertQuery("insert into altMMR Values("+query+","+ids.get(0)+","+score+",1);",db);
			Long alreadyCompared = ids.get(0);
			ids.remove(0);
			while(ids.size()>0)
			{
					HashMap<Long,Double> mmrs = new HashMap<Long,Double>();
					for(int j=0;j<=ids.size()-1; j++)
					{
					CosineSimilarity cs = new CosineSimilarity(db,ids.get(j), alreadyCompared,query, table, env);
					double c_s = 0.0;
					HashMap<Long,Double> cos_sims = sql.selectHashMapLongDoubbleQuery("select doc1,similarity from cosSim where doc2="+alreadyCompared+";", db);
					
					if(cos_sims.containsKey(ids.get(j)))
					{
						cos_sims.put(ids.get(j),cos_sims.get(ids.get(j)));
						c_s = cos_sims.get(ids.get(j));
					}
					else
					{
						c_s = cs.calculateCosineSimilarity();
						cos_sims.put(ids.get(j),c_s);
						sql.insertQuery("insert into cosSim Values("+query+","+ids.get(j)+","+ alreadyCompared+","+cs.getDoc1().getScore()+","+c_s+");",db);
						
						
					}
					Document doc1 = new Document(db,ids.get(j), query, table, env);
					double mmr = 0.3 * doc1.getScore() - (1 - 0.3) * c_s;
					mmrs.put(ids.get(j), mmr);
									
					}
					Long maxMMRid = getMaxMMRId(mmrs) ;
					sql.insertQuery("insert into altMMR Values("+query+","+maxMMRid+","+mmrs.get(maxMMRid)+","+system+");",db);
					alreadyCompared = maxMMRid;
					int removableIndex = 0 ;
					for(int i=0;i<ids.size();i++)
					{
						if(ids.get(i)==maxMMRid)
						{
							removableIndex = i;
							break;
						}
					
					}
					ids.remove(removableIndex);
				}
			}
	}
	//This technique check the similarity of one add with all the displayed results 
	public static void alternativeDiversification2() throws Exception
	{
		SqlCommands sql = new SqlCommands();
		
		for(int query:sql.selectListInt("select distinct(query) from "+table+" where system="+system+" and query not in (select distinct(query) from altMMRAvg where system="+system+");",db))
		{
			int count = 1;
			List<Long> ids = sql.selectListLong("select distinct(doc) from "+table+" where system="+system+" and query="+query+" and doc not in (select doc from altMMRAvg where system="+system+" and query="+query+") limit 100;",db);
			List<Long> displayedIds = new ArrayList<Long>();
			double score = sql.selectDoubleQuery("select score from "+table+" where query="+query+" and doc="+ids.get(0)+";", db);
			sql.insertQuery("insert into altMMRAvg Values("+query+","+ids.get(0)+","+score+",1,"+system+");",db);
			Long alreadyCompared = ids.get(0);
			displayedIds.add(alreadyCompared);
			ids.remove(0);
			while(ids.size()>0)
			{	
					//HashMap<Long,Double> cos_sims = new HashMap<Long,Double>();
					HashMap<Long,Double> mmrs = new HashMap<Long,Double>();
					for(int j=0;j<=ids.size()-1; j++)
					{
						Double avgMMR = getAvgMMR(table, query, ids.get(j),displayedIds, env, db);
						mmrs.put(ids.get(j), avgMMR);
									
					}
					Long maxMMRid = getMaxMMRId(mmrs) ;
					count++;
					sql.insertQuery("insert into altMMRAvg Values("+query+","+maxMMRid+","+mmrs.get(maxMMRid)+", "+count+","+system+");",db);
					alreadyCompared = maxMMRid;
					displayedIds.add(alreadyCompared);
					int removableIndex = 0 ;
					for(int i=0;i<ids.size();i++)
					{
						if(ids.get(i)==maxMMRid)
						{
							removableIndex = i;
							break;
						}
					
					}
					ids.remove(removableIndex);
				}
			}
	}
	
	//This technique check the similarity of one add with previous 4 displayed results 
		public static void alternativeDiversificationLast4() throws Exception
		{
			SqlCommands sql = new SqlCommands();
			for(int query:sql.selectListInt("select distinct(query) from "+table+" where query not in (select distinct(query) from altMMRAvgLast4Lamda05);","aob"))
			{
				int count = 1;
				List<Long> ids = sql.selectListLong("select distinct(doc) from "+table+" where query="+query+" and doc not in (select doc from altMMRAvgLast4Lamda05 where query="+query+") limit 100;","aob");
				List<Long> displayedIds = new ArrayList<Long>();
				double score = sql.selectDoubleQuery("select score from "+table+" where query="+query+" and doc="+ids.get(0)+";", "aob");
				sql.insertQuery("insert into altMMRAvgLast4Lamda05 Values("+query+","+ids.get(0)+","+score+",1);","aob");
				Long alreadyCompared = ids.get(0);
				displayedIds.add(alreadyCompared);
				ids.remove(0);
				while(ids.size()>0)
				{	
						//HashMap<Long,Double> cos_sims = new HashMap<Long,Double>();
						HashMap<Long,Double> mmrs = new HashMap<Long,Double>();
						for(int j=0;j<=ids.size()-1; j++)
						{
							Double avgMMR = 0.0;
							if(displayedIds.size()>4)
							{
								List<Long> last4DisplayedIds = chooseLast4(displayedIds);
								avgMMR = getAvgMMR(table, query, ids.get(j),last4DisplayedIds, env, db);
							}
							else 
								 avgMMR = getAvgMMR(table, query, ids.get(j),displayedIds, env, db);
							
							mmrs.put(ids.get(j), avgMMR);
										
						}
						Long maxMMRid = getMaxMMRId(mmrs) ;
						count++;
						sql.insertQuery("insert into altMMRAvgLast4 Values("+query+","+maxMMRid+","+mmrs.get(maxMMRid)+", "+count+");","aob");
						alreadyCompared = maxMMRid;
						displayedIds.add(alreadyCompared);
						int removableIndex = 0 ;
						for(int i=0;i<ids.size();i++)
						{
							if(ids.get(i)==maxMMRid)
							{
								removableIndex = i;
								break;
							}
						
						}
						ids.remove(removableIndex);
					}
				}
		}

		
		//This technique check the similarity of 10 next adds with previous 4 displayed results 
		public static void alternativeDiversificationLast4With10Next() throws Exception
		{
			SqlCommands sql = new SqlCommands();
			for(int query:sql.selectListInt("select distinct(query) from "+table+" where query not in (select distinct(query) from altMMRAvg4Last10Next);","aob"))
			{
				int count = 1;
				List<Long> ids = sql.selectListLong("select distinct(doc) from "+table+" where query="+query+" and doc not in (select doc from altMMRAvg4Last10Next where query="+query+") limit 100;","aob");
				List<Long> displayedIds = new ArrayList<Long>();
				double score = sql.selectDoubleQuery("select score from "+table+" where query="+query+" and doc="+ids.get(0)+";", "aob");
				sql.insertQuery("insert into altMMRAvg4Last10Next Values("+query+","+ids.get(0)+","+score+",1);","aob");
				Long alreadyCompared = ids.get(0);
				displayedIds.add(alreadyCompared);
				ids.remove(0);
				while(ids.size()>0)
				{	
						//HashMap<Long,Double> cos_sims = new HashMap<Long,Double>();
						HashMap<Long,Double> mmrs = new HashMap<Long,Double>();
						for(int j=0;j<=10; j++)
						{
							Double avgMMR = 0.0;
							if(displayedIds.size()>4)
							{
								List<Long> last4DisplayedIds = chooseLast4(displayedIds);
								avgMMR = getAvgMMR(table, query, ids.get(j),last4DisplayedIds, env, db);
							}
							else 
								 avgMMR = getAvgMMR(table, query, ids.get(j),displayedIds, env, db);
							
							mmrs.put(ids.get(j), avgMMR);			
						}
						Long maxMMRid = getMaxMMRId(mmrs) ;
						count++;
						sql.insertQuery("insert into altMMRAvg4Last10Next Values("+query+","+maxMMRid+","+mmrs.get(maxMMRid)+", "+count+");","aob");
						alreadyCompared = maxMMRid;
						displayedIds.add(alreadyCompared);
						int removableIndex = 0 ;
						for(int i=0;i<ids.size();i++)
						{
							if(ids.get(i)==maxMMRid)
							{
								removableIndex = i;
								break;
							}
						
						}
						ids.remove(removableIndex);
					}
				}
		}


		//This technique check the similarity of 10 next adds with previous 4 displayed results and gives a fine to sequent similar items
		public void alternativeDiversificationLast4With10NextAndFine() throws Exception
		{
			SqlCommands sql = new SqlCommands();
			for(int query:sql.selectListInt("select distinct(query) from "+table+" where query not in (select distinct(query) from altMMRAvg4Last10NextWithFine);",db))
			{
				int count = 1;
				List<Long> ids = sql.selectListLong("select distinct(doc) from "+table+" where query="+query+" and doc not in (select doc from altMMRAvg4Last10NextWithFine where query="+query+") limit 100;",db);
				List<Long> displayedIds = new ArrayList<Long>();
				double score = sql.selectDoubleQuery("select score from "+table+" where query="+query+" and doc="+ids.get(0)+";", db);
				sql.insertQuery("insert into altMMRAvg4Last10NextWithFine Values("+query+","+ids.get(0)+","+score+",1);",db);
				Long alreadyCompared = ids.get(0);
				displayedIds.add(alreadyCompared);
				ids.remove(0);
				while(ids.size()>=10)
				{	
						//HashMap<Long,Double> cos_sims = new HashMap<Long,Double>();
						HashMap<Long,Double> mmrs = new HashMap<Long,Double>();
						if(ids.size()>10)
						for(int j=0;j<=10; j++)
						{
							Double avgMMR = 0.0;
							if(displayedIds.size()>4)
							{
								List<Long> last4DisplayedIds = chooseLast4(displayedIds);
								avgMMR = getAvgMMR(table, query, ids.get(j),last4DisplayedIds, env, db);
							}
							else 
								if(ids.size()>=10)
								{ avgMMR = getAvgMMR(table, query, ids.get(j),displayedIds, env, db);

								mmrs.put(ids.get(j), avgMMR);		
								}
								
						
						Long maxMMRid = getMaxMMRId(mmrs) ;
						count++;
						sql.insertQuery("insert into altMMRAvg4Last10NextWithFine Values("+query+","+maxMMRid+","+mmrs.get(maxMMRid)+", "+count+");","aob");
						alreadyCompared = maxMMRid;
						displayedIds.add(alreadyCompared);
						int removableIndex = 0 ;
						for(int i=0;i<ids.size();i++)
						{
							if(ids.get(i)==maxMMRid)
							{
								removableIndex = i;
								break;
							}
						
						}
						ids.remove(removableIndex);
					}
				}
			}
		}

	private static List<Long> chooseLast4(List<Long> displayedIds) {
		List<Long> last = new ArrayList<Long>();
		for(int i=0; i<4;i++)
		{
			last.add(displayedIds.get(displayedIds.size()-1));
			displayedIds.remove(displayedIds.get(displayedIds.size()-1));
		}
		return last;
	}

	private static Double getAvgMMR(String table,int query,Long id, List<Long> displayedIds, QueryEnvironment env, String db) throws Exception {

		SqlCommands sql = new SqlCommands();
		HashMap<Long,Double> cos_sims = sql.selectHashMapLongDoubbleQuery("select doc2,similarity from cosSim where doc1="+id+" ;", db);
		List<Double> mmrs = new ArrayList<Double>();
		Double sum = 0.0;
		int count =0;
		for(Long displayedId:displayedIds)
		{
			count++;
			double c_s = 0.0;
			if(cos_sims.containsKey(displayedId))
			{
				c_s = cos_sims.get(displayedId);
			}
			else
			{

				CosineSimilarity cs = new CosineSimilarity(db, id, displayedId,query, table, env);
				c_s = cs.calculateCosineSimilarity();
				cos_sims.put(id,c_s);
				sql.insertQuery("insert into cosSim Values("+query+","+id+","+ displayedId+","+cs.getDoc1().getScore()+","+c_s+");",db);
			}
			
			Document doc1 = new Document(db,id, query, table, env);
			double mmr = 0.3 * doc1.getScore() -count* (1 - 0.3) * c_s;
			mmrs.add(mmr);
			sum +=mmr;
		}
		return sum/displayedIds.size();
	}

	private static Long getMaxMMRId(HashMap<Long, Double> mmrs) {
		double mmr = Collections.max(mmrs.values());
		
		for(Entry<Long,Double> entry : mmrs.entrySet())
		{
			if(entry.getValue()==mmr)
				return entry.getKey();
		}
		return null;
	}

	private static double getMaxCs(HashMap<Long, Double> cos_sims) {
		return Collections.max(cos_sims.values());
	}
	

	
	
}
