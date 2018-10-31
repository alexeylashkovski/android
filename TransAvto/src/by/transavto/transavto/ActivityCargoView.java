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

public class ActivityCargoView extends Activity implements OnClickListener, InterfaceAsyncTaskCompleteListener<String> {

	String hash = "";
	EntityCargo cargo;
	String TAG = "CARGO";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cargo_view);
		Intent intent = getIntent();
		hash = intent.getStringExtra("hash");
		String cargoId = intent.getStringExtra("cargoId");

		((LinearLayout)findViewById(R.id.llOneCargoView)).setVisibility(View.GONE);

		RequestInfo request = new RequestInfo();
		request.addKeyValue("hash", hash);
		request.addKeyValue("cargoId", cargoId);
		request.setUrl("/cargoidApp");
		request.setMethod("post");
		Http http_client = new Http(this);
		http_client.execute(request, 0);
	
	}
	
	
	@Override
	public void onTaskComplete(String result, int reqCode) {
		// TODO Auto-generated method stub
	//	Log.d(TAG,result);
		Dialog dialog = new Dialog(this);
		JSONObject json;
		try {
			json = new JSONObject(result);
			cargo = new EntityCargo(json);
			refreshData();
		} catch (Exception e) {
		//	Log.d(TAG, e.toString());
			dialog.ErrorDialog("Неверный ответ от сервера");
			//finish();
			return;
		}
	//	Log.d(TAG,json.toString());
		
	}
	
	private void refreshData() {
		((LinearLayout)findViewById(R.id.llOneCargoView)).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.tvOneCargoFrom)).setText(cargo.getFrom().getLocName(true));
		((TextView)findViewById(R.id.tvOneCargoTo)).setText(cargo.getTo().getLocName(true));
		
		ImageView image = ((ImageView)findViewById(R.id.ivOneCargoFrom));
		int res_id = getResources().getIdentifier("flag_" + cargo.getFrom().getFlag(), "drawable", getPackageName());
		image.setImageResource(res_id);
		image.getLayoutParams().width = 40;
		image.getLayoutParams().height = 24;

		image = ((ImageView)findViewById(R.id.ivOneCargoTo));
		res_id = getResources().getIdentifier("flag_" + cargo.getTo().getFlag(), "drawable", getPackageName());
		image.setImageResource(res_id);
		image.getLayoutParams().width = 40;
		image.getLayoutParams().height = 24;
		
		((TextView)findViewById(R.id.tvOneCargoDates)).setText(cargo.getDates());
		((TextView)findViewById(R.id.tvOneCargoDates)).setTypeface(null,Typeface.BOLD);
		
		if (!(cargo.getTruckType()+cargo.getWeight()+cargo.getVolume()+cargo.getTrucksNum()).equalsIgnoreCase("")) {
			TextView tmp =(TextView)findViewById(R.id.tvOneCargoTruck);
			String tmk = cargo.getTruckType()+" ";
			if (cargo.getWeight().length()>0)
				tmk += cargo.getWeight()+"т";
			if (cargo.getWeight().length()>0 && cargo.getVolume().length()>0)
				tmk += "/";
			if (cargo.getVolume().length()>0)
				tmk += cargo.getVolume()+"м3";
			
			tmp.setText(tmk);
			if (!cargo.getTrucksNum().equalsIgnoreCase(""))
				tmp.setText(tmp.getText()+" ("+cargo.getTrucksNum()+" авто)");	
			
		}
		else
			((TableRow)findViewById(R.id.trOneCargoTruck)).setVisibility(View.GONE);
		
		if (!(cargo.getFullCargoType()+cargo.getCargoDimensions()).equalsIgnoreCase("")) {
			((TextView)findViewById(R.id.tvOneCargoCargo)).setText(cargo.getFullCargoType());
			((TextView)findViewById(R.id.tvOneCargoCargo)).setTypeface(null,Typeface.BOLD);
			((TextView)findViewById(R.id.tvOneCargoDim)).setText(cargo.getCargoDimensions());
		}
		else
			((TableRow)findViewById(R.id.trOneCargoCargo)).setVisibility(View.GONE);
		
		if (!cargo.getZagruz().equalsIgnoreCase(""))
			((TextView)findViewById(R.id.tvOneCargoPogruz)).setText(cargo.getZagruz());
		else
			((TableRow)findViewById(R.id.trOneCargoPogruz)).setVisibility(View.GONE);
		
		if (!cargo.getDescription().equalsIgnoreCase(""))
			((TextView)findViewById(R.id.tvOneCargoDescription)).setText(cargo.getDescription());
		else
			((TableRow)findViewById(R.id.trOneCargoDescription)).setVisibility(View.GONE);
		
		if (!cargo.getFullPrice().equalsIgnoreCase(""))
			((TextView)findViewById(R.id.tvOneCargoOplata)).setText(cargo.getFullPrice());
		else
			((TableRow)findViewById(R.id.trOneCargoOplata)).setVisibility(View.GONE);
		
		((TextView)findViewById(R.id.tvOneCargoUserName)).setText(cargo.getUser().getFullName(true));
		
		if (!cargo.getUser().getSkype().equalsIgnoreCase("")) {
			((TextView)findViewById(R.id.tvOneCargoSkype)).setText(cargo.getUser().getSkype());
			((TextView)findViewById(R.id.tvOneCargoSkype)).setOnClickListener(this);
		}
		else
			((TableRow)findViewById(R.id.trOneCargoSkype)).setVisibility(View.GONE);
		
		if (!cargo.getUser().getTel1().equalsIgnoreCase("")) {
			res_id = getResources().getIdentifier(cargo.getUser().getTel1Op(), "drawable", getPackageName());
			((ImageView)findViewById(R.id.ivOneCargoPhone1)).setImageResource(res_id);
			((TextView)findViewById(R.id.tvOneCargoPhone1)).setText(cargo.getUser().getTel1());
			((TextView)findViewById(R.id.tvOneCargoPhone1)).setOnClickListener(this);			
		}
		else
			((TableRow)findViewById(R.id.trOneCargoPhone1)).setVisibility(View.GONE);

		if (!cargo.getUser().getTel2().equalsIgnoreCase("")) {
			res_id = getResources().getIdentifier(cargo.getUser().getTel2Op(), "drawable", getPackageName());
			((ImageView)findViewById(R.id.ivOneCargoPhone2)).setImageResource(res_id);
			((TextView)findViewById(R.id.tvOneCargoPhone2)).setText(cargo.getUser().getTel2());
			((TextView)findViewById(R.id.tvOneCargoPhone2)).setOnClickListener(this);			
		}
		else
			((TableRow)findViewById(R.id.trOneCargoPhone2)).setVisibility(View.GONE);

		if (!cargo.getUser().getTel3().equalsIgnoreCase("")) {
			res_id = getResources().getIdentifier(cargo.getUser().getTel3Op(), "drawable", getPackageName());
			((ImageView)findViewById(R.id.ivOneCargoPhone3)).setImageResource(res_id);
			((TextView)findViewById(R.id.tvOneCargoPhone3)).setText(cargo.getUser().getTel3());
			((TextView)findViewById(R.id.tvOneCargoPhone3)).setOnClickListener(this);			
		}
		else
			((TableRow)findViewById(R.id.trOneCargoPhone3)).setVisibility(View.GONE);

		if (!cargo.getUser().getTel4().equalsIgnoreCase("")) {
			res_id = getResources().getIdentifier(cargo.getUser().getTel4Op(), "drawable", getPackageName());
			((ImageView)findViewById(R.id.ivOneCargoPhone4)).setImageResource(res_id);
			((TextView)findViewById(R.id.tvOneCargoPhone4)).setText(cargo.getUser().getTel4());
			((TextView)findViewById(R.id.tvOneCargoPhone4)).setOnClickListener(this);			
		}
		else
			((TableRow)findViewById(R.id.trOneCargoPhone4)).setVisibility(View.GONE);

		res_id = getResources().getIdentifier("flag_"+cargo.getFirm().getFirmLoc().getFlag(), "drawable", getPackageName());
		((ImageView)findViewById(R.id.ivOneCargoFirmFlag)).setImageResource(res_id);
		((ImageView)findViewById(R.id.ivOneCargoFirmFlag)).getLayoutParams().width = 40;
		((ImageView)findViewById(R.id.ivOneCargoFirmFlag)).getLayoutParams().height = 24;
		((TextView)findViewById(R.id.tvOneCargoFirmName)).setText(cargo.getFirm().getFirmName());
		((LinearLayout)findViewById(R.id.llOneCargoFirmName)).setOnClickListener(this);

		res_id = getResources().getIdentifier(cargo.getFirm().getFirmRatingResourceName(), "drawable", getPackageName());
		((ImageView)findViewById(R.id.ivOneCargoFirmRating)).setImageResource(res_id);
		
		((TextView)findViewById(R.id.tvOneCargoFirmCargos)).setText(cargo.getFirm().getFirmCargos());
		((TableRow)findViewById(R.id.trOneCargoFirmCargos)).setOnClickListener(this);
		((TextView)findViewById(R.id.tvOneCargoFirmTrucks)).setText(cargo.getFirm().getFirmTrucks());
		((TableRow)findViewById(R.id.trOneCargoFirmTrucks)).setOnClickListener(this);
		((TextView)findViewById(R.id.tvOneCargoFirmNp)).setText(cargo.getFirm().getFirmNp());
		((TableRow)findViewById(R.id.trOneCargoFirmNp)).setOnClickListener(this);
		((TextView)findViewById(R.id.tvOneCargoFirmFbPos)).setText(cargo.getFirm().getFirmFbPos());
		((TableRow)findViewById(R.id.trOneCargoFirmFbPos)).setOnClickListener(this);
		((TextView)findViewById(R.id.tvOneCargoFirmFbNeg)).setText(cargo.getFirm().getFirmFbNeg());
		((TableRow)findViewById(R.id.trOneCargoFirmFbNeg)).setOnClickListener(this);
		((TextView)findViewById(R.id.tvOneCargoFirmPartners)).setText(cargo.getFirm().getFirmPartners());
		((TextView)findViewById(R.id.tvOneCargoFirmBl)).setText(cargo.getFirm().getFirmBlackList());
		
		if (cargo.getActivity().equals("2")){
			((TextView)findViewById(R.id.tvOneCargoActivity)).setText("Постоянно!");
			((TextView)findViewById(R.id.tvOneCargoActivity)).setTypeface(null,Typeface.BOLD);
			((TextView)findViewById(R.id.tvOneCargoActivity)).setTextColor(Color.GREEN);
		}
		else if (cargo.getActivity().equals("1")){
			((TextView)findViewById(R.id.tvOneCargoActivity)).setText("Срочно!");
			((TextView)findViewById(R.id.tvOneCargoActivity)).setTypeface(null,Typeface.BOLD);
			((TextView)findViewById(R.id.tvOneCargoActivity)).setTextColor(Color.RED);
		}
		else
			((TextView)findViewById(R.id.tvOneCargoActivity)).setVisibility(View.GONE);
		
		((TextView)findViewById(R.id.tvOneCargoDateOf)).setText("размещено " + cargo.getDateOf());
	}


	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()) {
		case R.id.tvOneCargoSkype:
			intent = new Intent(Intent.ACTION_VIEW);          
            intent.setData(Uri.parse("skype:"+((TextView)v).getText()+"?call"));
            try {startActivity(intent);}
            catch (Exception e) {}
			break;

		case R.id.tvOneCargoPhone1:
		case R.id.tvOneCargoPhone2:
		case R.id.tvOneCargoPhone3:
		case R.id.tvOneCargoPhone4:
			intent = new Intent(Intent.ACTION_DIAL);          
            intent.setData(Uri.parse("tel:"+((TextView)v).getText()));          
            try {startActivity(intent);}
            catch (Exception e) {}
			break;

		case R.id.trOneCargoFirmNp:
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.transavto.by/forum/upf?firm_id="+cargo.getFirm().getId()));
			startActivity(intent);
			break;
			
		case R.id.trOneCargoFirmFbPos:
		case R.id.trOneCargoFirmFbNeg:
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.transavto.by/feedbacks?to_firm="+cargo.getFirm().getId()));
			startActivity(intent);
			break;
			
		case R.id.trOneCargoFirmCargos:
			intent = new Intent(this, ActivityCargoSearch.class);
			intent.putExtra("hash", hash);
			intent.putExtra("firmId", cargo.getFirm().getId());
			startActivity(intent);
			break;

		case R.id.trOneCargoFirmTrucks:
			intent = new Intent(this, ActivityTruckSearch.class);
			intent.putExtra("hash", hash);
			intent.putExtra("firmId", cargo.getFirm().getId());
			startActivity(intent);
			break;

		case R.id.llOneCargoFirmName:
			intent = new Intent(this, ActivityFirmView.class);
			intent.putExtra("hash", hash);
			intent.putExtra("firmId", cargo.getFirm().getId());
			startActivity(intent);
			break;

		}
	}
	
}
