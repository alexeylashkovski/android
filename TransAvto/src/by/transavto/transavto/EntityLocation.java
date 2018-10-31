package by.transavto.transavto;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class EntityLocation implements Parcelable {
	protected String id = "";
	protected String flag = "";
	protected String country = "";
	protected String region = "";
	protected String city = "";
	protected String distance = "";

	public EntityLocation() {
		return;
	}
	
	public EntityLocation(JSONObject data) {
		try {setId(data.getString("id"));
		} catch (Exception e) {}
		try {setFlag(data.getString("flag"));
		} catch (Exception e) {}
		try {setRegion(data.getString("region"));
		} catch (Exception e) {}
		try {setCountry(data.getString("country"));
		} catch (Exception e) {}
		try {setCity(data.getString("city"));
		} catch (Exception e) {}
		try {setDistance(data.getString("distance"));
		} catch (Exception e) {}
	}

	// конструктор, считывающий данные из Parcel
	private EntityLocation(Parcel dest) {
		this.id = dest.readString();
		this.flag = dest.readString();
		this.country = dest.readString();
		this.region = dest.readString();
		this.city = dest.readString();
		this.distance = dest.readString();
	}
	
	public boolean isEmpty() {
		return (id.equals("") && country.equals("") && region.equals("") && city.equals(""));
	}

	public void setAll(String id, String flag, String country, String region,
			String city, String distance) {
		setId(id);
		setFlag(flag);
		setCountry(country);
		setRegion(region);
		setCity(city);
		setDistance(distance);
	}

	public void setId(String id) {
		this.id = id.trim().equalsIgnoreCase("null")?"":id;
	}

	public String[] getIdArray() {
		String data[] = new String[3];
		String dt[] = this.getId().split("!");
		for(int i=0;i<3;i++) {
			if (i<dt.length) data[i]=dt[i];
			else data[i]="";
		}
		return data;
	}
	public String getId() {
		return this.id;
	}

	public void setFlag(String flag) {
		this.flag = flag.trim().equalsIgnoreCase("null")?"":flag.toLowerCase();
	}

	public String getFlag() {
		return this.flag;
	}

	public void setCountry(String country) {
		this.country = country.trim().equalsIgnoreCase("null")?"":country;
	}

	public String getCountry() {
		return this.country;
	}

	public void setRegion(String region) {
		this.region = region.trim().equalsIgnoreCase("null")?"":region;
	}

	public String getRegion() {
		return this.region;
	}

	public void setCity(String city) {
		this.city = city.trim().equalsIgnoreCase("null")?"":city;
	}

	public String getCity() {
		return this.city;
	}

	public void setDistance(String dist) {
		this.distance = dist.trim().equalsIgnoreCase("null")?"":dist;
	}

	public String getDistance() {
		return this.distance;
	}

	public String getLocName(Boolean withCountry) {
		String ret = "";
		if (this.region.length()==0 && this.city.length()==0)
			return this.country;
		if (this.country.length() > 0 && withCountry)
			ret = this.country;
		if (ret.length() > 0 && this.region.length() > 0)
			ret += ", ";
		if (this.region.length() > 0)
			ret += this.region;
		if (ret.length() > 0 && this.city.length() > 0)
			ret += ", ";
		if (this.city.length() > 0)
			ret += this.city;

		return ret;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.flag);
		dest.writeString(this.country);
		dest.writeString(this.region);
		dest.writeString(this.city);
		dest.writeString(this.distance);
	}

	public static final Parcelable.Creator<EntityLocation> CREATOR = new Parcelable.Creator<EntityLocation>() {
		// распаковываем объект из Parcel
		public EntityLocation createFromParcel(Parcel in) {
			return new EntityLocation(in);
		}

		public EntityLocation[] newArray(int size) {
			return new EntityLocation[size];
		}
	};

}
