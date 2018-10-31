package by.transavto.transavto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class Dialog {

	Activity activity;
	
	protected String title="Œ¯Ë·Í‡!";

	public Dialog(Activity act) {
		activity = act;
	}
	
	public void setTitle(String vl){
		this.title = vl.trim();
	}

	public void ErrorDialog(String title, String message) {
		setTitle(title);
		ErrorDialog(message);
	}
	
	public void ErrorDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(this.title).setMessage(message)
				// .setIcon(R.drawable.ic_android_cat)
				.setCancelable(false)
				.setNegativeButton("Œ ", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

}
