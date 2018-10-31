package by.transavto.transavto;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ActivityTruckView extends Activity implements OnClickListener, InterfaceAsyncTaskCompleteListener<String> {

	String hash = "";
	EntityTruck truck;
	String TAG = "Truck";
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.truck_view);
		Intent intent = getIntent();
		hash = intent.getStringExtra("hash");
		String truckId = intent.getStringExtra("truckId");

		((LinearLayout)findViewById(R.id.llOneTruckView)).setVisibility(View.GONE);
		RequestInfo request = new RequestInfo();
		request.addKeyValue("hash", hash);
		request.addKeyValue("truckId", truckId);
		request.setUrl("/truckidApp");
		request.setMethod("post");
		Http http_client = new Http(this);
		http_client.execute(request,0);
	
	}
	
	
	@Override
	public void onTaskComplete(String result, int reqCode) {
		// TODO Auto-generated method stub
		//Log.d(TAG,result);
		Dialog dialog = new Dialog(this);
		JSONObject json;
		try {
			json = new JSONObject(result);
		} catch (Exception e) {
			//Log.d(TAG, e.toString());
			dialog.ErrorDialog("Неверный ответ от сервера");
			finish();
			return;
		}
		//Log.d(TAG,json.toString());
		truck = new EntityTruck(json);
		refreshData();
	}
	
	private void refreshData() {
		((LinearLayout)findViewById(R.id.llOneTruckView)).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.tvOneTruckFrom)).setText(truck.getFrom().getLocName(true));
		((TextView)findViewById(R.id.tvOneTruckTo)).setText(truck.getToTxt(true));
		
		ImageView image = ((ImageView)findViewById(R.id.ivOneTruckFrom));
		int res_id = getResources().getIdentifier("flag_" + truck.getFrom().getFlag(), "drawable", getPackageName());
		image.setImageResource(res_id);
		image.getLayoutParams().width = 40;
		image.getLayoutParams().height = 24;

		image = ((ImageView)findViewById(R.id.ivOneTruckTo));
		if (truck.getToAnyDirection().equalsIgnoreCase("0")) {
			res_id = getResources().getIdentifier("flag_" + truck.getTo().getFlag(), "drawable", getPackageName());
			image.setImageResource(res_id);
			image.getLayoutParams().width = 40;
			image.getLayoutParams().height = 24;
		}
		else
			image.setVisibility(View.GONE);
		
		((TextView)findViewById(R.id.tvOneTruckDates)).setText(truck.getDates());
		((TextView)findViewById(R.id.tvOneTruckDates)).setTypeface(null,Typeface.BOLD);
		
		if (!(truck.getTruckType()+truck.getWeight()+truck.getVolume()+truck.getTrucksNum()).equalsIgnoreCase("")) {
			TextView tmp =(TextView)findViewById(R.id.tvOneTruckTruck);
			String tmk = truck.getTruckType()+" ";
			if (truck.getWeight().length()>0)
				tmk += truck.getWeight()+"т";
			if (truck.getWeight().length()>0 && truck.getVolume().length()>0)
				tmk += "/";
			if (truck.getVolume().length()>0)
				tmk += truck.getVolume()+"м3";
			
			tmp.setText(tmk);
			if (!truck.getTrucksNum().equalsIgnoreCase(""))
				tmp.setText(tmp.getText()+" ("+truck.getTrucksNum()+" авто)");	
			
		}
		else
			((TableRow)findViewById(R.id.trOneTruckTruck)).setVisibility(View.GONE);
		
		if (!(truck.getTruckType()+truck.getTruckDimensions()).equalsIgnoreCase("")) {
			((TextView)findViewById(R.id.tvOneTruckTruck)).setText(truck.getTruckType());
			((TextView)findViewById(R.id.tvOneTruckTruck)).setTypeface(null,Typeface.BOLD);
			((TextView)findViewById(R.id.tvOneTruckDim)).setText(truck.getTruckDimensions());
		}
		else
			((TableRow)findViewById(R.id.trOneTruckTruck)).setVisibility(View.GONE);
		
		if (!truck.getZagruz().equalsIgnoreCase(""))
			((TextView)findViewById(R.id.tvOneTruckPogruz)).setText(truck.getZagruz());
		else
			((TableRow)findViewById(R.id.trOneTruckPogruz)).setVisibility(View.GONE);
		
		if (!truck.getDescription().equalsIgnoreCase(""))
			((TextView)findViewById(R.id.tvOneTruckDescription)).setText(truck.getDescription());
		else
			((TableRow)findViewById(R.id.trOneTruckDescription)).setVisibility(View.GONE);
		
		if (!truck.getFullPrice().equalsIgnoreCase(""))
			((TextView)findViewById(R.id.tvOneTruckOplata)).setText(truck.getFullPrice());
		else
			((TableRow)findViewById(R.id.trOneTruckOplata)).setVisibility(View.GONE);
		
		((TextView)findViewById(R.id.tvOneTruckUserName)).setText(truck.getUser().getFullName(true));
		
		if (!truck.getUser().getSkype().equalsIgnoreCase("")) {
			((TextView)findViewById(R.id.tvOneTruckSkype)).setText(truck.getUser().getSkype());
			((TextView)findViewById(R.id.tvOneTruckSkype)).setOnClickListener(this);
		}
		else
			((TableRow)findViewById(R.id.trOneTruckSkype)).setVisibility(View.GONE);
		
		if (!truck.getUser().getTel1().equalsIgnoreCase("")) {
			res_id = getResources().getIdentifier(truck.getUser().getTel1Op(), "drawable", getPackageName());
			((ImageView)findViewById(R.id.ivOneTruckPhone1)).setImageResource(res_id);
			((TextView)findViewById(R.id.tvOneTruckPhone1)).setText(truck.getUser().getTel1());
			((TextView)findViewById(R.id.tvOneTruckPhone1)).setOnClickListener(this);			
		}
		else
			((TableRow)findViewById(R.id.trOneTruckPhone1)).setVisibility(View.GONE);

		if (!truck.getUser().getTel2().equalsIgnoreCase("")) {
			res_id = getResources().getIdentifier(truck.getUser().getTel2Op(), "drawable", getPackageName());
			((ImageView)findViewById(R.id.ivOneTruckPhone2)).setImageResource(res_id);
			((TextView)findViewById(R.id.tvOneTruckPhone2)).setText(truck.getUser().getTel2());
			((TextView)findViewById(R.id.tvOneTruckPhone2)).setOnClickListener(this);			
		}
		else
			((TableRow)findViewById(R.id.trOneTruckPhone2)).setVisibility(View.GONE);

		if (!truck.getUser().getTel3().equalsIgnoreCase("")) {
			res_id = getResources().getIdentifier(truck.getUser().getTel3Op(), "drawable", getPackageName());
			((ImageView)findViewById(R.id.ivOneTruckPhone3)).setImageResource(res_id);
			((TextView)findViewById(R.id.tvOneTruckPhone3)).setText(truck.getUser().getTel3());
			((TextView)findViewById(R.id.tvOneTruckPhone3)).setOnClickListener(this);			
		}
		else
			((TableRow)findViewById(R.id.trOneTruckPhone3)).setVisibility(View.GONE);

		if (!truck.getUser().getTel4().equalsIgnoreCase("")) {
			res_id = getResources().getIdentifier(truck.getUser().getTel4Op(), "drawable", getPackageName());
			((ImageView)findViewById(R.id.ivOneTruckPhone4)).setImageResource(res_id);
			((TextView)findViewById(R.id.tvOneTruckPhone4)).setText(truck.getUser().getTel4());
			((TextView)findViewById(R.id.tvOneTruckPhone4)).setOnClickListener(this);			
		}
		else
			((TableRow)findViewById(R.id.trOneTruckPhone4)).setVisibility(View.GONE);

		res_id = getResources().getIdentifier("flag_"+truck.getFirm().getFirmLoc().getFlag(), "drawable", getPackageName());
		((ImageView)findViewById(R.id.ivOneTruckFirmFlag)).setImageResource(res_id);
		((ImageView)findViewById(R.id.ivOneTruckFirmFlag)).getLayoutParams().width = 40;
		((ImageView)findViewById(R.id.ivOneTruckFirmFlag)).getLayoutParams().height = 24;
		((TextView)findViewById(R.id.tvOneTruckFirmName)).setText(truck.getFirm().getFirmName());
		((LinearLayout)findViewById(R.id.llOneTruckFirmName)).setOnClickListener(this);

		res_id = getResources().getIdentifier(truck.getFirm().getFirmRatingResourceName(), "drawable", getPackageName());
		((ImageView)findViewById(R.id.ivOneTruckFirmRating)).setImageResource(res_id);
		
		((TextView)findViewById(R.id.tvOneTruckFirmCargos)).setText(truck.getFirm().getFirmTrucks());
		((TableRow)findViewById(R.id.trOneTruckFirmTrucks)).setOnClickListener(this);
		((TextView)findViewById(R.id.tvOneTruckFirmTrucks)).setText(truck.getFirm().getFirmTrucks());
		((TableRow)findViewById(R.id.trOneTruckFirmTrucks)).setOnClickListener(this);
		((TextView)findViewById(R.id.tvOneTruckFirmNp)).setText(truck.getFirm().getFirmNp());
		((TableRow)findViewById(R.id.trOneTruckFirmNp)).setOnClickListener(this);
		((TextView)findViewById(R.id.tvOneTruckFirmFbPos)).setText(truck.getFirm().getFirmFbPos());
		((TableRow)findViewById(R.id.trOneTruckFirmFbPos)).setOnClickListener(this);
		((TextView)findViewById(R.id.tvOneTruckFirmFbNeg)).setText(truck.getFirm().getFirmFbNeg());
		((TableRow)findViewById(R.id.trOneTruckFirmFbNeg)).setOnClickListener(this);
		((TextView)findViewById(R.id.tvOneTruckFirmPartners)).setText(truck.getFirm().getFirmPartners());
		((TextView)findViewById(R.id.tvOneTruckFirmBl)).setText(truck.getFirm().getFirmBlackList());
		
		if (truck.getActivity().equals("2")){
			((TextView)findViewById(R.id.tvOneTruckActivity)).setText("Постоянно!");
			((TextView)findViewById(R.id.tvOneTruckActivity)).setTypeface(null,Typeface.BOLD);
			((TextView)findViewById(R.id.tvOneTruckActivity)).setTextColor(Color.GREEN);
		}
		else if (truck.getActivity().equals("1")){
			((TextView)findViewById(R.id.tvOneTruckActivity)).setText("Срочно!");
			((TextView)findViewById(R.id.tvOneTruckActivity)).setTypeface(null,Typeface.BOLD);
			((TextView)findViewById(R.id.tvOneTruckActivity)).setTextColor(Color.RED);
		}
		else
			((TextView)findViewById(R.id.tvOneTruckActivity)).setVisibility(View.GONE);
		
		((TextView)findViewById(R.id.tvOneTruckDateOf)).setText("размещено " + truck.getDateOf());
	}


	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()) {
		case R.id.tvOneTruckSkype:
			intent = new Intent(Intent.ACTION_VIEW);          
            intent.setData(Uri.parse("skype:"+((TextView)findViewById(R.id.tvOneTruckSkype)).getText()+"?call"));
            try {startActivity(intent);}
            catch (Exception e) {}
			break;

		case R.id.tvOneTruckPhone1:
		case R.id.tvOneTruckPhone2:
		case R.id.tvOneTruckPhone3:
		case R.id.tvOneTruckPhone4:
			intent = new Intent(Intent.ACTION_DIAL);          
            intent.setData(Uri.parse("tel:"+((TextView)v).getText()));          
            try {startActivity(intent);}
            catch (Exception e) {}
			break;

		case R.id.trOneTruckFirmNp:
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.transavto.by/forum/upf?firm_id="+truck.getFirm().getId()));
			startActivity(intent);
			break;
			
		case R.id.trOneTruckFirmFbPos:
		case R.id.trOneTruckFirmFbNeg:
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.transavto.by/feedbacks?to_firm="+truck.getFirm().getId()));
			startActivity(intent);
			break;
			
		case R.id.trOneTruckFirmCargos:
			intent = new Intent(this, ActivityCargoSearch.class);
			intent.putExtra("hash", hash);
			intent.putExtra("firmId", truck.getFirm().getId());
			startActivity(intent);
			break;

		case R.id.trOneTruckFirmTrucks:
			intent = new Intent(this, ActivityTruckSearch.class);
			intent.putExtra("hash", hash);
			intent.putExtra("firmId", truck.getFirm().getId());
			startActivity(intent);
			break;
			
		case R.id.llOneTruckFirmName:
			intent = new Intent(this, ActivityFirmView.class);
			intent.putExtra("hash", hash);
			intent.putExtra("firmId", truck.getFirm().getId());
			startActivity(intent);
			break;
		}
	}
	
}
