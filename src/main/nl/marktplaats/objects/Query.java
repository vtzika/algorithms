package main.nl.marktplaats.objects;

public class Query {
	private Long qID;
	private String qString;
	
	public Query (Long id, String q)
	{
		this.qID = id;
		this.qString = q ;
		
	}

	public Query() {
		// TODO Auto-generated constructor stub
	}

	public void setQID(Long id) {
		this.qID = id;	
	}
	
	public Long getQID() {
		return this.qID;	
	}

	public String getQString() {
		return this.qString;
	}

	public void setQString(String qString) {
		this.qString = qString;
	}
	

}
