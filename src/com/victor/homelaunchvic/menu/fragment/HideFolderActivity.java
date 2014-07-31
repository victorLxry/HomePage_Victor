package com.victor.homelaunchvic.menu.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.victor.homelaunchvic.R;

public class HideFolderActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(getLayoutId("activity_common"));
		FragmentTransaction ftransaction = getSupportFragmentManager()
				.beginTransaction();
		ViewInfo vi = MultiVersionController.getViewInfo(
				MenuThirdHtmlFragment.class.getName(), null);
		MenuThirdHtmlFragment ll = (MenuThirdHtmlFragment) viewGenerator.getView(vi);
		ftransaction.replace(R.id.fl_activity_content, ll);
		ftransaction.commit();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LogM.i("HideFolderActivity+++onResume");
	}
}
