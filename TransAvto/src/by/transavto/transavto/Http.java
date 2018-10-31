package by.transavto.transavto;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

//**************************************************************************
class KeyValue {
	protected String key="";
	protected String value="";
	
	public KeyValue() {}
	public KeyValue(String key, String value) {
		setKey(key);
		setValue(value);
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getKey(){
		return this.key;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue(){
		return this.value;
	}
}
//**************************************************************************
class RequestInfo {
	// final String domain = "http://192.168.1.110";
	//final String domain = "http://10.10.1.6";
	 final String domain = "http://www.transavto.by";

	protected String Url;
	protected String method = "get";
	protected String reqMethod = "async";
//	Map<String, String> requestData = new HashMap<String, String>();
	ArrayList<KeyValue> requestData = new ArrayList<KeyValue>();

	public void setUrl(String url_path) {
		this.Url = this.domain + url_path;
	}
	
	public void addKeyValue(String key, String value) {
		KeyValue kv = new KeyValue(key, value);
		this.requestData.add(kv);
	}

	public String getUrl() {
		if (this.getMethod() == "get") {
			String para = "";
/*			for (Entry<String, String> entry : this.requestData.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				para = para + key + '=' + value + '&';
			}
*/
			for (int i=0; i<this.requestData.size();i++) {
				KeyValue entry = this.requestData.get(i);
				para = para + entry.getKey() + '=' + entry.getValue() + '&';
			}

			return this.Url + "?" + para;
		} else
			return this.Url;
	}

	public void setReqMethod(String new_method) {
		this.reqMethod = new_method.equalsIgnoreCase("sync") ? "sync" : "async";
	}

	public String getReqMethod() {
		return this.reqMethod;
	}

	public void setMethod(String method) {
		this.method = method.toLowerCase() == "post" ? "post" : "get";
	}

	public String getMethod() {
		return this.method;
	}
}

// ***************************************************************************************************
class RequestResult {
	protected Boolean reqResult = false;
	protected String reqBody = "";
	protected int reqCode;

	public void setReqResult(Boolean res) {
		this.reqResult = res;
	}

	public Boolean getReqResult() {
		return this.reqResult;
	}

	public void setReqBody(String txt) {
		this.reqBody = txt;
	}

	public String getReqBody() {
		return this.reqBody;
	}

	public void setReqCode(int txt) {
		this.reqCode = txt;
	}

}

// ***************************************************************************************************
class Http {

	protected int reqCode;
	Activity activity;
	final String TAG = "HTTP_REQUEST";

	public Http(Activity activity) {
		this.activity = activity;
	}

	public RequestResult execute(RequestInfo info, int reqCode) {
		RequestResult result = new RequestResult();
		// //Log.d(TAG,info.getReqMethod());
		this.reqCode = reqCode;
		if (info.getReqMethod().equalsIgnoreCase("async")) {
			try {
				new RequestMakerAsync(this.activity, this.reqCode).execute(info);
			} catch (Exception e) {
			}
		} else {

			try {

				HttpClient httpclient = new DefaultHttpClient();
				// //Log.d(TAG, info.getUrl()+" "+info.getMethod()+" send.");
				// GET дeлаем через POST все параметры в URL
				HttpPost httpReq = new HttpPost(info.getUrl());
				if (info.getMethod().equalsIgnoreCase("post")) {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
/*					for (Entry<String, String> entry : info.requestData
							.entrySet()) {
						String key = entry.getKey();
						String value = entry.getValue();
						nameValuePairs.add(new BasicNameValuePair(key, value));
					}
*/					//Log.d(TAG,httpReq.toString());
			
					for (int i=0; i<info.requestData.size();i++) {
						KeyValue entry = info.requestData.get(i);
						nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
					}

					httpReq.setEntity(new UrlEncodedFormEntity(nameValuePairs,
							HTTP.UTF_8));
				}

				HttpResponse response = httpclient.execute(httpReq);
				HttpEntity entity = response.getEntity();
				result.setReqResult(true);
				result.setReqBody(EntityUtils.toString(entity));
				result.setReqCode(this.reqCode);
				//Log.d(TAG, result.getReqBody());
			} catch (UnknownHostException e) {
				//Log.d(TAG, "excpetion: " + e.toString());
			} catch (Exception e) {
				//Log.d(TAG, "excpetion: " + e.toString());
			}

		}
		return result;
	}

	private class RequestMakerAsync extends
			AsyncTask<RequestInfo, Void, String> {

		private int reqCode;
		private ProgressDialog progressDialog;

		private Activity parentActivity;

		public RequestMakerAsync(Activity parentActivity, int reqCode) {
			if (parentActivity == null)
				throw new IllegalArgumentException("parentActivity");
			this.parentActivity = parentActivity;
			this.progressDialog = new ProgressDialog(this.parentActivity);
			this.progressDialog.setIndeterminate(true);
			this.progressDialog.setCancelable(false);
			this.reqCode = reqCode;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// //Log.d(TAG,"preexecute");
			this.progressDialog.setMessage("Пожалуйста подождите ...");
			this.progressDialog.show();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//Log.d(TAG, "postexecute " + result);
			if (this.progressDialog.isShowing()) {
				this.progressDialog.dismiss();
			}

			if (this.parentActivity instanceof InterfaceAsyncTaskCompleteListener)
				((InterfaceAsyncTaskCompleteListener) parentActivity)
						.onTaskComplete(result, this.reqCode);
		}

		@Override
		protected String doInBackground(RequestInfo... params) {
			// //Log.d(TAG,"Background");
			String result = "";

			try {

				HttpClient httpclient = new DefaultHttpClient();
				// //Log.d(TAG,
				// params[0].getUrl()+" "+params[0].getMethod()+" send.");
				// GET дeлаем через POST все параметры в URL
				HttpPost httpReq = new HttpPost(params[0].getUrl());
				if (params[0].getMethod().equalsIgnoreCase("post")) {
					httpReq = new HttpPost(params[0].getUrl());
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
/*					for (Entry<String, String> entry : params[0].requestData
							.entrySet()) {
						String key = entry.getKey();
						String value = entry.getValue();
						nameValuePairs.add(new BasicNameValuePair(key, value));
					}
*/
					for (int i=0; i<params[0].requestData.size();i++) {
						KeyValue entry = params[0].requestData.get(i);
						nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
					}

					httpReq.setEntity(new UrlEncodedFormEntity(nameValuePairs,	HTTP.UTF_8));
				}
				//Log.d(TAG, httpReq.getURI().toString());
				//Log.d(TAG, EntityUtils.toString(httpReq.getEntity()));
				HttpResponse response = httpclient.execute(httpReq);
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity);
				// //Log.d(TAG, result);
			} catch (UnknownHostException e) {
				// //Log.d(TAG,"excpetion: "+ e.toString());
				return "ERROR: Нет доступа к " + e.getMessage().toString();
			} catch (Exception e) {
				// //Log.d(TAG,"excpetion: "+ e.toString());
				return "ERROR:" + e.getMessage().toString();
			}
			// //Log.d(TAG,"Background finished");

			return result;
		}
	}

}
