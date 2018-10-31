package by.transavto.transavto;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import by.transavto.transavto.R.layout;

@SuppressWarnings("unchecked")
public class AdapterMyTrucks extends BaseExpandableListAdapter {

	public ArrayList<String> groupItem;
	public ArrayList<ArrayList<EntityTruck>> Childtem = new ArrayList<ArrayList<EntityTruck>>();
	public LayoutInflater minflater;
	public Activity activity;

	public AdapterMyTrucks(ArrayList<String> grList, ArrayList<ArrayList<EntityTruck>> childItem) {
		groupItem = grList;
		this.Childtem = childItem;
	}

	public void setInflater(LayoutInflater mInflater, Activity act) {
		this.minflater = mInflater;
		activity = act;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View view, ViewGroup parent) {
		Integer res_id;
		
		if (view == null) {
			view = minflater.inflate(layout.truck_one_record, null);
		}

        ArrayList<EntityTruck> tempChild = Childtem.get(groupPosition);
		EntityTruck c = (EntityTruck) tempChild.get(childPosition);

		DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        final float width = metrics.widthPixels;

        LinearLayout.LayoutParams layoutParamsWidth50 = new LinearLayout.LayoutParams((int) (width/2) , ViewGroup.LayoutParams.MATCH_PARENT );

		((LinearLayout)view.findViewById(R.id.llTruckFrom)).setLayoutParams(layoutParamsWidth50);
		((LinearLayout)view.findViewById(R.id.llTruckTo)).setLayoutParams(layoutParamsWidth50);
		((TextView) view.findViewById(R.id.tvTruckFrom)).setText(c.getFrom().getLocName(false));
		//((TextView) view.findViewById(R.id.tvTruckFrom)).setText(c.getId());
		//((TextView) view.findViewById(R.id.tvTruckFrom)).setTypeface(null,Typeface.BOLD);
		((TextView) view.findViewById(R.id.tvTruckTo)).setText(c.getToTxt(false));
		//((TextView) view.findViewById(R.id.tvTruckTo)).setTypeface(null,Typeface.BOLD);
		ImageView image = ((ImageView) view.findViewById(R.id.ivTruckFlagFrom));
		res_id = activity.getResources().getIdentifier("flag_" + c.getFrom().getFlag(), "drawable", activity.getPackageName());
		image.setImageResource(res_id);
		image.getLayoutParams().width = 40;
		image.getLayoutParams().height = 24;

		image = ((ImageView) view.findViewById(R.id.ivTruckFlagTo));
		if (!c.getToAnyDirection().equalsIgnoreCase("1")) 
			res_id = activity.getResources().getIdentifier("flag_" + c.getTo().getFlag(), "drawable", activity.getPackageName());
		else
			res_id = activity.getResources().getIdentifier("flag_blank", "drawable", activity.getPackageName());
		Log.d("TLV","TAD: "+c.getToAnyDirection()+" flag: "+c.getTo().getFlag() + " res_id: "+String.valueOf(res_id));
		
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

	@Override
	public int getChildrenCount(int groupPosition) {
		return ((ArrayList<EntityTruck>) Childtem.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return groupItem.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.expandable_list_group, null);
		}
		((TextView)convertView.findViewById(R.id.tvGroupName)).setText(groupItem.get(groupPosition));
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}