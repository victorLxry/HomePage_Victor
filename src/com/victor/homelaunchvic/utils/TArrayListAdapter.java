package com.victor.homelaunchvic.utils;

import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class TArrayListAdapter<T> extends ArrayListAdapter<T> {
	private Context context;
	private boolean isItemBackGroud = true;

	public TArrayListAdapter(Context context) {
		super(context);
		this.context = context;
	}

	private IOnDrawViewEx<T> drawViewEx;

	public void setDrawViewEx(IOnDrawViewEx<T> drawView) {
		this.drawViewEx = drawView;
	}

	int layout = 0;

	public TArrayListAdapter<T> setLayout(int layout) {
		this.layout = layout;
		return this;
	}

	public void setIsItemBackGround(boolean is) {
		isItemBackGroud = is;
	}

	private HashMap<View, View> viewGrop_map;
	private HashMap<View, ViewGropMap> view_maps = null;
	private View row = null;
	private ViewGropMap map = null;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (viewGrop_map == null || map == null) {
			viewGrop_map = new HashMap<View, View>();
			view_maps = new HashMap<View, ViewGropMap>();
		}
		if (convertView == null) {
			convertView = new LinearLayout(context);
		}
		if (!viewGrop_map.containsKey(convertView)) {

			row = mInflater.inflate(layout, parent, false);
			map = new ViewGropMap();
			map.mapViewGrop(row);

			view_maps.put(convertView, map);
			viewGrop_map.put(convertView, row);
			((ViewGroup) convertView).removeAllViews();
			((ViewGroup) convertView).addView(viewGrop_map.get(convertView),LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		} else {
			((ViewGroup) convertView).removeAllViews();
			map = view_maps.get(convertView);
			((ViewGroup) convertView).addView(viewGrop_map.get(convertView),LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		}

		// if (position % 2 == 0) {
		// convertView.setBackgroundResource(R.drawable.list_bluecolor_selector);
		// } else {
//		if (isItemBackGroud) {
//			row.setBackgroundResource(R.drawable.list_all_pressor);
//			row.getBackground().setAlpha(170);
//		}
		if (drawViewEx != null) {
			drawViewEx.OnDrawViewEx(context, getItem(position), map, position);
		}
		return convertView;
	}

	public interface IOnDrawViewEx<T> {
		public void OnDrawViewEx(Context aContext, T templateItem,
				ViewGropMap view, int aIndex);
	}
}
