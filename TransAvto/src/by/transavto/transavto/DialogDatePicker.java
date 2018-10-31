package by.transavto.transavto;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DialogDatePicker extends Activity implements OnClickListener {
	private ViewDatePicker dp;
	final String TAG = "DatePickerDialog";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.datepicker_dialog);
		
		Intent intent = getIntent();
		Long df = intent.getLongExtra("dateFrom", 0L);
		Long dt = intent.getLongExtra("dateTo", 0L);
		
		//Log.d(TAG, "df: "+String.valueOf(df));
		//Log.d(TAG, "dt: "+String.valueOf(dt));


		dp = (ViewDatePicker) findViewById(R.id.dpDPC);

		if (df != 0L) setDateFrom(df);
		if (dt != 0L) setDateTo(dt+6000);
		
		
////Log.d(TAG,"OnCreate");
				
		//Log.d(TAG,"df: "+dp.getDateFrom().toString());
		//Log.d(TAG,"dt: "+dp.getDateTo().toString());
		((Button) findViewById(R.id.btn3days)).setOnClickListener(this);
		((Button) findViewById(R.id.btn5days)).setOnClickListener(this);
		((Button) findViewById(R.id.btn7days)).setOnClickListener(this);
		((Button) findViewById(R.id.btnConfirm)).setOnClickListener(this);
	}
	
	public void setDateFrom(Long df) {
		dp.setDateFrom(df);
	}

	public void setDateTo(Long dt) {
		dp.setDateFrom(dt);
	}

	public void onResume() {
		super.onResume();
		dp.markDates();
	}

	@Override
	public void onClick(View v) {
		Calendar c = Calendar.getInstance();
		c.setTime(dp.getToday().getTime());
		// dp.clearAllMarks();
		switch (v.getId()) {
		case R.id.btn3days:
			dp.setDateFrom(c);
			c.add(Calendar.DAY_OF_MONTH, 2);
			dp.setDateTo(c);
			break;
		case R.id.btn5days:
			dp.setDateFrom(c);
			c.add(Calendar.DAY_OF_MONTH, 4);
			dp.setDateTo(c);
			break;
		case R.id.btn7days:
			dp.setDateFrom(c);
			c.add(Calendar.DAY_OF_MONTH, 6);
			dp.setDateTo(c);
			break;
		case R.id.btnConfirm:
			Intent intent = new Intent();
			//Log.d(TAG, "Range: " + getRusRange());
			intent.putExtra("StringRange", getRusRange());
			intent.putExtra("selectedFrom", dp.selectedFrom);
			intent.putExtra("selectedTo", dp.selectedTo);
			if (dp.selectedFrom)
				intent.putExtra("dateFrom", dp.getDateFrom().getTimeInMillis());
			else
				intent.putExtra("dateFrom", 0);
			if (dp.selectedTo && dp.getDateFrom()!=dp.getDateTo())
				intent.putExtra("dateTo", dp.getDateTo().getTimeInMillis());
			else
				intent.putExtra("dateTo", 0);
			setResult(RESULT_OK, intent);
			finish();
			break;
		}
	}

	private String getRusRange() {
		return dp.getRusRange();
	}
}
