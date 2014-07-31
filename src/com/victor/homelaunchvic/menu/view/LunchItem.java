package com.victor.homelaunchvic.menu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

public class LunchItem extends RelativeLayout {
	private LongPressClickListener longPressListener;
	private PressClickListener pressListener;
	private MoveListener moveListener;
	private Entity entity;

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public LunchItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LunchItem(Context context) {
		this(context, null);
	}

	private int width;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
	}

	private int downX;
	private LongPress longpress;
	private Press press;
	boolean flag = false;
	private long moveTime;// 用于计算长按时间与手指接触该按钮的时间间隔，避免改按钮down事件以后，点击事件被转移到别的按钮上，而误触发长按事件

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i("menu", "LunchItemOtuch" + event.getAction());
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getRawX();
			flag = false;
			if (press == null)
				press = new Press();
			press.setPress(C_iStop);
			post(press);
			if (longpress == null)
				longpress = new LongPress();
			postDelayed(longpress, ViewConfiguration.getLongPressTimeout());
			break;
		case MotionEvent.ACTION_MOVE:
			moveTime = System.currentTimeMillis();
			int moveX = (int) event.getRawX();
			// Log.i("tag", "downX" + downX + "moveX" + moveX + "%%"
			// + event.getRawX());
			if (moveX == downX) {
				flag = false;
				press.setPress(C_iStop);
				post(press);
			} else if (Math.abs(moveX - downX) < width / 6) {
				flag = false;
				press.setPress(C_iStop);
				post(press);
			} else {
				if (longpress != null)
					removeCallbacks(longpress);
				press.setPress(C_iMove);
				post(press);
				flag = true;
			}
			// Log.i("tag", "falg=" + flag);
			break;
		case MotionEvent.ACTION_UP:
			if (longpress != null) {
				Log.i("menu", "LunchItemOtuch" + "removeCallbacks");
				removeCallbacks(longpress);
			}
			if (pressListener != null && !flag) {
				// Log.i("tag", "press");
				pressListener.click(this);
			}
			break;

		default:
			break;
		}
		invalidate();
		return true;
	}

	public void setOnLongPressClickListener(LongPressClickListener l) {
		longPressListener = l;
	}

	public void setOnPressClickListener(PressClickListener l) {
		pressListener = l;
	}

	public void setOnMoveListener(MoveListener l) {
		moveListener = l;
	}

	class LongPress implements Runnable {
		@Override
		public void run() {
			if (longPressListener != null) {
				longPressListener.longClick(LunchItem.this);
			}
			flag = false;
			// Log.i("tag", "longpress");
		}

	}

	private static final int C_iStop = 0;
	private static final int C_iMove = 1;

	class Press implements Runnable {
		private int type;

		public void setPress(int type) {
			this.type = type;
		}

		@Override
		public void run() {
			if (moveListener != null) {
				if (type == C_iStop)
					moveListener.stop();
				else if (type == C_iMove)
					moveListener.move();

			}
		}

	}

}
