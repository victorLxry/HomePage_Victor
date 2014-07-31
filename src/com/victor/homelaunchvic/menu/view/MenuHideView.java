package com.victor.homelaunchvic.menu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.victor.homelaunchvic.R;
import com.victor.homelaunchvic.utils.TArrayListAdapter;

public class MenuHideView extends RelativeLayout {
	private LinearLayout llbar;
	private GridView gridview;
	private View v;

	public MenuHideView(Context context) {
		this(context, null);
	}
	
	public MenuHideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		v = LayoutInflater.from(context).inflate(R.layout.view_menu_hideview,
				null);
		addView(v,android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.FILL_PARENT);
		llbar = (LinearLayout) v.findViewById(R.id.ll_hideview_bar);
		gridview = (GridView) v.findViewById(R.id.gv);
		llbar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (gridview.isShown()) {
					gridview.setVisibility(View.GONE);
					v.findViewById(R.id.iv_hideview).setBackgroundResource(
							R.drawable.ic_hide_right);
				} else {
					gridview.setVisibility(View.VISIBLE);
					v.findViewById(R.id.iv_hideview).setBackgroundResource(
							R.drawable.ic_hide_down);
				}
			}
		});
	}

	public void setHide(){
		gridview.setVisibility(View.GONE);
		v.findViewById(R.id.iv_hideview).setBackgroundResource(
				R.drawable.ic_hide_right);
	}
	
	public <T> void setAdapter(String title,TArrayListAdapter<T> adapter) {
		((TextView)v.findViewById(R.id.tv_hideview)).setText(title);
		gridview.setAdapter(adapter);
	}
}
