package by.transavto.transavto;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class EntityFirm {
	
	protected String firmId="";
	protected String firmName="";
	protected EntityLocation firmLoc = new EntityLocation();
	protected String firmAddress="";
	protected String firmFax="";
	protected String firmDescription="";
	protected String firmRating="0.00";
	protected String firmDate="0";
	protected String firmUnp = "";
	
	protected String firm_cargos = "0";
	protected String firm_trucks = "0";
	protected String firm_fb_pos = "0";
	protected String firm_fb_neg = "0";
	protected String firm_np = "0";
	protected String firm_partners = "0";
	protected String firm_blacklist = "0";

	protected ArrayList<EntityUser> users = new ArrayList<EntityUser>();
	
	public EntityFirm() {}
	
	public EntityFirm(JSONObject data) {

		try {setId(data.getString("firm_id"));
		} catch (Exception e) {}
		try {setFirmName(data.getString("firm_name"));
		} catch (Exception e) {}
		try {setFirmRating(data.getString("firm_rating"));
		} catch (Exception e) {}
		try {setFirmLoc(data.getJSONObject("firm_loc"));
		} catch (Exception e) {}
		try {setFirmAddress(data.getString("firm_address"));
		} catch (Exception e) {}
		try {setFirmFax(data.getString("firm_fax"));
		} catch (Exception e) {}
		try {setFirmDescription(data.getString("firm_description"));
		} catch (Exception e) {}
		try {setFirmDate(data.getString("firm_date"));
		} catch (Exception e) {}
		try {setFirmUnp(data.getString("firm_unp"));
		} catch (Exception e) {}

		try {setFirmCargos(data.getString("firm_cargos"));
		} catch (Exception e) {}
		try {setFirmTrucks(data.getString("firm_trucks"));
		} catch (Exception e) {}
		try {setFirmFbPos(data.getString("firm_fb_pos"));
		} catch (Exception e) {}
		try {setFirmFbNeg(data.getString("firm_fb_neg"));
		} catch (Exception e) {}
		try {setFirmPartners(data.getString("firm_partners"));
		} catch (Exception e) {}
		try {setFirmBlackList(data.getString("firm_blacklist"));
		} catch (Exception e) {}
		try {setFirmNp(data.getString("firm_np"));
		} catch (Exception e) {}
		try {setUsers(data.getJSONArray("users"));
		} catch (Exception e) {}


	}

	public void setUsers(JSONArray users) {
		for(int i=0;i<users.length();i++) {
			try {
			JSONObject row = users.getJSONObject(i);
			EntityUser user = new EntityUser(row);
			this.users.add(user);
			} catch (Exception e){}
		}
		
	}
	public ArrayList<EntityUser> getUsers(){
		return this.users;
	}
	public EntityUser getContactPerson() {
		for(int i=0;i<this.users.size();i++) {
			if (this.users.get(i).getContact().equalsIgnoreCase("1"))
				return this.users.get(i);
		}
		return new EntityUser();
	}
	
	public void setId(String vl){
		this.firmId = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getId() {
		return this.firmId;
	}
	public void setFirmName(String vl){
		this.firmName = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getFirmName() {
		return this.firmName;
	}
	public void setFirmLoc(JSONObject vl){
		this.firmLoc = new EntityLocation(vl);
	}
	public EntityLocation getFirmLoc() {
		return this.firmLoc;
	}
	public void setFirmAddress(String vl){
		this.firmAddress = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getFirmAddress() {
		return this.firmAddress;
	}
	public void setFirmFax(String vl){
		this.firmFax = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getFirmFax() {
		return this.firmFax;
	}
	public void setFirmDescription(String vl){
		this.firmDescription = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getFirmDescription() {
		return this.firmDescription;
	}
	public void setFirmRating(String vl){
		try{
		   this.firmRating = String.valueOf(Float.parseFloat(vl));
		} catch (Exception e) { this.firmRating = "0.00";}
	}
	public String getFirmRating() {
		return this.firmRating;
	}
	public String getFirmRatingResourceName() {
		String tmp="rating";
		if (Float.parseFloat(getFirmRating())<0)
			tmp += "_neg";
		tmp += "_"+getFirmRating().replace('.', '_');
		return tmp;
	}
	
	
	public void setFirmDate(String vl){
		this.firmDate = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getFirmDate() {
		return this.firmDate;
	}
	public void setFirmUnp(String vl){
		this.firmUnp = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getFirmUnp() {
		return this.firmUnp;
	}


	
	
	public void setFirmCargos(String vl){
		this.firm_cargos = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getFirmCargos() {
		return this.firm_cargos;
	}
	public void setFirmTrucks(String vl){
		this.firm_trucks = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getFirmTrucks() {
		return this.firm_trucks;
	}
	public void setFirmFbNeg(String vl){
		this.firm_fb_neg = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getFirmFbNeg() {
		return this.firm_fb_neg;
	}
	public void setFirmFbPos(String vl){
		this.firm_fb_pos = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getFirmFbPos() {
		return this.firm_fb_pos;
	}
	public void setFirmPartners(String vl){
		this.firm_partners = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getFirmPartners() {
		return this.firm_partners;
	}
	public void setFirmBlackList(String vl){
		this.firm_blacklist = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getFirmBlackList() {
		return this.firm_blacklist;
	}
	public void setFirmNp(String vl){
		this.firm_np = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getFirmNp() {
		return this.firm_np;
	}


}
