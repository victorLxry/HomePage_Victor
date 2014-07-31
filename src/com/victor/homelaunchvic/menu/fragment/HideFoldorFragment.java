package com.victor.homelaunchvic.menu.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.victor.homelaunchvic.R;
import com.victor.homelaunchvic.menu.db.DateBaseUtils;
import com.victor.homelaunchvic.menu.db.LunchDataBaseAdapter;
import com.victor.homelaunchvic.menu.view.Entity;
import com.victor.homelaunchvic.menu.view.MenuHideView;
import com.victor.homelaunchvic.menu.view.SeeyonMainMenuLayout;
import com.victor.homelaunchvic.menu.view.UnableCheckBox;
import com.victor.homelaunchvic.utils.JSONUtil;
import com.victor.homelaunchvic.utils.TArrayListAdapter;
import com.victor.homelaunchvic.utils.TArrayListAdapter.IOnDrawViewEx;
import com.victor.homelaunchvic.utils.ViewGropMap;

public class HideFoldorFragment extends Activity {
	// private View v;
	// private SaBaseExpandableListView listview;
	// private MenuExpandableListAdapter baseAdapter;
	private Map<String, Entity> map = new HashMap<String, Entity>();
	// private M1ActionBar bar;
	private LinearLayout llHideviewContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_menu_hidefolder);
		// v = inflater.inflate(R.layout.fragment_menu_hidefolder, null);
		initLayout();
		// listview = (SaBaseExpandableListView) v.findViewById(R.id.lv_menu);
		// baseAdapter = new MenuExpandableListAdapter(baseActivity);
		// listview.setAdapter(baseAdapter);
		// bar = ((ActionBarBaseActivity) getActivity()).getM1Bar();
		// bar.setHeadTextViewContent(getString(R.string.Menu_hide));
		// bar.cleanAllView();
		Button btn = (Button) findViewById(R.id.btn_hide);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (String key : map.keySet()) {
					Entity en = map.get(key);
					if (!en.isBusiness()
							&& en.getFrom() != Entity.C_iMenuFrom_third) {
						for (Entity e1 : sqlUpdataList) {
							if (e1.getSourceID() == en.getSourceID()) {
								e1.setHide(false);
							}
						}
					} else {
						sqlUpdataList.add(sqlUpdataList.size() - 1, en);
					}
				}
				baseFalgList = null;
				businessFalgList = null;
				thirdFalgList = null;
				// bgAdapter.clear();
				 thirdAdapter.clear();
				moduleAdapter.clear();
				DateBaseUtils.saveLunchState(HideFoldorFragment.this,
						sqlUpdataList);
				// bar.reFreshLunchLayout();
				// bar.showNavigation(false);
				// bar.showManu();
				finish();
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		initDate();

	}

	private TArrayListAdapter<Entity> moduleAdapter;
	// private TArrayListAdapter<MBGMenu> bgAdapter;
	private TArrayListAdapter<Entity> thirdAdapter;

	private void initLayout() {
		llHideviewContent = (LinearLayout) findViewById(R.id.ll_hideview_content);
		baseHideView = new MenuHideView(this);
		llHideviewContent.addView(baseHideView, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		bgHideView = new MenuHideView(this);
		bgHideView.setHide();
		llHideviewContent.addView(bgHideView, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		thirdHideView = new MenuHideView(this);
		thirdHideView.setHide();
		thirdHideView.setVisibility(View.GONE);
		llHideviewContent.addView(thirdHideView, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		moduleAdapter = new TArrayListAdapter<Entity>(this);
		moduleAdapter.setDrawViewEx(new IOnDrawViewEx<Entity>() {

			@Override
			public void OnDrawViewEx(Context aContext,
					final Entity templateItem, ViewGropMap view,
					final int aIndex) {
				((TextView) view.getView(R.id.tv_menu_hide))
						.setText(templateItem.getName());
				final UnableCheckBox cb = (UnableCheckBox) view
						.getView(R.id.cb_menu_hide);
				((ImageView) view.getView(R.id.iv_menu_photo))
						.setImageResource(templateItem.getId());
				if (baseFalgList[aIndex]) {
					cb.setChecked(true);
				} else {
					cb.setChecked(false);
				}
				((LinearLayout) view.getView(R.id.ll_menu_hide))
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (cb.isChecked()) {
									baseFalgList[aIndex] = false;
									cb.setChecked(false);
									templateItem.setHide(true);
									map.remove(templateItem.getName());
								} else {
									baseFalgList[aIndex] = true;
									cb.setChecked(true);
									templateItem.setHide(false);
									map.put(templateItem.getName(),
											templateItem);
								}

							}
						});
			}
		});
		moduleAdapter.setLayout(R.layout.view_menu_hideitem);
		baseHideView.setAdapter(getString(R.string.Menu_hide_baseFunction),
				moduleAdapter);

		// bgAdapter = new TArrayListAdapter<MBGMenu>(baseActivity);
		// bgAdapter.setLayout(R.layout.view_menu_hideitem);
		// bgAdapter.setDrawViewEx(new IOnDrawViewEx<MBGMenu>() {
		//
		// @Override
		// public void OnDrawViewEx(Context aContext,
		// final MBGMenu templateItem, ViewGropMap view,
		// final int aIndex) {
		// ((TextView) view.getView(R.id.tv_menu_hide))
		// .setText(templateItem.getName());
		// final UnableCheckBox cb = (UnableCheckBox) view
		// .getView(R.id.cb_menu_hide);
		// ((ImageView) view.getView(R.id.iv_menu_photo))
		// .setImageResource(R.drawable.ic_business);
		// if (businessFalgList[aIndex]) {
		// cb.setChecked(true);
		// } else {
		// cb.setChecked(false);
		// }
		// ((LinearLayout) view.getView(R.id.ll_menu_hide))
		// .setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if (cb.isChecked()) {
		// businessFalgList[aIndex] = false;
		// cb.setChecked(false);
		// map.remove(templateItem.getName());
		// } else {
		// businessFalgList[aIndex] = true;
		// cb.setChecked(true);
		// Entity en = new Entity();
		// en.setType(SeeyonMainMenuLayout.C_iMPrivilegeMenu_Business);
		// en.setBusiness(true);
		// en.setName(templateItem.getName());
		// en.setSourceID(templateItem.getMenuID());
		// map.put(en.getName(), en);
		// }
		// }
		// });
		// }
		// });
		// bgHideView
		// .setAdapter(getString(R.string.Menu_hide_business), bgAdapter);
		//
		thirdAdapter = new TArrayListAdapter<Entity>(this);
		thirdAdapter.setLayout(R.layout.view_menu_hideitem);
		thirdAdapter.setDrawViewEx(new IOnDrawViewEx<Entity>() {
			//
			@Override
			public void OnDrawViewEx(Context aContext,
					final Entity templateItem, ViewGropMap view,
					final int aIndex) {
				((TextView) view.getView(R.id.tv_menu_hide))
						.setText(templateItem.getName());
				final UnableCheckBox cb = (UnableCheckBox) view
						.getView(R.id.cb_menu_hide);
				((ImageView) view.getView(R.id.iv_menu_photo))
						.setImageResource(templateItem.getId());
				if (thirdFalgList[aIndex]) {
					cb.setChecked(true);
				} else {
					cb.setChecked(false);
				}
				((LinearLayout) view.getView(R.id.ll_menu_hide))
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (cb.isChecked()) {
									thirdFalgList[aIndex] = false;
									cb.setChecked(false);
									templateItem.setHide(true);
									map.remove(templateItem.getName());
								} else {
									thirdFalgList[aIndex] = true;
									cb.setChecked(true);
									templateItem.setHide(false);
									map.put(templateItem.getName(),
											templateItem);
								}

							}
						});
			}
		});
		thirdHideView.setAdapter(getString(R.string.Menu_hide_third),
				thirdAdapter);
	}

	private LunchDataBaseAdapter lunchDB;
	private List<Entity> sqlUpdataList;

	private void initDate() {
		// bar.showNavigation(true);
		map.clear();
		if (lunchDB == null) {
			lunchDB = new LunchDataBaseAdapter(this);
		}
		lunchDB.open();
		// BaseActivity activity = (BaseActivity) baseActivity;
		// MOrgMember me = ((M1ApplicationContext) activity.getApplication())
		// .getCurrMember();
		// if (me != null) {
		Cursor cursor = lunchDB.select(198702280216l, 1212);
		if (cursor.moveToFirst()) {

			String arr = cursor.getString(cursor
					.getColumnIndex(LunchDataBaseAdapter.COLUMN_Date));
			// LogM.i("menu=" + arr);
			// try {
			sqlUpdataList = JSONUtil.parseJSONString(arr,
					new TypeReference<List<Entity>>() {
					});
			// } catch (M1Exception e1) {
			// e1.printStackTrace();
			// }
		}
		cursor.close();
		lunchDB.close();
		// }
		initListView();
	}

	private boolean[] baseFalgList;
	private boolean[] businessFalgList;
	private boolean[] thirdFalgList;

	private void initListView() {

		final View pb = findViewById(R.id.pb_menu);
		pb.setVisibility(View.VISIBLE);
		// baseAdapter.cleanAll();
		moduleAdapter.clear();
		moduleAdapter.addListData(getHidenModulList());
		if (baseFalgList == null
				|| baseFalgList.length != moduleAdapter.getCount())
			baseFalgList = new boolean[moduleAdapter.getCount()];

		if (moduleAdapter.getCount() != 0) {
			baseHideView.setVisibility(View.VISIBLE);
			moduleAdapter.notifyDataSetChanged();
		} else {
			if (baseHideView != null) {
				baseHideView.setVisibility(View.GONE);
			}
		}

		thirdAdapter.clear();
		thirdAdapter.addListData(getHidenThirdList(getWebModuleList()));
		if (thirdFalgList == null
				|| thirdFalgList.length != thirdAdapter.getCount())
			thirdFalgList = new boolean[thirdAdapter.getCount()];

		if (thirdAdapter.getCount() != 0) {
			thirdHideView.setVisibility(View.VISIBLE);
			thirdAdapter.notifyDataSetChanged();
		} else {
			if (thirdHideView != null) {
				thirdHideView.setVisibility(View.GONE);
			}
		}

		// baseAdapter.addSection(getString(R.string.Menu_hide_baseFunction),
		// moduleAdapter);

		// if ("5.1.0".compareTo(HttpConfigration.getC_sServerversion()) > 0) {
		pb.setVisibility(View.GONE);
		// listview.ExpAllGrop();
		// baseAdapter.NotifyDataChange();
		// return;
		// }
		// Object[] loginParms = new Object[3];
		// loginParms[0] = baseActivity;
		// loginParms[1] = 0;
		// loginParms[2] = 200;
		// MethodInvokeInfo aa = MultiVersionController.getMethodInvokeInfo(
		// MBGBiz.class, "getMenuList", null);
		// baseActivity.execute_asy(aa, loginParms,
		// new BizExecuteListener<MPageData<MBGMenu>>(baseActivity) {
		// @Override
		// public void sucess(MPageData<MBGMenu> result) {
		// pb.setVisibility(View.GONE);
		// if (result == null || result.getDataList() == null) {
		// baseActivity.sendNotifacationBroad("数据为空");
		// bgHideView.setVisibility(View.GONE);
		// return;
		// }
		// bgHideView.setVisibility(View.VISIBLE);
		// bgAdapter.clear();
		// bgAdapter.addListData(getHidenBusinessList(result
		// .getDataList()));
		// if (businessFalgList == null
		// || businessFalgList.length != bgAdapter
		// .getCount())
		// businessFalgList = new boolean[bgAdapter.getCount()];
		//
		// if (bgAdapter.getCount() != 0) {
		// bgHideView.setVisibility(View.VISIBLE);
		// bgAdapter.notifyDataSetChanged();
		//
		// } else {
		// bgHideView.setVisibility(View.GONE);
		// }
		//
		// // if (bgAdapter.getListData().size() != 0
		// // && "5.1.0".compareTo(HttpConfigration
		// // .getC_sServerversion()) <= 0){
		// //
		// // }
		// // baseAdapter.addSection(
		// // getString(R.string.Menu_hide_business),
		// // bgAdapter);
		// // if (bgAdapter.getListData().size() != 0
		// // && moduleAdapter.getListData().size() != 0)
		// // listview.expandGroup(0);
		// // baseAdapter.NotifyDataChange();
		// initThirdSoftware(pb, moduleAdapter);
		//
		// }
		// });
	}

	// @SuppressWarnings("unchecked")
	// private void initThirdSoftware(final View pb,
	// final TArrayListAdapter<Entity> moduleAdapter) {
	// Object[] loginParms = new Object[2];
	// loginParms[0] = baseActivity;
	// loginParms[1] = new HashMap<String, Object>();
	// ((HashMap<String, Object>) loginParms[1]).put(
	// MCommonKeyConstant.C_sPagination_Number, -1);
	// ((HashMap<String, Object>) loginParms[1]).put(
	// MCommonKeyConstant.C_sPagination_Size, -1);
	// ((HashMap<String, Object>) loginParms[1]).put(
	// MCmpAppInfoKeyConstants.C_sClientType,
	// MCmpAppInfoKeyConstants.C_sClientType_android);
	// ((HashMap<String, Object>) loginParms[1]).put(
	// MCmpAppInfoKeyConstants.C_sOperation_version, "4.0.4");
	//
	// MethodInvokeInfo aa = MultiVersionController.getMethodInvokeInfo(
	// MCmpAppInfoBiz.class, "getAppInfoList", null);
	// baseActivity.execute_asy(aa, loginParms,
	// new BizExecuteListener<MPageData<MCmpAppInfoListItem>>(
	// baseActivity) {
	// @Override
	// public void sucess(MPageData<MCmpAppInfoListItem> result) {
	// // pb.setVisibility(View.GONE);
	// if (result == null || result.getDataList() == null) {
	// baseActivity.sendNotifacationBroad("数据为空");
	// thirdHideView.setVisibility(View.GONE);
	// return;
	// }
	// thirdHideView.setVisibility(View.VISIBLE);
	// thirdAdapter.clear();
	// thirdAdapter.addListData(getHidenThirdList(result
	// .getDataList()));
	// if (thirdFalgList == null
	// || thirdFalgList.length != thirdAdapter
	// .getCount())
	// thirdFalgList = new boolean[thirdAdapter.getCount()];
	//
	// if (thirdAdapter.getCount() != 0) {
	// thirdHideView.setVisibility(View.VISIBLE);
	// thirdAdapter.notifyDataSetChanged();
	//
	// } else {
	// thirdHideView.setVisibility(View.GONE);
	// }
	//
	// // if (bgAdapter.getListData().size() != 0
	// // && "5.1.0".compareTo(HttpConfigration
	// // .getC_sServerversion()) <= 0)
	// // baseAdapter.addSection(
	// // getString(R.string.Menu_hide_third),
	// // bgAdapter);
	// // // listview.ExpAllGrop();
	// // if (bgAdapter.getListData().size() != 0
	// // && moduleAdapter.getListData().size() != 0)
	// // listview.expandGroup(0);
	// // baseAdapter.NotifyDataChange();
	//
	// }
	//
	// @Override
	// public void error(M1Exception e) {
	// // TODO Auto-generated method stub
	// // super.error(e);
	// thirdHideView.setVisibility(View.GONE);
	// }
	// });
	//
	// }

	private MenuHideView baseHideView;
	private MenuHideView bgHideView;
	private MenuHideView thirdHideView;

	private List<Entity> getHidenModulList() {
		List<Entity> hidenList = new ArrayList<Entity>();
		for (Entity e : sqlUpdataList) {
			if (e.isHide() && !e.isBusiness()
					&& e.getFrom() != Entity.C_iMenuFrom_third) {
				hidenList.add(e);
			}
		}
		return hidenList;
	}

	// thirdFalgList[aIndex] = true;
	// cb.setChecked(true);
	// Entity en = new Entity();
	// en.setType(SeeyonMainMenuLayout.C_iMPrivilegeMenu_Third);
	// en.setFrom(Entity.C_iMenuFrom_third);
	// en.setName(templateItem.getFullname());
	// en.setSourceID(templateItem.getAppID());
	// if (templateItem.getAttrdata().size() != 0) {
	// en.setAtt(templateItem.getAttrdata()
	// .get(0));
	// }
	// en.setDownLoadAddress(templateItem.getDownloadAddrForPhone());
	// en.setAction(templateItem.getInvokeAddrForPhone());
	// en.setHttp(templateItem.getInvokeAddrForWeb());
	// en.setAppType(templateItem.getAppType());
	// map.put(en.getName(), en);
	private String[] webAdress = { "http://www.baidu.com",
			"http://www.sina.com", "http://www.taobao.com" };
	private String[] webName = { "AAA", "BBB", "CCC" };
	private long[] webID = { 12001l, 12002l, 12003l };

	private List<Entity> getWebModuleList() {
		List<Entity> webList = new ArrayList<Entity>();
		for (int i = 0; i < webAdress.length; i++) {
			Entity en = new Entity();
			en.setType(SeeyonMainMenuLayout.C_iMPrivilegeMenu_Third);
			en.setFrom(Entity.C_iMenuFrom_third);
			en.setName(webName[i]);
			en.setSourceID(webID[i]);
			en.setHttp(webAdress[i]);
			en.setAppType(Entity.C_iMenuThird_Html5);
			webList.add(en);
		}
		return webList;
	}

	// private List<MBGMenu> getHidenBusinessList(List<MBGMenu> initList) {
	// List<MBGMenu> hidenList = new ArrayList<MBGMenu>();
	// for (int i = 0; i < initList.size(); i++) {
	// for (int j = 0; j < sqlUpdataList.size(); j++) {
	// if (sqlUpdataList.get(j).isBusiness()) {
	// if (sqlUpdataList.get(j).getSourceID() == initList.get(i)
	// .getMenuID()) {
	// hidenList.add(initList.get(i));
	// }
	// }
	// }
	// }
	// initList.removeAll(hidenList);
	// return initList;
	// }

	private List<Entity> getHidenThirdList(
			List<Entity> initList) {
		List<Entity> hidenList = new ArrayList<Entity>();
		for (int i = 0; i < initList.size(); i++) {
			for (int j = 0; j < sqlUpdataList.size(); j++) {
				if (sqlUpdataList.get(j).getFrom() == Entity.C_iMenuFrom_third) {
					if (sqlUpdataList.get(j).getSourceID() == initList.get(i)
							.getSourceID()) {
						hidenList.add(initList.get(i));
					}
				}
			}
		}
		initList.removeAll(hidenList);
		return initList;
	}

	// public static void returnBitMap(final AsyncImageView im,
	// final MAttachment att) {
	// if (att == null)
	// return;
	// MMemberIcon icon = new MMemberIcon();
	// icon.setIconPath(att.getAttID() + "");
	// icon.setIconType(2);
	// icon.setLastModifyDate(TransitionDate.DateToStr(att.getModifyTime(),
	// "yyyy-MM-dd HH:mm"));
	// icon.setSize(att.getSize());
	// im.setHandlerInfo(att.getAttID() + "", icon);
	// }
}
