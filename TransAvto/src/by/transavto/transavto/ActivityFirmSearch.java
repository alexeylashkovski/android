package by.transavto.transavto;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityFirmSearch extends Activity implements OnScrollListener, OnClickListener, InterfaceMyHandler, OnItemClickListener{

	String hash="";

	final String TAG = "FirmSearch";
	final int REQUEST_RESULTS = 1;
	ProgressBar pbFirmSearchSpinner;
	ListView  lvItem;
	ArrayList<EntityFirm> itemArray;
	AdapterFirmListView itemAdapter;
	EditText textSearch;
	LinearLayout infoBox;
	Boolean loadMoreRequest = false;
	Boolean footerAdded = false;
	int total_ads=0;
	int page = 0;
	
	ClassMyHandler h;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firm_search_screen);
		Intent intent = getIntent();

		hash = intent.getStringExtra("hash");

		pbFirmSearchSpinner = (ProgressBar) findViewById(R.id.pbFirmsSearchSpinner);
		lvItem = (ListView) this.findViewById(R.id.lvFirmsList);
		lvItem.setOnScrollListener(this);
		itemArray = new ArrayList<EntityFirm>();
		itemAdapter = new AdapterFirmListView(this, itemArray);

		View v = getLayoutInflater().inflate(R.layout.cargo_list_header, null);
		lvItem.addHeaderView(v, "", false);
		lvItem.setOnItemClickListener(this);
		lvItem.setAdapter(itemAdapter);

		lvItem.setVisibility(View.GONE);
		pbFirmSearchSpinner.setVisibility(View.GONE);
		
		findViewById(R.id.btnSearch).setOnClickListener(this);

		textSearch = (EditText)findViewById(R.id.etFirmSearch);
		textSearch.setImeOptions(EditorInfo.IME_ACTION_DONE);
		textSearch.setOnEditorActionListener(
		        new EditText.OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		    	if (actionId == EditorInfo.IME_ACTION_DONE) {
		    		 onClick(findViewById(R.id.btnSearch));
		    		 return true;
		    		 }
		        return false;
		    }
		});
		infoBox = (LinearLayout)findViewById(R.id.llFirmSearchInfo);
		
        // спрячем клавиатуру
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 

		h = new ClassMyHandler(this);

		//sendRequest(REQUEST_RESULT, "", "", true);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
		boolean loadMore = (firstVisibleItem + visibleItemCount >= totalItemCount)
				&& (totalItemCount > visibleItemCount) && (totalItemCount<total_ads);
		
		if (loadMore && !loadMoreRequest && totalItemCount<total_ads) {
			loadMoreRequest = true;
			page ++;
			sendRequest(REQUEST_RESULTS);
		}
		
//		Log.d(TAG,"TIC: "+String.valueOf(totalItemCount)+" ta: "+String.valueOf(total_ads));
		if (totalItemCount>=total_ads && !footerAdded) {
			View v = getLayoutInflater().inflate(R.layout.cargo_list_footer, null);
			lvItem.addFooterView(v, "", false);
			footerAdded = true;
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnSearch :
			if (loadMoreRequest)
				return;
			if (textSearch.length()<3) {
				Toast.makeText(getApplicationContext(), "Введите хотя бы 3 символа",  Toast.LENGTH_LONG).show();
				return;
			}
			lvItem.setVisibility(View.GONE);
			itemArray.clear();
			itemAdapter.notifyDataSetChanged();
			page=0;
			sendRequest(REQUEST_RESULTS);
			break;
		}
		
	}
	
	public void sendRequest(Integer handlerWhat) {
		final class SendRequestThread extends Thread {
			Integer handlerWhat;
			Activity parent;

			public SendRequestThread(Activity parent, Integer handlerWhat) {
				this.handlerWhat = handlerWhat;
				this.parent = parent;
			}

			public void run() {
				RequestInfo request = new RequestInfo();
				request.addKeyValue("hash", hash);
				request.addKeyValue("pg", String.valueOf(page));
				request.addKeyValue("sq", textSearch.getText().toString());

				// Log.d(TAG,String.valueOf(request.requestData));
				request.setReqMethod("sync");
				request.setUrl("/firmsearchApp");
				request.setMethod("post");
				Http http_client = new Http(this.parent);
				RequestResult result = http_client.execute(request, 0);
				h.sendMessage(Message.obtain(h, this.handlerWhat, 0, 0, result));
			}

		}

		infoBox.setVisibility(View.GONE);
		pbFirmSearchSpinner.setVisibility(View.VISIBLE);

		SendRequestThread t = new SendRequestThread(this, handlerWhat);
		loadMoreRequest = true;
		t.start();

	}

	@Override
	public void processHandler(Message msg) {
		// TODO Auto-generated method stub
		loadMoreRequest = false;
		pbFirmSearchSpinner.setVisibility(View.GONE);
		String result  = ((RequestResult) msg.obj).getReqBody();
	//	Log.d(TAG,result);
		Dialog dialog = new Dialog(this);

		JSONObject json;
		JSONArray data;
		try {
			json = new JSONObject(result);
			pbFirmSearchSpinner.setVisibility(View.GONE);
			if (json.getInt("total_ads")==0) {
				infoBox.setVisibility(View.VISIBLE);
				((TextView)findViewById(R.id.tvFirmSearchInfo)).setText("По данному запросу фирм не найдено.");
			}
			else
				lvItem.setVisibility(View.VISIBLE);
			total_ads = json.getInt("total_ads");
			((TextView)findViewById(R.id.tvAdsFound)).setText("Найдено фирм: "+String.valueOf(total_ads));
			data = json.getJSONArray("firms");
			Log.d(TAG, String.valueOf(data.length()));
			for (int i = 0; i < data.length(); i++) {
				json = data.getJSONObject(i);
				EntityFirm f = new EntityFirm(json);
				itemArray.add(f);
			}
		} catch (Exception e) {
//			Log.d(TAG, result);
//			Log.d(TAG, e.toString());
			dialog.ErrorDialog("Неверный ответ от сервера");
			//finish();
			return;
		}
		itemAdapter.notifyDataSetChanged();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
		// TODO Auto-generated method stub
		EntityFirm firm = itemArray.get(position-1);
		Intent intent = new Intent(this, ActivityFirmView.class);
		intent.putExtra("hash", hash);
		intent.putExtra("firmId", firm.getId());
		startActivity(intent);
	}


}
