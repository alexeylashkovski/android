package by.transavto.transavto;

import net.simonvt.numberpicker.NumberPicker;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class DialogNumberPicker extends DialogFragment implements OnClickListener{

	private Activity activity;
	protected NumberPicker npFrom;
	private int reqCode;
	private int initFrom = 0;
	protected int minValue = 0;
	protected int maxValue = 100;
	protected String title="";
	

	public DialogNumberPicker() {
		// Empty constructor required for DialogFragment
	}

	public DialogNumberPicker(Activity activity, int reqCode, String title, int minValue, int maxValue) {
		this.activity = activity;
		this.reqCode = reqCode;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.title = title;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.dialog_number_picker, container);
		getDialog().setTitle(this.title);
		npFrom = (NumberPicker) view.findViewById(R.id.npNumberPicker);
		((Button)view.findViewById(R.id.btnNumberPickerOk)).setOnClickListener(this);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		this.setStyle(DialogFragment.STYLE_NORMAL, R.style.SampleTheme_Light);
		npFrom.setMinValue(minValue);
		npFrom.setMaxValue(maxValue);
		npFrom.setValue(initFrom);	
	}

	@Override
	public void onClick(View v) {
		int from = npFrom.getValue();

		if (this.activity instanceof InterfaceFragmentDialogCallback) {
			((InterfaceFragmentDialogCallback) activity).fgdCallback(String.valueOf(from), String.valueOf(from), this.reqCode);
		}
		dismiss();
	}
	
	public void setInit(String vl) {
		int min = 0;
		try {
			min = Integer.parseInt(vl);
		} catch (Exception e) {
			min = this.minValue;
		}
		setInit(min);
	}
	public void setInit(int vl){
		this.initFrom = vl;
	}
}
