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

public class AdapterCargoListView extends BaseAdapter {

	Context ctx;
	LayoutInflater lInflater;
	ArrayList<EntityCargo> objects;

	public AdapterCargoListView(Context context,
			ArrayList<EntityCargo> messages) {
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
			view = lInflater.inflate(R.layout.cargo_one_record, parent, false);
		}

		EntityCargo c = (EntityCargo) getItem(position);

		((LinearLayout)view.findViewById(R.id.llCargoFrom)).setLayoutParams(layoutParamsWidth50);
		((LinearLayout)view.findViewById(R.id.llCargoTo)).setLayoutParams(layoutParamsWidth50);
		((TextView) view.findViewById(R.id.tvCargoFrom)).setText(c.getFrom().getLocName(false));
		//((TextView) view.findViewById(R.id.tvCargoFrom)).setText(c.getId());
		//((TextView) view.findViewById(R.id.tvCargoFrom)).setTypeface(null,Typeface.BOLD);
		((TextView) view.findViewById(R.id.tvCargoTo)).setText(c.getTo().getLocName(false));
		//((TextView) view.findViewById(R.id.tvCargoTo)).setTypeface(null,Typeface.BOLD);
		ImageView image = ((ImageView) view.findViewById(R.id.ivCargoFlagFrom));
		res_id = ctx.getResources().getIdentifier("flag_" + c.getFrom().getFlag(), "drawable", ctx.getPackageName());
		image.setImageResource(res_id);
		image.getLayoutParams().width = 40;
		image.getLayoutParams().height = 24;

		image = ((ImageView) view.findViewById(R.id.ivCargoFlagTo));
		res_id = ctx.getResources().getIdentifier("flag_" + c.getTo().getFlag(), "drawable", ctx.getPackageName());
		image.setImageResource(res_id);
		image.getLayoutParams().width = 40;
		image.getLayoutParams().height = 24;
		
		((TextView) view.findViewById(R.id.tvCargoDates)).setText(c.getDates());
		((TextView) view.findViewById(R.id.tvCargoDates)).setTypeface(null,Typeface.BOLD);
		
		((TextView) view.findViewById(R.id.tvCargoDesc)).setText(c.getFullDesc(50));
		
		((TextView) view.findViewById(R.id.tvCargoFrimName)).setText(c.getFirm().getFirmName());
		((TextView) view.findViewById(R.id.tvCargoUserName)).setText(c.getUser().getFullName(false));
		return view;
	}

}

