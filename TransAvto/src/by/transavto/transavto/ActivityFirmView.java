package by.transavto.transavto;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ActivityFirmView extends Activity implements OnClickListener, InterfaceAsyncTaskCompleteListener<String> {

	final String TAG = "FirmView";
	private String hash="";
	private String firmId="";
	
	EntityFirm firm;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firm_view_screen);
		Intent intent = getIntent();

		hash = intent.getStringExtra("hash");
		firmId = intent.getStringExtra("firmId");
		
		((LinearLayout)findViewById(R.id.llFirmView)).setVisibility(View.GONE);

		RequestInfo request = new RequestInfo();
		request.addKeyValue("hash", hash);
		request.addKeyValue("firmId", firmId);
		request.setUrl("/firmviewApp");
		request.setMethod("post");
		Http http_client = new Http(this);
		http_client.execute(request, 0);
	
	}
	
	
	@Override
	public void onTaskComplete(String result, int reqCode) {
		// TODO Auto-generated method stub
		Log.d(TAG,result);
		Dialog dialog = new Dialog(this);
		JSONObject json;
		try {
			json = new JSONObject(result);
			firm = new EntityFirm(json.getJSONObject("firms"));
			refreshData();
		} catch (Exception e) {
			//Log.d(TAG, e.toString());
			dialog.ErrorDialog("Неверный ответ от сервера");
	//		finish();
			return;
		}
		//Log.d(TAG,json.toString());
	}
	
	private void refreshData() {
		int res_id;
		
		res_id = getResources().getIdentifier("flag_"+firm.getFirmLoc().getFlag(), "drawable", getPackageName());
		((ImageView)findViewById(R.id.ivFirmFlag)).setImageResource(res_id);
		((ImageView)findViewById(R.id.ivFirmFlag)).getLayoutParams().width = 40;
		((ImageView)findViewById(R.id.ivFirmFlag)).getLayoutParams().height = 24;
		((TextView)findViewById(R.id.tvFirmName)).setText(firm.getFirmName());

		res_id = getResources().getIdentifier(firm.getFirmRatingResourceName(), "drawable", getPackageName());
		((ImageView)findViewById(R.id.ivFirmRating)).setImageResource(res_id);
		
		((TextView)findViewById(R.id.tvFirmUnp)).setText(firm.getFirmUnp());
		
		((TextView)findViewById(R.id.tvFirmAddress)).setText(firm.getFirmLoc().getLocName(false)+"\n"+firm.getFirmAddress());
		
		((TextView)findViewById(R.id.tvFirmFax)).setText(firm.getFirmFax().equals("")?"не указан":firm.getFirmFax());
		((TextView)findViewById(R.id.tvFirmDescription)).setText(firm.getFirmDescription());
		
		
		((TextView)findViewById(R.id.tvFirmCargos)).setText(firm.getFirmCargos());
		((TableRow)findViewById(R.id.trFirmCargos)).setOnClickListener(this);
		((TextView)findViewById(R.id.tvFirmTrucks)).setText(firm.getFirmTrucks());
		((TableRow)findViewById(R.id.trFirmTrucks)).setOnClickListener(this);
		((TextView)findViewById(R.id.tvFirmNp)).setText(firm.getFirmNp());
		((TableRow)findViewById(R.id.trFirmNp)).setOnClickListener(this);
		((TextView)findViewById(R.id.tvFirmFbPos)).setText(firm.getFirmFbPos());
		((TableRow)findViewById(R.id.trFirmFbPos)).setOnClickListener(this);
		((TextView)findViewById(R.id.tvFirmFbNeg)).setText(firm.getFirmFbNeg());
		((TableRow)findViewById(R.id.trFirmFbNeg)).setOnClickListener(this);
		((TextView)findViewById(R.id.tvFirmPartners)).setText(firm.getFirmPartners());
		((TextView)findViewById(R.id.tvFirmBl)).setText(firm.getFirmBlackList());
		
		((LinearLayout)findViewById(R.id.llFirmView)).setVisibility(View.VISIBLE);
		
		ArrayList<EntityUser> users = firm.getUsers();
		View userView;
		LayoutInflater lInflater = getLayoutInflater();
		LinearLayout ll = (LinearLayout)findViewById(R.id.llUsers);
		
		for(int i=0;i<users.size();i++) {
			EntityUser u = users.get(i);
			userView = lInflater.inflate(R.layout.user_info, ll, false);
			
			((TextView)userView.findViewById(R.id.tvUserUserName)).setText(u.getFullName(true));
			
			if (!u.getSkype().equalsIgnoreCase("")) {
				((TextView)userView.findViewById(R.id.tvUserSkype)).setText(u.getSkype());
				((TextView)userView.findViewById(R.id.tvUserSkype)).setOnClickListener(this);
			}
			else
				((TableRow)userView.findViewById(R.id.trUserSkype)).setVisibility(View.GONE);
			
			if (!u.getTel1().equalsIgnoreCase("")) {
				res_id = getResources().getIdentifier(u.getTel1Op(), "drawable", getPackageName());
				((ImageView)userView.findViewById(R.id.ivUserPhone1)).setImageResource(res_id);
				((TextView)userView.findViewById(R.id.tvUserPhone1)).setText(u.getTel1());
				((TextView)userView.findViewById(R.id.tvUserPhone1)).setOnClickListener(this);			
			}
			else
				((TableRow)userView.findViewById(R.id.trUserPhone1)).setVisibility(View.GONE);

			if (!u.getTel2().equalsIgnoreCase("")) {
				res_id = getResources().getIdentifier(u.getTel2Op(), "drawable", getPackageName());
				((ImageView)userView.findViewById(R.id.ivUserPhone2)).setImageResource(res_id);
				((TextView)userView.findViewById(R.id.tvUserPhone2)).setText(u.getTel2());
				((TextView)userView.findViewById(R.id.tvUserPhone2)).setOnClickListener(this);			
			}
			else
				((TableRow)userView.findViewById(R.id.trUserPhone2)).setVisibility(View.GONE);

			if (!u.getTel3().equalsIgnoreCase("")) {
				res_id = getResources().getIdentifier(u.getTel3Op(), "drawable", getPackageName());
				((ImageView)userView.findViewById(R.id.ivUserPhone3)).setImageResource(res_id);
				((TextView)userView.findViewById(R.id.tvUserPhone3)).setText(u.getTel3());
				((TextView)userView.findViewById(R.id.tvUserPhone3)).setOnClickListener(this);			
			}
			else
				((TableRow)userView.findViewById(R.id.trUserPhone3)).setVisibility(View.GONE);

			if (!u.getTel4().equalsIgnoreCase("")) {
				res_id = getResources().getIdentifier(u.getTel4Op(), "drawable", getPackageName());
				((ImageView)userView.findViewById(R.id.ivUserPhone4)).setImageResource(res_id);
				((TextView)userView.findViewById(R.id.tvUserPhone4)).setText(u.getTel4());
				((TextView)userView.findViewById(R.id.tvUserPhone4)).setOnClickListener(this);			
			}
			else
				((TableRow)userView.findViewById(R.id.trUserPhone4)).setVisibility(View.GONE);
			
			ll.addView(userView);
		}
	}


	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()) {
		case R.id.tvUserSkype:
			intent = new Intent(Intent.ACTION_VIEW);          
            intent.setData(Uri.parse("skype:"+((TextView)v)+"?call"));
            try {startActivity(intent);}
            catch (Exception e) {}
			break;

		case R.id.tvUserPhone1:
		case R.id.tvUserPhone2:
		case R.id.tvUserPhone3:
		case R.id.tvUserPhone4:
			intent = new Intent(Intent.ACTION_DIAL);          
            intent.setData(Uri.parse("tel:"+((TextView)v).getText()));          
            try {startActivity(intent);}
            catch (Exception e) {}
			break;

		case R.id.trFirmNp:
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.transavto.by/forum/upf?firm_id="+firm.getId()));
			startActivity(intent);
			break;
			
		case R.id.trFirmFbPos:
		case R.id.trFirmFbNeg:
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.transavto.by/feedbacks?to_firm="+firm.getId()));
			startActivity(intent);
			break;
			
		case R.id.trFirmCargos:
			intent = new Intent(this, ActivityCargoSearch.class);
			intent.putExtra("hash", hash);
			intent.putExtra("firmId", firm.getId());
			startActivity(intent);
			break;

		case R.id.trFirmTrucks:
			intent = new Intent(this, ActivityTruckSearch.class);
			intent.putExtra("hash", hash);
			intent.putExtra("firmId", firm.getId());
			startActivity(intent);
			break;
		}
	}
	
}
