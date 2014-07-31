package com.victor.homelaunchvic.menu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.CheckBox;

/**
 * 消除点击事件的CheckBox
 * 
 * @author xry
 * 
 */
public class UnableCheckBox extends CheckBox {

	public UnableCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}
}
