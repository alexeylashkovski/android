package by.transavto.transavto;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityMainScreen extends FragmentActivity implements OnClickListener, InterfaceFragmentDialogCallback {

	final int DIALOG_ADD_NEW = 1;
	final int DIALOG_MY_ADS = 2;
	
	final String TAG = "MainScreen";
	String hash = "";
	
	private ClassAds adView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen2);
		Intent intent = getIntent();

		hash = intent.getStringExtra("hash");
		String userName = intent.getStringExtra("userName");

		//Log.d(TAG, "hash: " + hash);
		//Log.d(TAG, "un: " + userName);

		TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
		tvUserName.setText(userName);

		findViewById(R.id.ibLogout).setOnClickListener(this);
		findViewById(R.id.ibChat).setOnClickListener(this);
		findViewById(R.id.ibSearch).setOnClickListener(this);
		findViewById(R.id.ibAdd).setOnClickListener(this);
		findViewById(R.id.ibMyAds).setOnClickListener(this);
		findViewById(R.id.ibFirmSearch).setOnClickListener(this);

	    LinearLayout layout = (LinearLayout)findViewById(R.id.llBanner);
	    adView = new ClassAds(this, layout);

	}

	public void onClick(View v) {
		List<String> ids = new ArrayList<String>();
		List<String> datas = new ArrayList<String>();
		FragmentManager fm;
		
		Intent intent;
		
		switch (v.getId()) {
		case R.id.ibLogout:
			intent = new Intent();
			intent.putExtra("action", "logout");
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.ibChat:
			intent = new Intent(this, ActivityChat.class);
			intent.putExtra("hash", hash);
			startActivity(intent);
			break;
		case R.id.ibSearch:
			intent = new Intent(this, ActivitySearch.class);
			intent.putExtra("hash", hash);
			startActivity(intent);
			break;
		case R.id.ibAdd:
			fm = getSupportFragmentManager();
			datas.add("Груз");
			datas.add("Транспорт");
			ids.add("cargo");
			ids.add("truck");
			DialogDbSpr dlg = new DialogDbSpr("Добавить заявку", (Activity)this,  DIALOG_ADD_NEW, ids, datas);
			dlg.show(fm, "DIALOG");
			break;
		case R.id.ibMyAds:
			fm = getSupportFragmentManager();
			datas.add("Грузы активные");
			datas.add("Грузы архив");
			datas.add("Транспорт активный");
			datas.add("Транспорт архив");
			ids.add("cargo");
			ids.add("cargo_archive");
			ids.add("truck");
			ids.add("truck_archive");
			DialogDbSpr dlg2 = new DialogDbSpr("Вид заявок", (Activity)this,  DIALOG_MY_ADS, ids, datas);
			dlg2.show(fm, "DIALOG");
			break;
		case R.id.ibFirmSearch:
			intent = new Intent(this, ActivityFirmSearch.class);	
			intent.putExtra("hash", hash);
			startActivity(intent);
			break;

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//Log.d("loginActivity", String.valueOf(keyCode));
		super.onKeyDown(keyCode, event);
		return true;
	}

	@Override
	public void fgdCallback(String id, String data, int reqCode) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (reqCode) {
		case DIALOG_ADD_NEW: 
			if (id.equalsIgnoreCase("cargo")) 
				intent = new Intent(this, ActivityCargoAdd.class);
			else 
				intent = new Intent(this, ActivityTruckAdd.class);
			intent.putExtra("hash", hash);
			startActivity(intent);
			break;
		case DIALOG_MY_ADS:
			if (id.equalsIgnoreCase("cargo") || id.equalsIgnoreCase("cargo_archive")) 
				intent = new Intent(this, ActivityMyCargos.class);
			else 
				intent = new Intent(this, ActivityMyTrucks.class);
			
			intent.putExtra("act", id);
			intent.putExtra("hash", hash);
			startActivity(intent);
			break;
		}

	}
}
