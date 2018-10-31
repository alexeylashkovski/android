package by.transavto.transavto;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class DialogTextInput extends DialogFragment implements OnClickListener{

	private Activity activity;
	private int reqCode;

	private String title="";
	private String text="";
	private EditText textField;

	public DialogTextInput() {
		// Empty constructor required for DialogFragment
	}

	public DialogTextInput(Activity activity, int reqCode, String title, String text) {
		this.activity = activity;
		this.reqCode = reqCode;
		this.title = title;
		this.text = text;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.text_input_dialog, container);
		getDialog().setTitle(this.title);
		
		textField = (EditText)view.findViewById(R.id.etInputTextDialodText);

		textField.setText(this.text);
		((Button)view.findViewById(R.id.btnInputTextDialogSubmit)).setOnClickListener(this);				
		return view;
	}
	
	@Override
	public void onClick(View v) {
	
		if (v.getId() == R.id.btnInputTextDialogSubmit) {
			if (activity instanceof InterfaceFragmentDialogCallback) {
				((InterfaceFragmentDialogCallback)activity).fgdCallback(textField.getText().toString(), textField.getText().toString(), this.reqCode);
			}
			dismiss();
		}
	}
	
}
