package by.transavto.transavto;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DialogDbSpr extends DialogFragment implements OnItemClickListener {

	List<String> ids = new ArrayList<String>();
	List<String> datas = new ArrayList<String>();
	private Context context;
	private ListView lvMain;
	private int reqCode;
	private String table = "";
	private String title = "Сделайте выбор";

	public DialogDbSpr() {
		// Empty constructor required for DialogFragment
	}

	public DialogDbSpr(String title, Context activity, int reqCode,
			List<String> ids, List<String> datas) {
		this("", title, activity, reqCode);
		this.ids = ids;
		this.datas = datas;
	}

	public DialogDbSpr(String table, String title, Context activity,
			int reqCode) {
		this.context = activity;
		this.reqCode = reqCode;
		this.table = table;
		this.title = title;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.single_choice_dialog, container);
		getDialog().setTitle(this.title);
		lvMain = (ListView) view.findViewById(R.id.lvDialog);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (!this.table.equalsIgnoreCase("")) {
			ClassDB dbh = new ClassDB(this.context);
			dbh.open();
			Cursor c = dbh.getAllData(this.table);

			if (c.moveToFirst()) {
				// определяем номера столбцов по имени в выборке
				int idIndex = c.getColumnIndex("id");
				int dataIndex = c.getColumnIndex("data");
				do {
					String id = String.valueOf(c.getInt(idIndex));
					String dt = c.getString(dataIndex);
					ids.add(id);
					datas.add(dt);
				} while (c.moveToNext());
			}
			c.close();
			dbh.close();
		}

		// устанавливаем режим выбора пунктов списка
		lvMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.context,
				android.R.layout.simple_list_item_single_choice, datas);
		lvMain.setAdapter(adapter);
		lvMain.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		dismiss();
		String selectedData = datas.get(position);
		String selectedId = ids.get(position);

		if (this.context instanceof InterfaceFragmentDialogCallback) {
			((InterfaceFragmentDialogCallback) this.context).fgdCallback(
					String.valueOf(selectedId), selectedData, this.reqCode);
		}
		// Toast.makeText(getActivity(), datas.get(position),
		// Toast.LENGTH_SHORT).show();

	}
}
