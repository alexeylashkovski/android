package by.transavto.transavto;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterFirmListView extends BaseAdapter {

	Context ctx;
	LayoutInflater lInflater;
	ArrayList<EntityFirm> objects;

	public AdapterFirmListView(Context context,
			ArrayList<EntityFirm> messages) {
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
			view = lInflater.inflate(R.layout.firm_one_record, parent, false);
		}

		EntityFirm f = (EntityFirm) getItem(position);
		
		res_id = ctx.getResources().getIdentifier("flag_"+f.getFirmLoc().getFlag(), "drawable", ctx.getPackageName());
		((ImageView)view.findViewById(R.id.ivFirmFlag)).setImageResource(res_id);
		((ImageView)view.findViewById(R.id.ivFirmFlag)).getLayoutParams().width = 40;
		((ImageView)view.findViewById(R.id.ivFirmFlag)).getLayoutParams().height = 24;
		((TextView)view.findViewById(R.id.tvFirmName)).setText(f.getFirmName());

		res_id = ctx.getResources().getIdentifier(f.getFirmRatingResourceName(), "drawable", ctx.getPackageName());
		((ImageView)view.findViewById(R.id.ivFirmRating)).setImageResource(res_id);

		((TextView)view.findViewById(R.id.tvFirmAddress)).setText(f.getFirmLoc().getLocName(true));
		((TextView)view.findViewById(R.id.tvFirmUnp)).setText(f.getFirmUnp());
		((TextView)view.findViewById(R.id.tvFirmContactName)).setText(f.getContactPerson().getFullName(true));
		
		
		return view;
	}

}



