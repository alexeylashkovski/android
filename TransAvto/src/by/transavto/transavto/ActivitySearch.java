package by.transavto.transavto;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

public class ActivitySearch extends FragmentActivity implements
		OnClickListener, InterfaceFragmentDialogCallback {

	final int MAX_WEIGHT_VALUE = 40;
	final int MAX_VOLUME_VALUE = 120;
	final int MAX_LENGTH_VALUE = 40;
	final int MAX_HEIGHT_VALUE = 5;
	final int MAX_WIDTH_VALUE = 5;

	final Integer LOC_CHOOSE_FROM1 = 1;
	final Integer LOC_CHOOSE_TO1 = 2;
	final Integer DATE_CHOOSE = 10;
	final Integer LOC_CHOOSE_FROM1_TRUCK = 5;
	final Integer LOC_CHOOSE_TO1_TRUCK = 6;
	final Integer DATE_CHOOSE_TRUCK = 11;
	final String TAG = "SearchActivity";

	final int TRUCK_TYPE_DIALOG = 21;
	final int LOAD_TYPE_DIALOG = 22;
	final int WEIGHT_DIALOG = 23;
	final int VOLUME_DIALOG = 24;
	final int LENGTH_DIALOG = 25;
	final int HEIGHT_DIALOG = 26;
	final int WIDTH_DIALOG = 27;
	
	final int TRUCK_TYPE_DIALOG_TRUCK = 31;
	final int LOAD_TYPE_DIALOG_TRUCK = 32;
	final int WEIGHT_DIALOG_TRUCK = 33;
	final int VOLUME_DIALOG_TRUCK = 34;
	final int LENGTH_DIALOG_TRUCK = 35;
	final int HEIGHT_DIALOG_TRUCK = 36;
	final int WIDTH_DIALOG_TRUCK = 37;

	String hash;

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

	EntityLocation locFrom1Truck = new EntityLocation();
	EntityLocation locTo1Truck = new EntityLocation();
	Long dateFromTruck = 0L;
	Long dateToTruck = 0L;
	String truckTypeTruck = "0";
	String loadTypeTruck = "up,side,back,tent,stoika";
	String weightFromTruck = "";
	String weightToTruck = "";
	String volumeFromTruck = "";
	String volumeToTruck = "";
	String lengthFromTruck = "";
	String lengthToTruck = "";
	String heightFromTruck = "";
	String heightToTruck = "";
	String widthFromTruck = "";
	String widthToTruck = "";
	
	private ClassAds adViewCargo, adViewTruck;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_screen);
		Intent intent = getIntent();
		hash = intent.getStringExtra("hash");

		//Log.d(TAG, "hash: " + hash);
		setupTabs();

		findViewById(R.id.tvLocationFrom1).setOnClickListener(this);
		findViewById(R.id.tvLocationTo1).setOnClickListener(this);
		findViewById(R.id.tvDates).setOnClickListener(this);
		findViewById(R.id.tvTruckType).setOnClickListener(this);
		findViewById(R.id.tvLoadType).setOnClickListener(this);

		findViewById(R.id.tvLocationFrom1Truck).setOnClickListener(this);
		findViewById(R.id.tvLocationTo1Truck).setOnClickListener(this);
		findViewById(R.id.tvDatesTruck).setOnClickListener(this);
		findViewById(R.id.tvTruckTypeTruck).setOnClickListener(this);
		findViewById(R.id.tvLoadTypeTruck).setOnClickListener(this);

		findViewById(R.id.ivFlagFrom1).setVisibility(View.GONE);
		findViewById(R.id.ivFlagTo1).setVisibility(View.GONE);
		findViewById(R.id.ivDatesRemove).setVisibility(View.GONE);
		findViewById(R.id.ivTruckTypeRemove).setVisibility(View.GONE);
		findViewById(R.id.ivLoadTypeRemove).setVisibility(View.GONE);

		findViewById(R.id.ivFlagFrom1Truck).setVisibility(View.GONE);
		findViewById(R.id.ivFlagTo1Truck).setVisibility(View.GONE);
		findViewById(R.id.ivDatesRemoveTruck).setVisibility(View.GONE);
		findViewById(R.id.ivTruckTypeRemoveTruck).setVisibility(View.GONE);
		findViewById(R.id.ivLoadTypeRemoveTruck).setVisibility(View.GONE);

		((ImageButton) findViewById(R.id.ibWeight)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.ibVolume)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.ibLength)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.ibHeight)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.ibWidth)).setOnClickListener(this);
		((ImageView) findViewById(R.id.ivDatesRemove)).setOnClickListener(this);
		((ImageView) findViewById(R.id.ivTruckTypeRemove)).setOnClickListener(this);
		((ImageView) findViewById(R.id.ivLoadTypeRemove)).setOnClickListener(this);
		((Button) findViewById(R.id.btnSearch)).setOnClickListener(this);

		((ImageButton) findViewById(R.id.ibWeightTruck)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.ibVolumeTruck)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.ibLengthTruck)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.ibHeightTruck)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.ibWidthTruck)).setOnClickListener(this);
		((ImageView) findViewById(R.id.ivDatesRemoveTruck)).setOnClickListener(this);
		((ImageView) findViewById(R.id.ivTruckTypeRemoveTruck)).setOnClickListener(this);
		((ImageView) findViewById(R.id.ivLoadTypeRemoveTruck)).setOnClickListener(this);
		((Button) findViewById(R.id.btnSearchTruck)).setOnClickListener(this);
		
		((ImageView) findViewById(R.id.ivLocFrom1Remove)).setVisibility(View.GONE);
		((ImageView) findViewById(R.id.ivLocFrom1Remove)).setOnClickListener(this);
		((ImageView) findViewById(R.id.ivLocTo1Remove)).setVisibility(View.GONE);
		((ImageView) findViewById(R.id.ivLocTo1Remove)).setOnClickListener(this);
		
		((ImageView) findViewById(R.id.ivLocFrom1RemoveTruck)).setVisibility(View.GONE);
		((ImageView) findViewById(R.id.ivLocFrom1RemoveTruck)).setOnClickListener(this);
		((ImageView) findViewById(R.id.ivLocTo1RemoveTruck)).setVisibility(View.GONE);
		((ImageView) findViewById(R.id.ivLocTo1RemoveTruck)).setOnClickListener(this);

	    LinearLayout layout = (LinearLayout)findViewById(R.id.llBanner);
	    adViewCargo = new ClassAds(this, layout);
	    layout = (LinearLayout)findViewById(R.id.llBannerTruck);
	    adViewTruck = new ClassAds(this, layout);

	}

	protected void setupTabs() {

		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
		// инициализация
		tabHost.setup();

		TabHost.TabSpec tabSpec;

		// создаем вкладку и указываем тег
		tabSpec = tabHost.newTabSpec("cargo");
		// название вкладки
		tabSpec.setIndicator("",
				getResources().getDrawable(R.drawable.tab_cargo_icon_selector));
		// указываем id компонента из FrameLayout, он и станет содержимым
		tabSpec.setContent(R.id.tabCargoSearch);
		// добавляем в корневой элемент
		tabHost.addTab(tabSpec);

		tabSpec = tabHost.newTabSpec("truck");
		// указываем название и картинку
		// в нашем случае вместо картинки идет xml-файл,
		// который определяет картинку по состоянию вкладки
		// tabSpec.setIndicator("Вкладка 2",
		// getResources().getDrawable(R.drawable.tab_icon_selector));
		tabSpec.setIndicator("",
				getResources().getDrawable(R.drawable.tab_truck_icon_selector));
		tabSpec.setContent(R.id.tabTruckSearch);
		tabHost.addTab(tabSpec);

		// вторая вкладка будет выбрана по умолчанию
		// tabHost.setCurrentTabByTag("tag2");

		// обработчик переключения вкладок
		// tabHost.setOnTabChangedListener(new OnTabChangeListener() {
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.tvLocationFrom1:
			intent = new Intent(this, ActivityChooseLocation.class);
			intent.putExtra("hash", hash);
			startActivityForResult(intent, LOC_CHOOSE_FROM1);
			break;
		case R.id.tvLocationTo1:
			intent = new Intent(this, ActivityChooseLocation.class);
			intent.putExtra("hash", hash);
			startActivityForResult(intent, LOC_CHOOSE_TO1);
			break;
		case R.id.tvDates:
			intent = new Intent(this, DialogDatePicker.class);
			intent.putExtra("dateFrom", dateFrom);
			intent.putExtra("dateTo", dateTo);
			startActivityForResult(intent, DATE_CHOOSE);
			break;
		case R.id.ivLocFrom1Remove:
			onActivityResult(LOC_CHOOSE_FROM1, RESULT_CANCELED, null);
			break;
		case R.id.ivLocTo1Remove:
			onActivityResult(LOC_CHOOSE_TO1, RESULT_CANCELED, null);
			break;
		case R.id.ivDatesRemove:
			//Log.d(TAG, "Dates REmove");
			onActivityResult(DATE_CHOOSE, RESULT_CANCELED, null);
			break;
		case R.id.tvTruckType:
			DisplayDialog(TRUCK_TYPE_DIALOG);
			break;
		case R.id.ivTruckTypeRemove:
			truckType = "0";
			changeText(R.id.tvTruckType, R.id.ivTruckTypeRemove, "(Любые)",
					false);
			break;
		case R.id.tvLoadType:
			DisplayDialog(LOAD_TYPE_DIALOG);
			break;
		case R.id.ivLoadTypeRemove:
			loadType = "up,side,back,tent,stoika";
			changeText(R.id.tvLoadType, R.id.ivLoadTypeRemove, "(Любые)", false);
			break;
		case R.id.ibWeight:
			DisplayDialog(WEIGHT_DIALOG);
			break;
		case R.id.ibVolume:
			DisplayDialog(VOLUME_DIALOG);
			break;
		case R.id.ibLength:
			DisplayDialog(LENGTH_DIALOG);
			break;
		case R.id.ibHeight:
			DisplayDialog(HEIGHT_DIALOG);
			break;
		case R.id.ibWidth:
			DisplayDialog(WIDTH_DIALOG);
			break;
		case R.id.btnSearch:
			intent = new Intent(this, ActivityCargoSearch.class);
			intent.putExtra("hash", hash);
			intent.putExtra("type", "cargo");
			intent.putExtra("locFrom1", locFrom1);
			intent.putExtra("locTo1", locTo1);
			intent.putExtra("dateFrom", dateFrom);
			intent.putExtra("dateTo", dateTo);
			intent.putExtra("truckType", truckType);
			intent.putExtra("loadType", loadType);
			intent.putExtra("weightFrom", weightFrom);
			intent.putExtra("weightTo", weightTo);
			intent.putExtra("volumeFrom", volumeFrom);
			intent.putExtra("volumeTo", volumeTo);
			intent.putExtra("lengthFrom", lengthFrom);
			intent.putExtra("lengthTo", lengthTo);
			intent.putExtra("heightFrom", heightFrom);
			intent.putExtra("heightTo", heightTo);
			intent.putExtra("widthFrom", widthFrom);
			intent.putExtra("widthTo", widthTo);
			startActivity(intent);
			break;
//   TRUCK SEARCH
		case R.id.tvLocationFrom1Truck:
			intent = new Intent(this, ActivityChooseLocation.class);
			intent.putExtra("hash", hash);
			startActivityForResult(intent, LOC_CHOOSE_FROM1_TRUCK);
			break;
		case R.id.tvLocationTo1Truck:
			intent = new Intent(this, ActivityChooseLocation.class);
			intent.putExtra("hash", hash);
			startActivityForResult(intent, LOC_CHOOSE_TO1_TRUCK);
			break;
		case R.id.tvDatesTruck:
			intent = new Intent(this, DialogDatePicker.class);
			intent.putExtra("dateFrom", dateFromTruck);
			intent.putExtra("dateTo", dateToTruck);
			startActivityForResult(intent, DATE_CHOOSE_TRUCK);
			break;
		case R.id.ivLocFrom1RemoveTruck:
			onActivityResult(LOC_CHOOSE_FROM1_TRUCK, RESULT_CANCELED, null);
			break;
		case R.id.ivLocTo1RemoveTruck:
			onActivityResult(LOC_CHOOSE_TO1_TRUCK, RESULT_CANCELED, null);
			break;
		case R.id.ivDatesRemoveTruck:
			//Log.d(TAG, "Dates REmove");
			onActivityResult(DATE_CHOOSE_TRUCK, RESULT_CANCELED, null);
			break;
		case R.id.tvTruckTypeTruck:
			DisplayDialog(TRUCK_TYPE_DIALOG_TRUCK);
			break;
		case R.id.ivTruckTypeRemoveTruck:
			truckTypeTruck = "0";
			changeText(R.id.tvTruckTypeTruck, R.id.ivTruckTypeRemoveTruck, "(Любые)",	false);
			break;
		case R.id.tvLoadTypeTruck:
			DisplayDialog(LOAD_TYPE_DIALOG_TRUCK);
			break;
		case R.id.ivLoadTypeRemoveTruck:
			loadTypeTruck = "up,side,back,tent,stoika";
			changeText(R.id.tvLoadTypeTruck, R.id.ivLoadTypeRemoveTruck, "(Любые)", false);
			break;
		case R.id.ibWeightTruck:
			DisplayDialog(WEIGHT_DIALOG_TRUCK);
			break;
		case R.id.ibVolumeTruck:
			DisplayDialog(VOLUME_DIALOG_TRUCK);
			break;
		case R.id.ibLengthTruck:
			DisplayDialog(LENGTH_DIALOG_TRUCK);
			break;
		case R.id.ibHeightTruck:
			DisplayDialog(HEIGHT_DIALOG_TRUCK);
			break;
		case R.id.ibWidthTruck:
			DisplayDialog(WIDTH_DIALOG_TRUCK);
			break;
		case R.id.btnSearchTruck:
			intent = new Intent(this, ActivityTruckSearch.class);
			intent.putExtra("hash", hash);
			intent.putExtra("type", "truck");
			intent.putExtra("locFrom1", locFrom1Truck);
			intent.putExtra("locTo1", locTo1Truck);
			intent.putExtra("dateFrom", dateFromTruck);
			intent.putExtra("dateTo", dateToTruck);
			intent.putExtra("truckType", truckTypeTruck);
			intent.putExtra("loadType", loadTypeTruck);
			intent.putExtra("weightFrom", weightFromTruck);
			intent.putExtra("weightTo", weightToTruck);
			intent.putExtra("volumeFrom", volumeFromTruck);
			intent.putExtra("volumeTo", volumeToTruck);
			intent.putExtra("lengthFrom", lengthFromTruck);
			intent.putExtra("lengthTo", lengthToTruck);
			intent.putExtra("heightFrom", heightFromTruck);
			intent.putExtra("heightTo", heightToTruck);
			intent.putExtra("widthFrom", widthFromTruck);
			intent.putExtra("widthTo", widthToTruck);
			startActivity(intent);
			break;

		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_CANCELED) {
			if (requestCode == LOC_CHOOSE_FROM1) { // возврат из MainScreen
				locFrom1 = new EntityLocation();
				changeText(R.id.tvLocationFrom1, R.id.ivLocFrom1Remove, "", false);
				setFlag(R.id.ivFlagFrom1, 0);
			}
			if (requestCode == LOC_CHOOSE_TO1) { // возврат из MainScreen
				locTo1 = new EntityLocation();
				changeText(R.id.tvLocationTo1, R.id.ivLocTo1Remove, "", false);
				setFlag(R.id.ivFlagTo1, 0);
			}
			if (requestCode == DATE_CHOOSE) { // возврат из MainScreen
				dateFrom = dateTo = 0L;
				changeText(R.id.tvDates, R.id.ivDatesRemove, "(Любые)", false);
			}
// TRUCK
			if (requestCode == LOC_CHOOSE_FROM1_TRUCK) { // возврат из MainScreen
				locFrom1Truck = new EntityLocation();
				changeText(R.id.tvLocationFrom1Truck, R.id.ivLocFrom1RemoveTruck, "", false);
				setFlag(R.id.ivFlagFrom1Truck, 0);
			}
			if (requestCode == LOC_CHOOSE_TO1_TRUCK) { // возврат из MainScreen
				locTo1Truck = new EntityLocation();
				changeText(R.id.tvLocationTo1Truck, R.id.ivLocTo1RemoveTruck, "", false);
				setFlag(R.id.ivFlagTo1Truck, 0);
			}
			if (requestCode == DATE_CHOOSE_TRUCK) { // возврат из MainScreen
				dateFromTruck = dateToTruck = 0L;
				changeText(R.id.tvDatesTruck, R.id.ivDatesRemoveTruck, "(Любые)", false);
			}

		}

		if (resultCode != RESULT_OK) {
			return;
		}

		if (requestCode == LOC_CHOOSE_FROM1) {
			locFrom1 = data.getExtras().getParcelable("location");
			changeText(R.id.tvLocationFrom1, R.id.ivLocFrom1Remove, locFrom1.getLocName(false), true);
			setFlag(R.id.ivFlagFrom1, locFrom1.getFlag());
		}
		if (requestCode == LOC_CHOOSE_TO1) {
			locTo1 = data.getExtras().getParcelable("location");
			changeText(R.id.tvLocationTo1, R.id.ivLocTo1Remove, locTo1.getLocName(false), true);
			setFlag(R.id.ivFlagTo1, locTo1.getFlag());
		}
		if (requestCode == DATE_CHOOSE) {
			String range = data.getExtras().getString("StringRange");
			Boolean selectedFrom = data.getExtras().getBoolean("selectedFrom");
			Boolean selectedTo = data.getExtras().getBoolean("selectedTo");
			if (selectedFrom || selectedTo) {
				dateFrom = data.getExtras().getLong("dateFrom");
				dateTo = data.getExtras().getLong("dateTo");
				 //Log.d(TAG, "df: " + String.valueOf(dateFrom));
				 //Log.d(TAG, "dt: " + String.valueOf(dateTo));
				changeText(R.id.tvDates, R.id.ivDatesRemove, range, true);
			}
		}
// TRUCK
		if (requestCode == LOC_CHOOSE_FROM1_TRUCK) {
			locFrom1Truck = data.getExtras().getParcelable("location");
			changeText(R.id.tvLocationFrom1Truck, R.id.ivLocFrom1RemoveTruck, locFrom1Truck.getLocName(false), true);
			setFlag(R.id.ivFlagFrom1Truck, locFrom1Truck.getFlag());
		}
		if (requestCode == LOC_CHOOSE_TO1_TRUCK) {
			locTo1Truck = data.getExtras().getParcelable("location");
			changeText(R.id.tvLocationTo1Truck, R.id.ivLocTo1RemoveTruck, locTo1Truck.getLocName(false), true);
			setFlag(R.id.ivFlagTo1Truck, locTo1Truck.getFlag());
		}
		if (requestCode == DATE_CHOOSE_TRUCK) {
			String range = data.getExtras().getString("StringRange");
			Boolean selectedFrom = data.getExtras().getBoolean("selectedFrom");
			Boolean selectedTo = data.getExtras().getBoolean("selectedTo");
			if (selectedFrom || selectedTo) {
				dateFromTruck = data.getExtras().getLong("dateFrom");
				dateToTruck = data.getExtras().getLong("dateTo");
				changeText(R.id.tvDatesTruck, R.id.ivDatesRemoveTruck, range, true);
			}
		}

		return;
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

	private void setFlag(int imageResource, String flag) {
		int res_id = getResources().getIdentifier("flag_" + flag, "drawable", getPackageName());
		setFlag(imageResource, res_id);
	}
	
	private void setFlag(int imageResource, int flag) {
		ImageView img = (ImageView) findViewById(imageResource);
		if (flag == 0) {
			img.setVisibility(View.GONE);
			return;
		}
		img.setVisibility(View.VISIBLE);
		img.setImageResource(flag);
		img.getLayoutParams().width = 40;
		img.getLayoutParams().height = 24;
	}

	private void DisplayDialog(int dialog) {
		FragmentManager fm = getSupportFragmentManager();
		switch (dialog) {
		case TRUCK_TYPE_DIALOG_TRUCK:
		case TRUCK_TYPE_DIALOG:
			DialogDbSpr dlg = new DialogDbSpr("truck_type","Тип кузова",this, dialog);
			dlg.show(fm, "DIALOG");
			break;
		case LOAD_TYPE_DIALOG_TRUCK:
		case LOAD_TYPE_DIALOG:
			DialogLoadType dlg2 = new DialogLoadType(this, dialog,loadType);
			dlg2.show(fm, "DIALOG");
			break;
		case WEIGHT_DIALOG_TRUCK:
		case WEIGHT_DIALOG:
			DialogNumberPickerRange dlg3 = new DialogNumberPickerRange(this,
					dialog, "Вес, т.", 0, MAX_WEIGHT_VALUE);
			dlg3.setInitFrom(dialog==WEIGHT_DIALOG ? weightFrom : weightFromTruck);
			dlg3.setInitTo(dialog==WEIGHT_DIALOG ? weightTo : weightToTruck);
			dlg3.show(fm, "WEIGHT_DIALOG");
			break;
		case VOLUME_DIALOG_TRUCK:
		case VOLUME_DIALOG:
			DialogNumberPickerRange dlg4 = new DialogNumberPickerRange(this,
					dialog, "Объем, м3", 0, MAX_VOLUME_VALUE);
			dlg4.setInitFrom(dialog==VOLUME_DIALOG ? volumeFrom : volumeFromTruck);
			dlg4.setInitTo(dialog==VOLUME_DIALOG ? volumeTo : volumeToTruck);
			dlg4.show(fm, "VOLUME_DIALOG");
			break;
		case LENGTH_DIALOG_TRUCK:
		case LENGTH_DIALOG:
			DialogNumberPickerRange dlg5 = new DialogNumberPickerRange(this,
					dialog, "Длинна, м.", 0, MAX_LENGTH_VALUE);
			dlg5.setInitFrom(dialog==LENGTH_DIALOG ? lengthFrom : lengthFromTruck);
			dlg5.setInitTo(dialog==LENGTH_DIALOG ? lengthTo : lengthToTruck);
			dlg5.show(fm, "LENGTH_DIALOG");
			break;
		case HEIGHT_DIALOG_TRUCK:
		case HEIGHT_DIALOG:
			DialogNumberPickerRange dlg6 = new DialogNumberPickerRange(this,
					dialog, "Высота, м.", 0, MAX_HEIGHT_VALUE);
			dlg6.setInitFrom(dialog==HEIGHT_DIALOG ? heightFrom : heightFromTruck);
			dlg6.setInitTo(dialog==HEIGHT_DIALOG ? heightTo : heightToTruck);
			dlg6.show(fm, "HEIGHT_DIALOG");
			break;
		case WIDTH_DIALOG_TRUCK:
		case WIDTH_DIALOG:
			DialogNumberPickerRange dlg7 = new DialogNumberPickerRange(this,
					dialog, "Ширина, м.", 0, MAX_WIDTH_VALUE);
			dlg7.setInitFrom(dialog==WIDTH_DIALOG ? widthFrom : widthFromTruck);
			dlg7.setInitTo(dialog==WIDTH_DIALOG ? widthTo : widthToTruck);
			dlg7.show(fm, "WIDTH_DIALOG");
			break;

		}
	}

	@Override
	public void fgdCallback(String id, String data, int reqCode) {
		switch (reqCode) {
		case TRUCK_TYPE_DIALOG:
			truckType = id;
			changeText(R.id.tvTruckType, R.id.ivTruckTypeRemove, data, true);
			break;
		case LOAD_TYPE_DIALOG:
			if (id.length() > 0) {
				loadType = id;
				changeText(R.id.tvLoadType, R.id.ivLoadTypeRemove, data, true);
			} else {
				loadType = "up,side,back,tent,stoika";
				changeText(R.id.tvLoadType, R.id.ivLoadTypeRemove, "(Любые)",
						false);
			}
			break;
		case WEIGHT_DIALOG:
			if (id.length() > 0) {
				String[] tmp = id.split(",");
				weightFrom = tmp[0].trim();
				weightTo = tmp[1].trim();
				changeText(R.id.tvWeight, 0, data + "т", true);
			} else {
				weightFrom = weightTo = "";
				changeText(R.id.tvWeight, 0, "Любой", false);
			}
			break;
		case VOLUME_DIALOG:
			if (id.length() > 0) {
				String[] tmp = id.split(",");
				volumeFrom = tmp[0].trim();
				volumeTo = tmp[1].trim();
				changeText(R.id.tvVolume, 0, data, true);
			} else {
				volumeFrom = volumeTo = "";
				changeText(R.id.tvVolume, 0, "Любой", false);
			}
			break;
		case LENGTH_DIALOG:
			if (id.length() > 0) {
				String[] tmp = id.split(",");
				lengthFrom = tmp[0].trim();
				lengthTo = tmp[1].trim();
				changeText(R.id.tvLength, 0, data, true);
			} else {
				lengthFrom = lengthTo = "";
				changeText(R.id.tvLength, 0, "Любой", false);
			}
			break;
		case HEIGHT_DIALOG:
			if (id.length() > 0) {
				String[] tmp = id.split(",");
				heightFrom = tmp[0].trim();
				heightTo = tmp[1].trim();
				changeText(R.id.tvHeight, 0, data, true);
			} else {
				heightFrom = heightTo = "";
				changeText(R.id.tvHeight, 0, "Любой", false);
			}
			break;
		case WIDTH_DIALOG:
			if (id.length() > 0) {
				String[] tmp = id.split(",");
				widthFrom = tmp[0].trim();
				widthTo = tmp[1].trim();
				changeText(R.id.tvWidth, 0, data, true);
			} else {
				widthFrom = widthTo = "";
				changeText(R.id.tvWidth, 0, "Любой", false);
			}
			break;
// TRUCK
		case TRUCK_TYPE_DIALOG_TRUCK:
			truckTypeTruck = id;
			changeText(R.id.tvTruckTypeTruck, R.id.ivTruckTypeRemoveTruck, data, true);
			break;
		case LOAD_TYPE_DIALOG_TRUCK:
			if (id.length() > 0) {
				loadTypeTruck = id;
				changeText(R.id.tvLoadTypeTruck, R.id.ivLoadTypeRemoveTruck, data, true);
			} else {
				loadTypeTruck = "up,side,back,tent,stoika";
				changeText(R.id.tvLoadTypeTruck, R.id.ivLoadTypeRemoveTruck, "(Любые)",
						false);
			}
			break;
		case WEIGHT_DIALOG_TRUCK:
			if (id.length() > 0) {
				String[] tmp = id.split(",");
				weightFromTruck = tmp[0].trim();
				weightToTruck = tmp[1].trim();
				changeText(R.id.tvWeightTruck, 0, data + "т", true);
			} else {
				weightFromTruck = weightToTruck = "";
				changeText(R.id.tvWeightTruck, 0, "Любой", false);
			}
			break;
		case VOLUME_DIALOG_TRUCK:
			if (id.length() > 0) {
				String[] tmp = id.split(",");
				volumeFromTruck = tmp[0].trim();
				volumeToTruck = tmp[1].trim();
				changeText(R.id.tvVolumeTruck, 0, data, true);
			} else {
				volumeFromTruck = volumeToTruck = "";
				changeText(R.id.tvVolumeTruck, 0, "Любой", false);
			}
			break;
		case LENGTH_DIALOG_TRUCK:
			if (id.length() > 0) {
				String[] tmp = id.split(",");
				lengthFromTruck = tmp[0].trim();
				lengthToTruck = tmp[1].trim();
				changeText(R.id.tvLengthTruck, 0, data, true);
			} else {
				lengthFromTruck = lengthToTruck = "";
				changeText(R.id.tvLengthTruck, 0, "Любой", false);
			}
			break;
		case HEIGHT_DIALOG_TRUCK:
			if (id.length() > 0) {
				String[] tmp = id.split(",");
				heightFromTruck = tmp[0].trim();
				heightToTruck = tmp[1].trim();
				changeText(R.id.tvHeightTruck, 0, data, true);
			} else {
				heightFromTruck = heightToTruck = "";
				changeText(R.id.tvHeightTruck, 0, "Любой", false);
			}
			break;
		case WIDTH_DIALOG_TRUCK:
			if (id.length() > 0) {
				String[] tmp = id.split(",");
				widthFromTruck = tmp[0].trim();
				widthToTruck = tmp[1].trim();
				changeText(R.id.tvWidthTruck, 0, data, true);
			} else {
				widthFromTruck = widthToTruck = "";
				changeText(R.id.tvWidthTruck, 0, "Любой", false);
			}
			break;
		}

	}
}
