package by.transavto.transavto;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.view.Window;

public class ActivityDbUpdate extends Activity implements InterfaceMyHandler {

	final String TAG = "DB_UPDATE";
	String hash = "";
	String user = "";
	int dbVer = 0;

	final int DB_UPDATE = 1;

	ClassMyHandler h;
	Http http_client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dbupdate_layout);

		Intent intent = getIntent();

		hash = intent.getStringExtra("hash");
		user = intent.getStringExtra("userName");
		dbVer = intent.getIntExtra("dbVer", 0);

		h = new ClassMyHandler(this);
		http_client = new Http(this);
		Thread t = new Thread(new Runnable() {
			public void run() {
				RequestInfo request = new RequestInfo();
				request.addKeyValue("hash", hash);
				request.setReqMethod("sync");
				request.setUrl("/updateDb");
				request.setMethod("post");
				RequestResult result = http_client.execute(request,0);
	//			Log.d(TAG,result.toString());
				h.sendMessage(Message.obtain(h, DB_UPDATE, 0, 0, result));
			}
		});
		t.start();
	}

	@Override
	public void processHandler(Message msg) {
		Intent intent = new Intent();
		intent.putExtra("userName", user);
		if (msg.what != DB_UPDATE) {
			setResult(RESULT_CANCELED, intent);
			finish();
		}
		RequestResult rr;
		rr = (RequestResult) msg.obj;
		saveData(rr.getReqBody());
		intent.putExtra("dbVer", dbVer);
		setResult(RESULT_OK, intent);
		finish();
	}

	private void saveData(String data) {
		Dialog dialog = new Dialog(this);

	//	Log.d(TAG, data);
		JSONArray json;
		try {
			json = new JSONArray(data);
		} catch (Exception e) {
	//		Log.d(TAG, e.toString());
			// dialog.ErrorDialog("Неверный ответ от сервера");
			finish();
			return;
		}

		try {
			for (int i = 0; i < json.length(); i++) {
				try {
					JSONObject row = json.getJSONObject(i);
					updateTable(row);
				} catch (Exception e) {
					finish();
				}
				;
			}

			return;
		} catch (Exception e) {
			dialog.ErrorDialog(e.getMessage());
			finish();
		}

	}

	private void updateTable(JSONObject data) {
		ClassDB dbh = new ClassDB(this);
		SQLiteDatabase db = dbh.open();

		// Log.d(TAG,data.toString());
		try {
			JSONArray dt = data.getJSONArray("data");
			String table_name = data.getString("name");
//			Log.d(TAG, table_name);
//			Log.d(TAG, dt.toString());
			db.execSQL("DROP TABLE IF EXISTS '" + table_name + "'");
			db.execSQL("create table " + table_name
					+ " (id integer primary key, data text);");
			for (int i = 0; i < dt.length(); i++) {
				try {
					JSONObject row = dt.getJSONObject(i);
					ContentValues cv = new ContentValues();
					cv.put("id", row.getInt("id"));
					cv.put("data", row.getString("text"));
	//				Log.d(TAG, cv.toString());
					long rowID = db.insert(table_name, null, cv);
	//				Log.d(TAG, table_name + " rowId: " + String.valueOf(rowID));
				} catch (Exception e) {
					finish();
				}
				;
			}

		} catch (Exception e) {
			finish();
		}
		dbh.close();
	}
}
