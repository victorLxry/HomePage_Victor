package com.victor.homelaunchvic.menu.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.victor.homelaunchvic.R;

public class SaBaseMenuLayout extends FrameLayout {
	private BaseLunchFlipper lunch;
	private View view;

	public SaBaseMenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		view = LayoutInflater.from(context).inflate(R.layout.view_base_menu,
				null);
		addView(view);
		lunch = (BaseLunchFlipper) view.findViewById(R.id.gridview);
		lunch.setParentLayout(this);
//		BaseActivity activity = (BaseActivity) getContext();
//		final MOrgMember me = ((M1ApplicationContext) activity.getApplication())
//				.getCurrMember();
//		if (me == null)
//			return;
//		AsyncImageView ivPhoto = (AsyncImageView) view
//				.findViewById(R.id.iv_menu_photo);
//		ivPhoto.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//			}
//		});
//		me.getIcon().setSize(300l);
//		ivPhoto.setHandlerInfo(me.getOrgID() + "x", me.getIcon());
	}

	private int viewHeight = 1000;
	private int heightt;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		((BaseLunchFlipper) findViewById(R.id.gridview))
				.setViewHeight(MeasureSpec.getSize(heightMeasureSpec) / 10 * 8);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		heightt = height;
//		LogM.i("SaBaseMenuLayout=" + height);
		if (viewHeight > height) {
			// findViewById(R.id.rl_menu1).setLayoutParams(
			// new android.widget.LinearLayout.LayoutParams(
			// android.widget.LinearLayout.LayoutParams.FILL_PARENT,
			// height / 10 * 3));
			// android.widget.LinearLayout.LayoutParams ll = new
			// android.widget.LinearLayout.LayoutParams(
			// height / 11 * 3, height / 11 * 3);
			// ll.gravity = Gravity.CENTER;
			// findViewById(R.id.iv_menu_photo).setLayoutParams(ll);
			// findViewById(R.id.gridview).setLayoutParams(
			// new android.widget.LinearLayout.LayoutParams(
			// android.widget.LinearLayout.LayoutParams.FILL_PARENT,
			// height / 10 * 7));
			// findViewById(R.id.gridview).requestLayout();
			// handler.post(run);
			viewHeight = height;
		}

	}

	private Handler handler = new Handler();
	private Runnable run = new Runnable() {

		@Override
		public void run() {
			if(findViewById(R.id.gridview).getHeight()==heightt / 10 * 8)return;
			findViewById(R.id.rl_menu1)
					.setLayoutParams(
							new android.widget.LinearLayout.LayoutParams(
									android.widget.LinearLayout.LayoutParams.FILL_PARENT,
									heightt / 10 * 2));
			android.widget.LinearLayout.LayoutParams ll = new android.widget.LinearLayout.LayoutParams(
					heightt / 11 * 2, heightt / 11 * 2);
			ll.gravity = Gravity.CENTER;
			findViewById(R.id.iv_menu_photo).setLayoutParams(ll);
			findViewById(R.id.gridview)
					.setLayoutParams(
							new android.widget.LinearLayout.LayoutParams(
									android.widget.LinearLayout.LayoutParams.FILL_PARENT,
									heightt / 10 * 8));
			//
//			findViewById(R.id.gridview).requestLayout();
		}
	};
	private int layoutHeight;

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		layoutHeight = bottom - top;
		// findViewById(R.id.rl_menu1).setLayoutParams(
		// new android.widget.LinearLayout.LayoutParams(
		// android.widget.LinearLayout.LayoutParams.FILL_PARENT,
		// heightt / 10 * 3));
		// android.widget.LinearLayout.LayoutParams ll = new
		// android.widget.LinearLayout.LayoutParams(
		// heightt / 11 * 3, heightt / 11 * 3);
		// ll.gravity = Gravity.CENTER;
		// findViewById(R.id.iv_menu_photo).setLayoutParams(ll);
		// findViewById(R.id.gridview).setLayoutParams(
		// new android.widget.LinearLayout.LayoutParams(
		// android.widget.LinearLayout.LayoutParams.FILL_PARENT,
		// heightt / 10 * 7));
		// //
		// findViewById(R.id.gridview).requestLayout();
		handler.post(run);
	}

	private boolean flag;

	public SaBaseMenuLayout(Context context) {
		this(context, null);
	}

	public BaseLunchFlipper getLunchFlipper() {
		return lunch;
	}

}
