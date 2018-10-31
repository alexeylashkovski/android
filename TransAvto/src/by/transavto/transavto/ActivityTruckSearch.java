package by.transavto.transavto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActivityTruckSearch extends Activity implements OnScrollListener,OnItemClickListener,
		InterfaceMyHandler {

	final String TAG = "TruckSearch";
	final Integer REQUEST_RESULT = 1;
	final Integer NEW_MESSAGE_RESULT = 2;

	int pg = 0;
	
	Boolean LoadMoreRequest = false;
	String hash = "";
	final Activity chatActivity = this;
	
	EntityLocation locFrom1 = new EntityLocation();
	EntityLocation locTo1 = new EntityLocation();
	Long dateFrom = 0L;
	Long dateTo = 0L;
	String truckType = "0";
	String loadType = "up,side,back,tent,stoika";
	String weightFrom = "";
	String weightTo = "";
	String volumeFrom = "";
	String volumeTo = "";
	String lengthFrom = "";
	String lengthTo = "";
	String heightFrom = "";
	String heightTo = "";
	String widthFrom = "";
	String widthTo = "";
	String firmId = "";
	int total_ads = 0;
	boolean footerAdded =  false;

	ListView lvItem;
	ProgressBar pbSpinner;
	ArrayList<EntityTruck> itemArray;
	AdapterTruckListView itemAdapter;
	TextView totalCargos;
	
	LinearLayout infoBox;
	
	ClassMyHandler h;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cargo_truck_search_screen);
		Intent intent = getIntent();

		this.hash = intent.getStringExtra("hash");
		if (intent.hasExtra("locFrom1"))
			this.locFrom1 = intent.getParcelableExtra("locFrom1");
		if (intent.hasExtra("locTo1"))
			this.locTo1 = intent.getParcelableExtra("locTo1");
		if (intent.hasExtra("truckType"))
			this.truckType = intent.getStringExtra("truckType");
		if (intent.hasExtra("dateFrom"))
			this.dateFrom = intent.getLongExtra("dateFrom",0L);
		if (intent.hasExtra("dateTo"))
			this.dateTo = intent.getLongExtra("dateTo",0L);
		if (intent.hasExtra("weightFrom"))
			this.weightFrom = intent.getStringExtra("weightFrom");
		if (intent.hasExtra("weightTo"))
			this.weightTo = intent.getStringExtra("weightTo");
		if (intent.hasExtra("volumeFrom"))
			this.volumeFrom = intent.getStringExtra("volumeFrom");
		if (intent.hasExtra("volumeTo"))
			this.volumeTo = intent.getStringExtra("volumeTo");
		if (intent.hasExtra("lengthFrom"))
			this.lengthFrom = intent.getStringExtra("lengthFrom");
		if (intent.hasExtra("lengthTo"))
			this.lengthTo = intent.getStringExtra("lengthTo");
		if (intent.hasExtra("heightFrom"))
			this.heightFrom = intent.getStringExtra("heightFrom");
		if (intent.hasExtra("heightTo"))
			this.heightTo = intent.getStringExtra("heightTo");
		if (intent.hasExtra("widthFrom"))
			this.widthFrom = intent.getStringExtra("widthFrom");
		if (intent.hasExtra("widthTo"))
			this.widthTo = intent.getStringExtra("widthTo");
		if (intent.hasExtra("loadType"))
			this.loadType = intent.getStringExtra("loadType");

		if (intent.hasExtra("firmId"))
			this.firmId = intent.getStringExtra("firmId");
		
		//Log.d(TAG,"firmId:"+ String.valueOf(firmId));
		//Log.d(TAG,locFrom1.getId()+" "+locFrom1.getCity());
		//Log.d(TAG,locTo1.getId()+" "+locTo1.getCity());

		pbSpinner = (ProgressBar) findViewById(R.id.pbAdsSpinner);
		lvItem = (ListView) this.findViewById(R.id.lvAds);
		lvItem.setVisibility(View.GONE);
		lvItem.setOnScrollListener(this);
		
		View v = getLayoutInflater().inflate(R.layout.cargo_list_header, null);
		lvItem.addHeaderView(v, "", false);
		totalCargos = ((TextView)v.findViewById(R.id.tvAdsFound));

		
		itemArray = new ArrayList<EntityTruck>();
		itemAdapter = new AdapterTruckListView(this, itemArray);
		lvItem.setAdapter(itemAdapter);
		lvItem.setOnItemClickListener(this);

		h = new ClassMyHandler(this);

		infoBox = (LinearLayout)findViewById(R.id.llCargoSearchInfo);
		infoBox.setVisibility(View.GONE);

		sendRequest(REQUEST_RESULT, true);
	}


	public void processResponce(String result, Boolean addTop) {
		Dialog dialog = new Dialog(this);

		JSONObject json;
		JSONArray data;
		try {
			json = new JSONObject(result);
			
			this.total_ads = json.getInt("total_records");
			if (total_ads==0)
				infoBox.setVisibility(View.VISIBLE);
			else {
				totalCargos.setText("Найдено машин: "+String.valueOf(this.total_ads));
				lvItem.setVisibility(View.VISIBLE);
			}

			if (this.total_ads>0) {
				data = json.getJSONArray("data");
				for (int i = 0; i < data.length(); i++) {
					try {
						JSONObject row = data.getJSONObject(i);
						EntityTruck cg = new EntityTruck(row);
						itemArray.add(cg);

					} catch (Exception e) {};
				}
			}
		} catch (Exception e) {
			//Log.d(TAG, e.toString());
			dialog.ErrorDialog("Неверный ответ от сервера");
			//finish();
			return;
		}
		itemAdapter.notifyDataSetChanged();
		
	}

	public void sendRequest(Integer handlerWhat, Boolean switchSpinner) {
		final class SendRequestThread extends Thread {
			Integer handlerWhat;
			Activity parent;
			RequestInfo request;

			public SendRequestThread(Activity parent, Integer handlerWhat, RequestInfo request) {
				this.handlerWhat = handlerWhat;
				this.parent = parent;
				this.request = request;
			}

			public void run() {
				request.setReqMethod("sync");
				request.setUrl("/trucksearchApp?pg="+String.valueOf(pg));
				request.setMethod("post");
				//Log.d(TAG,request.requestData.toString());
				Http http_client = new Http(this.parent);
				RequestResult result = http_client.execute(request,0);
				h.sendMessage(Message.obtain(h, this.handlerWhat, 0, 0, result));
			}

		}
		if (switchSpinner)
			pbSpinner.setVisibility(View.VISIBLE);

		RequestInfo request = new RequestInfo();
		fillRequestParams(request);
		SendRequestThread t = new SendRequestThread(chatActivity, handlerWhat, request);
		t.start();

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		boolean loadMore = (firstVisibleItem + visibleItemCount >= totalItemCount)
				&& (totalItemCount > visibleItemCount) && (totalItemCount<total_ads);

		if (loadMore && !LoadMoreRequest && totalItemCount<total_ads) {
			LoadMoreRequest = true;
			pg ++;
			sendRequest(REQUEST_RESULT, true);
		}
		
		//Log.d(TAG,"TIC: "+String.valueOf(totalItemCount)+" ta: "+String.valueOf(total_ads));
		if (totalItemCount>=total_ads && !footerAdded) {
			View v = getLayoutInflater().inflate(R.layout.cargo_list_footer, null);
			lvItem.addFooterView(v, "", false);
			footerAdded = true;
		}
	}

	@Override
	public void processHandler(Message msg) {
		// //Log.d(TAG,"Handler: "+String.valueOf(msg.what));
		if (msg.what == REQUEST_RESULT) {
			// //Log.d(TAG,"REQUEST RESULTS");
			LoadMoreRequest = false;
			pbSpinner.setVisibility(View.GONE);
			RequestResult rr;
			rr = (RequestResult) msg.obj;
			processResponce(rr.getReqBody(), false);
		}
	}
	
	public void fillRequestParams(RequestInfo request) {
		request.addKeyValue("hash", hash);
		request.addKeyValue("pg", String.valueOf(pg));
		
		if (locFrom1.getId().length()>0)
			request.addKeyValue("id_combined_from", locFrom1.getId());
		if (locTo1.getId().length()>0)
			request.addKeyValue("id_combined_to", locTo1.getId());
		if (truckType.length()>0 && !truckType.equals("0"))
			request.addKeyValue("truck_type", truckType);
		if (weightFrom.length()>0 && !weightFrom.equals("0"))
			request.addKeyValue("weight_from", weightFrom);
		if (weightTo.length()>0)
			request.addKeyValue("weight_to", weightTo);
		if (volumeFrom.length()>0 && !volumeFrom.equals("0"))
			request.addKeyValue("volume_from", volumeFrom);
		if (volumeTo.length()>0)
			request.addKeyValue("volume_to", volumeTo);
		if (lengthFrom.length()>0 && !lengthFrom.equals("0"))
			request.addKeyValue("cargo_length_from", lengthFrom);
		if (lengthTo.length()>0)
			request.addKeyValue("cargo_length_to", lengthTo);
		if (heightFrom.length()>0 && !heightFrom.equals("0"))
			request.addKeyValue("cargo_height_from", heightFrom);
		if (heightTo.length()>0)
			request.addKeyValue("cargo_height_to", heightTo);
		if (widthFrom.length()>0 && !widthFrom.equals("0"))
			request.addKeyValue("cargo_width_from", widthFrom);
		if (widthTo.length()>0)
			request.addKeyValue("cargo_width_to", widthTo);

		
		Date dateFr=new Date(dateFrom);
		Date dateT= new Date(dateTo);
		Date dateNow = new Date();
		String df = new SimpleDateFormat("dd.MM.yyyy").format(dateFr);
		String dt = new SimpleDateFormat("dd.MM.yyyy").format(dateT);
		
		if (dateFr.compareTo(dateNow)>0)
			request.addKeyValue("date_from", df);
		if (dateT.compareTo(dateNow)>0)
			request.addKeyValue("date_to", dt);

		//Log.d(TAG,"df: "+dateFr.toString());
		//Log.d(TAG,"dt: "+dateT.toString());
		
		if (firmId != null && firmId.length()>0)
			request.addKeyValue("firm_id", this.firmId);
		
		String[] lt = loadType.split(",");
		if (lt.length>0 && lt.length<5) {
			for(int i=0;i<lt.length;i++) {
				//Log.d(TAG,lt[i]);
				request.addKeyValue("load_type["+lt[i]+"]", lt[i]);
			}
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		EntityTruck t = new EntityTruck();
		t = itemArray.get(position-1);
		Intent intent = new Intent(this, ActivityTruckView.class);
		intent.putExtra("hash", hash);
		//Log.d(TAG,t.getId());
		intent.putExtra("truckId", t.getId());
		startActivity(intent);
	}
}

