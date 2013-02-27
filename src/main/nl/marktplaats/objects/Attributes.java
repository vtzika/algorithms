package main.nl.marktplaats.objects;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class Attributes {
	private List<String> attributes;
	
	private HashMap<Long, Long> attrIdAndAdId;
	private HashMap<Long, String> attrIdAndValues;
	private HashMap<Long, Long> attrValueIdAndAttrId;
	private HashMap<Long, String> attrValueIdAndValue;
	
	private HashMap<Long, Long> marktAdId_AttrValueId;
	private HashMap<Long, Long> marktAttrValueIdAndAttributesId;
	private HashMap<Long, String> marktAttrValueIdAndValue;
	private HashMap<Long, String> marktAttrIdAndValue;
	
	public Attributes(String a) {
		this.attributes = new ArrayList<String>(); 
		String[] words = a.split(" ");
		for(String word:words)
		{
			attributes.add(word);
		}
	}

	public Attributes() {
		this.setHashAtr_IdAndAd_id();
		this.setHashAtr_IdAnd2Labels();
		this.setHashAtr_Value_IdAndAttr_id();
		this.setHashAtr_Value_IdAndLabel();

	}
	public void setMarktAttributeHashMaps()
	{
		this.setMarktAdId_AttrValueId();
		this.setMarktAttrValueIdAndAttributesId();
		this.setMarktAttrIdAndValue();
	}

	private void setMarktAttrIdAndValue() {
		HashMap<Long, String> atrId_label = new HashMap<Long, String>();
		ResultSet rs;
		try {
			String connectionURL = "jdbc:mysql://localhost:3306/production_dump";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			String QueryString = "select attribute_id, label from attributes; ";
			rs = statement.executeQuery(QueryString);
			while (rs.next()) {
				atrId_label.put(rs.getLong(1), rs.getString(2));
			}
			rs.close();
			statement.close();
			connection.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.marktAttrIdAndValue = atrId_label;
		
	}

	private void setMarktAttrValueIdAndAttributesId() {
		HashMap<Long, Long> atrValueId_AttrId = new HashMap<Long, Long>();
		HashMap<Long, String> atrValueId_label = new HashMap<Long, String>();

		ResultSet rs;
		try {
			String connectionURL = "jdbc:mysql://localhost:3306/production_dump";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			String QueryString = "select attribute_value_id,attribute_id, label from attribute_values; ";
			rs = statement.executeQuery(QueryString);
			while (rs.next()) {
				atrValueId_AttrId.put(rs.getLong(1), rs.getLong(2));
				atrValueId_label.put(rs.getLong(1), rs.getString(3));
			}
			rs.close();
			statement.close();
			connection.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.marktAttrValueIdAndAttributesId = atrValueId_AttrId;
		this.marktAttrValueIdAndValue = atrValueId_label;
	}

	private void setMarktAdId_AttrValueId() {
		HashMap<Long, Long> atrValueId_AdId = new HashMap<Long, Long>();

		ResultSet rs;
		try {
			String connectionURL = "jdbc:mysql://localhost:3306/production_dump";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			String QueryString = "select *  from attribute_chosen_values; ";
			rs = statement.executeQuery(QueryString);
			while (rs.next()) {
				atrValueId_AdId.put(rs.getLong(1), rs.getLong(2));
			}
			rs.close();
			statement.close();
			connection.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.marktAdId_AttrValueId = atrValueId_AdId;
	}

	private HashMap<Long, String> getHashAtr_IdAnd2Labels() {
		return this.attrIdAndValues;

	}

	private List<Long> getHashAtr_IdAndAd_id(Long ad_id) {
		List<Long> attrID = new ArrayList<Long>();
		for(Entry<Long,Long> entry:this.attrIdAndAdId.entrySet())
		{
			if(entry.getValue().equals(ad_id))
			{
				attrID.add((Long) entry.getKey());
			}
		}
		return attrID;
		
	}

	private HashMap<Long, Long> getHashAtr_Value_IdAndAttr_id() {
		return this.attrValueIdAndAttrId;

	}

	private HashMap<Long, String> getHashAtr_Value_IdAndLabel() {
		return this.attrValueIdAndValue;
	}

	private void setHashAtr_IdAnd2Labels() {
		HashMap<Long, String> atr_IdAndValues = new HashMap<Long, String>();

		ResultSet rs;
		try {
			String connectionURL = "jdbc:mysql://localhost:3306/cas_ad_service";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			String QueryString = "select attribute_id,label,backend_name from attributes; ";
			rs = statement.executeQuery(QueryString);
			while (rs.next()) {
				atr_IdAndValues.put(rs.getLong(1),
						rs.getString(2) + ' ' + rs.getString(3));
			}
			rs.close();
			statement.close();
			connection.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("setHashAtr_IdAnd2Labels");
		}
		this.attrIdAndValues = atr_IdAndValues;

	}

	private void setHashAtr_IdAndAd_id() {
		HashMap<Long, Long> atr_IdAndAd_Id = new HashMap<Long, Long>();

		ResultSet rs;
		try {
			String connectionURL = "jdbc:mysql://localhost:3306/cas_ad_service";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			String QueryString = "select ads.id,ad_chosen_attribute_values.attribute_id from ad_chosen_attribute_values,ads where ad_chosen_attribute_values.ad_id=ads.id ; ";
			rs = statement.executeQuery(QueryString);
			while (rs.next()) {
				atr_IdAndAd_Id.put(rs.getLong(2), rs.getLong(1));
			}
			rs.close();
			statement.close();
			connection.close();

		} catch (Exception ex) {
			System.out.println("setHashAtr_IdAndAd_id");
			ex.printStackTrace();
		}

		this.attrIdAndAdId = atr_IdAndAd_Id;

	}

	private void setHashAtr_Value_IdAndAttr_id() {
		HashMap<Long, Long> atr_Value_IdAndAttr_Id = new HashMap<Long, Long>();

		ResultSet rs;
		try {
			String connectionURL = "jdbc:mysql://localhost:3306/cas_ad_service";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			// Add description,price,category_id + not implemented attributes
			String QueryString = "select attribute_value_id, attribute_id from attribute_values ; ";
			rs = statement.executeQuery(QueryString);
			while (rs.next()) {
				atr_Value_IdAndAttr_Id.put(rs.getLong(1), rs.getLong(2));
			}
			rs.close();
			statement.close();
			connection.close();

		} catch (Exception ex) {
			System.out.println("setHashAtr_Value_IdAndAttr_id");
			ex.printStackTrace();
		}
		this.attrValueIdAndAttrId = atr_Value_IdAndAttr_Id;
	}

	private void setHashAtr_Value_IdAndLabel() {
		HashMap<Long, String> atr_ValueAndLabels = new HashMap<Long, String>();

		ResultSet rs;
		try {
			String connectionURL = "jdbc:mysql://localhost:3306/cas_ad_service";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			// Add description,price,category_id + not implemented attributes
			String QueryString = "select attributes.attribute_id, attributes.label, attributes.backend_name from attributes; ";
			rs = statement.executeQuery(QueryString);
			while (rs.next()) {
				atr_ValueAndLabels.put(rs.getLong(1), rs.getString(2) + ' '
						+ rs.getString(3));
			}
			rs.close();
			statement.close();
			connection.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("setHashAtr_Value_IdAndLabel");
		}
		this.attrValueIdAndValue = atr_ValueAndLabels;
	}
	public String getLabelsByAdId(long ad_id) {
		String labels = "";
		for(Long attrId:this.getHashAtr_IdAndAd_id(ad_id))
		{
			labels += " "+ this.getHashAtr_IdAnd2Labels().get(attrId);
		}
		return labels;
	}

	public List<Long> getAttrIdByAdId(long ad_id) {
		return this.getHashAtr_IdAndAd_id(ad_id);
	}

	public String getLabelByAttrId(long attr_id) {
		return this.getHashAtr_Value_IdAndLabel().get(this.getHashAtr_Value_IdAndAttr_id().get(attr_id));
	}

	public String getCasAttributeStringByAdId(long ad_id) {
		String labels = "";
		if(this.attrIdAndAdId.containsValue(ad_id))
		{
			String label = this.getLabelsByAdId(ad_id);
			for(Long attrId:this.getAttrIdByAdId(ad_id))
			{
				labels += " "+getLabelByAttrId(attrId);	
			}
			return label + ' ' + labels;
			
		}
		else
			return " ";
	}
	public String getMarktAttributeStringByAdId(long ad_id) {
		List<Long> attrValueIds = new ArrayList<Long>();
		String attributeString = " ";
		if(this.getMarktAttrValueIdsFromAdId(ad_id).size()>0)
		{
			attrValueIds = getMarktAttrValueIdsFromAdId(ad_id);
			for(Long attrValueId:attrValueIds)
			{
				Long attrId =  getMarktAttrIdFromAtrrValueId(attrValueId);
				attributeString+=" "+getMarktValueFromAttrValueId(attrValueId) +" "+getMarktValueFromAttrValueId(attrId);
			}
		}
		return attributeString;
	}
	
	private String getMarktValueFromAttrValueId(Long attrValueId) {
		return this.marktAttrValueIdAndValue.get(attrValueId);
	}

	private Long getMarktAttrIdFromAtrrValueId(Long attrValueId) {
		return this.marktAttrValueIdAndAttributesId.get(attrValueId);
	}

	private List<Long> getMarktAttrValueIdsFromAdId(long ad_id) {
		List<Long> attrvalueIds = new ArrayList<Long>();
			ResultSet rs;
		try {
			String connectionURL = "jdbc:mysql://localhost:3306/production_dump";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(connectionURL,
					"root", "qwe123");
			Statement statement = connection.createStatement();
			// Add description,price,category_id + not implemented attributes
			String query = "select *  from attribute_chosen_values where ad_id="+ad_id+";";
			rs = statement.executeQuery(query);
			while (rs.next()) {
				attrvalueIds.add(rs.getLong(1));
			}
			rs.close();
			statement.close();
			connection.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return attrvalueIds;

	}

	public List<String> getAttributesWords(){
		return this.attributes;
	}

	public void setAttributesWords(List<String> words) {
		this.attributes = words;
	}

}
