package by.transavto.transavto;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

final class ClassMyHandler extends Handler {

	WeakReference<Activity> wrActivity;

	public ClassMyHandler(Activity activity) {
		wrActivity = new WeakReference<Activity>(activity);
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		Activity activity = wrActivity.get();
		if (activity != null && activity instanceof InterfaceMyHandler)
			((InterfaceMyHandler) activity).processHandler(msg);
	}
}
