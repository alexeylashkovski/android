package by.transavto.transavto;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterChatListView extends BaseAdapter {

	Context ctx;
	LayoutInflater lInflater;
	ArrayList<EntityChatMessage> objects;

	public AdapterChatListView(Context context,
			ArrayList<EntityChatMessage> messages) {
		ctx = context;
		objects = messages;
		lInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return;
	}

	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public Object getItem(int position) {
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Integer res_id;
		// используем созданные, но не используемые view
		View view = convertView;
		if (view == null) {
			view = lInflater.inflate(R.layout.chat_list_item, parent, false);
		}

		EntityChatMessage m = (EntityChatMessage) getItem(position);

		((TextView) view.findViewById(R.id.tvLocName)).setText(m.firmName);
		((TextView) view.findViewById(R.id.tvMesDate)).setText("[" + m.mesDate
				+ "]");
		((TextView) view.findViewById(R.id.tvMessage)).setText(m.message);
		ImageView image = ((ImageView) view.findViewById(R.id.ivCountryFlag));
		image.setImageResource(m.firmCountry);
		image.getLayoutParams().height = 25;

		return view;
	}

}
