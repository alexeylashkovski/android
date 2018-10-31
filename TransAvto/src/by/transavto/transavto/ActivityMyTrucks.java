package by.transavto.transavto;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

public class ActivityMyTrucks extends FragmentActivity implements OnChildClickListener, InterfaceAsyncTaskCompleteListener<String>, InterfaceFragmentDialogCallback {

	String hash="";
	String act="";
	
	String truckId = "";
	Boolean showAll = false;
	
	final String TAG = "MyTrucks";
	
	ArrayList<String> groupData;
    ArrayList<ArrayList<EntityTruck>> childData;
    AdapterMyTrucks listAdapter;
	ExpandableListView list;
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_cargos);
		Intent intent = getIntent();

		hash = intent.getStringExtra("hash");
		act = intent.getStringExtra("act");
		
		//((ExpandableListView)findViewById(R.id.elvMyCargos)).setVisibility(View.GONE);

		groupData = new ArrayList<String>();
		childData = new ArrayList<ArrayList<EntityTruck>>();
		
        listAdapter = new AdapterMyTrucks(groupData, childData);
        listAdapter.setInflater(
          (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        list = (ExpandableListView) findViewById(R.id.elvMyCargos);

        View v = getLayoutInflater().inflate(R.layout.my_cargo_truck_list_header, null);
		list.addHeaderView(v, "", false);
		if (this.act.equalsIgnoreCase("truck"))
			((TextView)v.findViewById(R.id.tvHeaderTitle)).setText("Ваш активный транспорт");
		else
			((TextView)v.findViewById(R.id.tvHeaderTitle)).setText("Ваш транспорт в архиве");
        list.setAdapter(listAdapter);
        list.setOnChildClickListener(this);
        
        ((CheckBox)findViewById(R.id.cbShowAll)).setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
				showAll = isChecked;
				getTrucks("");
			}
        	
        });
        
		getTrucks("");
	}
	
	protected void onResume() {
		super.onResume();
		getTrucks("");
	}
	
	protected void getTrucks(String act) {
		RequestInfo request = new RequestInfo();
		request.addKeyValue("hash", hash);
		if (!act.equals("")) {
		   request.addKeyValue("truck_id[]", truckId);
		   request.addKeyValue("act", act);
		}
		
		if (this.showAll)
			request.addKeyValue("show_all", "1");
				
		if (this.act.equalsIgnoreCase("truck"))
			request.setUrl("/mytruckactiveApp");
		else
			request.setUrl("/mytruckarchiveApp");
		
		request.setMethod("post");
		
		Http http_client = new Http(this);
		http_client.execute(request, 0);
	}
	
	@Override
	public void onTaskComplete(String result, int reqCode) {
		// TODO Auto-generated method stub
		Dialog dialog = new Dialog(this);
		JSONObject json;
		try {
			json = new JSONObject(result);
			String message = json.getString("message");
			refreshList(json.getJSONArray("periods"));
			if (message.length()>0) {
				dialog.setTitle("Информация");
				dialog.ErrorDialog(message);
			}
		} catch (Exception e) {
			//Log.d(TAG, e.toString());
			dialog.ErrorDialog("Неверный ответ от сервера");
			return;
		}
//		//Log.d(TAG,json.toString());
	}

	private void refreshList(JSONArray data) {
		ArrayList<EntityTruck> childDataItem;
		
		groupData.clear();
		childData.clear();
		
		for (int i = 0; i < data.length(); i++) {
			try {
				JSONObject row = data.getJSONObject(i);
				String period = row.getString("tab_week");
				JSONArray trucks = row.getJSONArray("trucks");
		         groupData.add(period); 
		         childDataItem = new ArrayList<EntityTruck>();
		         for(int j=0;j<trucks.length(); j++) {
		        	 EntityTruck truck = new EntityTruck(trucks.getJSONObject(j));
		             childDataItem.add(truck);
		         }
		         childData.add(childDataItem);
			} catch (Exception e) {}
		}
		listAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		
		List<String> ids = new ArrayList<String>();
		List<String> datas = new ArrayList<String>();
		FragmentManager fm;

        ArrayList<EntityTruck> tempChild = childData.get(groupPosition);
		EntityTruck c = (EntityTruck) tempChild.get(childPosition);

		truckId = c.getId();
		
		if (this.act.equalsIgnoreCase("truck")) {
			datas.add("Редактировать");
			datas.add("Обновить");
			datas.add("В архив");
			datas.add("Удалить");
			ids.add("edit");
			ids.add("renew");
			ids.add("archive");
			ids.add("del");
		}
		else {
			datas.add("Редактировать");
			datas.add("Подать заявку заново");
			datas.add("Удалить");
			ids.add("edit");
			ids.add("repost");
			ids.add("del");
		}
		
		fm = getSupportFragmentManager();
		DialogDbSpr dlg2 = new DialogDbSpr("Действие с заявкой", (Activity)this,  0, ids, datas);
		dlg2.show(fm, "DIALOG");

		//Log.d(TAG,"click "+c.getId());
		return false;
	}

	@Override
	public void fgdCallback(String id, String data, int reqCode) {
		if (!id.equalsIgnoreCase("edit"))
			getTrucks(id);
		else {
			Intent intent = new Intent(this, ActivityTruckAdd.class);
			intent.putExtra("hash", hash);
			intent.putExtra("truckId", truckId);
//			Log.d(TAG,"tId "+truckId);
			startActivity(intent);
		}
	}
	
}

