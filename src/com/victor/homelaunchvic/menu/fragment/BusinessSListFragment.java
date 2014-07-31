package com.seeyon.mobile.android.model.common.menu.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.seeyon.apps.m1.bg.parameters.MBGKeyConstant;
import com.seeyon.apps.m1.bg.vo.MBGChildMenu;
import com.seeyon.apps.m1.bg.vo.MBGMenu;
import com.seeyon.apps.m1.common.vo.MPageData;
import com.seeyon.m1.base.error.M1Exception;
import com.seeyon.m1.multiversion.biz.BizExecuteListener;
import com.seeyon.m1.multiversion.controller.MultiVersionController;
import com.seeyon.m1.multiversion.controller.entity.MethodInvokeInfo;
import com.seeyon.m1.utils.json.JSONUtil;
import com.seeyon.mobile.android.R;
import com.seeyon.mobile.android.biz.bg.MBGBiz;
import com.seeyon.mobile.android.model.base.ActionBarBaseActivity;
import com.seeyon.mobile.android.model.base.ActionBarBaseActivity.M1ActionBar;
import com.seeyon.mobile.android.model.base.BaseFragment;
import com.seeyon.mobile.android.model.business.BusinessShowActivity;
import com.seeyon.mobile.android.model.business.fragment.BusinessNoflowList2;
import com.seeyon.mobile.android.model.common.adapter.ArrayListAdapter;
import com.seeyon.mobile.android.model.flow.fragment.FlowListFragment;
import com.seeyon.mobile.android.model.template.TemplateShowActivity;

public class BusinessSListFragment extends BaseFragment {

	public static final int C_iBusinessList_NewFlowCol = 111;
	public static final String C_iBusinessList_MenuIdKEY = "meunIDKEY";
	public static final String C_iBusinessList_MenuNameKEY = "meunNameKEY";
	private ListView exList;
	private View allView;
	private boolean isShowB = true;
	private MBGMenu mgData = null;
	private ProgressBar progress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		allView = inflater.inflate(R.layout.fragment_business_slist, null);
		exList = (ListView) allView.findViewById(R.id.exlv_business_list);
		progress = (ProgressBar) allView.findViewById(R.id.pb_business_loading);

		M1ActionBar m1Bar = ((ActionBarBaseActivity) baseActivity).getM1Bar();
		m1Bar.cleanAllView();
		m1Bar.setHeadTextViewContent(getString(R.string.Menu_BG));
		return allView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	}
	
	private void initData() {
		M1ActionBar m1Bar = ((ActionBarBaseActivity) baseActivity).getM1Bar();
		m1Bar.cleanAllView();
		m1Bar.setHeadTextViewContent(m1Bar.getCurrentMenuEntity().getName());
		
		Object[] loginParms = new Object[2];
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(
				MBGKeyConstant.C_lBG_MenuID,
				m1Bar.getCurrentMenuEntity().getSourceID());
		loginParms[0] = baseActivity;
		loginParms[1] = params;
		MethodInvokeInfo aa = MultiVersionController.getMethodInvokeInfo(
				MBGBiz.class, "getMenuByID", null);
		baseActivity.execute_asy(aa, loginParms,
				new BizExecuteListener<MBGMenu>(baseActivity) {
					@Override
					public void sucess(MBGMenu result) {
						if (result == null) {
							baseActivity.sendNotifacationBroad("数据为空");
							return;
						}
						mgData = result;
						MBGMenu item = result;
						ChildAdapter itemAdapter = new ChildAdapter(
								baseActivity);
						itemAdapter.addListData(item.getChildMenuList());
						exList.setAdapter(itemAdapter);

						exList.setVisibility(View.VISIBLE);
						progress.setVisibility(View.GONE);
					}
				});

	}

	/**
	 * 组列表的适配器
	 * 
	 * @author wangxk
	 * 
	 */

	/**
	 * 子列表的 适配器
	 * 
	 * @author wangxk
	 * 
	 */
	public class ChildAdapter extends ArrayListAdapter<MBGChildMenu> {

		public ChildAdapter(Context context) {
			super(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			ViewNameHolder holder;
			if (row == null) {
				row = mInflater.inflate(R.layout.view_business_fristitem,
						parent, false);
				holder = new ViewNameHolder();
				holder.v = row;
				holder.tvName = (TextView) row
						.findViewById(R.id.tv_business_firstlist_title);
				holder.imgFlag = (ImageView) row
						.findViewById(R.id.iv_business_firstlist_flag);
				holder.imgType = (ImageView) row
						.findViewById(R.id.iv_business_firstlist_icon);
				holder.mentB = (TextView) row
						.findViewById(R.id.tv_business_fristlist_b);
				holder.mentT = (TextView) row
						.findViewById(R.id.tv_business_fristlist_t);
				row.setTag(holder);
			} else {
				holder = (ViewNameHolder) row.getTag();
				if (holder == null || holder.tvName == null) {
					return row;
				}
			}
			holder.v.setBackgroundColor(getResources().getColor(
					R.color.businsess_sec_bg));
			holder.imgFlag.setVisibility(View.GONE);
			holder.mentB.setVisibility(View.INVISIBLE);
			holder.mentT.setVisibility(View.INVISIBLE);
			if (position == 0) {
				holder.mentT.setVisibility(View.VISIBLE);
			}
			if ((getCount() - 1) == position && isShowB) {
				holder.mentB.setVisibility(View.VISIBLE);
			}

			final MBGChildMenu childMenu = getItem(position);
			holder.tvName.setText(childMenu.getName());
			switch (childMenu.getSourceType()) {
			case MBGChildMenu.C_iSourceType_BasicDataAppBind:// 基础数据应用绑定-无流程
				holder.imgType.setImageResource(R.drawable.ic_menu_basic_data);
				holder.v.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(getActivity(),
								BusinessShowActivity.class);
						intent.putExtra(
								BusinessShowActivity.C_iBusinessShow_TypeKey,
								BusinessShowActivity.C_iBusinessShow_Type_UnFlowList);
						Bundle bundlee = new Bundle();
						bundlee.putLong(
								BusinessNoflowList2.C_sBusinessNoflowList_FormID,
								childMenu.getFormAppMainID());
						bundlee.putInt(
								FlowListFragment.C_sFlowListFragment_ModleType,
								FlowListFragment.C_iFlowListFragment_ModleType_BG);
						bundlee.putLong(
								FlowListFragment.C_sFlowListFragment_TemplateID,
								childMenu.getSourceID());
						bundlee.putString(
								FlowListFragment.C_sFlowListFragment_Title,
								childMenu.getName());
						intent.putExtra("data", bundlee);
						baseActivity.startActivity(intent);
					}
				});

				break;

			case MBGChildMenu.C_iSourceType_InfoMgrAppBind:// 信息管理应用绑定-无流程
				holder.imgType.setImageResource(R.drawable.ic_menu_inf_man);

				holder.v.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(getActivity(),
								BusinessShowActivity.class);
						intent.putExtra(
								BusinessShowActivity.C_iBusinessShow_TypeKey,
								BusinessShowActivity.C_iBusinessShow_Type_UnFlowList);
						Bundle bundlee = new Bundle();
						bundlee.putLong(
								BusinessNoflowList2.C_sBusinessNoflowList_FormID,
								childMenu.getFormAppMainID());
						bundlee.putInt(
								FlowListFragment.C_sFlowListFragment_ModleType,
								FlowListFragment.C_iFlowListFragment_ModleType_BG);
						bundlee.putLong(
								FlowListFragment.C_sFlowListFragment_TemplateID,
								childMenu.getSourceID());
						bundlee.putString(
								FlowListFragment.C_sFlowListFragment_Title,
								childMenu.getName());
						intent.putExtra("data", bundlee);
						baseActivity.startActivity(intent);
					}
				});

				break;

			case MBGChildMenu.C_iSourceType_FlowTemplate:// 流程模版-有流程
				switch (childMenu.getFlowMenuType()) {
				case MBGChildMenu.C_iFlowMenuType_Create:// 有流程 新建
					holder.imgType.setImageResource(R.drawable.ic_menu_new);
					holder.v.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent();
							intent.setClass(baseActivity,
									TemplateShowActivity.class);
							intent.putExtra(
									TemplateShowActivity.C_sTemplate_TemplateID,
									childMenu.getSourceID());
							intent.putExtra(
									TemplateShowActivity.C_sTemplate_AffairID,
									-1);
							intent.putExtra(
									TemplateShowActivity.C_sTemplate_TemplateListItem,
									"");
							intent.putExtra(
									TemplateShowActivity.C_sTemplate_ModleType,
									TemplateShowActivity.C_iTemplate_ModleType_BG);
							baseActivity.startActivityForResult(intent,
									C_iBusinessList_NewFlowCol);
						}
					});

					break;
				case MBGChildMenu.C_iFlowMenuType_List:// 有流程 列表

					holder.imgType.setImageResource(R.drawable.ic_menu_form);

					holder.v.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent();
							intent.setClass(getActivity(),
									BusinessShowActivity.class);
							intent.putExtra(
									BusinessShowActivity.C_iBusinessShow_TypeKey,
									BusinessShowActivity.C_iBusinessShow_Type_FlowList);
							Bundle bundle = new Bundle();
							bundle.putInt(
									FlowListFragment.C_sFlowListFragment_ModleType,
									FlowListFragment.C_iFlowListFragment_ModleType_BG);
							bundle.putLong(
									FlowListFragment.C_sFlowListFragment_TemplateID,
									childMenu.getSourceID());
							bundle.putString(
									FlowListFragment.C_sFlowListFragment_Title,
									childMenu.getName());
							bundle.putBoolean(
									FlowListFragment.C_sFlowListFragment_HasNew,
									isNewOperate(childMenu));
							intent.putExtra("data", bundle);
							baseActivity.startActivity(intent);
						}
					});

					break;
				default:
					break;
				}
				break;

			case MBGChildMenu.C_iSourceType_PublicInfoID:// 公共信息ID
				holder.imgType.setImageResource(R.drawable.ic_menu_announce);// 公告
				break;
			case MBGChildMenu.C_iSourceType_QueryID:// 查询ID
				// holder.imgFlag.setImageResource(resId);
				break;
			case MBGChildMenu.C_iSourceType_ArchiveID:// 文档ID
				holder.imgType.setImageResource(R.drawable.ic_menu_doc);
				break;
			case MBGChildMenu.C_iSourceType_StatisticsID:// 统计ID
				holder.imgType.setImageResource(R.drawable.ic_menu_statistics);
				break;
			case MBGChildMenu.C_iSourceType_MenuItem:
				holder.imgType.setImageResource(R.drawable.ic_forder_48);
				final List<MBGChildMenu> childMenuList = childMenu
						.getChildMenuList();
				if (childMenuList == null) {
					break;
				}
				// holder.imgFlag.setVisibility(View.VISIBLE);
				// holder.imgFlag.setImageResource(R.drawable.ic_progressive_r);
				holder.v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(baseActivity,
								BusinessShowActivity.class);
						intent.putExtra(
								BusinessShowActivity.C_iBusinessShow_TypeKey,
								BusinessShowActivity.C_iBusinessShow_Type_Archive);
						Bundle bundle = new Bundle();
						bundle.putString("title", childMenu.getName());
						String archiveList = "";
						try {
							archiveList = JSONUtil
									.writeEntityToJSONString(childMenuList);
						} catch (M1Exception e) {
							e.printStackTrace();
						}
						bundle.putString("archiveList", archiveList);
						intent.putExtra("data", bundle);
						baseActivity.startActivity(intent);
					}

				});
				break;
			default:
				break;
			}

			return row;
		}
	}

	private boolean isNewOperate(MBGChildMenu childMenu) {
			if (mgData.getChildMenuList() != null) {
				for (MBGChildMenu scItem : mgData.getChildMenuList()) {
					if (childMenu.getSourceType() == MBGChildMenu.C_iSourceType_FlowTemplate
							&& scItem.getFlowMenuType() == MBGChildMenu.C_iFlowMenuType_Create
							&& childMenu.getSourceID() == scItem.getSourceID()) {
						return true;
					}
				}
			}
		return false;

	}



	public static class ViewNameHolder {
		public View v;
		public TextView tvName;
		public ImageView imgType;
		public ImageView imgFlag;
		public TextView mentT;
		public TextView mentB;
	}

}
