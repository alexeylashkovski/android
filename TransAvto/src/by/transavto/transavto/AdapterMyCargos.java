package by.transavto.transavto;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
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
public class AdapterMyCargos extends BaseExpandableListAdapter {

	public ArrayList<String> groupItem;
	public ArrayList<ArrayList<EntityCargo>> Childtem = new ArrayList<ArrayList<EntityCargo>>();
	public LayoutInflater minflater;
	public Activity activity;

	public AdapterMyCargos(ArrayList<String> grList, ArrayList<ArrayList<EntityCargo>> childItem) {
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
			view = minflater.inflate(layout.cargo_one_record, null);
		}
		
		DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        final float width = metrics.widthPixels;

        LinearLayout.LayoutParams layoutParamsWidth50 = new LinearLayout.LayoutParams((int) (width/2) , ViewGroup.LayoutParams.MATCH_PARENT );

        
        ArrayList<EntityCargo> tempChild = Childtem.get(groupPosition);
		EntityCargo c = (EntityCargo) tempChild.get(childPosition);

		((LinearLayout)view.findViewById(R.id.llCargoFrom)).setLayoutParams(layoutParamsWidth50);
		((LinearLayout)view.findViewById(R.id.llCargoTo)).setLayoutParams(layoutParamsWidth50);
		((TextView) view.findViewById(R.id.tvCargoFrom)).setText(c.getFrom().getLocName(false));
		((TextView) view.findViewById(R.id.tvCargoTo)).setText(c.getTo().getLocName(false));
		//((TextView) view.findViewById(R.id.tvCargoTo)).setTypeface(null,Typeface.BOLD);
		ImageView image = ((ImageView) view.findViewById(R.id.ivCargoFlagFrom));
		res_id = activity.getResources().getIdentifier("flag_" + c.getFrom().getFlag(), "drawable", activity.getPackageName());
		image.setImageResource(res_id);
		image.getLayoutParams().width = 40;
		image.getLayoutParams().height = 24;

		image = ((ImageView) view.findViewById(R.id.ivCargoFlagTo));
		res_id = activity.getResources().getIdentifier("flag_" + c.getTo().getFlag(), "drawable", activity.getPackageName());
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

	@Override
	public int getChildrenCount(int groupPosition) {
		return ((ArrayList<EntityCargo>) Childtem.get(groupPosition)).size();
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