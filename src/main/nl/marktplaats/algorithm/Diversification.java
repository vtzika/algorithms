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
import main.nl.marktplaats.utils.MMRMethod;
import main.nl.marktplaats.utils.SqlCommands;

public class Diversification {
	private Configuration configuration ;

	public Diversification(Configuration configuration ) throws Exception
	{
		this.configuration = configuration;
	}
	public void diversificationNew() throws Exception
	{
		SqlCommands sql = new SqlCommands();
		System.out.println("Reading original results from "+configuration.getReadTable());
		HashMap<Integer, HashMap<Long,HashMap<Long, Double>>> query_doc1_doc2_sim = new HashMap<Integer,HashMap<Long, HashMap<Long,Double>>>();
		HashMap<Long, HashMap<Long, Double>> doc1_doc2_sim = new HashMap<Long, HashMap<Long,Double>>();
		for(int query:sql.selectListInt("select distinct(query) from "+configuration.getReadTable()+" where query not in (select distinct(query) from "+configuration.getMMRTable()+" );",configuration.getDb()))
		{
			List<Long> ids = sql.selectListLong("select distinct(docs) from "+configuration.getReadTable()+" where  query="+query+" and docs not in (select doc from "+configuration.getMMRTable()+" where query="+query+") limit 100;",configuration.getDb());
			for(Long id:ids)
			{
				HashMap<Long,Double> doc2_sim = sql.selectHashMapLongDoubbleQuery("select doc2,similarity from cosSim where doc1="+id+";", configuration.getDb());
				doc1_doc2_sim.put(id, doc2_sim);
			}
			query_doc1_doc2_sim.put(query, doc1_doc2_sim);
			getDiversificationMethod(query, ids, query_doc1_doc2_sim.get(query));
		}
	}
	
	private void getDiversificationMethod(int query, List<Long> ids, HashMap<Long, HashMap<Long, Double>> doc1_doc2_sim) throws Exception {
		MMRMethod mmrMethod = configuration.getMmrMethod();
		switch (mmrMethod) {
		case simpleMMR:
			diversificationSimple(query, ids, doc1_doc2_sim);
			break;
		case altAllMMR:
			//alternativeDiversification(query, ids, doc1_doc2_sim);
			break;
		case altLastOneMMR:
			//alternativeDiversification2(query, ids, doc1_doc2_sim);
			break;
		case altLastFourMMR:
			//alternativeDiversificationLast4(query, ids, doc1_doc2_sim);
			break;
		case altLastFourTenNextMMR:
			//alternativeDiversificationLast4With10Next(query, ids, doc1_doc2_sim);
			break;
		case altMMRwithFine:
			//alternativeDiversificationLast4With10NextAndFine(query, ids, doc1_doc2_sim);
			break;
		default:
			break;
		}
	}

	public void diversificationSimple(int query, List<Long> ids, HashMap<Long, HashMap<Long, Double>> doc1_doc2_sim) throws Exception
	{
		SqlCommands sql = new SqlCommands();
		int count = 0;
		for(int i=0; i<=ids.size()-2;i++)
		{
			HashMap<Long,Double> doc2_sim = doc1_doc2_sim.get(ids.get(i));
			HashMap<Long,Double> currentCosSim = new HashMap<Long, Double>();
			for(int j=i+1;j<=ids.size()-1; j++)
			{
				double c_s = 0.0;
				if(currentCosSim.containsKey(ids.get(j)))
				{ 
					c_s = currentCosSim.get(ids.get(j));
				}
				else if(doc2_sim.containsKey(ids.get(j)))
				{
					c_s = (double) doc2_sim.get(ids.get(j));
					currentCosSim.put(ids.get(j), c_s);
				}
				else
				{
					calculateCosineSimilarity(ids.get(i), ids.get(j), query);	
					currentCosSim.put(ids.get(j),c_s);
				}
			}
			Document doc1 = new Document(configuration.getDb(),ids.get(i), query, configuration.getReadTable(), configuration.getQueryEnvRepository());
			double maxCosineSimilarity = getMaxCs(currentCosSim) ;
			double mmr = 0.3 * doc1.getScore() - (1 - 0.3) * maxCosineSimilarity;
			count++;
			sql.insertQuery("insert into "+configuration.getMMRTable()+" Values("+query+","+ids.get(i)+","+mmr+","+count+",'diversificationSimple');",configuration.getDb());
			
		}
}



	private Double calculateCosineSimilarity(Long doc1, Long doc2, int query) throws Exception {
		SqlCommands sql = new SqlCommands();
		CosineSimilarity cs = new CosineSimilarity(configuration.getDb(), doc1, doc2,query, configuration.getReadTable(), configuration.getQueryEnvRepository());
		Double c_s = cs.calculateCosineSimilarity();
		sql.insertQuery("insert into cosSim Values("+doc1+","+ doc2+","+cs.getDoc1().getScore()+","+c_s+");",configuration.getDb());
		return c_s;
	}

	//This technique check the similarity of one add with all the previous results 
	public void alternativeDiversification(int query2, List<Long> ids2, HashMap<Long, Double> hashMap) throws Exception
	{
		SqlCommands sql = new SqlCommands();
		for(int query:sql.selectListInt("select distinct(query) from "+configuration.getReadTable()+" where query not in (select distinct(query) from "+configuration.getMMRTable()+");",configuration.getDb()))
		{
			int count = 0;
			List<Long> ids = sql.selectListLong("select distinct(docs) from "+configuration.getReadTable()+" where query="+query+" and docs not in (select doc from "+configuration.getMMRTable()+" where query="+query+") limit 100;",configuration.getDb());
			double score = sql.selectDoubleQuery("select score from "+configuration.getReadTable()+" where query="+query+" and docs="+ids.get(0)+";",configuration.getDb());
			sql.insertQuery("insert into "+configuration.getMMRTable()+" Values("+query+","+ids.get(0)+","+score+","+count+",'alternativeDiversification');",configuration.getDb());
			Long alreadyCompared = ids.get(0);
			ids.remove(0);
			while(ids.size()>0)
			{
				HashMap<Long,Double> mmrs = new HashMap<Long,Double>();
				for(int j=0;j<=ids.size()-1; j++)
				{
				CosineSimilarity cs = new CosineSimilarity(configuration.getDb(),ids.get(j), alreadyCompared,query, configuration.getReadTable(), configuration.getQueryEnvRepository());
				double c_s = 0.0;
				HashMap<Long,Double> cos_sims = sql.selectHashMapLongDoubbleQuery("select doc1,similarity from cosSim where doc2="+alreadyCompared+";", configuration.getDb());
				
				if(cos_sims.containsKey(ids.get(j)))
				{
					cos_sims.put(ids.get(j),cos_sims.get(ids.get(j)));
					c_s = cos_sims.get(ids.get(j));
				}
				else
				{
					c_s = cs.calculateCosineSimilarity();
					cos_sims.put(ids.get(j),c_s);
					sql.insertQuery("insert into cosSim Values("+ids.get(j)+","+ alreadyCompared+","+cs.getDoc1().getScore()+","+c_s+");",configuration.getDb());
				}
					Document doc1 = new Document(configuration.getDb(),ids.get(j), query, configuration.getReadTable(), configuration.getQueryEnvRepository());
					double mmr = 0.3 * doc1.getScore() - (1 - 0.3) * c_s;
					mmrs.put(ids.get(j), mmr);
									
					}
					Long maxMMRid = getMaxMMRId(mmrs) ;
					count++;
					sql.insertQuery("insert into "+configuration.getMMRTable()+" Values("+query+","+maxMMRid+","+mmrs.get(maxMMRid)+","+count+",'alternativeDiversification');",configuration.getDb());
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
	public void alternativeDiversification2(int query2, List<Long> ids2, HashMap<Long, Double> hashMap) throws Exception
	{
		SqlCommands sql = new SqlCommands();
		
		for(int query:sql.selectListInt("select distinct(query) from "+configuration.getReadTable()+" where query not in (select distinct(query) from "+configuration.getMMRTable()+");",configuration.getDb()))
		{
			int count = 1;
			List<Long> ids = sql.selectListLong("select distinct(docs) from "+configuration.getReadTable()+" where query="+query+" and docs not in (select doc from "+configuration.getMMRTable()+" where query="+query+") limit 100;",configuration.getDb());
			List<Long> displayedIds = new ArrayList<Long>();
			double score = sql.selectDoubleQuery("select score from "+configuration.getReadTable()+" where query="+query+" and docs="+ids.get(0)+";", configuration.getDb());
			sql.insertQuery("insert into "+configuration.getMMRTable()+" Values("+query+","+ids.get(0)+","+score+",1,'alternativeDiversification2');",configuration.getDb());
			Long alreadyCompared = ids.get(0);
			displayedIds.add(alreadyCompared);
			ids.remove(0);
			while(ids.size()>0)
			{	
					//HashMap<Long,Double> cos_sims = new HashMap<Long,Double>();
					HashMap<Long,Double> mmrs = new HashMap<Long,Double>();
					for(int j=0;j<=ids.size()-1; j++)
					{
						Double avgMMR = getAvgMMR(configuration.getReadTable(), query, ids.get(j),displayedIds, configuration.getQueryEnvRepository(), configuration.getDb());
						mmrs.put(ids.get(j), avgMMR);
									
					}
					Long maxMMRid = getMaxMMRId(mmrs) ;
					count++;
					sql.insertQuery("insert into "+configuration.getMMRTable()+" Values("+query+","+maxMMRid+","+mmrs.get(maxMMRid)+", "+count+",'alternativeDiversification2');",configuration.getDb());
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
		public void alternativeDiversificationLast4(int query2, List<Long> ids2, HashMap<Long, Double> hashMap) throws Exception
		{
			SqlCommands sql = new SqlCommands();
			for(int query:sql.selectListInt("select distinct(query) from "+configuration.getReadTable()+" where query not in (select distinct(query) from "+configuration.getMMRTable()+");",configuration.getDb()))
			{
				int count = 1;
				List<Long> ids = sql.selectListLong("select distinct(docs) from "+configuration.getReadTable()+" where query="+query+" and docs not in (select doc from "+configuration.getMMRTable()+" where query="+query+") limit 100;", configuration.getDb());
				List<Long> displayedIds = new ArrayList<Long>();
				double score = sql.selectDoubleQuery("select score from "+configuration.getReadTable()+" where query="+query+" and docs="+ids.get(0)+";", configuration.getDb());
				sql.insertQuery("insert into "+configuration.getMMRTable()+" Values("+query+","+ids.get(0)+","+score+",1,'alternativeDiversificationLast4');", configuration.getDb());
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
								avgMMR = getAvgMMR(configuration.getReadTable(), query, ids.get(j),last4DisplayedIds, configuration.getQueryEnvRepository(), configuration.getDb());
							}
							else 
								 avgMMR = getAvgMMR(configuration.getReadTable(), query, ids.get(j),displayedIds, configuration.getQueryEnvRepository(), configuration.getDb());
							
							mmrs.put(ids.get(j), avgMMR);
										
						}
						Long maxMMRid = getMaxMMRId(mmrs) ;
						count++;
						sql.insertQuery("insert into "+configuration.getMMRTable()+" Values("+query+","+maxMMRid+","+mmrs.get(maxMMRid)+", "+count+",'alternativeDiversificationLast4');", configuration.getDb());
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
		public void alternativeDiversificationLast4With10Next(int query2, List<Long> ids2, HashMap<Long, Double> hashMap) throws Exception
		{
			SqlCommands sql = new SqlCommands();
			for(int query:sql.selectListInt("select distinct(query) from "+configuration.getReadTable()+" where query not in (select distinct(query) from "+configuration.getMMRTable()+");",configuration.getDb()))
			{
				int count = 1;
				List<Long> ids = sql.selectListLong("select distinct(docs) from "+configuration.getReadTable()+" where query="+query+" and docs not in (select doc from "+configuration.getMMRTable()+" where query="+query+") limit 100;",configuration.getDb());
				List<Long> displayedIds = new ArrayList<Long>();
				double score = sql.selectDoubleQuery("select score from "+configuration.getReadTable()+" where query="+query+" and docs="+ids.get(0)+";", configuration.getDb());
				sql.insertQuery("insert into "+configuration.getMMRTable()+" Values("+query+","+ids.get(0)+","+score+",1,'alternativeDiversificationLast4With10Next');",configuration.getDb());
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
								avgMMR = getAvgMMR(configuration.getReadTable(), query, ids.get(j),last4DisplayedIds, configuration.getQueryEnvRepository(), configuration.getDb());
							}
							else 
								 avgMMR = getAvgMMR(configuration.getReadTable(), query, ids.get(j),displayedIds, configuration.getQueryEnvRepository(), configuration.getDb());
							
							mmrs.put(ids.get(j), avgMMR);			
						}
						Long maxMMRid = getMaxMMRId(mmrs) ;
						count++;
						sql.insertQuery("insert into "+configuration.getMMRTable()+" Values("+query+","+maxMMRid+","+mmrs.get(maxMMRid)+", "+count+",'alternativeDiversificationLast4With10Next');", configuration.getDb());
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
		public void alternativeDiversificationLast4With10NextAndFine(int query2, List<Long> ids2, HashMap<Long, Double> hashMap) throws Exception
		{
			SqlCommands sql = new SqlCommands();
			for(int query:sql.selectListInt("select distinct(query) from "+configuration.getReadTable()+" where query not in (select distinct(query) from "+configuration.getMMRTable()+");",configuration.getDb()))
			{
				int count = 1;
				List<Long> ids = sql.selectListLong("select distinct(docs) from "+configuration.getReadTable()+" where query="+query+" and docs not in (select doc from "+configuration.getMMRTable()+" where query="+query+") limit 100;", configuration.getDb());
				List<Long> displayedIds = new ArrayList<Long>();
				double score = sql.selectDoubleQuery("select score from "+configuration.getReadTable()+" where query="+query+" and docs="+ids.get(0)+";", configuration.getDb());
				sql.insertQuery("insert into "+configuration.getMMRTable()+" Values("+query+","+ids.get(0)+","+score+",1,'alternativeDiversificationLast4With10NextAndFine');",configuration.getDb());
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
								avgMMR = getAvgMMR(configuration.getReadTable(), query, ids.get(j),last4DisplayedIds, configuration.getQueryEnvRepository(), configuration.getDb());
							}
							else 
								if(ids.size()>=10)
								{ avgMMR = getAvgMMR(configuration.getReadTable(), query, ids.get(j),displayedIds, configuration.getQueryEnvRepository(), configuration.getDb());

								mmrs.put(ids.get(j), avgMMR);		
								}
								
						
						Long maxMMRid = getMaxMMRId(mmrs) ;
						count++;
						sql.insertQuery("insert into "+configuration.getMMRTable()+" Values("+query+","+maxMMRid+","+mmrs.get(maxMMRid)+", "+count+",'alternativeDiversificationLast4With10NextAndFine');",configuration.getDb());
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
				sql.insertQuery("insert into cosSim Values("+id+","+ displayedId+","+cs.getDoc1().getScore()+","+c_s+");",db);
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
