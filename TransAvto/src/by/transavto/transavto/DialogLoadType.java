package by.transavto.transavto;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class DialogLoadType extends DialogFragment implements OnClickListener {

	List<String> ids = new ArrayList<String>();
	List<String> datas = new ArrayList<String>();
	private Activity activity;
	private ListView lvMain;
	private int reqCode;
	private String preSelect;
	
	public DialogLoadType() {
		// Empty constructor required for DialogFragment
	}

	public DialogLoadType(Activity activity, int reqCode, String preSelect) {
		this.activity = activity;
		this.reqCode = reqCode;
		this.preSelect = preSelect;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.multi_choice_dialog, container);
		getDialog().setTitle("Тип загрузки");
		lvMain = (ListView) view.findViewById(R.id.lvDialogMulti);
		((Button) view.findViewById(R.id.btnMultiDialogOk))
				.setOnClickListener(this);
		;
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		datas.add("Верхняя");
		datas.add("Боковая");
		datas.add("Задняя");
		datas.add("С полной растентовкой");
		datas.add("Со снятием стоек/поперечин");
		ids.add("up");
		ids.add("side");
		ids.add("back");
		ids.add("tent");
		ids.add("stoika");

		// устанавливаем режим выбора пунктов списка
		lvMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
				android.R.layout.simple_list_item_multiple_choice, datas);
		lvMain.setAdapter(adapter);

		String[] exploded=preSelect.split(",");
		for(int i=0;i<exploded.length;i++) {
			int index = ids.indexOf(exploded[i]);
			lvMain.setItemChecked(index, true);
		}
	}

	@Override
	public void onClick(View v) {
		String id = "";
		String txt = "";
		SparseBooleanArray sbArray = lvMain.getCheckedItemPositions();
		for (int i = 0; i < sbArray.size(); i++) {
			int key = sbArray.keyAt(i);
			if (sbArray.get(key) && key>=0) {
	//			//Log.d("DIALOG","id "+id+" "+String.valueOf(key)+" "+String.valueOf(sbArray.get(key)));
				id = id + ids.get(key) + ",";
				txt = txt + datas.get(key) + ',';
			}
		}
		if (id.length() > 1)
			id = id.substring(0, id.length() - 1);
		if (txt.length() > 1)
			txt = txt.substring(0, txt.length() - 1);
		
		if (txt.length()>20)
			txt = txt.substring(0, 20) + "...";
		//Log.d("DIALOG", "id: " + id);
		//Log.d("DIALOG", "txt: " + txt);
		if (activity instanceof InterfaceFragmentDialogCallback) {
			((InterfaceFragmentDialogCallback) activity).fgdCallback(id, txt,
					this.reqCode);
		}
		dismiss();
	}
}
