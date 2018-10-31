package by.transavto.transavto;

import org.json.JSONObject;

public class EntityUser {

	protected String id = "";
	protected String firstName = "";
	protected String lastName = "";
	protected String middleName = "";
	protected String skype = "";
	protected String tel1_op = "";
	protected String tel1 = "";
	protected String tel2_op = "";
	protected String tel2 = "";
	protected String tel3_op = "";
	protected String tel3 = "";
	protected String tel4_op = "";
	protected String tel4 = "";
	protected String contact = "0";
	
	public EntityUser(){};
	
	public EntityUser(JSONObject data) {
		try {setFirstName(data.getString("first_name"));
		} catch (Exception e) {}
		try {setLastName(data.getString("last_name"));
		} catch (Exception e) {}
		try {setMiddleName(data.getString("middle_name"));
		} catch (Exception e) {}

		try {setId(data.getString("id"));
		} catch (Exception e) {}
		try {setSkype(data.getString("skype"));
		} catch (Exception e) {}
		try {setTel1(data.getString("tel1"));
		} catch (Exception e) {}
		try {setTel1Op(data.getString("tel1_op"));
		} catch (Exception e) {}
		try {setTel2(data.getString("tel2"));
		} catch (Exception e) {}
		try {setTel2Op(data.getString("tel2_op"));
		} catch (Exception e) {}
		try {setTel3(data.getString("tel3"));
		} catch (Exception e) {}
		try {setTel3Op(data.getString("tel3_op"));
		} catch (Exception e) {}
		try {setTel4(data.getString("tel4"));
		} catch (Exception e) {}
		try {setTel4Op(data.getString("tel4_op"));
		} catch (Exception e) {}

		try {setContact(data.getString("contact"));
		} catch (Exception e) {}

	}
	
	public void setId(String vl){
		this.id = vl.trim().equalsIgnoreCase("null")?"":vl.trim();
	}
	public String getId() {
		return this.id;
	}
	
	public void setFirstName(String vl){
		this.firstName = vl.trim().equalsIgnoreCase("null")?"":vl.trim();
	}
	public String getFirstName() {
		return this.firstName;
	}
	
	public void setLastName(String vl){
		this.lastName = vl.trim().equalsIgnoreCase("null")?"":vl.trim();
	}
	public String getLastName() {
		return this.lastName;
	}
	
	public void setMiddleName(String vl){
		this.middleName = vl.trim().equalsIgnoreCase("null")?"":vl.trim();
	}
	public String getMiddleName() {
		return this.middleName;
	}
	
	public void setSkype(String vl){
		this.skype = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getSkype() {
		return this.skype;
	}	
	public void setTel1(String vl){
		this.tel1 = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getTel1() {
		return this.tel1;
	}	
	public void setTel2(String vl){
		this.tel2 = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getTel2() {
		return this.tel2;
	}	
	public void setTel3(String vl){
		this.tel3 = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getTel3() {
		return this.tel3;
	}	
	public void setTel4(String vl){
		this.tel4 = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getTel4() {
		return this.tel4;
	}	
	public void setTel1Op(String vl){
		this.tel1_op = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getTel1Op() {
		return this.tel1_op;
	}	
	public void setTel2Op(String vl){
		this.tel2_op = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getTel2Op() {
		return this.tel2_op;
	}	
	public void setTel3Op(String vl){
		this.tel3_op = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getTel3Op() {
		return this.tel3_op;
	}	
	public void setTel4Op(String vl){
		this.tel4_op = vl.trim().equalsIgnoreCase("null")?"":vl;
	}
	public String getTel4Op() {
		return this.tel4_op;
	}

	public void setContact(String vl){
		this.contact = vl.trim().equalsIgnoreCase("1")?"1":"0";
	}
	public String getContact() {
		return this.contact;
	}


	public String getFullName(boolean midName){
		String ret = "";
		if (!this.lastName.equalsIgnoreCase(""))
			ret += this.lastName + " ";
		if (!this.firstName.equalsIgnoreCase(""))
			ret += this.firstName+ " ";
		if (!this.middleName.equalsIgnoreCase("") && midName)
			ret += this.middleName;
		
		return ret.trim();
	}
	
}
