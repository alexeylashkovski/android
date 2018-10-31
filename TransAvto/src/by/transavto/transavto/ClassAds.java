package by.transavto.transavto;

import android.app.Activity;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class ClassAds {
	
	private AdView adView;
	final String pubId = "ca-app-pub-5817253873712337/9253413204";
	private Activity activity;
	
	boolean testMode = false;
	
	public ClassAds(Activity act, LinearLayout ll) {
	    adView = new AdView(act);
	    adView.setAdUnitId(this.pubId);
	    adView.setAdSize(AdSize.BANNER);

	    ll.addView(adView);
	    
	    AdRequest adRequest;
	    
	    if (this.testMode) {
	    	adRequest  = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)       // Эмулятор
	    							  .addTestDevice("AC98C820A50B4AD8A2106EDE96FB87D4") // Тестовый телефон Galaxy Nexus
	    							  .build();
	    }
	    else 
	    	adRequest = new AdRequest.Builder().build();
	    // Загрузка adView с объявлением.
	    adView.loadAd(adRequest);
	}

}
