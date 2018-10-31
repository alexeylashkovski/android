package by.transavto.transavto;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityLogin extends Activity implements OnClickListener,
		InterfaceAsyncTaskCompleteListener<String> {

	final String TAG = "loginActivity";

	EditText etPassword, etLogin;
	Button btnSignIn;
	ProgressDialog progDialog;
	Integer dbVersion = 0;
	int dbVer = 0;
	String app_ver = "";

	int runBeforeVotes = 0;
	String hash = "";
	String user = "";
	
	String UUID = "";
	String OP_COUNTRY = "";
	String RES = "";
	String MODEL = "";
	String MANUFACTURER = "";
	String SOFT_VER = "";
	String SDK = "";

	final String PASSWORD = "password";
	final String LOGIN = "login";

	final int MAIN_SCREEN = 1;
	final int DB_UPDATE = 2;
	final int VOTE4US = 3;
	final String app_version = "1.0.2";

	SharedPreferences sPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		etPassword = (EditText) findViewById(R.id.etPassword);
		etLogin = (EditText) findViewById(R.id.etLogin);

		loadLoginData();

		btnSignIn = (Button) findViewById(R.id.btnSingIn);
		btnSignIn.setOnClickListener(this);
		((TextView) findViewById(R.id.tvRegistration)).setOnClickListener(this);
		((TextView) findViewById(R.id.tvVersion)).setText("Версия: "+this.app_version);

		if (!etLogin.getText().toString().trim().equalsIgnoreCase("")
				&& !etPassword.getText().toString().trim().equalsIgnoreCase(""))
			onClick((View) btnSignIn);

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode != KeyEvent.KEYCODE_BACK) 
			return super.onKeyDown(keyCode, event);
		
		//Log.d(TAG,"onStop "+ String.valueOf(runBeforeVotes));
		if (runBeforeVotes>=0) {   // else already voted
			sPref = getPreferences(MODE_PRIVATE);
			runBeforeVotes=runBeforeVotes+1;
			if (runBeforeVotes>=5) {
				runBeforeVotes=0;
				Intent intent = new Intent(this, DialogVote4Us.class);
				startActivityForResult(intent, VOTE4US);
			}
			Editor ed = sPref.edit();
			ed.putInt("runBeforeVotes", runBeforeVotes);
			ed.commit();
			if (runBeforeVotes<10) {
				return super.onKeyDown(keyCode, event);
			}
			else return false;
		}
		
		return super.onKeyDown(keyCode, event);
	}

	protected void onResume() {
		super.onResume();		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}

		if (requestCode == VOTE4US && resultCode == RESULT_OK) {
			sPref = getPreferences(MODE_PRIVATE);
			Editor ed = sPref.edit();
			ed.putInt("runBeforeVotes", -5);
			ed.commit();
			finish();
		}

		if (requestCode == VOTE4US && resultCode == RESULT_CANCELED) {
			finish();
		}
		
		if (requestCode == MAIN_SCREEN && resultCode == RESULT_OK) { // возврат
																		// из
			// MainScreen
			etPassword.setText("");
			saveLoginData();
		}

		if (requestCode == DB_UPDATE && resultCode != RESULT_OK) {
			Dialog dialog = new Dialog(this);
			dialog.ErrorDialog("Не удалось обновить базу данны. Попробуйте позже!");
			return;
		}
		if (requestCode == DB_UPDATE && resultCode == RESULT_OK) { // возврат из
			// DB UPDATE
			this.dbVersion = dbVer;
			saveLoginData();
			Intent intent;
			intent = new Intent(this, ActivityMainScreen.class);
			intent.putExtra("userName", user);
			intent.putExtra("hash", hash);
			startActivityForResult(intent, MAIN_SCREEN);
		}
		return;
	}

	public void onClick(View v) {

		if (!isOnline()) {
			Dialog dialog = new Dialog(this);
			dialog.ErrorDialog("Требуется активное подключение к интеренету.");
			return;
		}

		
		switch (v.getId()) {
		case R.id.btnSingIn:
			if (etLogin.getText().toString().trim().equalsIgnoreCase("")) {
				Toast.makeText(this, "Не задан логин", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if (etPassword.getText().toString().trim().equalsIgnoreCase("")) {
				Toast.makeText(this, "Не задан пароль", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			saveLoginData();
			getDeviceInfo();
			RequestInfo request = new RequestInfo();
			request.addKeyValue("login", etLogin.getText().toString());
			request.addKeyValue("password", etPassword.getText().toString());
			request.addKeyValue("act", "login");
			request.addKeyValue("uuid", UUID);
			request.addKeyValue("op_country", OP_COUNTRY);
			request.addKeyValue("resolution", RES);
			request.addKeyValue("model", MODEL);
			request.addKeyValue("manufacturer", MANUFACTURER);
			request.addKeyValue("soft_ver", SOFT_VER);
			request.addKeyValue("sdk_ver", SDK);

			request.setUrl("/login_app");
			request.setMethod("post");
			Http http_client = new Http(this);
			http_client.execute(request,0);
			break;
		case R.id.tvRegistration:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.transavto.by/registration"));
			startActivity(browserIntent);
			break;
		}
	}

	public void onTaskComplete(String result, int reqCode) {
		Dialog dialog = new Dialog(this);
		// //Log.d(TAG,"HTTP Request complete");
		// //Log.d(TAG,result);
		JSONObject json;
		try {
			json = new JSONObject(result);
		} catch (Exception e) {
			//Log.d(TAG, e.toString());
			dialog.ErrorDialog("Неверный ответ от сервера");
			return;
		}

		try {
			if (!json.getBoolean("result")) {
				dialog.ErrorDialog(json.getString("reason"));
			} else {
				hash = json.getString("hash");
				user = json.getString("userName");
				app_ver = json.getString("app_ver");
				dbVer = json.getInt("db_ver");

				if (app_ver.compareToIgnoreCase(this.app_version) > 0 ) {
					dialog.ErrorDialog("Новая версия!","Выпущена новая версия. Пожалуйста обновите приложение.");
					return;
				}
				Intent intent;
				if (this.dbVersion != dbVer)
					intent = new Intent(this, ActivityDbUpdate.class);
				else
					intent = new Intent(this, ActivityMainScreen.class);
				intent.putExtra("userName", user);
				intent.putExtra("hash", hash);
				intent.putExtra("dbVer", dbVer);
				if (this.dbVersion != dbVer)
					startActivityForResult(intent, DB_UPDATE);
				else
					startActivityForResult(intent, MAIN_SCREEN);
				
				return;
			}
		} catch (Exception e) {
		}
		;

	}

	void saveLoginData() {
		sPref = getPreferences(MODE_PRIVATE);
		Editor ed = sPref.edit();
		ed.putString(PASSWORD, etPassword.getText().toString());
		ed.putString(LOGIN, etLogin.getText().toString());
		ed.putInt("dbVersion", dbVersion);
		ed.commit();
	}

	void loadLoginData() {
		sPref = getPreferences(MODE_PRIVATE);
		etPassword.setText(sPref.getString(PASSWORD, ""));
		etLogin.setText(sPref.getString(LOGIN, ""));
		dbVersion = sPref.getInt("dbVersion", 0);
		runBeforeVotes = sPref.getInt("runBeforeVotes", 0);
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}
	
	public void getDeviceInfo() {
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		UUID = tm.getDeviceId();
		OP_COUNTRY = tm.getNetworkCountryIso();
		RES = metrics.widthPixels + "X" + metrics.heightPixels;
		MODEL = Build.MODEL;
		MANUFACTURER = Build.MANUFACTURER;
		SOFT_VER = Build.VERSION.RELEASE;
		SDK = String.valueOf(Build.VERSION.SDK_INT);
	}
}
