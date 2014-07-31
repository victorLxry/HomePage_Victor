package com.victor.homelaunchvic.menu.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.victor.homelaunchvic.R;
import com.victor.homelaunchvic.menu.db.LunchDataBaseAdapter;
import com.victor.homelaunchvic.utils.JSONUtil;

public class LunchFlipper extends ViewGroup implements LongPressClickListener,
		MoveListener, OnTouchListener, PressClickListener {
	/**
	 * 每页锟斤拷锟斤拷
	 */
	private int lunchRow = 3;
	/**
	 * 每页锟斤拷锟斤拷
	 */
	private int lunchColumns = 3;
	/**
	 * 每页锟斤拷锟斤拷
	 */
	private int lunchPageCount;
	protected List<LunchItem> lunchItemList;
	private Scroller mScroller;
	private Scroller mItemScroller;
	private Scroller nItemScroller;
	private BaseAdapter adapter;
	private Context ctx;
	private int width;
	private int height;
	private LunchView pressBackGroudView;
	private RoundView roundView;
	private boolean isRoundViewVisiable = true;
	private int[] groupIDArray;
	private int C_iLimitSlipSpan = 50;
	private static int C_iLongPressLimitSpan = 30;
	/**
	 * up锟铰硷拷锟斤拷杉锟揭筹拷锟斤拷位锟斤拷
	 */
	private int visiableViewIndex = 0;
	/**
	 * down锟铰硷拷时锟缴硷拷页锟斤拷锟轿伙拷锟?
	 */
	private int visiableViewIndexWhenDown = 0;

	private int viewWidth;
	private int viewHeight;
	/**
	 * 锟角否滑碉拷锟竭界，锟斤拷锟斤拷蔷筒锟斤拷芸锟斤拷锟??锟饺伙拷??
	 */
	private boolean isCanMaxSlip = false;
	/**
	 * 锟劫度革拷锟斤拷
	 */
	private VelocityTracker mVelocityTracker;
	private int mMaximumVelocity;
	/**
	 * 锟斤拷锟斤拷锟劫斤拷锟劫度ｏ拷锟斤拷锟劫度筹拷锟斤拷锟斤拷锟绞憋拷谢锟斤拷锟斤拷锟揭??
	 */
	private static final int SNAP_VELOCITY = 150;
	/**
	 * 锟劫讹拷锟斤拷锟斤拷
	 */
	private static final int velocityRight = -1;
	/**
	 * 锟劫讹拷锟斤拷锟斤拷
	 */
	private static final int velocityLeft = 1;
	/**
	 * 没锟斤拷锟劫讹拷
	 */
	private static final int velocityNo = 0;
	/**
	 * 锟斤拷锟斤拷??
	 */
	private boolean lockSlip = false;
	private OnItemCilickListener onItemClickListener;

	private DrawingCacheView ShadowView;
	private boolean isShadowFlag = false;
	private int[] colorArrays = { R.color.menu_0, R.color.menu_1,
			R.color.menu_2, R.color.menu_3, R.color.menu_4, R.color.menu_5,
			R.color.menu_6, R.color.menu_7, R.color.menu_8 };

	/*
	 * 
	 */
	public LunchFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
		ctx = context;
		setLayoutRowsAndColumns();
		setOnTouchListener(this);
		final ViewConfiguration configuration = ViewConfiguration
				.get(getContext());
		lunchItemList = new ArrayList<LunchItem>();
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
		mScroller = new Scroller(context, new DecelerateInterpolator());
		mItemScroller = new Scroller(context, new DecelerateInterpolator());
		nItemScroller = new Scroller(context, new DecelerateInterpolator());
		initDate();
	}

	public LunchFlipper(Context context) {
		this(context, null);
	}

	private void setLayoutRowsAndColumns() {
		DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
		// lunchColumns = dm.widthPixels
		// / (int) getResources().getDimension(R.dimen.lunch_Columns);
		// lunchRow = (dm.heightPixels - (int) (getResources().getDimension(
		// R.dimen.lunch_bottom_height) * dm.density))
		// / (int) getResources().getDimension(R.dimen.lunch_rows);
		lunchPageCount = lunchRow * lunchColumns;
	}

	private void initDate() {
		groupIDArray = new int[lunchPageCount];
		for (int i = 0; i < lunchPageCount; i++) {
			groupIDArray[i] = C_iRlViewID1 + i;
		}
	}

	void initlayout() {
		lunchItemList.clear();
		hideList.clear();
		removeAllViews();
		if (adapter == null)
			return;
		for (int x = 0; x < adapter.getCount(); x++) {
			Entity en = (Entity) adapter.getItem(x);
			if (en.isHide()) {
				hideList.add(en);
			} else {
				LunchItem lun = new LunchItem(ctx);
				View chileView = adapter.getView(x, null, null);
				android.widget.RelativeLayout.LayoutParams laP = new android.widget.RelativeLayout.LayoutParams(
						width / lunchColumns, lunHeight / lunchRow);
				laP.addRule(RelativeLayout.CENTER_IN_PARENT);
				lun.addView(chileView, laP);
				chileView.findViewById(R.id.rll).setBackgroundResource(
						((Entity) adapter.getItem(x)).getColor());
				// if (x == adapter.getCount() - 1)
				// chileView.findViewById(R.id.rll).getBackground()
				// .setAlpha(50);
				lun.setEntity(en);
				lunchItemList.add(lun);
				addView(lun);
				lun.setOnLongPressClickListener(this);
				lun.setOnMoveListener(this);
				lun.setOnPressClickListener(this);
			}
		}
		saveLunchState();
	}

	private boolean onceFlag = true;
	private int widthMeasureSpec;
	private int heightMeasureSpec;
	private int lunHeight;

	public void setViewHeight(int height) {
		if (!viewHeightFlag) {
			lunHeight = height;
			lunHeight -= (int) (getResources()
					.getDimension(R.dimen.lunch_bottom_height));
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		this.widthMeasureSpec = widthMeasureSpec;
		this.heightMeasureSpec = heightMeasureSpec;
		viewWidth = MeasureSpec.getSize(widthMeasureSpec);
		viewHeight = MeasureSpec.getSize(heightMeasureSpec);
		if (viewHeight < 10)
			return;
		// if (getResources().getDisplayMetrics().density == 2)
		// viewHeight -= (int) (getResources().getDimension(
		// R.dimen.lunch_bottom_height)
		// * getResources().getDisplayMetrics().density * 2);
		// else
		viewHeight -= (int) (getResources()
				.getDimension(R.dimen.lunch_bottom_height));

		width = viewWidth;
		height = viewHeight;
		Log.i("menu页面", "width=" + width + "height=" + height
				+ "onLayoutheight=" + onLayoutheight);
		// if (height > (getResources().getDisplayMetrics().heightPixels/2) &&
		// height < onLayoutheight) {
		// onLayoutForItem();
		// onLayoutheight = height;//
		// onLayout方法内得到的高度比实际高度要高一些，不知道是不是把标题栏计算在内，此处处理一下，如果高度比onlayout时的低，在设置一次布局
		// }
		if (onceFlag == false) {
			for (LunchItem item : lunchItemList) {
				measureChild(item, widthMeasureSpec, heightMeasureSpec);
			}
			roundView.measure(widthMeasureSpec, heightMeasureSpec);
			pressBackGroudView.measure(widthMeasureSpec, heightMeasureSpec);
			ShadowView.measure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	private static int onLayoutheight;

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		Log.i("menuAAA", "width=" + width + "height=" + height);
		if (onceFlag) {
			onLayoutheight = height;
			Log.i("menu页面", "onLayout  tt" + (b - t));
			Log.i("menu页面", "width=" + width + "height=" + height);
			initlayout();
			if (pressBackGroudView == null)
				pressBackGroudView = new LunchView(ctx);
			if (ShadowView == null)
				ShadowView = new DrawingCacheView(ctx);
			if (roundView == null)
				roundView = new RoundView(ctx);
			for (LunchItem item : lunchItemList) {
				measureChild(item, widthMeasureSpec, heightMeasureSpec);
			}
			roundView.measure(widthMeasureSpec, heightMeasureSpec);
			pressBackGroudView.measure(widthMeasureSpec, heightMeasureSpec);
			ShadowView.measure(widthMeasureSpec, heightMeasureSpec);
			onceFlag = false;
			onLayoutForItem();
		}
		// onLayoutForItem();
		int pageCount = lunchItemList.size() / lunchPageCount
				+ (lunchItemList.size() % lunchPageCount == 0 ? 0 : 1);
		if (pageCount == 1) {
			isRoundViewVisiable = false;
			C_iLimitSlipSpan = 1;
			C_iLongPressLimitSpan = 0;
		} else {
			C_iLimitSlipSpan = 50;
			isRoundViewVisiable = true;
		}
		roundView.setCount(pageCount);
		roundView.layout(
				0,
				height,
				width,
				height
						+ ((int) getResources().getDimension(
								R.dimen.lunch_bottom_height)));
		roundView.requestLayout();
		pressBackGroudView.layout(0 * width, 0, (0 + 1) * width, height
				+ (int) getResources()
						.getDimension(R.dimen.lunch_bottom_height));
		ShadowViewSlip(downX, downY);
		// invalidate();
	}

	private Runnable requestLayoutRun = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			requestLayout();
		}
	};

	private void onLayoutForItem() {
		if (roundView != null)
			onLayoutForItem(-roundView.getIndex() * width);
		else
			onLayoutForItem(0);
	}

	/**
	 * 锟斤拷始锟斤拷页锟芥布锟斤拷
	 * 
	 * @param dictanceX
	 */
	private void onLayoutForItem(int dictanceX) {
		Log.i("menu页面", "onLayoutForItem=" + dictanceX);
		int itemWidth = width / lunchColumns;
		int itemHeight = lunHeight / lunchRow;
		// Log.i("tag", "dictanceX"+dictanceX);
		for (int i = 0; i < lunchItemList.size(); i++) {
			int x = (i / lunchPageCount) * width
					+ (i - lunchPageCount * (i / lunchPageCount))
					% lunchColumns * itemWidth;
			int y = (i - lunchPageCount * (i / lunchPageCount)) / lunchColumns
					* itemHeight;
			lunchItemList.get(i).layout(x + dictanceX + firstViewLeft, y,
					x + itemWidth + dictanceX + firstViewLeft, y + itemHeight);
			// lunchItemList.get(i).invalidate();
			// lunchItemList.get(i).requestLayout();
		}
		// invalidate();
		if (dictanceX % width == 0)// 初始化九宫格，由于在正文等非首页页面，初始化高度不对，所以需要在调一次requestLayout方法，但是在左右滑动时不需要
			post(requestLayoutRun);
		// requestLayout();
	}

	/**
	 * 锟斤拷锟斤拷某一锟斤拷lunchitem锟斤拷位锟斤拷
	 * 
	 * @param down
	 *            锟斤拷始位锟斤拷
	 * @param i
	 *            锟斤拷锟轿伙拷锟?
	 */
	private void onLayoutOne(int down, int i) {
		if (down == -1)
			return;
		i = i % lunchPageCount;
		int itemWidth = width / lunchColumns;
		int itemHeight = height / lunchRow;
		int x = (i / lunchPageCount) * width
				+ (i - lunchPageCount * (i / lunchPageCount)) % lunchColumns
				* itemWidth;
		int y = (i - lunchPageCount * (i / lunchPageCount)) / lunchColumns
				* itemHeight;
		lunchItemList.get(down).layout(x, y, x + itemWidth, y + itemHeight);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (canvas == null) {
			Log.v("taggg", "canvas");
		}
		if (pressBackGroudView != null) {
			// Log.v("taggg", "pressBackGroudView");
			// drawChild(canvas, pressBackGroudView, getDrawingTime());//需求取消背景色
			if (isRoundViewVisiable)
				drawChild(canvas, roundView, getDrawingTime());
			// if (isShadowFlag)
			// drawChild(canvas, ShadowView, getDrawingTime());
		}

	}

	private int firstViewLeft;
	private int lastViewRight;
	private int downX;
	private int downY;
	private boolean downPressFlag;
	private int longPressDownLocal;
	private int downVisiableIndex;
	private boolean hasFinishUPEvent = true;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getPointerCount() > 1)
			return true;
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);
		if (isSwitchRunnable || !isCanLongPress || lunchItemList.size() == 0)
			return false;
		// LogM.i("lunchfillper=" + height);
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (lunchItemList.size() > 0) {
				// Log.i("tag","leftLoacl="+lunchItemList.get(0).getLeft());
				firstViewLeft = lunchItemList.get(0).getLeft();
				lastViewRight = getLastItemRight();
			}
			downX = (int) ev.getX();
			downY = (int) ev.getY();
			hasFinishUPEvent = false;
			lockSlip = true;
			longFlag = true;
			longFlag11 = true;
			iscomeLongpress = true;
			longPressDownLocal = getLongPressBackLocal((int) ev.getX(),
					(int) ev.getY());
			visiableViewIndexWhenDown = Math.abs(lunchItemList.get(0).getLeft()
					/ width);
			if (!isMoveRunnable)
				indexForScroller = visiableViewIndexWhenDown;// 锟节伙拷锟斤拷停止锟斤拷时锟斤拷锟斤拷锟矫伙拷锟斤拷页锟斤拷锟斤拷锟斤拷锟斤拷为indexForScroller锟斤拷锟街碉拷锟斤拷锟斤拷诨锟斤拷锟斤拷锟绞憋拷锟斤拷锟?
			 Log.i("1tag",
			 "longPressDownLocal="+longPressDownLocal+"visiableViewIndexWhenDown="+visiableViewIndexWhenDown);
			if (visiableViewIndexWhenDown * lunchPageCount + longPressDownLocal < lunchItemList
					.size()) {
				if (!isMoveRunnable) {
					// setBackGroud(
					// 0,
					// pressBackGroudView
					// .findViewById(groupIDArray[longPressDownLocal]));
					Log.i("tag", "gggg" + dayuHeight);
					if (!dayuHeight) {
						setPressItemBackGroud(0);
					}
				}
			}
			longPressLocal = longPressDownLocal;
			downPressFlag = false;
			break;
		case MotionEvent.ACTION_MOVE:
			// Log.i("tag", "lockSlip=" + lockSlip + "@" + longFlag);
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			// int local = getLongPressBackLocal(x, y);
			if (lockSlip && longFlag && !isSwitchRunnable && !isMoveRunnable) {// 页锟斤拷锟斤拷锟?
				slipItemView((int) ev.getX() - downX);
				setPageIndexRoundView();
				// if (!downPressFlag) {
				// setPressItemBackGroud(1);
				// // setBackGroud(1,
				// // pressBackGroudView
				// // .findViewById(groupIDArray[local]));
				// downPressFlag = true;
				// }
			} else if (!longFlag) {// 锟斤拷锟斤拷锟铰硷拷
				// if (local != longPressLocal && local != -1) {
				// // Log.i("tag", "local=" + local);
				//
				// if (!isSwitchRunnable
				// && !isMoveRunnable
				// && (downVisiableIndex * lunchPageCount
				// + longPressLocal != visiableViewIndex
				// * lunchPageCount + local)
				// && ((visiableViewIndex * lunchPageCount + local) <
				// lunchItemList
				// .size())) {
				// setBackGroud(1,
				// pressBackGroudView
				// .findViewById(groupIDArray[local]));
				// if (longPressLocal != -1)
				// setBackGroud(
				// 0,
				// pressBackGroudView
				// .findViewById(groupIDArray[longPressLocal]));
				// // Log.i("tag", "SWITCHMO");
				// isMoveRunnable = true;
				// RefreshLunchView(downVisiableIndex * lunchPageCount
				// + longPressLocal, visiableViewIndex
				// * lunchPageCount + local);
				// longPressLocal = local;
				// downVisiableIndex = visiableViewIndex;
				// }
				//
				// }
				// LogM.i("移动"+x+"&&"+y);
				ShadowViewSlip(x, y);
				longPressSlip(x);
			}
			break;
		case MotionEvent.ACTION_UP:
			Log.i("tag", "up");
			int x1 = (int) ev.getX();
			int y1 = (int) ev.getY();
			int local = getLongPressBackLocal(x1, y1);
			longFlag11 = true;
			dayuHeight = false;
			hasFinishUPEvent = true;
			if (lockSlip && longFlag) {// 锟斤拷锟?
				int sign = getVelocityType();
				touchUpEvent(sign);
			} else if (!longFlag) {// 锟斤拷锟斤拷锟铰硷拷
				if (onItemClickListener != null)
					onItemClickListener.stopLongPress();
				if (local != longPressLocal && local != -1) {
					 Log.i("tag", "local=" + local);

					if (!isSwitchRunnable
							&& !isMoveRunnable
							&& (downVisiableIndex * lunchPageCount
									+ longPressLocal != visiableViewIndex
									* lunchPageCount + local)
							&& (visiableViewIndex * lunchPageCount + local != lunchItemList
									.size() - 1)
							&& ((visiableViewIndex * lunchPageCount + local) < lunchItemList
									.size())) {
						setBackGroud(1,
								pressBackGroudView
										.findViewById(groupIDArray[local]));
						if (longPressLocal != -1)
							setBackGroud(
									0,
									pressBackGroudView
											.findViewById(groupIDArray[longPressLocal]));
						// Log.i("tag", "SWITCHMO");
						isMoveRunnable = true;
						RefreshLunchView(downVisiableIndex * lunchPageCount
								+ longPressLocal, visiableViewIndex
								* lunchPageCount + local);
						longPressLocal = local;
						downVisiableIndex = visiableViewIndex;
					} else {
						lunchItemList.get(
								visiableViewIndexWhenDown * lunchPageCount
										+ longPressDownLocal).setVisibility(
								View.VISIBLE);
					}

				} else {
					if (!isHide)
						lunchItemList.get(
								visiableViewIndexWhenDown * lunchPageCount
										+ longPressDownLocal).setVisibility(
								View.VISIBLE);
				}
				// LogM.i("绉诲姩妯″潡"+x+"&&"+y);
				// ShadowViewSlip(x1, y1);
				// longPressSlip(x1);
				// for (LunchItem itrm : lunchItemList) {
				// if (!itrm.isShown()) {
				// itrm.setVisibility(View.VISIBLE);
				// }
				// }
			}
			if (longPressLocal != -1) {
				setBackGroud(1,
						pressBackGroudView
								.findViewById(groupIDArray[longPressLocal]));
				setPressItemBackGroud(1);
			}
			longPressLocal = -1;
			isHide = false;
			break;

		default:
			break;
		}
		invalidate();
		return super.dispatchTouchEvent(ev);
	}

	private void slipItemView(int dictanceX) {
		if (limitSlip()) {
			isCanMaxSlip = false;
			int right = lastViewRight;
			if ((firstViewLeft + dictanceX) > C_iLimitSlipSpan) {
				onLayoutForItem(C_iLimitSlipSpan);
				pressBackGroudView.layout(+C_iLimitSlipSpan, 0, ((1) * width)
						+ C_iLimitSlipSpan, height);
			} else if ((right + dictanceX) < (width - C_iLimitSlipSpan)) {
				isCanMaxSlip = false;
				onLayoutForItem(-C_iLimitSlipSpan);
				pressBackGroudView.layout(-C_iLimitSlipSpan, 0, (1 * width)
						- C_iLimitSlipSpan, height);
			} else {
				onLayoutForItem(dictanceX);
				isCanMaxSlip = true;
				pressBackGroudView.layout(+dictanceX, 0, ((1) * width)
						+ dictanceX, height);
			}
			invalidate();
		}
	}

	private int offset;

	private void slipViewForShrikRUN(int dictanceX) {
		int xOffSet = dictanceX - offset;
		// Log.i("tag", "offset="+xOffSet);
		for (int i = 0; i < lunchItemList.size(); i++) {
			lunchItemList.get(i).offsetLeftAndRight(xOffSet);
			// lunchItemList.get(i).invalidate();
			// lunchItemList.get(i).requestLayout();
		}
		setPageIndexRoundView();
		invalidate();
		// requestLayout();
		offset = dictanceX;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷
	 * 
	 * @return
	 */
	private boolean limitSlip() {
		boolean flag = true;
		if (lunchItemList.size() > 0) {
			LunchItem viewFirst = lunchItemList.get(0);
			int right = getLastItemRight();
			if (viewFirst.getLeft() > C_iLimitSlipSpan) {
				flag = false;
				isCanMaxSlip = false;
			}
			if (right < width - C_iLimitSlipSpan) {
				flag = false;
				isCanMaxSlip = false;
			}
		}
		return flag;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷
	 * 
	 * @param moveX
	 */
	protected boolean isMoveRunnable = false;
	private int longPressX;

	private void longPressSlip(int moveX) {
		if (!isMoveRunnable) {
			if (Math.abs(longPressX - moveX) > 5) {
				Log.i("tag", "LongSlip=" + moveX + "**" + viewWidth);
				Log.i("tag", C_iLongPressLimitSpan + "");
				if (moveX > (viewWidth - C_iLongPressLimitSpan)) {
					Log.i("tag",
							visiableViewIndex + "@@@"
									+ (lunchItemList.size() / lunchPageCount));
					if (visiableViewIndex != (lunchItemList.size() / (lunchPageCount + 1))) {
						indexForScroller++;
						startScrollByScroller(0, -width);
					}
				} else if (moveX < C_iLongPressLimitSpan) {
					if (visiableViewIndex != 0) {
						indexForScroller--;
						startScrollByScroller(0, width);
					}
				}
			}
		} else {
			longPressX = moveX;
		}
	}

	private int getVelocityType() {
		final VelocityTracker velocityTracker = mVelocityTracker;
		// 锟斤拷锟姐当前锟劫讹拷
		velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
		// x锟斤拷锟斤拷锟????
		int velocityX = (int) velocityTracker.getXVelocity();
		Log.i("tag", "velocityX=" + velocityX);
		Log.i("tag", "isCanMaxSlip=" + isCanMaxSlip + "indexForScroller="
				+ indexForScroller);
		int sign = 0;
		int firstItemLeft = lunchItemList.get(0).getLeft();
		int lastItemRight = getLastItemRight();
		Log.i("tag", "lastItemRight+" + lastItemRight);
		if ((velocityX > SNAP_VELOCITY) && isCanMaxSlip
				&& (indexForScroller != 0) && (firstItemLeft < 0))// 锟姐够锟斤拷??锟斤拷锟斤拷锟揭伙拷??
		{
			sign = velocityRight;
			Log.e("enough to move left", "right");
		} else if ((velocityX < -SNAP_VELOCITY)
				&& isCanMaxSlip
				&& (indexForScroller != (lunchItemList.size() / lunchPageCount))
				&& (lastItemRight > width))// 锟姐够锟斤拷??锟斤拷锟斤拷锟斤拷??
		{
			sign = velocityLeft;
			Log.e("enough to move right", "left");
		} else {
			Log.e("enough to move right", "center");
			sign = velocityNo;
		}
		return sign;
	}

	/**
	 * up锟铰硷拷锟斤拷锟斤拷锟斤拷锟斤拷
	 * 
	 * @param sign
	 */
	private void touchUpEvent(int sign) {
		if (!mScroller.isFinished()) {
			mScroller.forceFinished(true);
			handler.removeCallbacks(mScrollerRunnable);
		}
		// Log.i("tag","leftLoacl="+lunchItemList.get(0).getLeft());
		if (lunchItemList.size() > 0) {
			int firstItemLeft = lunchItemList.get(0).getLeft();
			int lastItemRight = getLastItemRight();
			LunchItem view = lunchItemList.get(0);
			int left = view.getLeft() % width;
			if (left >= 0) {
				switch (sign) {
				case velocityRight:
					indexForScroller--;
					startScrollByScroller(left, width - left);
					break;
				case velocityLeft:
					indexForScroller++;
					startScrollByScroller(left, -left);
					break;
				case velocityNo:
					if (Math.abs(left) < (width / 2) || firstItemLeft > 0) {
						startScrollByScroller(left, -left);
					} else {
						indexForScroller--;
						startScrollByScroller(left, width - left);
					}
					break;

				default:
					break;
				}
			} else {
				switch (sign) {
				case velocityRight:
					indexForScroller--;
					startScrollByScroller(left, -left);
					break;
				case velocityLeft:
					indexForScroller++;
					startScrollByScroller(left, -width - left);
					break;
				case velocityNo:
					if (Math.abs(left) < (width / 2) || lastItemRight < width) {
						startScrollByScroller(left, -left);
					} else {
						indexForScroller++;
						startScrollByScroller(left, -width - left);
					}
					break;

				default:
					break;
				}
			}

		}
	}

	private int indexForScroller = 0;

	private void startScrollByScroller(int startX, int dX) {
		Log.i("tag", "startX=" + startX + "dX=" + dX);
		offset = startX;
		mScroller.startScroll(startX, 0, dX, 0, 400);
		handler.post(mScrollerRunnable);
	}

	// SwitchRunnable switchRunnable;

	private void RefreshLunchView(int downIndex, int upIndex) {
		downIndex = downIndex > (lunchItemList.size() - 1) ? lunchItemList
				.size() - 1 : downIndex;
		if (upIndex >= lunchItemList.size() - 1)
			upIndex = lunchItemList.size() - 1;
		startScrollByScrollerForLongPress(downIndex, upIndex, false);
	}

	private boolean isHide;

	public void hideLunchItemView() {
		isHide = true;
		startScrollByScrollerForLongPress(downVisiableIndex * lunchPageCount
				+ longPressLocal, lunchItemList.size() - 1, true);
	}

	/**
	 * 锟斤拷锟斤拷模锟斤拷
	 * 
	 * @param downIndex
	 * @param upIndex
	 */
	private void startScrollByScrollerForLongPress(int downIndex, int upIndex,
			boolean isHide) {
		if(isHide&&downIndex==-1){
			lunchItemList.get(
					visiableViewIndexWhenDown * lunchPageCount
							+ longPressDownLocal).setVisibility(
					View.VISIBLE);
			return;
		}
		List<Integer> normal = new ArrayList<Integer>();
		List<Integer> spacial = new ArrayList<Integer>();
		List<Integer> spanSpacial = new ArrayList<Integer>();
		if (downIndex < upIndex) {
			for (int i = 0; i < upIndex - downIndex; i++) {
				int selectedIndex = downIndex + 1 + i;
				if (downIndex / lunchPageCount != selectedIndex
						/ lunchPageCount
						&& selectedIndex % lunchPageCount == 0) {
					spanSpacial.add(selectedIndex);
				} else {

					if (isLeftBoundary(selectedIndex)) {
						spacial.add(selectedIndex);
					} else {
						normal.add(selectedIndex);
					}

				}
			}
			// offsetX = 0;
			// spacialOffsetX = 0;
			// spacialOffsetY = 0;
			// spanSpacialOffsetX = 0;
			// spanSpacialOffsetY = 0;
			mScroller.startScroll(0, 0, -width / lunchColumns, 0, 400);
			mItemScroller.startScroll(0, 0, width / lunchColumns
					* (lunchColumns - 1), -height / lunchRow, 400);
			nItemScroller.startScroll(0, 0, -width / lunchColumns, height
					/ lunchRow * (lunchRow - 1), 400);
		} else if (downIndex > upIndex) {
			for (int i = 0; i < downIndex - upIndex; i++) {
				int selectedIndex = upIndex + i;
				if (downIndex / lunchPageCount != selectedIndex
						/ lunchPageCount
						&& selectedIndex % lunchPageCount == (lunchPageCount - 1)) {
					spanSpacial.add(selectedIndex);
				} else {
					if (isRightBoundary(upIndex + i)) {
						spacial.add(upIndex + i);
					} else {
						normal.add(upIndex + i);
					}
				}
			}
			// offsetX = 0;
			// spacialOffsetX = 0;
			// spacialOffsetY = 0;
			// spanSpacialOffsetX = 0;
			// spanSpacialOffsetY = 0;
			mScroller.startScroll(0, 0, width / lunchColumns, 0, 400);
			mItemScroller.startScroll(0, 0, -width / lunchColumns
					* (lunchColumns - 1), height / lunchRow, 400);
			nItemScroller.startScroll(0, 0, width / lunchColumns, -height
					/ lunchRow * (lunchRow - 1), 400);
		}
		// if (switchRunnable == null)
		SwitchRunnable switchRunnable = new SwitchRunnable();
		switchRunnable.setData(normal, spacial, spanSpacial, downIndex,
				upIndex, isHide,longPressDownLocal);
		handler.post(switchRunnable);
	}

	private boolean isLeftBoundary(int index) {
		if ((index + 1) % lunchColumns == 1)
			return true;
		return false;
	}

	private boolean isRightBoundary(int index) {
		if ((index + 1) % lunchColumns == 0)
			return true;
		return false;
	}

	private Handler handler = new Handler();
	/**
	 * 锟斤拷锟斤拷锟斤拷锟狡讹拷锟斤拷位锟斤拷锟斤拷锟斤拷
	 * 
	 * @param x
	 * @param y
	 * @return 锟斤拷锟斤拷-1锟斤拷示锟斤拷锟斤拷幕之锟斤拷
	 */
	private int longPressLocal = -1;
	private boolean dayuHeight;

	private int getLongPressBackLocal(int x, int y) {
		// LogM.i( "x=" + x + "y=" + y);
		int local = 0;
		if (y < 0)
			return -1;
		if (y > height) {
			dayuHeight = true;
		}
		local = (x / (viewWidth / lunchColumns))
				+ (y / (viewHeight / lunchRow)) * lunchColumns;
		if (local < 0)
			local = 0;
		if (local > lunchPageCount - 1)
			local = lunchPageCount - 1;
		// LogM.i("lunchindex=" + local);
		return local;
	}

	private void setQuake() {
		Vibrator vibrator = (Vibrator) ctx
				.getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 5, 100, 0, 0 }; // 停止 ???? 停止 ????
		vibrator.vibrate(pattern, -1);
	}

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
		onceFlag = true;
		firstViewLeft = 0;
		if (width != 0) {
			initlayout();
			onLayoutForItem();
		}
		requestLayout();
	}

	public void setOnItemClickListener(OnItemCilickListener l) {
		onItemClickListener = l;
	}

	private boolean longFlag = true;
	private boolean longFlag11 = true;
	private boolean isCanLongPress = true;

	public void setIsCanLongPress(boolean is) {
		isCanLongPress = is;
	}

	private boolean viewHeightFlag;

	/**
	 * 锟斤拷锟斤拷锟铰硷拷
	 */
	@Override
	public void longClick(View v) {
		int offset = Math.abs(lunchItemList.get(0).getLeft() % width);
		if (((visiableViewIndexWhenDown * lunchPageCount + longPressDownLocal == lunchItemList
				.size() - 1) && (lunchItemList.get(lunchItemList.size() - 1)
				.getEntity().getType() == SeeyonMainMenuLayout.C_iMPrivilegeMenu_Add))
				|| (offset > 10) || hasFinishUPEvent)// &&后面是判断当页面已经开始滑动时，不能做长按操作
			return;
		longFlag = false;
		if (!downPressFlag) {
			longFlag11 = false;
			viewHeightFlag = true;
			if (onItemClickListener != null)
				onItemClickListener.startLongPress();
			// setPressItemBackGroud(1);
			setQuake();
			// pressBackGroudView.setBac/kGround(0);
			LunchItem itemview = lunchItemList.get(visiableViewIndexWhenDown
					* lunchPageCount + longPressDownLocal);
			// Log.i("tag", "long="+itemview.getEntity().getName());
			itemview.setVisibility(View.INVISIBLE);
			if (mOnMenuLongPressListener != null) {
				mOnMenuLongPressListener.onLongPress(itemview);
			}
			ShadowView.setBitmap(getItemDrawingCache(itemview));
			ShadowViewSlip(downX, downY);
			ShadowView.setFlag(true);
			ShadowView.invalidate();
			ShadowView.requestLayout();
			isShadowFlag = true;
			downPressFlag = true;
			// invalidate();
			// requestLayout();
		}

	}

	public interface OnMenuLongPressListener {
		public void onLongPress(LunchItem itemview);
	}

	private OnMenuLongPressListener mOnMenuLongPressListener;

	public void setOnMenuLongPressListener(OnMenuLongPressListener l) {
		mOnMenuLongPressListener = l;
	}

	@Override
	public void move() {
		lockSlip = true;
	}

	@Override
	public void stop() {
		lockSlip = false;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟??
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN)
			lockSlip = true;
		return true;
	}

	/**
	 * 锟斤拷锟斤拷锟铰硷拷
	 */
	@Override
	public void click(View v) {
		int offset = Math.abs(lunchItemList.get(0).getLeft() % width);
		if (longFlag && (offset < 10)) {
			Log.i("tag", "锟斤拷锟斤拷");
			if (longPressLocal != -1)
				setBackGroud(1,
						pressBackGroudView
								.findViewById(groupIDArray[longPressLocal]));
			if (onItemClickListener != null)
				onItemClickListener.itemClick(((LunchItem) v).getEntity());
		}
	}

	public boolean isReset() {
		if (lunchItemList.size() == 0)
			return true;
		int offset = Math.abs(lunchItemList.get(0).getLeft() % width);
		return offset < 10 && !isSwitchRunnable && longFlag11;
	}

	/**
	 * 锟斤拷图
	 * 
	 * @param view
	 * @return
	 */
	private Bitmap getItemDrawingCache(View view) {
		// Drawable drawable= view.findViewById(R.id.rll).getBackground();
		// view.findViewById(R.id.rll).setBackgroundColor(getResources().getColor(R.color.menu_8));;
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(view.getLeft(), view.getTop(), view.getRight(),
				view.getBottom());
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache();
		// view.findViewById(R.id.rll).setBackgroundDrawable(drawable);;
		return bitmap;
	}

	/**
	 * 闀挎寜鎸夐挳鐨勭Щ鍔?
	 * 
	 * @param x
	 * @param y
	 */
	private void ShadowViewSlip(int x, int y) {
		// ShadowViewSlipRunnable run=new ShadowViewSlipRunnable(x, y);
		// handler.post(run);
		int width = viewWidth / lunchColumns;
		int height = viewHeight / lunchRow;
		ShadowView.layout(x - width / 2, y - height / 2, x + width / 2, y
				+ height / 2);
	}

	private int roundIndex;

	/**
	 * 页锟斤拷丶锟斤拷锟斤拷锟?
	 */
	private void setPageIndexRoundView() {
		int pageIndex = Math.abs((lunchItemList.get(0).getLeft() - width / 2)
				/ width);
		if (pageIndex != roundIndex) {
			roundView.setIndex(pageIndex);
			roundIndex = pageIndex;
		}
	}

	private int getLastItemRight() {
		int lastItemRight = lunchItemList.get(lunchItemList.size() - 1)
				.getRight();
		int i = lunchItemList.size() % lunchColumns;
		if (i == 0)
			i = lunchColumns;
		lastItemRight = lastItemRight + width / lunchColumns
				* (lunchColumns - i);
		return lastItemRight;
	}

	private Runnable mScrollerRunnable = new Runnable() {
		@Override
		public void run() {
			final Scroller scroller = mScroller;
			if (!scroller.isFinished()) {
				isMoveRunnable = true;
				scroller.computeScrollOffset();
				// Log.i("tag", "LL="+scroller.getCurrX());
				slipViewForShrikRUN(scroller.getCurrX());
				handler.postDelayed(this, 16);
				// Log.i("tag","leftLoacl="+lunchItemList.get(0).getLeft());
			} else {
				isMoveRunnable = false;
				if (lunchItemList.size() > 0) {
					int firstLeft = lunchItemList.get(0).getLeft();
					visiableViewIndex = Math.abs(firstLeft / width);// 锟斤拷取锟缴硷拷页锟斤拷位锟斤拷
					if (longFlag) {
						downVisiableIndex = visiableViewIndex;
					}
					invalidate();
				}

				pressBackGroudView.layout(0, 0, width, height);
			}
		}
	};

	private boolean iscomeLongpress = true;
	private boolean isSwitchRunnable;

	class SwitchRunnable implements Runnable {
		private List<Integer> normalArray;
		private List<Integer> spacialArray;
		private List<Integer> spanSpacialArray;
		private int downIndex;
		private int upIndex;
		private boolean isHide;
		private int longPressDownLocal;

		public void setData(List<Integer> normalArray,
				List<Integer> spacialArray, List<Integer> spanSpacialArray,
				int downIndex, int upIndex,int longPressDownLocal) {
			this.normalArray = normalArray;
			this.spacialArray = spacialArray;
			this.spanSpacialArray = spanSpacialArray;
			this.downIndex = downIndex;
			this.upIndex = upIndex;
			this.longPressDownLocal=longPressDownLocal;
			iscomeLongpress = false;
		}

		public void setData(List<Integer> normalArray,
				List<Integer> spacialArray, List<Integer> spanSpacialArray,
				int downIndex, int upIndex, boolean isHide,int longPressDownLocal) {
			this.normalArray = normalArray;
			this.spacialArray = spacialArray;
			this.spanSpacialArray = spanSpacialArray;
			this.downIndex = downIndex;
			this.upIndex = upIndex;
			iscomeLongpress = false;
			this.longPressDownLocal=longPressDownLocal;
			this.isHide = isHide;
		}

		@Override
		public void run() {
			final Scroller scroller = mScroller;
			final Scroller spcailScroller = mItemScroller;
			final Scroller spanSpcailScroller = nItemScroller;
			if (!scroller.isFinished() && !spcailScroller.isFinished()
					&& !spanSpcailScroller.isFinished()) {
				isMoveRunnable = true;
				isSwitchRunnable = true;
				scroller.computeScrollOffset();
				spcailScroller.computeScrollOffset();
				spanSpcailScroller.computeScrollOffset();
				switchViewForShrikRUN(normalArray, scroller.getCurrX());
				switchSpacialViewForShrikRUN(spacialArray,
						spcailScroller.getCurrX(), spcailScroller.getCurrY());
				switchSpanSpacialViewForShrikRUN(spanSpacialArray,
						spanSpcailScroller.getCurrX(),
						spanSpcailScroller.getCurrY());
				handler.postDelayed(this, 16);
			} else {
				isSwitchRunnable = false;
				if (lunchItemList.size() > 0) {
					int firstLeft = lunchItemList.get(0).getLeft();
					visiableViewIndex = Math.abs(firstLeft / width);// 锟斤拷取锟缴硷拷页锟斤拷位锟斤拷
				}
				onLayoutOne(downIndex, upIndex);
				if (!isHide)
					lunchItemList.get(
							visiableViewIndexWhenDown * lunchPageCount
									+ longPressDownLocal).setVisibility(
							View.VISIBLE);
				rePackageLunchList(downIndex, upIndex);
				isMoveRunnable = false;
			}
		}

		private int spanSpacialOffsetX;
		private int spanSpacialOffsetY;

		private void switchSpanSpacialViewForShrikRUN(
				List<Integer> spaciallArray, int dictanceX, int dictanceY) {
			int xOffSet = dictanceX - spanSpacialOffsetX;
			int yOffSet = dictanceY - spanSpacialOffsetY;
			// Log.i("tag", "offset="+xOffSet);
			// Log.i("tag", "offset="+yOffSet);
			for (int i : spaciallArray) {
				// Log.i("tag", "$$$$$$$"+lunchItemList.get(i).getTop());
				lunchItemList.get(i).offsetLeftAndRight(xOffSet);
				lunchItemList.get(i).offsetTopAndBottom(yOffSet);
				// lunchItemList.get(i).invalidate();
				// lunchItemList.get(i).requestLayout();
				invalidate();
				// requestLayout();
			}
			spanSpacialOffsetX = dictanceX;
			spanSpacialOffsetY = dictanceY;
		}

		private int offsetX;

		private void switchViewForShrikRUN(List<Integer> normalArray,
				int dictanceX) {
			int xOffSet = dictanceX - offsetX;
			for (int i : normalArray) {
				lunchItemList.get(i).offsetLeftAndRight(xOffSet);
				// lunchItemList.get(i).invalidate();
				// lunchItemList.get(i).requestLayout();
				invalidate();
				// requestLayout();
			}
			offsetX = dictanceX;
		}

		private int spacialOffsetX;
		private int spacialOffsetY;

		private void switchSpacialViewForShrikRUN(List<Integer> spaciallArray,
				int dictanceX, int dictanceY) {
			int xOffSet = dictanceX - spacialOffsetX;
			int yOffSet = dictanceY - spacialOffsetY;
			// Log.i("tag", "offset="+xOffSet);
			// Log.i("tag", "offset="+yOffSet);
			for (int i : spaciallArray) {
				// Log.i("tag", "$$$$$$$"+lunchItemList.get(i).getTop());
				lunchItemList.get(i).offsetLeftAndRight(xOffSet);
				lunchItemList.get(i).offsetTopAndBottom(yOffSet);
				// lunchItemList.get(i).invalidate();
				// lunchItemList.get(i).requestLayout();
				invalidate();
				// requestLayout();
			}
			spacialOffsetX = dictanceX;
			spacialOffsetY = dictanceY;
		}

		private void rePackageLunchList(int downIndex, int upIndex) {
			if (isHide) {
				if(downIndex==-1)return;
				removeView(lunchItemList.get(downIndex));
				Entity e = lunchItemList.get(downIndex).getEntity();
				if (!e.isBusiness() && e.getFrom() != Entity.C_iMenuFrom_third) {
					e.setHide(true);
					hideList.add(e);
				}
				lunchItemList.remove(downIndex);
			} else {
				if (upIndex < lunchItemList.size()) {
					if (upIndex > downIndex) {
						lunchItemList.add(upIndex + 1,
								lunchItemList.get(downIndex));
						lunchItemList.remove(downIndex);
					} else if (upIndex < downIndex) {
						lunchItemList
								.add(upIndex, lunchItemList.get(downIndex));
						lunchItemList.remove(downIndex + 1);
					}
				} else if (upIndex >= lunchItemList.size()) {
					lunchItemList.add(lunchItemList.size(),
							lunchItemList.get(downIndex));
					lunchItemList.remove(downIndex);
				}
			}
			saveLunchState();
		}

	}

	private PresserRunnable pressRunnable;

	private void setBackGroud(int type, View view) {
		pressRunnable = new PresserRunnable(type, view);
		post(pressRunnable);
	}

	private void setPressItemBackGroud(int type) {
		int index = visiableViewIndexWhenDown * lunchPageCount
				+ longPressDownLocal;
		if (index >= lunchItemList.size())
			return;
		if (type == 0) {
			((ViewGroup) ((ViewGroup) lunchItemList.get(index).getChildAt(0))
					.getChildAt(0)).getChildAt(0).setBackgroundResource(R.drawable.home_menugray);
		} else if (type == 1) {
			((ViewGroup) ((ViewGroup) lunchItemList.get(index).getChildAt(0))
					.getChildAt(0)).getChildAt(0).setBackgroundResource(
					lunchItemList.get(index).getEntity().getColor());
		}
	}

	class PresserRunnable implements Runnable {
		private int type;
		private View view;

		public PresserRunnable(int type, View view) {
			this.type = type;
			this.view = view;
		}

		@Override
		public void run() {
			if (type == 0) {
				view.setBackgroundColor(getResources().getColor(R.color.blue));
				view.getBackground().setAlpha(50);
			} else if (type == 1) {
				view.setBackgroundResource(R.drawable.transport);
			}
		}

	}

	private static final int C_iRlViewID1 = 0x000101;
	private static final int C_iLLGrouplViewID = 0x000111;

	class LunchView extends LinearLayout {
		private List<List<RelativeLayout>> rlArray;
		private RelativeLayout rlBottom;

		public LunchView(Context context) {
			super(context);
			int width = viewWidth / lunchColumns;
			int height = viewHeight / lunchRow;
			LayoutParams llP = new LayoutParams(LayoutParams.FILL_PARENT,
					height);
			LayoutParams rlP = new LayoutParams(width, LayoutParams.FILL_PARENT);
			setId(C_iLLGrouplViewID);
			setOrientation(LinearLayout.VERTICAL);
			List<LinearLayout> viewRows = new ArrayList<LinearLayout>();
			for (int i = 0; i < lunchRow; i++) {
				LinearLayout view = new LinearLayout(ctx);
				view.setOrientation(LinearLayout.HORIZONTAL);
				viewRows.add(view);
			}
			List<List<RelativeLayout>> viewRLs = new ArrayList<List<RelativeLayout>>();
			int id = C_iRlViewID1;
			for (int x = 0; x < lunchRow; x++) {
				List<RelativeLayout> list = new ArrayList<RelativeLayout>();
				for (int y = 0; y < lunchColumns; y++) {
					RelativeLayout rl = new RelativeLayout(ctx);
					rl.setId(id);
					id++;
					list.add(rl);
				}
				viewRLs.add(list);
			}
			for (int i = 0; i < viewRows.size(); i++) {
				addView(viewRows.get(i), llP);
				for (int j = 0; j < lunchColumns; j++) {
					viewRows.get(i).addView(viewRLs.get(i).get(j), rlP);
				}
			}

			rlBottom = new RelativeLayout(context);
			addView(rlBottom, LayoutParams.FILL_PARENT, (int) getResources()
					.getDimension(R.dimen.lunch_bottom_height));
			rlArray = new ArrayList<List<RelativeLayout>>();
			rlArray = viewRLs;
		}

		public void setBackGround(int type) {
			for (List<RelativeLayout> list : rlArray) {
				for (RelativeLayout rl : list) {
					if (type == 0) {
						rl.setBackgroundColor(getResources().getColor(
								R.color.blue));
						rl.getBackground().setAlpha(50);
						rlBottom.setBackgroundColor(getResources().getColor(
								R.color.blue));
						rlBottom.getBackground().setAlpha(50);

					} else if (type == 1) {
						rl.setBackgroundResource(R.drawable.transport);
						rlBottom.setBackgroundResource(R.drawable.transport);
					}

				}
			}

		}

	}

	private LunchDataBaseAdapter lunchDB;
	private List<Entity> hideList = new ArrayList<Entity>();// 保存被隐藏的标准模块，在保存数据的时候拼接在九宫格后面

	public void saveLunchState() {// 将保存放在初始九宫格页面,切换删除动作,设置模块条数时调用，
		if (lunchDB == null) {
			lunchDB = new LunchDataBaseAdapter(ctx);
		}
		long useID = 198702280216l;

		List<Entity> array = new ArrayList<Entity>();
		for (LunchItem i : lunchItemList) {
			array.add(i.getEntity());
		}
		if (array.size() == 0)// 在打开一个新页面而且没有展开菜单的情况下，array的size==0，此时不用保留数据
			return;
		array.addAll(hideList);
		lunchDB.open();
		Cursor cursor = lunchDB.select(useID, 1212);

			String update = JSONUtil.writeEntityToJSONString(array);
			if (!cursor.moveToFirst()) {
				lunchDB.insert(useID, update, 1212);
			} else {
				lunchDB.update(update, useID,1212);
			}

		cursor.close();
		lunchDB.close();
	}

	public int getViewWidth() {
		return width / lunchColumns;
	}

	public int getViewHeight() {
		return height / lunchRow;
	}
}
