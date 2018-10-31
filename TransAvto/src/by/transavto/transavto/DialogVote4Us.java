package by.transavto.transavto;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DialogVote4Us extends Activity implements OnClickListener {
	
	final String TAG = "Vote4Us";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vote4us);
		
		((Button) findViewById(R.id.btnVote)).setOnClickListener(this);
		((Button) findViewById(R.id.btnCancel)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		boolean res;
	    Intent intent = new Intent();
	    
		switch (v.getId()) {
		case R.id.btnVote:
		    Uri uri = Uri.parse("market://details?id=" + getPackageName());
		    Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
		    try {
		        startActivity(myAppLinkToMarket);
		        res=true;
		    } catch (ActivityNotFoundException e) {
		//    	Log.d(TAG, e.getMessage());
		        Toast.makeText(this, "Не возможно найти приложение Google Play", Toast.LENGTH_LONG).show();
		        res=false;
		    }
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.btnCancel:
			setResult(RESULT_CANCELED, intent);
			finish();
			break;
		}
	}

}
