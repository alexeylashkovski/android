package by.transavto.transavto;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdapterTruckListView extends BaseAdapter {

	Context ctx;
	LayoutInflater lInflater;
	ArrayList<EntityTruck> objects;

	public AdapterTruckListView(Context context,
			ArrayList<EntityTruck> messages) {
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
		DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        final float width = metrics.widthPixels;

        LinearLayout.LayoutParams layoutParamsWidth50 = new LinearLayout.LayoutParams((int) (width/2) , ViewGroup.LayoutParams.MATCH_PARENT );

		Integer res_id;
		// используем созданные, но не используемые view
		View view = convertView;
		if (view == null) {
			view = lInflater.inflate(R.layout.truck_one_record, parent, false);
		}

		EntityTruck c = (EntityTruck) getItem(position);

		((LinearLayout)view.findViewById(R.id.llTruckFrom)).setLayoutParams(layoutParamsWidth50);
		((LinearLayout)view.findViewById(R.id.llTruckTo)).setLayoutParams(layoutParamsWidth50);
		((TextView) view.findViewById(R.id.tvTruckFrom)).setText(c.getFrom().getLocName(false));
		//((TextView) view.findViewById(R.id.tvTruckFrom)).setText(c.getId());
		//((TextView) view.findViewById(R.id.tvTruckFrom)).setTypeface(null,Typeface.BOLD);
		((TextView) view.findViewById(R.id.tvTruckTo)).setText(c.getToTxt(false));
		//((TextView) view.findViewById(R.id.tvTruckTo)).setTypeface(null,Typeface.BOLD);
		ImageView image = ((ImageView) view.findViewById(R.id.ivTruckFlagFrom));
		res_id = ctx.getResources().getIdentifier("flag_" + c.getFrom().getFlag(), "drawable", ctx.getPackageName());
		image.setImageResource(res_id);
		image.getLayoutParams().width = 40;
		image.getLayoutParams().height = 24;

		image = ((ImageView) view.findViewById(R.id.ivTruckFlagTo));
		if (!c.getToAnyDirection().equalsIgnoreCase("1")) 
			res_id = ctx.getResources().getIdentifier("flag_" + c.getTo().getFlag(), "drawable", ctx.getPackageName());
		else
			res_id = ctx.getResources().getIdentifier("flag_blank", "drawable", ctx.getPackageName());

		image.setImageResource(res_id);
		image.getLayoutParams().width = 40;
		image.getLayoutParams().height = 24;
		
		((TextView) view.findViewById(R.id.tvTruckDates)).setText(c.getDates());
		((TextView) view.findViewById(R.id.tvTruckDates)).setTypeface(null,Typeface.BOLD);
		
		((TextView) view.findViewById(R.id.tvTruckDesc)).setText(c.getFullDesc(50));
		
		((TextView) view.findViewById(R.id.tvTruckFrimName)).setText(c.getFirm().getFirmName());
		((TextView) view.findViewById(R.id.tvTruckUserName)).setText(c.getUser().getFullName(false));
		return view;
	}

}


