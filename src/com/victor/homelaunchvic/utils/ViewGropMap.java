package com.victor.homelaunchvic.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.view.View;
import android.view.ViewGroup;

public class ViewGropMap {
	private Map<Integer,View> ma=new HashMap<Integer,View>();
	protected void  mapViewGrop(View row){
		if(View.class.isInstance(row)&&(!ViewGroup.class.isInstance(row))){
			ma.put(row.getId(), row);
			return;
		}else{
			ma.put(row.getId(), row);
		}
		LinkedList<ViewGroup> li=new LinkedList<ViewGroup>();
		for(int i=0;i<((ViewGroup) row).getChildCount();i++){
			View rows=((ViewGroup) row).getChildAt(i) ;
			if(ViewGroup.class.isInstance(rows)){
				li.add((ViewGroup)rows);
				ma.put(rows.getId(), rows);
			}else if(View.class.isInstance(rows)){
				ma.put(rows.getId(), rows);
			}
		}
		ViewGroup view_group=null;
		while(!li.isEmpty()){
			view_group = li.removeFirst();
			for(int i=0;i<view_group.getChildCount();i++){
				View rows=view_group.getChildAt(i) ;
				if(ViewGroup.class.isInstance(rows)){
					li.add((ViewGroup)rows);
					ma.put(rows.getId(), rows);
				}else if(View.class.isInstance(rows)){
					ma.put(rows.getId(), rows);
				}
			}
		}
	}
	public View getView(int view_id){
		return ma.get(view_id);
	}
}
