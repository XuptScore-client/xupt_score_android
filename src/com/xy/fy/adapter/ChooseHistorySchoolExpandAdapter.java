package com.xy.fy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.xy.fy.view.HistoryCollege;

/**
 * 该类继承BaseExpandableListAdapter,通过复写该父类的方法实现数据在树形组件中显示出来
 * 
 * @author Tracy
 * 
 */
public class ChooseHistorySchoolExpandAdapter extends BaseExpandableListAdapter {

	private Context context;

	public ChooseHistorySchoolExpandAdapter(Context c) {
		context = c;
	}

	/**
	 * 设置每个条目的布局
	 * 
	 * @param string
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public TextView getGenericView(String string) {
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 64);
		TextView text = new TextView(context);
		text.setLayoutParams(layoutParams);
		text.setPadding(100, 0, 0, 0);
		text.setText(string);
		text.setTextSize(20);
		// 255,140,0
		text.setTextColor(Color.rgb(255, 140, 0));
		return text;
	}

	public Object getChild(int groupPosition, int childPosition) {
		return HistoryCollege.allHistory.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		String string = HistoryCollege.allHistory.get(groupPosition).get(childPosition).toString();
		return getGenericView(string);
	}

	public int getChildrenCount(int groupPosition) {
		return HistoryCollege.allHistory.get(groupPosition).size();
	}

	public Object getGroup(int groupPosition) {
		return "历史";
	}

	public int getGroupCount() {
		return 1;
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		String string = "历史";
		return getGenericView(string);
	}

	public boolean hasStableIds() {
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
