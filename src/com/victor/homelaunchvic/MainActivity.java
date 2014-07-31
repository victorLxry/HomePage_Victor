package com.victor.homelaunchvic;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.victor.homelaunchvic.menu.fragment.HideFoldorFragment;
import com.victor.homelaunchvic.menu.fragment.MenuThirdHtmlFragment;
import com.victor.homelaunchvic.menu.view.BaseLunchFlipper;
import com.victor.homelaunchvic.menu.view.Entity;
import com.victor.homelaunchvic.menu.view.MenuLayout;
import com.victor.homelaunchvic.menu.view.MenuLayout.OnMenuHideListener;
import com.victor.homelaunchvic.menu.view.OnItemCilickListener;
import com.victor.homelaunchvic.menu.view.SaBaseMenuLayout;
import com.victor.homelaunchvic.menu.view.SeeyonMainMenuLayout;

public class MainActivity extends Activity implements OnItemCilickListener {
	private BaseLunchFlipper lunch;
	private SaBaseMenuLayout mMenuLayout;
	private boolean isShowMenu;
	private RelativeLayout rlCenter;
	private LinearLayout llLeftContent;
	private LinearLayout llRightContent;
	private LinearLayout llRightSet;
	private ImageView ivDis;
	private TextView tvTitle;
	private String tempString;
	private ImageView imgI;
	private RelativeLayout rlTitle;
	private int maxLaftButtoncount = 0;
	private int maxRightButtoncount = 0;
	private View rl_Hide;
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		setContentView(R.layout.fragment_base_menu);
		MenuLayout vHideLayout = (MenuLayout) findViewById(R.id.ll_main);
		mMenuLayout = (SaBaseMenuLayout) findViewById(R.id.menu_layout);
		rl_Hide = findViewById(R.id.rl_menu_hide);
		rl_Hide.getBackground().setAlpha(150);
		lunch = mMenuLayout.getLunchFlipper();
		rlCenter = (RelativeLayout) activity.findViewById(R.id.rl_head_center);
		ivDis = (ImageView) activity.findViewById(R.id.iv_head_dis);
		tvTitle = (TextView) activity.findViewById(R.id.tv_head_title);
		llLeftContent = (LinearLayout) activity
				.findViewById(R.id.ll_m1bar_left);
		llRightContent = (LinearLayout) activity
				.findViewById(R.id.ll_m1bar_right);
		llRightSet = (LinearLayout) activity
				.findViewById(R.id.ll_m1bar_right_set);
		llRightSet.setVisibility(View.GONE);
		llRightSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!lunch.isReset()) {
					return;
				}
				mMenuLayout.startAnimation(setSmallPopAnimation());
				isShowMenu = false;
			}
		});
		imgI = (ImageView) findViewById(R.id.iv_head_i);
		rlTitle = (RelativeLayout) findViewById(R.id.rl_head_title);
		layout = new SeeyonMainMenuLayout(this);
		layout.setLayout(lunch, true);
		lunch.setOnItemClickListener(this);
		rlCenter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!lunch.isReset()) {
					return;
				}
				rlCenterOnclickEvent();
			}
		});
		vHideLayout.setOnMenuHideListener(new OnMenuHideListener() {

			@Override
			public void onHide(int action) {
				if (action == -100) {
					rl_Hide.setBackgroundColor(getResources().getColor(
							R.color.black));
					rl_Hide.getBackground().setAlpha(150);
				} else {
					rl_Hide.setBackgroundResource(R.drawable.shape_menu_hide);
					rl_Hide.getBackground().setAlpha(200);
				}
				if (action == MotionEvent.ACTION_UP) {
					rl_Hide.setBackgroundColor(getResources().getColor(
							R.color.black));
					rl_Hide.getBackground().setAlpha(150);
					lunch.hideLunchItemView();
				}
			}
		});
		// 这里设置这两个的事件主要是为了消化点击 不用在继续下传了
		llLeftContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		llRightContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
	}

	public void rlCenterOnclickEvent() {
		if (!isShowMenu) {
			try {
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(this.getCurrentFocus()
								.getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
			} catch (Exception e) {
			}
			lunch.setIsCanLongPress(true);
			mMenuLayout.setVisibility(View.VISIBLE);
			mMenuLayout.startAnimation(setPopAnimation());
			ivDis.setVisibility(View.INVISIBLE);
			findViewById(R.id.v_line).setVisibility(View.VISIBLE);
			isShowMenu = true;
			llLeftContent.setVisibility(View.GONE);
			llRightContent.setVisibility(View.GONE);
			llRightSet.setVisibility(View.VISIBLE);
			tvTitle.setText(getString(R.string.Menu_Title));
		} else {
			mMenuLayout.startAnimation(setSmallPopAnimation());
			isShowMenu = false;
		}
	}

	private SeeyonMainMenuLayout layout;

	@Override
	protected void onResume() {
		super.onResume();
		layout.setLayout(lunch);
	}

	@Override
	protected void onPause() {
		if (layout != null) {
			layout.saveLunchState();
		}
		super.onPause();
	}

	@Override
	public final void itemClick(Entity e) {
		switch (e.getType()) {
		case SeeyonMainMenuLayout.C_iMPrivilegeMenu_Add:
			Intent in = new Intent();
			in.setClass(getApplicationContext(), HideFoldorFragment.class);
			startActivity(in);
			break;
		case SeeyonMainMenuLayout.C_iMPrivilegeMenu_Third:
			Intent in1 = new Intent();
			in1.putExtra("url", e.getHttp());
			in1.setClass(getApplicationContext(), MenuThirdHtmlFragment.class);
			startActivity(in1);
			break;
		default:
			break;
		}
	}

	@Override
	public void startLongPress() {
		SharedPreferences share = getSharedPreferences("menu", -1);
		String name = share.getString("user", "");
		rlCenter.setEnabled(false);
		llLeftContent.setVisibility(View.GONE);
		llRightContent.setVisibility(View.GONE);
		llRightSet.setVisibility(View.VISIBLE);
		rl_Hide.setVisibility(View.VISIBLE);
		rl_Hide.setAnimation(setPopAnimation());
	}

	@Override
	public void stopLongPress() {
		// if (isCanChlicNavigation) {
		// rlCenter.setEnabled(true);
		// } else {
		// rlCenter.setEnabled(false);
		// }
		rl_Hide.setAnimation(setSmallPopAnimation1());
	}

	// public void saveLunchData() {
	// layout.saveLunchData();
	// }
	private Animation setPopAnimation() {
		// if (onStatisticalListener != null) {
		// onStatisticalListener.onDown();
		// }
		lunch.setIsCanLongPress(true);
		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0);
		animation.setInterpolator(AnimationUtils.loadInterpolator(activity,
				android.R.anim.accelerate_decelerate_interpolator));
		animation.setDuration(400);
		return animation;
	}

	private Animation setSmallPopAnimation() {
		lunch.setIsCanLongPress(false);
		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
		animation.setDuration(400);
		animation.setInterpolator(AnimationUtils.loadInterpolator(this,
				android.R.anim.accelerate_decelerate_interpolator));
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mMenuLayout.setVisibility(View.GONE);
				// if (onStatisticalListener != null) {
				// onStatisticalListener.onUp();
				// }
				ivDis.setVisibility(View.VISIBLE);
				ivDis.setBackgroundResource(R.drawable.ic_banner_triangle_down);
				llLeftContent.setVisibility(View.VISIBLE);
				llRightContent.setVisibility(View.VISIBLE);
				llRightSet.setVisibility(View.GONE);
				tvTitle.setText(tempString);
				findViewById(R.id.v_line).setVisibility(View.GONE);
				// PoptipDataBase popDataBase = new PoptipDataBase(
				// ActionBarBaseActivity.this);
				// long currUserID = ((M1ApplicationContext) getApplication())
				// .getUserID();
				// String[] userID = { currUserID + "", 1 + "" };
				// PopEntity popEntity = popDataBase
				// .getSignEntityByUserIDAndPopType(userID);
				// if (popEntity == null) {
				// popPrompt(R.layout.view_pot_tip);
				// popEntity = new PopEntity();
				// popEntity.userId = currUserID + "";
				// popEntity.popType = 1;
				// popEntity.hasState = 1;
				// popDataBase.insert(popEntity);
				// }
			}
		});
		return animation;
	}

	private Animation setSmallPopAnimation1() {
		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
		animation.setDuration(400);
		animation.setInterpolator(AnimationUtils.loadInterpolator(this,
				android.R.anim.accelerate_decelerate_interpolator));
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				rl_Hide.setVisibility(View.GONE);
			}
		});
		return animation;
	}
}
