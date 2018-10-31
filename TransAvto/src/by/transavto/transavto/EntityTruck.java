package by.transavto.transavto;

import org.json.JSONObject;

import android.util.Log;

public class EntityTruck {
	
	protected String id = "";
	
	protected EntityLocation from = new EntityLocation();
	protected EntityLocation to = new EntityLocation();
	protected EntityFirm firm = new EntityFirm();
	protected EntityUser user = new EntityUser();

	protected String to_any_direction = "0";
	
	protected String dates = "";
	protected String truck = "";
	protected String truck_type = "0";
	protected String trucks_num = "0";
	protected String volume = "0";
	protected String weight = "0";
	protected String description = "";
	
	protected String truck_length = "0.00";
	protected String truck_width = "0.00";
	protected String truck_height = "0.00";
	

	protected String lt_up = "0";
	protected String lt_side = "0";
	protected String lt_back = "0";
	protected String lt_tent = "0";
	protected String lt_stoika = "0";

	protected String price_type = "0";
	protected String price = "0.00";
	protected String currency = "0";
	protected String pryam_dogovor = "0";
	
	protected String activity = "0";
	
	protected String date_of = "";
	
	public EntityTruck() {};
	
	public EntityTruck(JSONObject data) {
		try {setId(data.getString("id"));
		} catch (Exception e) {}


		try {setFrom(data.getJSONObject("from"));
		} catch (Exception e) {}
		try {setTo(data.getJSONObject("to"));
		} catch (Exception e) {}

		try {setToAnyDirection(data.getString("to_any_direction"));
		} catch (Exception e) {}

		
		try {setDates(data.getString("dates"));
		} catch (Exception e) {}
		try {setTruck(data.getString("truck"));
		} catch (Exception e) {}

		try {setTruckType(data.getString("truck_type"));
		} catch (Exception e) {}
		try {setTrucksNum(data.getString("trucks_num"));
		} catch (Exception e) {}
		try {setVolume(data.getString("volume"));
		} catch (Exception e) {}
		try {setWeight(data.getString("weight"));
		} catch (Exception e) {}
		try {setDescription(data.getString("description"));
		} catch (Exception e) {}

		try {setFirm(data.getJSONObject("firm"));
		} catch (Exception e) {}
		try {setUser(data.getJSONObject("user"));
		} catch (Exception e) {}

		try {setTruckLength(data.getString("truck_length"));
		} catch (Exception e) {}
		try {setTruckWidth(data.getString("truck_width"));
		} catch (Exception e) {}
		try {setTruckHeight(data.getString("truck_height"));
		} catch (Exception e) {}

		try {setLtUp(data.getString("load_type_up"));
		} catch (Exception e) {}
		try {setLtBack(data.getString("load_type_back"));
		} catch (Exception e) {}
		try {setLtSide(data.getString("load_type_side"));
		} catch (Exception e) {}
		try {setLtTent(data.getString("load_type_tent"));
		} catch (Exception e) {}
		try {setLtStoika(data.getString("load_type_stoika"));
		} catch (Exception e) {}

		try {setPriceType(data.getString("price_type"));
		} catch (Exception e) {}
		try {setPrice(data.getString("price"));
		} catch (Exception e) {}
		try {setCurrencyType(data.getString("currency_type"));
		} catch (Exception e) {}
		try {setPryamDogovor(data.getString("pryam_dogovor"));
		} catch (Exception e) {}

		try {setActivity(data.getString("activity_type"));
		} catch (Exception e) {}

		try {setDateOf(data.getString("date_of"));
		} catch (Exception e) {}

	}

	public void setId(String vl){
		this.id = vl.trim().equalsIgnoreCase("null")?"":vl.trim().toLowerCase();
	}
	public String getId() {
		return this.id;
	}

	public void setFrom(JSONObject json) {
		this.from = new EntityLocation(json);
	}
	public EntityLocation getFrom(){
		return from;
	}
	
	public void setTo(JSONObject json) {
		this.to = new EntityLocation(json);
	}
	public EntityLocation getTo(){
		return to;
	}

	public void setFirm(JSONObject json){
		this.firm = new EntityFirm(json);
	}
	public EntityFirm getFirm(){
		return this.firm;
	}

	public String getToTxt(int length){
		String ret=getToTxt(true);
		if (ret.length()>length+3)
			ret = ret.substring(0,length)+"...";
		
		return ret;
	}
	public String getToTxt(boolean withCountry) {
		String ret = "";
		if (getToAnyDirection().equalsIgnoreCase("1"))
			return "любое направление";
		return getTo().getLocName(withCountry);
	}

	public void setToAnyDirection(String vl){
		this.to_any_direction = vl.trim().equalsIgnoreCase("null")?"":vl.trim();
	}
	public String getToAnyDirection(){
		return this.to_any_direction;
	}

	
	public void setDates(String vl){
		this.dates = vl.trim().equalsIgnoreCase("null")?"":vl.trim();
	}
	public String getDates(){
		return this.dates;
	}

	public void setTruck(String vl){
		this.truck = vl.trim().equalsIgnoreCase("null")?"":vl.trim();
	}
	public String getTruck(){
		return this.truck;
	}

	
	public void setTruckType(String vl){
		this.truck_type = vl.trim().equalsIgnoreCase("null")?"":vl.trim();
	}
	public String getTruckType(){
		return this.truck_type;
	}
	public void setTrucksNum(String vl){
		this.trucks_num = vl.trim().equalsIgnoreCase("null")?"":vl.trim();
	}
	public String getTrucksNum(){
		return this.trucks_num;
	}

	public void setVolume(String vl){
		this.volume = vl.trim().equalsIgnoreCase("null")?"":vl.trim();
	}
	public String getVolume(){
		return this.volume;
	}
	public void setWeight(String vl){
		this.weight = vl.trim().equalsIgnoreCase("null")?"":vl.trim();
	}
	public String getWeight(){
		return this.weight;
	}

	public void setDescription(String vl){
		this.description = vl.trim().equalsIgnoreCase("null")?"":vl.trim();
	}
	public String getDescription(){
		return this.description;
	}

	
	public String getFullDesc(int len){
		String ret=getFullDesc();
		if (ret.length()>(len-3))
			ret = ret.substring(0, len-3)+"...";
		return ret;
	}
	public String getFullDesc() {
		String ret="";
		
		if (getTruckType().length()>0)
			ret += getTruckType()+" ";
		if (getVolume().length()>0)
			ret += getVolume()+"м3 ";
		if (getWeight().length()>0)
			ret += getWeight()+"т ";
		if (getDescription().length()>0)
			ret += getDescription();
		
		return ret;
	}

	
	public void setTruckLength(String vl){
		try {
			Float.parseFloat(vl);
			this.truck_length = vl;
		}
		catch (Exception e) {
			this.truck_length = "0.00";
		}
	}
	public String getTruckLength() {
		return this.truck_length;
	}
	public void setTruckWidth(String vl){
		try {
			Float.parseFloat(vl);
			this.truck_width = vl;
		}
		catch (Exception e) {
			this.truck_width = "0.00";
		}
	}
	public String getTruckWidth() {
		return this.truck_width;
	}
	public void setTruckHeight(String vl){
		try {
			Float.parseFloat(vl);
			this.truck_height = vl;
		}
		catch (Exception e) {
			this.truck_height = "0.00";
		}
	}
	public String getTruckHeight() {
		return this.truck_height;
	}
	public String getTruckDimensions() {
		String h,l,w;
		h = getTruckHeight();
		w = getTruckWidth();
		l = getTruckLength();
		
		String ret="";
	
		Log.d("TE","h "+String.valueOf(h)+" w "+String.valueOf(w)+ " l "+String.valueOf(l));
		if (Float.parseFloat(l)>0) ret += "Д:"+h+" X ";
		if (Float.parseFloat(w)>0) ret += "Ш:"+h+" X ";
		if (Float.parseFloat(h)>0) ret += "В:"+h+" X ";
		
		if (ret.length()>0) ret = ret.substring(0, ret.length()-3).trim();
		return ret;
	}

	public void setLtUp(String vl){
		this.lt_up = vl.trim().equalsIgnoreCase("null")?"":"1";
	}
	public String getLtUp() {
		return this.lt_up;
	}
	public void setLtBack(String vl){
		this.lt_back = vl.trim().equalsIgnoreCase("null")?"":"1";
	}
	public String getLtBack() {
		return this.lt_back;
	}
	public void setLtStoika(String vl){
		this.lt_stoika = vl.trim().equalsIgnoreCase("null")?"":"1";
	}
	public String getLtStoika() {
		return this.lt_stoika;
	}
	public void setLtSide(String vl){
		this.lt_side = vl.trim().equalsIgnoreCase("null")?"":"1";
	}
	public String getLtSide() {
		return this.lt_side;
	}
	public void setLtTent(String vl){
		this.lt_tent = vl.trim().equalsIgnoreCase("null")?"":"1";
	}
	public String getLtTent() {
		return this.lt_tent;
	}
	public String getZagruz() {
		String ret="";
		
		if (getLtBack().length()>0) ret+="зад,";
		if (getLtSide().length()>0) ret+="бок,";
		if (getLtUp().length()>0) ret+="верх,";
		if (getLtTent().length()>0) ret+="растентовка,";
		if (getLtStoika().length()>0) ret+="снятие стоек,";
		
		if (ret.length()>0) ret = ret.substring(0, ret.length()-1).trim();

		return ret;
	}


	public void setPriceType(String vl){
		this.price_type = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getPriceType() {
		return this.price_type;
	}
	public void setPrice(String vl){
		try {
			Float.parseFloat(vl);
			this.price = vl;
		}
		catch (Exception e) {
			this.price = "0.00";
		}
	}
	public String getPrice() {
		return this.price;
	}
	public void setCurrencyType(String vl){
		this.currency = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getCurrencyType() {
		return this.currency;
	}	
	public void setPryamDogovor(String vl){
		this.pryam_dogovor = vl.trim().equalsIgnoreCase("null")?"":"1";
	}
	public String getPryamDogovor() {
		return this.pryam_dogovor;
	}

	public String getFullPrice(){
		if (Float.parseFloat(getPrice())<=0)	
			return getPryamDogovor().equalsIgnoreCase("0")?"":" прям. дог.";
		return getPrice().trim()+" "+getCurrencyType() + " "+(getPriceType().equalsIgnoreCase("0")?"":getPriceType()) + (getPryamDogovor().equalsIgnoreCase("0")?"":" прям. дог.");
	}

	public void setUser(JSONObject json){
		this.user = new EntityUser(json);
	}
	public EntityUser getUser(){
		return this.user;
	}


	public void setActivity(String vl){
		this.activity = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getActivity() {
		return this.activity;
	}

	public void setDateOf(String vl){
		this.date_of = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getDateOf() {
		return this.date_of;
	}

}

