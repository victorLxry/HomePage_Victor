package com.victor.homelaunchvic.menu.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.victor.homelaunchvic.R;
import com.victor.homelaunchvic.utils.ArrayListAdapter;
import com.victor.homelaunchvic.utils.TArrayListAdapter;
import com.victor.homelaunchvic.utils.TArrayListAdapter.IOnDrawViewEx;
import com.victor.homelaunchvic.utils.ViewGropMap;

@SuppressWarnings("rawtypes")
public class MenuExpandableListAdapter extends BaseExpandableListAdapter
		implements ArrayListAdapter.SeeyonNotifyDataChange {
	public TArrayListAdapter<String> mGroupData;
	private List<ArrayListAdapter> mChildData;

	protected Activity mContext;

	public MenuExpandableListAdapter(Activity context) {
		super();
		this.mContext = context;
		mGroupData = new TArrayListAdapter<String>(context);
		mGroupData.setLayout(R.layout.view_menulist_header);
		mGroupData.setDrawViewEx(new IOnDrawViewEx<String>() {

			@Override
			public void OnDrawViewEx(Context aContext, String templateItem,
					ViewGropMap view, int aIndex) {
				// TODO Auto-generated method stub
				TextView tv = (TextView) view.getView(R.id.list_header_title);
				tv.setText(templateItem);
				tv.setText(tv.getText());
			}
		});
		mChildData = new ArrayList<ArrayListAdapter>();
	}

	public void addSection(String section, ArrayListAdapter adapter) {
		if (mGroupData.getCount() > 0
				&& mGroupData.getItem(mGroupData.getCount() - 1).indexOf(
						"回复") == 0) {// 避免加载到回复下面
			mGroupData.insert(section, mGroupData.getCount() - 1);
			if (adapter != null)
				adapter.setSeeyonNotifyDataChange(this);
			this.mChildData.add(mChildData.size() - 1, adapter);
			return;
		}
		this.mGroupData.add(section);
		if (adapter != null)
			adapter.setSeeyonNotifyDataChange(this);
		this.mChildData.add(adapter);
	}

	public void setSection(String section, ArrayListAdapter adapter) {
		int index = mGroupData.getPosition(section);
		if (adapter != null)
			adapter.setSeeyonNotifyDataChange(this);
		if (index < 0) {
			return;
		}
		mChildData.remove(index);
		mChildData.add(index, adapter);
	}

	public void cleanAll() {
		this.mGroupData.clear();
		this.mChildData.clear();
	}

	public Object getChild(int groupPosition, int childPosition) {
		return mChildData.get(groupPosition).getItem(childPosition);
		// return mChildData.get(groupPosition).get(childPosition);
	}

	public Object getChild(int groupPosition) {
		return mChildData.get(groupPosition);
		// return mChildData.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		return mChildData.get(groupPosition).getView(childPosition,
				convertView, parent);
	}

	public int getChildrenCount(int groupPosition) {
		if (mChildData == null || mChildData.get(groupPosition) == null) {
			return 0;
		}
		return mChildData.get(groupPosition).getCount();
		// return mChildData.get(groupPosition).size();
	}

	public Object getGroup(int groupPosition) {
		return mGroupData.getItem(groupPosition);// get(groupPosition);
	}

	public int getGroupCount() {
		if (mGroupData == null)
			return 0;
		return mGroupData.getCount();// size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView != null) {
			ImageView tab = (ImageView) convertView.findViewById(R.id.iv_top);
			if (!isExpanded) {
				tab.setBackgroundResource(R.drawable.ic_progressive_b);
			} else {
				tab.setBackgroundResource(R.drawable.ic_progressive_t);
			}
		}
		return mGroupData.getView(groupPosition, convertView, parent);
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}

	public void insertDataChild() {
		notifyDataSetChanged();
	}

	@Override
	public void NotifyDataChange() {
		for (int i = 0; i < mGroupData.getCount(); i++) {
			if (mChildData.get(i) == null || mChildData.get(i).getCount() == 0) {
				mGroupData.remove(mGroupData.getItem(i));
				mChildData.remove(i);
			}
		}
		notifyDataSetChanged();
		// mGroupData.clear();
	}

}
