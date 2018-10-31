package by.transavto.transavto;

import net.simonvt.numberpicker.NumberPicker;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class DialogNumberPickerRange extends DialogFragment implements OnClickListener{

	private Activity activity;
	private NumberPicker npFrom, npTo;
	private int reqCode;
	private LinearLayout ll;
	private int initFrom = 0, initTo = 0;
	protected int minValue = 0;
	protected int maxValue = 100;
	private String title="";
	

	public DialogNumberPickerRange() {
		// Empty constructor required for DialogFragment
	}

	public DialogNumberPickerRange(Activity activity, int reqCode, String title, int minValue, int maxValue) {
		this.activity = activity;
		this.reqCode = reqCode;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.title = title;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.number_range_dialog, container);
		getDialog().setTitle(this.title);
		npFrom = (NumberPicker) view.findViewById(R.id.npFrom);
		npTo = (NumberPicker) view.findViewById(R.id.npTo);
		((Button)view.findViewById(R.id.btnNpDialogOk)).setOnClickListener(this);
		((Button)view.findViewById(R.id.btnNpDialogCancel)).setOnClickListener(this);
		ll = (LinearLayout)view.findViewById(R.id.llNpRange);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		this.setStyle(DialogFragment.STYLE_NORMAL, R.style.SampleTheme_Light);
		npFrom.setMinValue(minValue);
		npFrom.setMaxValue(maxValue);
		npFrom.setValue(initFrom);
		npTo.setMinValue(minValue);
		npTo.setMaxValue(maxValue);
		npTo.setValue(initTo);
		
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnNpDialogCancel) {
			((InterfaceFragmentDialogCallback) activity).fgdCallback("", "", this.reqCode);
			dismiss();
			return;
		}
		
		int from = npFrom.getValue();
		int to = npTo.getValue();
		if (from > to) {
			Animation anim = AnimationUtils.loadAnimation(activity, R.anim.shake);
			ll.startAnimation(anim);
			return;
		}
		String id = String.valueOf(from)+", "+String.valueOf(to);
		String txt = String.valueOf(from)+" - "+String.valueOf(to);
		
		if (this.activity instanceof InterfaceFragmentDialogCallback) {
			((InterfaceFragmentDialogCallback) activity).fgdCallback(id, txt, this.reqCode);
		}
		dismiss();
	}
	
	public void setInitFrom(String vl) {
		int min = 0;
		try {
			min = Integer.parseInt(vl);
		} catch (Exception e) {
			min = this.minValue;
		}
		setInitFrom(min);
	}
	public void setInitFrom(int vl){
		this.initFrom = vl;
	}
	public void setInitTo(String vl) {
		int max = 0;
		try {
			max = Integer.parseInt(vl);
		} catch (Exception e) {
			max = this.maxValue;
		}
		setInitTo(max);
	}

	public void setInitTo(int vl){
		this.initTo = vl;
	}

}
