package by.transavto.transavto;

public class EntityChatMessage {
	Integer firmCountry;
	String firmName = "";
	String mesDate = "";
	String message = "";

	public void setFirmCountry(Integer res_id) {
		this.firmCountry = res_id;
	}

	public void setFirmName(String txt) {
		this.firmName = txt;
	}

	public void setMesDate(String txt) {
		this.mesDate = txt;
	}

	public void setMessage(String txt) {
		this.message = txt;
	}

	public void setAll(String firmName, Integer firmCountry_res,
			String mesDate, String message) {
		setFirmName(firmName);
		setFirmCountry(firmCountry_res);
		setMesDate(mesDate);
		setMessage(message);
	}

}
