package by.transavto.transavto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActivityChooseLocation extends Activity implements
		InterfaceMyHandler, OnItemClickListener, TextWatcher {

	final Integer NEAREST_REQUEST = 1;
	final Integer SEARCH_REQUEST = 2;
	final String TAG = "LOC_CHOOSE";
	String hash;

	Boolean locationUpdated = false;
	Boolean searchRequestSend = false;

	ListView lvNearest, lvLast, lvSearch;
	ProgressBar pbLoader;
	ArrayList<EntityLocation> itemArrayNearest, itemArrayLast, itemArraySearch;
	AdapterLocationListView itemAdapterNearest, itemAdapterLast,
			itemAdapterSearch;
	ClassMyHandler h;

	private LocationManager locationManager;
	TextView tvLocationGps;
	TextView tvStatusGps;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.choose_location_screen);
		Intent intent = getIntent();
		hash = intent.getStringExtra("hash");
		// Log.d(TAG, "hash: " + hash);

		pbLoader = (ProgressBar) findViewById(R.id.pbLoader);
		lvNearest = (ListView) this.findViewById(R.id.lvNearest);
		lvLast = (ListView) this.findViewById(R.id.lvLast);
		lvSearch = (ListView) this.findViewById(R.id.lvSearchList);

		// lvNearest.setOnScrollListener(this);
		itemArrayNearest = new ArrayList<EntityLocation>();
		itemArrayLast = new ArrayList<EntityLocation>();
		itemArraySearch = new ArrayList<EntityLocation>();

		itemAdapterNearest = new AdapterLocationListView(this, itemArrayNearest);
		itemAdapterLast = new AdapterLocationListView(this, itemArrayLast);
		itemAdapterSearch = new AdapterLocationListView(this, itemArraySearch);

		lvNearest.setAdapter(itemAdapterNearest);
		lvNearest.setOnItemClickListener(this);

		lvLast.setAdapter(itemAdapterLast);
		lvLast.setOnItemClickListener(this);

		lvSearch.setAdapter(itemAdapterSearch);
		lvSearch.setOnItemClickListener(this);

		loadLastLocations();

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		h = new ClassMyHandler(this);

		((Button) findViewById(R.id.btnCancel))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						setResult(RESULT_CANCELED, intent);
						finish();
						return;
					}
				});

		((EditText) findViewById(R.id.etSearch)).addTextChangedListener(this);
		((ListView) findViewById(R.id.lvSearchList)).setVisibility(View.GONE);
		
        // спрячем клавиатуру
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
	}

	protected void onResume() {
		super.onResume();
	//	Log.d(TAG, "OnResume");
		pbLoader.setVisibility(View.GONE);
		// budem zaprashivat' update tolko esli poziciya izmenials' bolee chem
		// na 1km
		if (locationManager.getAllProviders().contains(
				LocationManager.GPS_PROVIDER))
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 1000, locationListener);
		if (locationManager.getAllProviders().contains(
				LocationManager.NETWORK_PROVIDER))
			locationManager
					.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
							0, 1000, locationListener);
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(locationListener);
	}

	private LocationListener locationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
		//	Log.d(TAG, "Location changed " + location.toString());
			sendRequest(NEAREST_REQUEST, "/getnearestcities", "",
					String.valueOf(location.getLatitude()),
 					String.valueOf(location.getLongitude()), true);
			locationUpdated = true;
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
			Location loc = locationManager.getLastKnownLocation(provider);
		//	Log.d(TAG, "Last known provider " + loc.toString());
			sendRequest(NEAREST_REQUEST, "/getnearestcities", "",
					String.valueOf(loc.getLatitude()),
					String.valueOf(loc.getLongitude()), true);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
//			Log.d(TAG, "Status changed");
		}
	};

	@Override
	public void processHandler(Message msg) {
		if (msg.what == NEAREST_REQUEST) {
//			Log.d(TAG, "NEAREST RESULTS");
			// LoadMoreRequest = false;
			pbLoader.setVisibility(View.GONE);
			RequestResult rr;
			rr = (RequestResult) msg.obj;
			// Log.d(TAG,rr.getReqBody());
			processResponce(rr.getReqBody(), false);
		}
		if (msg.what == SEARCH_REQUEST) {
			searchRequestSend = false;
			((LinearLayout) findViewById(R.id.llNearestLayout))
					.setVisibility(View.GONE);
			((LinearLayout) findViewById(R.id.llHistoryLayout))
					.setVisibility(View.GONE);
			((ListView) findViewById(R.id.lvSearchList))
					.setVisibility(View.VISIBLE);

			RequestResult rr;
			rr = (RequestResult) msg.obj;

			appendSearchArray(rr.getReqBody());
		}
	}

	private void appendSearchArray(String reqBody) {
		Dialog dialog = new Dialog(this);
	//	Log.d(TAG, "appendSearch");
		JSONArray json;
		try {
			json = new JSONArray(reqBody);
		} catch (JSONException e) {
	//		Log.d(TAG, e.toString());
			dialog.ErrorDialog("Неверный ответ от сервера");
			return;
		}

		itemArraySearch.clear();
		for (int i = 0; i < json.length(); i++) {
			try {
				JSONObject row = json.getJSONObject(i);
				String id = row.getString("id");
				String flag = row.getString("flag").toLowerCase();
				String country = row.getString("country");
				String region = row.getString("region");
				String city = row.getString("city");
				String distance = row.getString("distance");

				EntityLocation loc = new EntityLocation();
				loc.setAll(id, flag, country, region, city, "");
				itemArraySearch.add(loc);
				// Log.d(TAG,String.valueOf(itemArrayNearest.toString()));
				itemAdapterSearch.notifyDataSetChanged();
			} catch (Exception e) {
			}
			;
		}

	}

	public void processResponce(String result, Boolean addTop) {
		Dialog dialog = new Dialog(this);
		//Log.d(TAG, "processResponce");
		// if (result.length()<=0) return;
		// Log.d(TAG,result);

		JSONArray json;
		try {
			json = new JSONArray(result);
			addNearest(json);
		} catch (JSONException e) {
			//Log.d(TAG, e.toString());
			dialog.ErrorDialog("Неверный ответ от сервера");
			return;
		}
	}

	public void addNearest(JSONArray data) {
		itemArrayNearest.clear();

		for (int i = 0; i < data.length(); i++) {
			try {
				JSONObject row = data.getJSONObject(i);
				String id = row.getString("id");
				String flag = row.getString("flag").toLowerCase();
				String country = row.getString("country");
				String region = row.getString("region");
				String city = row.getString("city");
				String distance = row.getString("distance");

				EntityLocation loc = new EntityLocation();
				loc.setAll(id, flag, country, region, city, distance + "км");
				itemArrayNearest.add(loc);
				// Log.d(TAG,String.valueOf(itemArrayNearest.toString()));
				itemAdapterNearest.notifyDataSetChanged();
			} catch (Exception e) {
			}
			;
		}
	}

	public void sendRequest(Integer handlerWhat, String url, String sq,
			String lat, String lng, Boolean switchSpinner) {
		// Log.d(TAG, "Request lat:" + lat + " lng:" + lng);
		if (locationUpdated && sq.length() == 0)
			return;
		else {
			if (switchSpinner)
				pbLoader.setVisibility(View.VISIBLE);
		}

		final class SendRequestThread extends Thread {
			Integer handlerWhat;
			String lat;
			String lng;
			String url;
			String sq;
			Activity parent;

			public SendRequestThread(Activity parent, Integer handlerWhat,
					String url, String sq, String lat, String lng) {
				this.handlerWhat = handlerWhat;
				this.lat = lat;
				this.lng = lng;
				this.url = url;
				this.sq = sq;
				this.parent = parent;
			}

			public void run() {
				RequestInfo request = new RequestInfo();
				request.addKeyValue("hash", hash);
				if (this.sq.length() > 0) {
					request.addKeyValue("sq", this.sq);
				} else {
					request.addKeyValue("lat", this.lat);
					request.addKeyValue("lng", this.lng);
				}

				//Log.d(TAG, String.valueOf(request.requestData));
				request.setReqMethod("sync");
				request.setUrl(this.url);
				request.setMethod("post");
				Http http_client = new Http(this.parent);
				RequestResult result = http_client.execute(request,0);
				h.sendMessage(Message.obtain(h, this.handlerWhat, 0, 0, result));
			}

		}
		// Log.d(TAG,"Sending request1");
		if (switchSpinner)
			pbLoader.setVisibility(View.VISIBLE);
		// Log.d(TAG,"Sending request2");
		SendRequestThread t = new SendRequestThread(this, handlerWhat, url, sq,
				lat, lng);
		// Log.d(TAG,"Sending request3");
		t.start();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		EntityLocation l = new EntityLocation();

		switch (parent.getId()) {

		case R.id.lvNearest:
			l = itemArrayNearest.get(position);
			break;
		case R.id.lvLast:
			l = itemArrayLast.get(position);
			break;
		case R.id.lvSearchList:
			l = itemArraySearch.get(position);
			break;
		}
		saveLocationToDb(l);
		//Log.d(TAG, l.getLocName(true) + " " + l.getId());
		Intent intent = new Intent();
		intent.putExtra("location", l);
		setResult(RESULT_OK, intent);
		finish();
		return;

	}

	public void loadLastLocations() {
		ClassDB dbh = new ClassDB(this);
		SQLiteDatabase db = dbh.open();
		Cursor c = db.rawQuery(
				"select * from last_locations order by time desc limit 10",
				null);
		//Log.d(TAG, "loadedLoc:" + c.getCount());
		if (c.moveToFirst()) {

			// определяем номера столбцов по имени в выборке
			int idIndex = c.getColumnIndex("locId");
			int flagIndex = c.getColumnIndex("flag");
			int countryIndex = c.getColumnIndex("country");
			int regionIndex = c.getColumnIndex("region");
			int cityIndex = c.getColumnIndex("city");
			int timeIndex = c.getColumnIndex("time");
			int minTime = 0;
			String date = "";
			do {

				try {
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
					Long time = c.getLong(timeIndex) * 1000;
					Date netDate = new Date(time);
					//Log.d(TAG, String.valueOf(time) + " "+c.getString(cityIndex));
					date = sdf.format(netDate);
				} catch (Exception ex) {
					date = "";
				}
				if (minTime == 0)
					minTime = c.getInt(timeIndex);
				else
					minTime = Math.min(minTime, c.getInt(timeIndex));
				EntityLocation loc = new EntityLocation();
				loc.setAll(c.getString(idIndex), c.getString(flagIndex),
						c.getString(countryIndex), c.getString(regionIndex),
						c.getString(cityIndex), date);
				itemArrayLast.add(loc);
				itemAdapterLast.notifyDataSetChanged();
			} while (c.moveToNext());
			if (minTime != 0) {
				db.delete("last_locations", "time<" + String.valueOf(minTime),
						null);
			}
		}

		dbh.close();
		c.close();
	}

	public void saveLocationToDb(EntityLocation loc) {
		ClassDB dbh = new ClassDB(this);
		SQLiteDatabase db = dbh.open();
		ContentValues cv = new ContentValues();
		Long tsLong = System.currentTimeMillis() / 1000;
		String ts = tsLong.toString();

		Cursor c = db.rawQuery("select id from last_locations where locId='"
				+ loc.getId() + "'", null);

//		Log.d(TAG, ts);
		cv.put("time", ts);
		if (!c.moveToFirst()) { //нет ID
			cv.put("locId", loc.getId());
			cv.put("flag", loc.getFlag());
			cv.put("country", loc.getCountry());
			cv.put("region", loc.getRegion());
			cv.put("city", loc.getCity());
			// вставляем запись и получаем ее ID
			long rowID = db.insert("last_locations", null, cv);
			//Log.d(TAG, "row inserted, ID = " + rowID);
		}
		else {
			int id = c.getInt( c.getColumnIndex("id") );
			int updCount = db.update("last_locations", cv, "id = ?", new String[] { String.valueOf(id) });
//			Log.d(TAG,"id "+String.valueOf(id)+" updated "+String.valueOf(updCount));
		}
		c.close();
		dbh.close();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		//Log.d(TAG, "onKey");
		EditText et = (EditText) findViewById(R.id.etSearch);
		if (et.getText().length() >= 3) {
			if (!searchRequestSend) {
				searchRequestSend = true;
				//Log.d(TAG, String.valueOf(et.getText().length()));
				sendRequest(SEARCH_REQUEST, "/autocompleteApp", et.getText()
						.toString(), "", "", false);
			}
		} else {
			((ListView) findViewById(R.id.lvSearchList))
					.setVisibility(View.GONE);
			((LinearLayout) findViewById(R.id.llNearestLayout))
					.setVisibility(View.VISIBLE);
			((LinearLayout) findViewById(R.id.llHistoryLayout))
					.setVisibility(View.VISIBLE);
		}
		return;
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

}