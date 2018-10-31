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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

public class ActivityMyCargos extends FragmentActivity implements OnChildClickListener, InterfaceAsyncTaskCompleteListener<String>, InterfaceFragmentDialogCallback {

	String hash="";
	String act="";
	
	String cargoId = "";
	Boolean showAll = false;
	
	final String TAG = "MyCargo";
	
	ArrayList<String> groupData;
    ArrayList<ArrayList<EntityCargo>> childData;
    AdapterMyCargos listAdapter;
	ExpandableListView list;
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_cargos);
		Intent intent = getIntent();

		hash = intent.getStringExtra("hash");
		act = intent.getStringExtra("act");
		
		//((ExpandableListView)findViewById(R.id.elvMyCargos)).setVisibility(View.GONE);

		groupData = new ArrayList<String>();
		childData = new ArrayList<ArrayList<EntityCargo>>();
		
        listAdapter = new AdapterMyCargos(groupData, childData);
        listAdapter.setInflater(
          (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        list = (ExpandableListView) findViewById(R.id.elvMyCargos);

        View v = getLayoutInflater().inflate(R.layout.my_cargo_truck_list_header, null);
		list.addHeaderView(v, "", false);
		if (this.act.equalsIgnoreCase("cargo"))
			((TextView)v.findViewById(R.id.tvHeaderTitle)).setText("Ваши активные грузы");
		else
			((TextView)v.findViewById(R.id.tvHeaderTitle)).setText("Ваши грузы в архиве");
        list.setAdapter(listAdapter);
        list.setOnChildClickListener(this);
        
        ((CheckBox)findViewById(R.id.cbShowAll)).setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
				showAll = isChecked;
				getCargos("");
			}
        	
        });
        
		getCargos("");
	}
	
	protected void onResume() {
		super.onResume();
		getCargos("");
	}
	
	protected void getCargos(String act) {
		RequestInfo request = new RequestInfo();
		request.addKeyValue("hash", hash);
		if (!act.equals("")) {
		   request.addKeyValue("cargo_id[]", cargoId);
		   request.addKeyValue("act", act);
		}
		
		if (this.showAll)
			request.addKeyValue("show_all", "1");
				
		if (this.act.equalsIgnoreCase("cargo"))
			request.setUrl("/mycargoactiveApp");
		else
			request.setUrl("/mycargoarchiveApp");
		
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
		ArrayList<EntityCargo> childDataItem;
		
		groupData.clear();
		childData.clear();
		
		for (int i = 0; i < data.length(); i++) {
			try {
				JSONObject row = data.getJSONObject(i);
				String period = row.getString("tab_week");
				JSONArray cargos = row.getJSONArray("cargos");
		         groupData.add(period); 
		         childDataItem = new ArrayList<EntityCargo>();
		         for(int j=0;j<cargos.length(); j++) {
		        	 EntityCargo cargo = new EntityCargo(cargos.getJSONObject(j));
		             childDataItem.add(cargo);
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

        ArrayList<EntityCargo> tempChild = childData.get(groupPosition);
		EntityCargo c = (EntityCargo) tempChild.get(childPosition);

		cargoId = c.getId();
		
		if (this.act.equalsIgnoreCase("cargo")) {
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
			getCargos(id);
		else {
			Intent intent = new Intent(this, ActivityCargoAdd.class);
			intent.putExtra("hash", hash);
			intent.putExtra("cargoId", cargoId);
			startActivity(intent);
		}
	}
	
}
