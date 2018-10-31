package by.transavto.transavto;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityCargoAdd extends FragmentActivity implements OnClickListener, InterfaceFragmentDialogCallback, InterfaceAsyncTaskCompleteListener<String>{

	List<Integer> ids = new ArrayList<Integer>();
	List<String> datas = new ArrayList<String>();

	String hash = "";
	final String TAG = "CargoAdd";
	
	final int MAX_WEIGHT_VALUE = 30;
	final int MAX_VOLUME_VALUE = 120;
	final int MAX_LENGTH_VALUE = 40;
	final int MAX_HEIGHT_VALUE = 5;
	final int MAX_WIDTH_VALUE = 5;
	
	final Integer LOC_CHOOSE_FROM1 = 1;
	final Integer LOC_CHOOSE_TO1 = 2;
	final Integer DATE_CHOOSE = 10;

	final int TRUCK_TYPE_DIALOG = 21;
	final int LOAD_TYPE_DIALOG = 22;
	final int WEIGHT_DIALOG = 23;
	final int VOLUME_DIALOG = 24;
	final int LENGTH_DIALOG = 25;
	final int HEIGHT_DIALOG = 26;
	final int WIDTH_DIALOG = 27;
	final int CARGO_TYPE_DIALOG = 28;
	final int DESCRIPTION_DIALOG = 29;
	
	final int CARGO_ADD_REQUEST = 30;
	final int CARGO_DATA_REQUEST = 31;

	EntityLocation locFrom1 = new EntityLocation();
	EntityLocation locTo1 = new EntityLocation();
	Long dateFrom = 0L;
	Long dateTo = 0L;
	String cargoId = "";
	String cargoType = "";
	String truckType = "0";
	String loadType = "";
	String weight = "";
	String volume = "";
	String length = "";
	String height = "";
	String width = "";
	String description = "";
	String currency = "";

	ClassAds adView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cargo_add);
		
		Intent intent = getIntent();
		hash = intent.getStringExtra("hash");
		if (intent.hasExtra("cargoId"))
			cargoId = intent.getStringExtra("cargoId");
		else
			cargoId = "";
		
		findViewById(R.id.tvCargoAddLocFrom1).setOnClickListener(this);
		findViewById(R.id.tvCargoAddLocTo1).setOnClickListener(this);
		findViewById(R.id.tvCargoAddDates).setOnClickListener(this);
		findViewById(R.id.tvCargoAddTruckType).setOnClickListener(this);
		findViewById(R.id.tvCargoAddLoadType).setOnClickListener(this);
		findViewById(R.id.tvCargoAddDescription).setOnClickListener(this);
		findViewById(R.id.tvCargoAddCargoType).setOnClickListener(this);

		findViewById(R.id.ivCargoAddFlagFrom1).setVisibility(View.GONE);
		findViewById(R.id.ivCargoAddFlagTo1).setVisibility(View.GONE);
		findViewById(R.id.ivCargoAddDatesRemove).setVisibility(View.GONE);
		findViewById(R.id.ivCargoAddTruckTypeRemove).setVisibility(View.GONE);
		findViewById(R.id.ivCargoAddLoadTypeRemove).setVisibility(View.GONE);
		findViewById(R.id.ivCargoAddCargoTypeRemove).setVisibility(View.GONE);
		findViewById(R.id.ivCargoAddDescriptionRemove).setVisibility(View.GONE);

		
		((ImageButton) findViewById(R.id.ibCargoAddWeight)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.ibCargoAddVolume)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.ibCargoAddLength)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.ibCargoAddHeight)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.ibCargoAddWidth)).setOnClickListener(this);
		((ImageView) findViewById(R.id.ivCargoAddDatesRemove)).setOnClickListener(this);
		((ImageView) findViewById(R.id.ivCargoAddTruckTypeRemove)).setOnClickListener(this);
		((ImageView) findViewById(R.id.ivCargoAddCargoTypeRemove)).setOnClickListener(this);
		((ImageView) findViewById(R.id.ivCargoAddLoadTypeRemove)).setOnClickListener(this);
		((ImageView) findViewById(R.id.ivCargoAddDescriptionRemove)).setOnClickListener(this);
		((Button) findViewById(R.id.btnCargoAdd)).setOnClickListener(this);


		((ImageView) findViewById(R.id.ivCargoAddLocFrom1Remove)).setVisibility(View.GONE);
		((ImageView) findViewById(R.id.ivCargoAddLocFrom1Remove)).setOnClickListener(this);
		((ImageView) findViewById(R.id.ivCargoAddLocTo1Remove)).setVisibility(View.GONE);
		((ImageView) findViewById(R.id.ivCargoAddLocTo1Remove)).setOnClickListener(this);
		
		fillCurrencySpinner();
		
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        Spinner spinner = (Spinner) findViewById(R.id.spnrCargoAddCurrency);
        spinner.setAdapter(adapter);
        // ���������
        spinner.setPrompt("������");
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,    int position, long id) {
              currency = String.valueOf( ids.get(position) );
            }
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
        });
        
        if (!cargoId.equals("")) {
        	((Button)findViewById(R.id.btnCargoAdd)).setText("��������� ����");
        	requestCargoData();
        }
        
	    LinearLayout layout = (LinearLayout)findViewById(R.id.llBanner);
	    adView = new ClassAds(this, layout);
	    
        // ������� ����������
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.tvCargoAddLocFrom1:
			intent = new Intent(this, ActivityChooseLocation.class);
			intent.putExtra("hash", hash);
			startActivityForResult(intent, LOC_CHOOSE_FROM1);
			break;
		case R.id.tvCargoAddLocTo1:
			intent = new Intent(this, ActivityChooseLocation.class);
			intent.putExtra("hash", hash);
			startActivityForResult(intent, LOC_CHOOSE_TO1);
			break;
		case R.id.tvCargoAddDates:
			intent = new Intent(this, DialogDatePicker.class);
			intent.putExtra("dateFrom", dateFrom);
			intent.putExtra("dateTo", dateTo);
			startActivityForResult(intent, DATE_CHOOSE);
			break;
		case R.id.ivCargoAddLocFrom1Remove:
			onActivityResult(LOC_CHOOSE_FROM1, RESULT_CANCELED, null);
			break;
		case R.id.ivCargoAddLocTo1Remove:
			onActivityResult(LOC_CHOOSE_TO1, RESULT_CANCELED, null);
			break;
		case R.id.ivCargoAddDatesRemove:
			onActivityResult(DATE_CHOOSE, RESULT_CANCELED, null);
			break;
		case R.id.tvCargoAddTruckType:
			DisplayDialog(TRUCK_TYPE_DIALOG);
			break;
		case R.id.ivCargoAddTruckTypeRemove:
			truckType = "0";
			changeText(R.id.tvCargoAddTruckType, R.id.ivCargoAddTruckTypeRemove, "(�� ������)",
					false);
			break;
		case R.id.tvCargoAddCargoType:
			DisplayDialog(CARGO_TYPE_DIALOG);
			break;
		case R.id.ivCargoAddCargoTypeRemove:
			cargoType = "0";
			changeText(R.id.tvCargoAddCargoType, R.id.ivCargoAddCargoTypeRemove, "(�� ������)",	false);
			break;
		case R.id.tvCargoAddLoadType:
			DisplayDialog(LOAD_TYPE_DIALOG);
			break;
		case R.id.ivCargoAddLoadTypeRemove:
			loadType = "";
			changeText(R.id.tvCargoAddLoadType, R.id.ivCargoAddLoadTypeRemove, "(�����)", false);
			break;
		case R.id.ivCargoAddDescriptionRemove:
			description = "";
			changeText(R.id.tvCargoAddDescription, R.id.ivCargoAddDescriptionRemove, "(�� ������)", false);
			break;

		case R.id.tvCargoAddDescription:
			DisplayDialog(DESCRIPTION_DIALOG);
			break;
		case R.id.ibCargoAddWeight:
			DisplayDialog(WEIGHT_DIALOG);
			break;
		case R.id.ibCargoAddVolume:
			DisplayDialog(VOLUME_DIALOG);
			break;
		case R.id.ibCargoAddLength:
			DisplayDialog(LENGTH_DIALOG);
			break;
		case R.id.ibCargoAddHeight:
			DisplayDialog(HEIGHT_DIALOG);
			break;
		case R.id.ibCargoAddWidth:
			DisplayDialog(WIDTH_DIALOG);
			break;
		case R.id.btnCargoAdd:
			if (checkData())
				sendAddRequest();
			break;
		}
	}
	
	private void DisplayDialog(int dialog) {
		FragmentManager fm = getSupportFragmentManager();
		switch (dialog) {
		case TRUCK_TYPE_DIALOG:
			DialogDbSpr dlg = new DialogDbSpr("truck_type","��� ������",this, dialog);
			dlg.show(fm, "DIALOG");
			break;
		case CARGO_TYPE_DIALOG:
			DialogDbSpr dlg2 = new DialogDbSpr("cargo_type","����",this, dialog);
			dlg2.show(fm, "DIALOG");
			break;

		case LOAD_TYPE_DIALOG:
			DialogLoadType dlg3 = new DialogLoadType(this, dialog,loadType);
			dlg3.show(fm, "DIALOG");
			break;
		case WEIGHT_DIALOG:
			DialogNumberPicker dlg4 = new DialogNumberPicker(this,dialog, "���, �.", 0, MAX_WEIGHT_VALUE);
			dlg4.setInit(weight);
			dlg4.show(fm, "WEIGHT_DIALOG");
			break;
		case VOLUME_DIALOG:
			DialogNumberPicker dlg5 = new DialogNumberPicker(this,dialog, "�����, �3", 0, MAX_VOLUME_VALUE);
			dlg5.setInit(volume);
			dlg5.show(fm, "VOLUME_DIALOG");
			break;
		case LENGTH_DIALOG:
			DialogNumberPicker dlg6 = new DialogNumberPicker(this,dialog, "������, �.", 0, MAX_LENGTH_VALUE);
			dlg6.setInit(length);
			dlg6.show(fm, "LENGTH_DIALOG");
			break;
		case WIDTH_DIALOG:
			DialogNumberPicker dlg7 = new DialogNumberPicker(this,dialog, "������, �.", 0, MAX_WIDTH_VALUE);
			dlg7.setInit(width);
			dlg7.show(fm, "WIDTH_DIALOG");
			break;
		case HEIGHT_DIALOG:
			DialogNumberPicker dlg8 = new DialogNumberPicker(this,dialog, "������, �.", 0, MAX_HEIGHT_VALUE);
			dlg8.setInit(height);
			dlg8.show(fm, "HEIGHT_DIALOG");
			break;
			
		case DESCRIPTION_DIALOG:
			DialogTextInput dlg10 = new DialogTextInput(this, dialog, "��������", description);
			dlg10.show(fm, "DIALOG");
			break;

		}
	}

	
	private void changeText(int textResId, int ivRemoveResId, String text,
			Boolean bold) {
		TextView tv = ((TextView) findViewById(textResId));
		tv.setText(text);
		if (bold) {
			tv.setTypeface(null, Typeface.BOLD);
			tv.setTextColor(Color.BLACK);
			if (ivRemoveResId != 0)
				findViewById(ivRemoveResId).setVisibility(View.VISIBLE);
		} else {
			tv.setTextColor(Color.LTGRAY);
			tv.setTypeface(null, Typeface.NORMAL);
			if (ivRemoveResId != 0)
				findViewById(ivRemoveResId).setVisibility(View.GONE);
		}
	}

	@Override
	public void fgdCallback(String id, String data, int reqCode) {
		switch (reqCode) {
		case TRUCK_TYPE_DIALOG:
			truckType = id;
			changeText(R.id.tvCargoAddTruckType, R.id.ivCargoAddTruckTypeRemove, data, true);
			break;
		case CARGO_TYPE_DIALOG:
			cargoType = id;
			changeText(R.id.tvCargoAddCargoType, R.id.ivCargoAddCargoTypeRemove, data, true);
			break;

		case LOAD_TYPE_DIALOG:
			if (id.length() > 0) {
				loadType = id;
				changeText(R.id.tvCargoAddLoadType, R.id.ivCargoAddLoadTypeRemove, data, true);
			} else {
				loadType = "";
				changeText(R.id.tvCargoAddLoadType, R.id.ivCargoAddLoadTypeRemove, "(�����)",
						false);
			}
			break;
		case WEIGHT_DIALOG:
			if (id.length() > 0 && !id.equals("0")) {
				weight = id;
				changeText(R.id.tvCargoAddWeight, 0, data + "�", true);
			} else {
				weight = "";
				changeText(R.id.tvCargoAddWeight, 0, "�� ������", false);
			}
			break;
		case VOLUME_DIALOG:
			if (id.length() > 0 && !id.equals("0")) {
				volume = id;
				changeText(R.id.tvCargoAddVolume, 0, data + "�3", true);
			} else {
				volume = "";
				changeText(R.id.tvCargoAddVolume, 0, "�� ������", false);
			}
			break;
		case LENGTH_DIALOG:
			if (id.length() > 0 && !id.equals("0")) {
				length = id;
				changeText(R.id.tvCargoAddLength, 0, data + "�", true);
			} else {
				length = "";
				changeText(R.id.tvCargoAddLength, 0, "�� ������", false);
			}
			break;
		case HEIGHT_DIALOG:
			if (id.length() > 0 && !id.equals("0")) {
				height = id;
				changeText(R.id.tvCargoAddHeight, 0, data + "�", true);
			} else {
				height = "";
				changeText(R.id.tvCargoAddHeight, 0, "�� ������", false);
			}
			break;
		case WIDTH_DIALOG:
			if (id.length() > 0 && !id.equals("0")) {
				width = id;
				changeText(R.id.tvCargoAddWidth, 0, data + "�", true);
			} else {
				width = "";
				changeText(R.id.tvCargoAddWidth, 0, "�� ������", false);
			}
			break;
		case DESCRIPTION_DIALOG:
			if (data.length() > 0) {
				description = data;
				String tmp;
				if (description.length()>30)
					tmp = description.substring(0, 30)+"...";
				else
					tmp = description;
				changeText(R.id.tvCargoAddDescription, R.id.ivCargoAddDescriptionRemove, tmp, true);
			} else {
				description = "";
				changeText(R.id.tvCargoAddDescription, R.id.ivCargoAddDescriptionRemove, "(�� ������)", false);
			}
//			Log.d(TAG,description);
			break;
		}		
	}

	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_CANCELED) {
			if (requestCode == LOC_CHOOSE_FROM1) { // ������� �� MainScreen
				locFrom1 = new EntityLocation();
				changeText(R.id.tvCargoAddLocFrom1, R.id.ivCargoAddLocFrom1Remove, "", false);
				setFlag(R.id.ivCargoAddFlagFrom1, 0);
			}
			if (requestCode == LOC_CHOOSE_TO1) { // ������� �� MainScreen
				locTo1 = new EntityLocation();
				changeText(R.id.tvCargoAddLocTo1, R.id.ivCargoAddLocTo1Remove, "", false);
				setFlag(R.id.ivCargoAddFlagTo1, 0);
			}
			if (requestCode == DATE_CHOOSE) { // ������� �� MainScreen
				dateFrom = dateTo = 0L;
				changeText(R.id.tvCargoAddDates, R.id.ivCargoAddDatesRemove, "(�����)", false);
			}
		}

		if (resultCode != RESULT_OK) {
			return;
		}

		if (requestCode == LOC_CHOOSE_FROM1) {
			locFrom1 = data.getExtras().getParcelable("location");
			changeText(R.id.tvCargoAddLocFrom1, R.id.ivCargoAddLocFrom1Remove, locFrom1.getLocName(false), true);
			setFlag(R.id.ivCargoAddFlagFrom1, locFrom1.getFlag());
		}
		if (requestCode == LOC_CHOOSE_TO1) {
			locTo1 = data.getExtras().getParcelable("location");
			changeText(R.id.tvCargoAddLocTo1, R.id.ivCargoAddLocTo1Remove, locTo1.getLocName(false), true);
			setFlag(R.id.ivCargoAddFlagTo1, locTo1.getFlag());
		}
		if (requestCode == DATE_CHOOSE) {
			String range = data.getExtras().getString("StringRange");
			Boolean selectedFrom = data.getExtras().getBoolean("selectedFrom");
			Boolean selectedTo = data.getExtras().getBoolean("selectedTo");
			if (selectedFrom || selectedTo) {
				dateFrom = data.getExtras().getLong("dateFrom");
				dateTo = data.getExtras().getLong("dateTo");
				changeText(R.id.tvCargoAddDates, R.id.ivCargoAddDatesRemove, range, true);
			}
		}

		return;
	}

	private void setFlag(int imageResource, String flag) {
		int res_id = getResources().getIdentifier("flag_" + flag, "drawable", getPackageName());
		setFlag(imageResource, res_id);
	}
	
	private void setFlag(int imageResource, int flag) {
		ImageView img = (ImageView) findViewById(imageResource);
		if (flag == 0) {
			img.setVisibility(View.GONE);
		}
		img.setVisibility(View.VISIBLE);
		img.setImageResource(flag);
	}

	private boolean checkData() {
		if (locFrom1.isEmpty()) {
			Toast.makeText(getApplicationContext(), "������� ����� ��������.",  Toast.LENGTH_LONG).show();
			return false;			
		}
		if (locTo1.isEmpty()) {
			Toast.makeText(getApplicationContext(), "������� ����� ��������.",  Toast.LENGTH_LONG).show();
			return false;			
		}
		if (dateFrom == 0l && dateTo==0l) {
			Toast.makeText(getApplicationContext(), "������� ���� ��������.",  Toast.LENGTH_LONG).show();
			return false;			
		}
		if (truckType.equals("")) {
			Toast.makeText(getApplicationContext(), "������� ��� ����.",  Toast.LENGTH_LONG).show();
			return false;			
		}
		if (cargoType.equals("")) {
			Toast.makeText(getApplicationContext(), "������� ��� �����.",  Toast.LENGTH_LONG).show();
			return false;			
		}
		if (weight.equals("")) {
			Toast.makeText(getApplicationContext(), "������� ��� �����.",  Toast.LENGTH_LONG).show();
			return false;
		}
		if (volume.equals("")) {
			Toast.makeText(getApplicationContext(), "������� ����� �����.",  Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
	
	private void fillCurrencySpinner() {
		ClassDB dbh = new ClassDB(this);
		dbh.open();
		Cursor c = dbh.getAllData("currency_type");

		if (c.moveToFirst()) {
			// ���������� ������ �������� �� ����� � �������
			int idIndex = c.getColumnIndex("id");
			int dataIndex = c.getColumnIndex("data");
			do {
				int id = c.getInt(idIndex);
				String dt = c.getString(dataIndex);
				ids.add(id);
				datas.add(dt);
			} while (c.moveToNext());
		}
		c.close();
		dbh.close();

	}
	
	private void sendAddRequest() {
		RequestInfo request = new RequestInfo();
		request.addKeyValue("hash", hash);
		if (this.cargoId.length()>0)
			request.addKeyValue("id", cargoId);
		
		if (!this.locFrom1.isEmpty()) {
			String data[] = locFrom1.getIdArray();
			request.addKeyValue("from_country", data[0]);
			request.addKeyValue("from_region", data[1]);
			request.addKeyValue("from_city", data[2]);
			request.addKeyValue("from_city_txt", this.locFrom1.getCity());
		}

		if (!this.locTo1.isEmpty()) {
			String data[] = locTo1.getIdArray();
			request.addKeyValue("to_country", data[0]);
			request.addKeyValue("to_region", data[1]);
			request.addKeyValue("to_city", data[2]);
			request.addKeyValue("to_city_txt", this.locTo1.getCity());
		}

		if (this.cargoType.length()>0)
			request.addKeyValue("cargo_type", cargoType);

		if (this.truckType.length()>0)
			request.addKeyValue("truck_type", truckType);

		if (this.description.length()>0)
			request.addKeyValue("description", description);

		if (this.weight.length()>0)
			request.addKeyValue("weight", weight);

		if (this.volume.length()>0)
			request.addKeyValue("volume", volume);

		if (this.height.length()>0)
			request.addKeyValue("cargo_height", height);

		if (this.width.length()>0)
			request.addKeyValue("cargo_width", width);

		if (this.length.length()>0)
			request.addKeyValue("cargo_length", length);

		if (this.dateFrom>0l) 
			request.addKeyValue("date_from", String.valueOf( dateFrom / 1000 ) );
		
		if (this.dateTo>0l) 
			request.addKeyValue("date_to", String.valueOf( dateTo / 1000 ));
		

		String price = ((EditText)findViewById(R.id.etCargoAddPrice)).getText().toString();
		if (price.length()>0) 
			request.addKeyValue("price", price);	

		if (currency.length()>0) 
			request.addKeyValue("currency_type", currency);	

		if (this.loadType.length()>0) {
			String[] exploded=this.loadType.split(",");
			for(int i=0;i<exploded.length;i++) {
				if (!exploded[i].equalsIgnoreCase("tent"))
					request.addKeyValue("load_type_"+exploded[i], "1");
				else
					request.addKeyValue("load_type_full_tent_remove", "1");
			}
		}
		
		request.setUrl("/cargoaddApp");
		request.setMethod("post");
		Http http_client = new Http(this);
		http_client.execute(request, CARGO_ADD_REQUEST);
	}
	
	private void requestCargoData() {
		RequestInfo request = new RequestInfo();
		request.addKeyValue("hash", hash);
		request.addKeyValue("id", cargoId);
		request.setUrl("/cargodataApp");
		request.setMethod("post");
		Http http_client = new Http(this);
		http_client.execute(request, CARGO_DATA_REQUEST);
		
	}
	
	@Override
	public void onTaskComplete(String result, int reqCode) {
		// TODO Auto-generated method stub
//		Log.d(TAG,result);
		Dialog dialog = new Dialog(this);
		JSONObject json;
		try {
			json = new JSONObject(result);
			Boolean res = json.getBoolean("result");
			String reason = json.getString("reason");
			if (reqCode == CARGO_ADD_REQUEST) {
				if (res) {
					dialog.setTitle("����������");
					this.cargoId = json.getString("cargoId");
					((Button)findViewById(R.id.btnCargoAdd)).setText("��������� ����");
			//		Log.d(TAG,"cargoid: "+cargoId);
				}
				dialog.ErrorDialog(reason);
			}
			if (reqCode == CARGO_DATA_REQUEST) {
				if (res)
					refreshData(json);
				else
					dialog.ErrorDialog(reason);
			}
		} catch (Exception e) {
//			Log.d(TAG, e.toString());
			dialog.ErrorDialog("�������� ����� �� �������");
//			finish();
			return;
		}
//		Log.d(TAG,json.toString());
	//	cargo = new EntityCargo(json);
	}

	private void refreshData(JSONObject data) {
		String tmp;
/*		try {locFrom1.setId(data.getString("locFromId"));
		} catch (Exception e) {}
		try {locFrom1.setCountry(data.getString("locCountryFrom"));
		} catch (Exception e) {}
		try {locFrom1.setRegion(data.getString("locRegionFrom"));
		} catch (Exception e) {}
		try {locFrom1.setCity(data.getString("locCityFrom"));
		} catch (Exception e) {}

		try {
			String flag = data.getString("locFromFlag").toLowerCase();
			Integer res_id = getResources().getIdentifier("flag_" + flag,"drawable", getPackageName());
			locFrom1.setFlag(res_id);
		} catch (Exception e) {}
*/

		try { locFrom1 = new EntityLocation(data.getJSONObject("locFrom"));
		} catch (Exception e) {locFrom1 = new EntityLocation();}
		
		if (!locFrom1.isEmpty()) {
			changeText(R.id.tvCargoAddLocFrom1, R.id.ivCargoAddLocFrom1Remove, locFrom1.getLocName(false), true);
			setFlag(R.id.ivCargoAddFlagFrom1, locFrom1.getFlag());
		}

/*		try {locTo1.setId(data.getString("locToId"));
		} catch (Exception e) {}
		try {locTo1.setCountry(data.getString("locCountryTo"));
		} catch (Exception e) {}
		try {locTo1.setRegion(data.getString("locRegionTo"));
		} catch (Exception e) {}
		try {locTo1.setCity(data.getString("locCityTo"));
		} catch (Exception e) {}

		try {
			String flag = data.getString("locToFlag").toLowerCase();
			Integer res_id = getResources().getIdentifier("flag_" + flag,"drawable", getPackageName());
			locTo1.setFlag(res_id);
		} catch (Exception e) {}
*/
		try { locTo1 = new EntityLocation(data.getJSONObject("locTo"));
		} catch (Exception e) {locTo1 = new EntityLocation();}
		
		if (!locTo1.isEmpty()) {
			changeText(R.id.tvCargoAddLocTo1, R.id.ivCargoAddLocTo1Remove, locTo1.getLocName(false), true);
			setFlag(R.id.ivCargoAddFlagTo1, locTo1.getFlag());
		}

		try {
			dateFrom = data.getLong("date_from") * 1000 ;
		} catch (Exception e) {}

		try {
			dateTo = data.getLong("date_to") * 1000;
		} catch (Exception e) {}
		
//		Log.d(TAG, "df: "+String.valueOf(dateFrom));
//		Log.d(TAG, "dt: "+String.valueOf(dateTo));
		
		ViewDatePicker dpd = new ViewDatePicker(this);
		dpd.setDateFrom(dateFrom);
		dpd.setDateFrom(dateTo);
		String range = dpd.getRusRange();
		dpd = null;
		if (range.length()>0 && (dateFrom>0 || dateTo>0))
			changeText(R.id.tvCargoAddDates, R.id.ivCargoAddDatesRemove, range, true);
		
		try {
			truckType = data.getString("truck_type_id");
			tmp = data.getString("truck_type_txt");
			if (tmp.length()>0)
				changeText(R.id.tvCargoAddTruckType, R.id.ivCargoAddTruckTypeRemove, tmp, true);
		} catch (Exception e) {}

		try {
			cargoType = data.getString("cargo_type_id");
			tmp = data.getString("cargo_type_txt");
			if (tmp.length()>0)
				changeText(R.id.tvCargoAddCargoType, R.id.ivCargoAddCargoTypeRemove, tmp, true);
		} catch (Exception e) {}

		try {
			description = data.getString("description");
			if (description.length()>30)
				tmp = description.substring(0, 30)+"...";
			else
				tmp = description;
			if (tmp.length()>0)
				changeText(R.id.tvCargoAddDescription, R.id.ivCargoAddDescriptionRemove, tmp, true);
		} catch (Exception e) {}
		
		tmp = "";
		try {
			if (data.getInt("lt_up")==1) {
				loadType+="up,";
				tmp += "�������,";
			}	
		} catch (Exception e) {}
		try {
			if (data.getInt("lt_back")==1) {
				loadType+="back,";
				tmp += "������,";
			}
		} catch (Exception e) {}
		try {
			if (data.getInt("lt_side")==1) {
				loadType+="side,";
				tmp += "�������,";
			}
		} catch (Exception e) {}
		try {
			if (data.getInt("lt_stoika")==1) {
				loadType+="stoika,";
				tmp += "�� ������� �����/���������,";
			}
		} catch (Exception e) {}
		try {
			if (data.getInt("lt_tent")==1) {
				loadType+="tent,";
				tmp += "� ������ ������������,";
			}
		} catch (Exception e) {}
		
		if (loadType.length()>0) {
			loadType = loadType.substring(0, loadType.length()-1);
			tmp = tmp.substring(0, loadType.length()-1);
			if (tmp.length()>20)
				tmp = tmp.substring(0,  20)+"...";
			changeText(R.id.tvCargoAddLoadType, R.id.ivCargoAddLoadTypeRemove, tmp, true);
		}

		try {
			tmp = data.getString("price");
			if (Integer.valueOf(tmp)>0){
				((EditText)findViewById(R.id.etCargoAddPrice)).setText(tmp);
			}
		} catch (Exception e) {}

		try {
			currency = data.getString("currency_type_id");
			tmp = data.getString("currency_type_txt");
			if (tmp.length()>0) {
				Spinner spinner = (Spinner) findViewById(R.id.spnrCargoAddCurrency);
				spinner.setSelection(Integer.valueOf(currency)-1);
			//	changeText(R.id.tvCargoAddCargoType, R.id.ivCargoAddCargoTypeRemove, tmp, true);
			}
		} catch (Exception e) {}

		try {
			tmp = data.getString("weight");
//			Log.d(TAG, weight);
			if (Float.valueOf(tmp)>0){
				weight = tmp;
				changeText(R.id.tvCargoAddWeight, 0, weight + "�", true);
			}
		} catch (Exception e) {}

		try {
			tmp = data.getString("volume");
			if (Float.valueOf(tmp)>0){
				volume = tmp;
				changeText(R.id.tvCargoAddVolume, 0, volume + "�3", true);
			}
		} catch (Exception e) {}
		
		try {
			tmp = data.getString("length");
			if (Float.valueOf(tmp)>0){
				length = tmp;
				changeText(R.id.tvCargoAddLength, 0, length + "�", true);
			}
		} catch (Exception e) {}

		try {
			tmp = data.getString("width");
			if (Float.valueOf(tmp)>0){
				width = tmp;
				changeText(R.id.tvCargoAddWidth, 0, width + "�", true);
			}
		} catch (Exception e) {}

		try {
			tmp = data.getString("height");
			if (Float.valueOf(tmp)>0){
				height = tmp;
				changeText(R.id.tvCargoAddHeight, 0, height + "�", true);
			}
		} catch (Exception e) {}

	}
}
