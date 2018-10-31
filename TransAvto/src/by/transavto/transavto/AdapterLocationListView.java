package by.transavto.transavto;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterLocationListView extends BaseAdapter {

	Context ctx;
	LayoutInflater lInflater;
	ArrayList<EntityLocation> objects;

	public AdapterLocationListView(Context context,
			ArrayList<EntityLocation> messages) {
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
		// используем созданные, но не используемые view
		View view = convertView;
		if (view == null) {
			view = lInflater
					.inflate(R.layout.location_list_item, parent, false);
		}

		EntityLocation l = (EntityLocation) getItem(position);

		((TextView) view.findViewById(R.id.tvLocName)).setText(l
				.getLocName(false));
		((TextView) view.findViewById(R.id.tvDistance)).setText(String
				.valueOf(l.getDistance()));
		ImageView image = ((ImageView) view.findViewById(R.id.ivCountryFlag));
		int res_id = ctx.getResources().getIdentifier("flag_" + l.getFlag(), "drawable", ctx.getPackageName());
		image.setImageResource(res_id);
		image.getLayoutParams().width = 40;
		image.getLayoutParams().height = 24;

		return view;
	}

}
