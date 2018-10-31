package by.transavto.transavto;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityChat extends Activity implements OnScrollListener,
		OnClickListener, InterfaceMyHandler {

	final String TAG = "chat";
	final Integer REQUEST_RESULT = 1;
	final Integer NEW_MESSAGE_RESULT = 2;
	final Integer TIMER_REQUEST = 3;
	Boolean LoadMoreRequest = false;
	Integer min_id = 2147483647;
	Integer max_id = 0;
	String hash = "";
	final Activity chatActivity = this;

	ListView lvItem;
	ProgressBar pbChatSpinner;
	ArrayList<EntityChatMessage> itemArray;
	AdapterChatListView itemAdapter;
	ClassMyHandler h;
	EditText textInput;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_screen);
		Intent intent = getIntent();

		hash = intent.getStringExtra("hash");

		pbChatSpinner = (ProgressBar) findViewById(R.id.pbChatSpinner);
		lvItem = (ListView) this.findViewById(R.id.lvChat);
		lvItem.setOnScrollListener(this);
		itemArray = new ArrayList<EntityChatMessage>();
		itemAdapter = new AdapterChatListView(this, itemArray);
		lvItem.setAdapter(itemAdapter);

		findViewById(R.id.btnChatSend).setOnClickListener(this);

		textInput = (EditText)findViewById(R.id.etChatMessage);
		textInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
		textInput.setOnEditorActionListener(
		        new EditText.OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		    	if (actionId == EditorInfo.IME_ACTION_DONE) {
		    		 onClick(findViewById(R.id.btnChatSend));
		    		 return true;
		    		 }
		        return false;
		    }
		});
		
		
		
        // спрячем клавиатуру
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 

		h = new ClassMyHandler(this);

		sendRequest(REQUEST_RESULT, "", "", true);
	}

	public void onDestroy() {
		super.onDestroy();
		h.removeMessages(TIMER_REQUEST);
	}

	public void processResponce(String result, Boolean addTop) {
		Dialog dialog = new Dialog(this);
		// Log.d(TAG,"HTTP Request complete");
		// if (result.length()<=0) return;
		// Log.d(TAG,result);
		JSONObject json;
		try {
			json = new JSONObject(result);
		} catch (Exception e) {
		//	Log.d(TAG, e.toString());
			dialog.ErrorDialog("Неверный ответ от сервера");
			finish();
			return;
		}

		try {
			max_id = Math.max(max_id, json.getInt("msg_id"));
			JSONArray data = json.getJSONArray("data");
			addChatMessages(data, addTop);
			return;
		} catch (Exception e) {
			dialog.ErrorDialog(e.getMessage());
		}
		
	}

	public void addChatMessages(JSONArray data, Boolean addTop) {

		Integer res_id;

		for (int i = 0; i < data.length(); i++) {
			try {
				JSONObject row = data.getJSONObject(i);
				// if (i==0) min_id=row.getInt("mesId");
				min_id = Math.min(min_id, row.getInt("mesId"));
				String firmName = row.getString("firmName");
				String mesDate = row.getString("mesDate");
				String message = row.getString("message");
				String firmCountry = row.getString("firmCountry").toLowerCase();

				res_id = getResources().getIdentifier("flag_" + firmCountry,
						"drawable", getPackageName());
				EntityChatMessage chMes = new EntityChatMessage();
				chMes.setAll(firmName, res_id, mesDate, message);
				if (addTop) {
					// Log.d(TAG,"addTOP");
					itemArray.add(0, chMes);
				} else {
					// Log.d(TAG,"addBOttom");
					itemArray.add(chMes);
				}

				// Log.d(TAG,String.valueOf(itemArray.toString()));
				// log(itemArray);
				itemAdapter.notifyDataSetChanged();
			} catch (Exception e) {};
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnChatSend) {
			String msg = textInput.getText().toString();
			if (msg.trim().equalsIgnoreCase("")) {
				Toast.makeText(this, "Введите сообщение", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			sendRequest(NEW_MESSAGE_RESULT, "msg", msg.trim(), false);
			textInput.setText("");
		}

	}

	public void sendRequest(Integer handlerWhat, String paraKey,
			String paraValue, Boolean switchSpinner) {
		final class SendRequestThread extends Thread {
			Integer handlerWhat;
			String paraKey;
			String paraValue;
			Activity parent;

			public SendRequestThread(Activity parent, Integer handlerWhat,
					String paraKey, String paraValue) {
				this.handlerWhat = handlerWhat;
				this.paraKey = paraKey;
				this.paraValue = paraValue;
				this.parent = parent;
			}

			public void run() {
				h.removeMessages(TIMER_REQUEST); // удалим таймер
				RequestInfo request = new RequestInfo();
				request.addKeyValue("hash", hash);
				if (!this.paraKey.equals(""))
					request.addKeyValue(this.paraKey, this.paraValue);

				if (!this.paraKey.equalsIgnoreCase("min_id"))
					request.addKeyValue("msg_id", String.valueOf(max_id));

				// Log.d(TAG,String.valueOf(request.requestData));
				request.setReqMethod("sync");
				request.setUrl("/chat_app");
				request.setMethod("post");
				Http http_client = new Http(this.parent);
				RequestResult result = http_client.execute(request, 0);
				h.sendEmptyMessageDelayed(TIMER_REQUEST, 5000); // ставим таймер
																// на 5 сек для
																// проверки
																// новых
																// сообщений
				h.sendMessage(Message.obtain(h, this.handlerWhat, 0, 0, result));
			}

		}
		if (switchSpinner)
			pbChatSpinner.setVisibility(View.VISIBLE);

		SendRequestThread t = new SendRequestThread(chatActivity, handlerWhat,
				paraKey, paraValue);
		t.start();

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		boolean loadMore = (firstVisibleItem + visibleItemCount >= totalItemCount)
				&& (totalItemCount > visibleItemCount);

		// Log.d(TAG,"fVI: "+String.valueOf(firstVisibleItem));
		// Log.d(TAG,"vIC: "+String.valueOf(visibleItemCount));
		// Log.d(TAG,"tIC: "+String.valueOf(totalItemCount));
		// Log.d(TAG,"loadMore: "+(loadMore ? "true" : "false"));

		if (loadMore && !LoadMoreRequest) {
			LoadMoreRequest = true;
			sendRequest(REQUEST_RESULT, "min_id", String.valueOf(min_id), true);
		}
	}

	@Override
	public void processHandler(Message msg) {
		// Log.d(TAG,"Handler: "+String.valueOf(msg.what));
		if (msg.what == REQUEST_RESULT) {
			// Log.d(TAG,"REQUEST RESULTS");
			LoadMoreRequest = false;
			pbChatSpinner.setVisibility(View.GONE);
			RequestResult rr;
			rr = (RequestResult) msg.obj;
			processResponce(rr.getReqBody(), false);
		}
		if (msg.what == NEW_MESSAGE_RESULT) {
			// Log.d(TAG,"REQUEST RESULTS");
			pbChatSpinner.setVisibility(View.GONE);
			RequestResult rr;
			rr = (RequestResult) msg.obj;
			// Log.d(TAG,"New message recived.");
			Integer old_id = max_id;
			processResponce(rr.getReqBody(), true);
			// Log.d(TAG,"old_id: "+String.valueOf(old_id)+" new_id:"+String.valueOf(max_id));
			if (!old_id.equals(max_id)) {
				// Log.d(TAG,"Move list to top");
				lvItem.setSelection(0);
			}
		}
		if (msg.what == TIMER_REQUEST) {
			// Log.d(TAG,"TIMER REQUEST");
			sendRequest(NEW_MESSAGE_RESULT, "", "", false);
		}
	}
}
