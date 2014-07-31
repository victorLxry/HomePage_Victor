package com.victor.homelaunchvic.menu.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.victor.homelaunchvic.R;
import com.victor.homelaunchvic.menu.view.LunchFlipper.OnMenuLongPressListener;

/**
 * 选人总布局页面(此页面会显示长按显示的图)
 * 
 * @author xry
 * 
 */
public class MenuLayout extends RelativeLayout implements
		OnMenuLongPressListener {
	private boolean isShowPic;
	/**
	 * 长按后的移动节点
	 */
	private DrawingCacheView pic;
	private int picWidth = 100;
	private int picHeight = 100;

	public MenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		pic = new DrawingCacheView(context);
		pic.setFlag(false);
		picWidth = (int) getResources().getDimension(R.dimen.nodeview_height);

	}

	public MenuLayout(Context context) {
		this(context, null);
	}

	private int widthMeasureSpec;
	private int heightMeasureSpec;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		this.widthMeasureSpec = widthMeasureSpec;
		this.heightMeasureSpec = heightMeasureSpec;
	}

	public void setIsShowPic(boolean is) {
		this.isShowPic = is;
	}

	private boolean flag;

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		BaseLunchFlipper nsh = null;
		if (!flag) {
			pic.measure(widthMeasureSpec, heightMeasureSpec);
			int count = getChildCount();
			for (int i = 0; i < count; i++) {
				if (SaBaseMenuLayout.class.isInstance(getChildAt(i))) {
					nsh = (BaseLunchFlipper) ((SaBaseMenuLayout) getChildAt(i))
							.getLunchFlipper();
					picWidth = nsh.getViewWidth();
					picHeight = nsh.getViewHeight();
				}
			}
			if (nsh != null) {
				nsh.setOnMenuLongPressListener(this);

			}
			if (picWidth != 0)
				flag = true;
		}
		invalidate();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		pic.layout(x - picWidth / 2, y - picHeight / 2 - 15, x + picWidth / 2,
				y + picWidth / 2 - 15);// 让显示位于手指靠上，这样比较明显
		invalidate();
		if (isShowPic) {
			if (y < getResources().getDimension(R.dimen.menu_hide_height)) {
				if (mOnMenuHideListener != null) {
					mOnMenuHideListener.onHide(ev.getAction());
				}
			} else {
				if (mOnMenuHideListener != null) {
					mOnMenuHideListener.onHide(-100);
				}
			}
		}
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			isShowPic = false;
		}

		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isShowPic) {
			return false;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (isShowPic) {
			drawChild(canvas, pic, getDrawingTime());
		}
	}

	@Override
	public void onLongPress(LunchItem org) {
		if (org != null) {
			pic.setBitmap(getItemDrawingCache(org));
		}
		setIsShowPic(true);
	}

	private Bitmap getItemDrawingCache(View view) {
		view.setDrawingCacheEnabled(true);
		return view.getDrawingCache();
	}

	private OnMenuHideListener mOnMenuHideListener;

	public void setOnMenuHideListener(OnMenuHideListener l) {
		mOnMenuHideListener = l;
	}

	public interface OnMenuHideListener {
		public void onHide(int action);
	}
}
