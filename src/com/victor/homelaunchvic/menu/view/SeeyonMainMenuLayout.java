package com.victor.homelaunchvic.menu.view;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.victor.homelaunchvic.MainActivity;
import com.victor.homelaunchvic.R;
import com.victor.homelaunchvic.menu.db.LunchDataBaseAdapter;
import com.victor.homelaunchvic.utils.JSONUtil;
import com.victor.homelaunchvic.utils.TArrayListAdapter;
import com.victor.homelaunchvic.utils.TArrayListAdapter.IOnDrawViewEx;
import com.victor.homelaunchvic.utils.ViewGropMap;

public class SeeyonMainMenuLayout {
	private BaseLunchFlipper lunch;
	private LunchDataBaseAdapter lunchDB;
	private Context ctx;
	private boolean isUpdateForServer = true;// 避免在oncreat和onresume的时候调用两次
	public static final int C_iMPrivilegeMenu_Add = 15;
	public static final int C_iMPrivilegeMenu_Business = 129;
	public static final int C_iMPrivilegeMenu_Third = 130;
	private int[] colorArrays = { R.drawable.home_menu0, R.drawable.home_menu1,
			R.drawable.home_menu2,
			R.drawable.home_menu3,
			R.drawable.home_menu4,R.drawable.home_menu5,R.drawable.home_menu6,R.drawable.home_menu7,
			R.drawable.home_menu8,R.drawable.home_menuadd,};

	public SeeyonMainMenuLayout(Context con) {
		ctx = con;
	}

	public void setLayout(BaseLunchFlipper lun) {
		lunch = lun;
		if (isUpdateForServer) {
			isUpdateForServer = false;
			return;
		}
		setLayoutToView(false);
	}

	public void setLayout(BaseLunchFlipper lun, final boolean isUpdateForServer) {
		this.isUpdateForServer = isUpdateForServer;
		lunch = lun;
		setLayoutToView(isUpdateForServer);
	}

	public void setLayoutToView(boolean isUpdateForServer) {
		List<Entity> updateList = getMenuData(isUpdateForServer);
		if (updateList == null || !lunch.isHasUpdate(updateList))
			return;
		TArrayListAdapter<Entity> t = new TArrayListAdapter<Entity>(ctx);
		t.setIsItemBackGround(false);
		t.setLayout(R.layout.view_menu_item);
		t.addListData(updateList);
		t.setDrawViewEx(new IOnDrawViewEx<Entity>() {
			@Override
			public void OnDrawViewEx(Context aContext, Entity templateItem,
					ViewGropMap view, int aIndex) {
				((TextView) view.getView(R.id.tv_modle)).setText(templateItem
						.getName());
				if (templateItem.getFrom() == Entity.C_iMenuFrom_third) {
//					HideFoldorFragment.returnBitMap(
//							((AsyncImageView) view.getView(R.id.ItemImage)),
//							templateItem.getAtt());
				} else
					((ImageView) view.getView(R.id.ItemImage))
							.setImageResource(templateItem.getId());
				if (templateItem.getCount() != -1
						&& templateItem.getCount() != 0) {
					TextView tvCount = (TextView) view
							.getView(R.id.tv_typeCounte);
					tvCount.setVisibility(View.VISIBLE);
					tvCount.setText(""
							+ (templateItem.getCount() > 999 ? "999+"
									: templateItem.getCount()) + "");
				}
				View ivNew = view.getView(R.id.iv_newmessage);
				if (templateItem.isNews()) {
					ivNew.setVisibility(View.VISIBLE);
				} else {
					ivNew.setVisibility(View.GONE);
				}
			}
		});
		lunch.setAdapter(t);
		saveLunchState();
	}

	/**
	 * 根据数据保存信息
	 */
	public void saveLunchData() {
		if (lunch == null)
			return;
		long useID = 198702280216l;
		List<Entity> initUpdataList = getInitMenuData();
		List<Entity> sqlUpdataList = getSqlData();
		List<Entity> array = bingInitdataAndSqldata(initUpdataList,
				sqlUpdataList);
		if (array.size() == 0)// 在打开一个新页面而且没有展开菜单的情况下，array的size==0，此时不用保留数据
			return;
		lunchDB.open();
		Cursor cursor = lunchDB.select(useID, 1212);

			String update = JSONUtil.writeEntityToJSONString(array);
			if (!cursor.moveToFirst()) {
				lunchDB.insert(useID, update,1212);
			} else {
				lunchDB.update(update, useID,1212);
			}

		cursor.close();
		lunchDB.close();

	}

	private boolean isCanSave = true;

	public void IsCanSaveLunchState(boolean is) {
		isCanSave = is;
	}

	/**
	 * 根据页面的值保存信息
	 */
	public void saveLunchState() {
		// if (lunch == null || !isCanSave)
		// return;
		// BaseActivity activity = (BaseActivity) lunch.getContext();
		// MOrgMember me = ((M1ApplicationContext) activity.getApplication())
		// .getCurrMember();
		// if (me == null)
		// return;
		// long useID = me.getOrgID();
		// List<Entity> array = lunch.getArrayName();
		// if (array.size() == 0)// 在打开一个新页面而且没有展开菜单的情况下，array的size==0，此时不用保留数据
		// return;
		// lunchDB.open();
		// Cursor cursor = lunchDB.select(useID, me.getAccount().getOrgID());
		//
		// try {
		// String update = JSONUtil.writeEntityToJSONString(array);
		// if (!cursor.moveToFirst()) {
		// lunchDB.insert(useID, update, me.getAccount().getOrgID());
		// } else {
		// lunchDB.update(update, useID, me.getAccount().getOrgID());
		// }
		// } catch (M1Exception e) {
		// e.printStackTrace();
		// }

		// cursor.close();
		// lunchDB.close();
	}

	public void setItemCount(Entity entity) {
		lunch.setItemCount(entity);
		lunch.saveLunchState();
	}

	private List<Entity> getInitList(List<Entity> list) {
		for (int a = 0; a < list.size(); a++) {
			if (list.get(a).getType() == 0) {
				list.get(a).setId(R.drawable.ic_message);
				list.get(a).setSourceID(R.drawable.ic_message);
				list.get(a).setName(ctx.getString(R.string.Menu_Notification));
			} else if (list.get(a).getType() == 1) {
				list.get(a).setId(R.drawable.ic_collaborative);
				list.get(a).setSourceID(R.drawable.ic_collaborative);
				list.get(a).setName(ctx.getString(R.string.Menu_Collaboration));
			} else if (list.get(a).getType() == 2) {
				list.get(a).setId(R.drawable.ic_off_doc);
				list.get(a).setSourceID(R.drawable.ic_off_doc);
				list.get(a).setName(ctx.getString(R.string.Menu_EDoc));
			} else if (list.get(a).getType() == 3) {
				list.get(a).setId(R.drawable.ic_announcement);
				list.get(a).setSourceID(R.drawable.ic_announcement);
				list.get(a).setName(ctx.getString(R.string.Menu_Bulletin));
			} else if (list.get(a).getType() == 4) {
				list.get(a).setId(R.drawable.ic_news);
				list.get(a).setSourceID(R.drawable.ic_news);
				list.get(a).setName(ctx.getString(R.string.Menu_News));
			} else if (list.get(a).getType() == 5) {
				list.get(a).setId(R.drawable.ic_doc);
				list.get(a).setSourceID(R.drawable.ic_doc);
				list.get(a).setName(ctx.getString(R.string.Menu_Doc));
			} else if (list.get(a).getType() == 6) {
				list.get(a).setId(R.drawable.ic_address_book);
				list.get(a).setSourceID(R.drawable.ic_address_book);
				list.get(a).setName(ctx.getString(R.string.Menu_AddressBook));
			} else if (list.get(a).getType() == 7) {
				list.get(a).setId(R.drawable.ic_statistics);
				list.get(a).setSourceID(R.drawable.ic_statistics);
				list.get(a).setName(ctx.getString(R.string.Menu_Statistics));
			} else if (list.get(a).getType() == 8) {
				list.get(a).setId(R.drawable.ic_schedule);
				list.get(a).setSourceID(R.drawable.ic_schedule);
				list.get(a).setName(ctx.getString(R.string.Menu_Schedule));
			} else if (list.get(a).getType() == 9) {
				list.get(a).setId(R.drawable.ic_mmetting);
				list.get(a).setSourceID(R.drawable.ic_mmetting);
				list.get(a).setName(ctx.getString(R.string.Menu_Meeting));
			} else if (list.get(a).getType() == 10) {
				list.get(a).setId(R.drawable.ic_register);
				list.get(a).setSourceID(R.drawable.ic_register);
				list.get(a).setName(ctx.getString(R.string.Menu_LBS));
			} else if (list.get(a).getType() == 11) {
				list.get(a).setId(R.drawable.ic_im);
				list.get(a).setSourceID(R.drawable.ic_im);
				list.get(a).setName(ctx.getString(R.string.Menu_UC));
			} else if (list.get(a).getType() == 12) {
				list.get(a).setId(R.drawable.ic_business_control);
				list.get(a).setSourceID(R.drawable.ic_business_control);
				list.get(a).setName(ctx.getString(R.string.Menu_BG));
			} else if (list.get(a).getType() == 13) {
				list.get(a).setId(R.drawable.ic_offline_file);
				list.get(a).setSourceID(R.drawable.ic_offline_file);
				list.get(a).setName(ctx.getString(R.string.Menu_Offline));
			} else if (list.get(a).getType() == C_iMPrivilegeMenu_Add) {
				list.get(a).setId(R.drawable.ic_add);
				list.get(a).setSourceID(R.drawable.ic_add);
				list.get(a).setName("");
			} else if (list.get(a).getType() == C_iMPrivilegeMenu_Business) {
				list.get(a).setId(R.drawable.ic_business);// 此处业务管理已经设置了sourceID，不用设置，否则出错
			} else if (list.get(a).getType() == C_iMPrivilegeMenu_Third) {
				list.get(a).setId(R.drawable.ic_set);
			} else if (list.get(a).getType() == 14) {
				list.get(a).setId(R.drawable.ic_mmetting_cmp);
				list.get(a).setSourceID(R.drawable.ic_mmetting_cmp);
				list.get(a).setName(ctx.getString(R.string.Menu_Report));
			} else if (list.get(a).getType() == 1001) {
				list.get(a).setId(R.drawable.ic_mmetting_cmp);
				list.get(a).setSourceID(R.drawable.ic_mmetting_cmp);
				list.get(a).setName(list.get(a).getName());
			}
		}
		return list;
	}

	private List<Entity> getInitMenuData() {

		
		
		List<Entity> list = new ArrayList<Entity>();
		for(int i=0;i<16;i++){
			Entity e=new Entity();
			e.setType(i);
			list.add(e);
		}
//		RBACControlService controlService = (RBACControlService) M1ApplicationContext
//				.getInstance().getM1Service(M1ApplicationContext.RBAC_SERVICE);
//		MList<MPrivilegeResource> mList = controlService
//				.getPrivilegeResourceMList();
//		if (mList == null) {
//			return null;
//		}
//
//		List<MPrivilegeResource> rList = mList.getValue();
//
//		for (MPrivilegeResource res : rList) {
//			if (res.isHasPermissions()) {
//				if (res.getResourceType() == MPrivilegeConstant.C_iMPrivilegeMenu_Message) {
//					lunch.getParentLayout()
//							.findViewById(R.id.rl_menu_notification)
//							.setVisibility(View.VISIBLE);
//					TextView tvCount = (TextView) lunch.getParentLayout()
//							.findViewById(R.id.tv_typeCounte);
//					if (NotificationService.notificationCount != 0) {
//						tvCount.setVisibility(View.VISIBLE);
//						tvCount.setText((NotificationService.notificationCount > 999 ? "999+"
//								: NotificationService.notificationCount)
//								+ "");
//					} else {
//						tvCount.setVisibility(View.INVISIBLE);
//					}
//				} else if (res.getResourceType() == MPrivilegeConstant.C_iMPrivilegeMenu_IM) {
//					lunch.getParentLayout().findViewById(R.id.rl_menu_uc)
//							.setVisibility(View.VISIBLE);
//				} else if (res.getResourceType() == MPrivilegeConstant.C_iMPrivilegeMenu_Meeting) {// 如果为会议模块需要判断是否有待开，和已开列表
//					List<MPrivilegeResource> listc = res
//							.getChildMPrivilegeResource();
//					if (listc != null) {
//						boolean hasMeetting = false;
//						for (MPrivilegeResource ic : listc) {
//							if (ic.isHasPermissions()) {
//								hasMeetting = true;
//							}
//						}
//						if (hasMeetting) {
//							Entity entity = new Entity();
//							entity.setName(res.getResourceName());
//							entity.setType(res.getResourceType());
//							list.add(entity);
//						}
//					}
//
//				} else {
//					if (res.getResourceType() != MPrivilegeConstant.C_iMPrivilegeMenu_SetUp
//							&& res.getResourceType() != 1001) {
//						Entity entity = new Entity();
//						entity.setName(res.getResourceName());
//						entity.setType(res.getResourceType());
//						list.add(entity);
//					}
//
//				}
//			}
//		}
//
//		addOffline2List(list);
//		/**
//		 * 添加九宫格 报表
//		 */
//		// addReport2List(list);
//		Log.i("taggg", "setcasdf="+HttpConfigration.C_sServerversion);
//		if ("5.1.0".compareTo(HttpConfigration.C_sServerversion) <= 0) {
//			Entity addEntity = new Entity();
//			addEntity.setName("");
//			addEntity.setType(C_iMPrivilegeMenu_Add);
//			list.add(addEntity);
//		}
		return list;
	}

	private Handler handler = new Handler();
	private Runnable inutRun = new Runnable() {
		@Override
		public void run() {
			setLayout(lunch, true);
		}
	};

	private List<Entity> getMenuData(boolean isUpdateForServer) {
		List<Entity> updateList = null;
		List<Entity> initUpdataList = getInitMenuData();
		List<Entity> sqlUpdataList = getSqlData();
		if ((initUpdataList == null || initUpdataList.size() == 0)
				&& (MainActivity.class.isInstance(ctx))) {
			String text = "RBAC没有获取到,";
			if (initUpdataList == null)
				text += "是null";
			else
				text += "size=0";

			Dialog dialog = new Dialog(lunch.getContext());
			dialog.setTitle(text);
			dialog.show();
			// if (isUpdateForServer) {
			// handler.postDelayed(inutRun, 1000);
			// }
//			((BaseActivity) ctx).sendNotifacationBroad("rbac权限获取出错，请重新登录");
//			((BaseActivity) ctx).finish();
		}
		if (initUpdataList == null && sqlUpdataList == null) {
			return null;
		} else {
			if (sqlUpdataList == null
					|| (isUpdateForServer )) {
				if (sqlUpdataList == null)
					updateList = initUpdataList;
				else {
					updateList = bingInitdataAndSqldata(initUpdataList,
							sqlUpdataList);
				}
			} else {
				updateList = sqlUpdataList;
			}
		}
		if (updateList == null)
			return null;
		updateList = getInitList(updateList);
		for (int i = 0; i < updateList.size(); i++) {
			Entity entity = updateList.get(i);
			// for (int j = i + 1; j < updateList.size(); j++) {
			// if (entity.getId() == updateList.get(j).getId()) {
			// updateList.remove(j);
			// }
			// }
			// if (entity.getType() ==
			// MPrivilegeConstant.C_iMPrivilegeMenu_SetUp) {
			// updateList.remove(i);
			// }
			if (i == updateList.size() - 1&&entity.getType()==C_iMPrivilegeMenu_Add)
				updateList.get(i).setColor(R.drawable.home_menuadd);
			else {
				if (entity.getColor() == 0)
					entity.setColor(
							colorArrays[i % 9]);
			}

		}
		return updateList;
	}

	private List<Entity> getSqlData() {
		List<Entity> sqlUpdataList = null;
		if (lunchDB == null) {
			lunchDB = new LunchDataBaseAdapter(ctx);
		}
			lunchDB.open();
			Cursor cursor = lunchDB.select(198702280216l, 1212);
			if (cursor.moveToFirst()) {

				String arr = cursor.getString(cursor
						.getColumnIndex(LunchDataBaseAdapter.COLUMN_Date));
					sqlUpdataList = JSONUtil.parseJSONString(arr,
							new TypeReference<List<Entity>>() {
							});
			}
			lunchDB.close();
			cursor.close();
		if (sqlUpdataList == null)
			return null;
		for (int i = 0; i < sqlUpdataList.size(); i++) {// 将添加模块永远放在最后一位
			if (sqlUpdataList.get(i).getType() == C_iMPrivilegeMenu_Add) {
				Entity e = sqlUpdataList.get(i);
				sqlUpdataList.remove(e);
				sqlUpdataList.add(e);
				break;
			}
		}
		return sqlUpdataList;
	}

	private List<Entity> bingInitdataAndSqldata(List<Entity> init,
			List<Entity> sql) {
		if (sql == null)
			return init;
		if (init == null && sql != null)
			return sql;
		List<Entity> updata = new ArrayList<Entity>();
		List<Entity> remove = new ArrayList<Entity>();
		Entity add = null;
		for (int i = 0; i < sql.size(); i++) {
			if (sql.get(i).isBusiness()
					|| sql.get(i).getFrom() == Entity.C_iMenuFrom_third) {
				updata.add(sql.get(i));
			}// 业务管理模块不用加入与init数据的排序比较中，直接加入update里即可
			int type = sql.get(i).getType();
			for (int j = 0; j < init.size(); j++) {
				if (type == init.get(j).getType()) {
					init.get(j).setNews(sql.get(i).isNews());
					init.get(j).setHide(sql.get(i).isHide());
					init.get(j).setColor(sql.get(i).getColor());
					if (type == C_iMPrivilegeMenu_Add) {
						add = init.get(j);
					} else
						updata.add(init.get(j));
					remove.add(init.get(j));
				}
			}
		}
		init.removeAll(remove);
		updata.addAll(init);
		if (add != null)
			updata.add(add);
		return updata;
	}

	/**
	 * 判断现有的list中是否有包含"离线文档"模块，来选择添加与否
	 * 
	 * @param list
	 */
//	private void addOffline2List(List<Entity> list) {
//		boolean hasOffline = false;
//		Entity offEntity = new Entity();
//		offEntity.setName(ctx.getString(R.string.Menu_Offline));
//		offEntity.setType(MPrivilegeConstant.C_iMPrivilegeMenu_OfflineDoc);
//		for (Iterator<Entity> iterator = list.iterator(); iterator.hasNext();) {
//			Entity entity = iterator.next();
//			if (entity.getType() == MPrivilegeConstant.C_iMPrivilegeMenu_OfflineDoc) {
//				hasOffline = true;
//			}
//		}
//		if (!hasOffline) {
//			list.add(offEntity);
//		}
//	}

	/************** 修改 ***************/

//	private void addReport2List(List<Entity> list) {
//		boolean hasReport = false;
//		Entity reportEntity = new Entity();
//		reportEntity.setName(ctx.getString(R.string.Menu_Report));
//
//		reportEntity.setType(CMPConfig.MENU_TYPE_REPORT);
//
//		for (Iterator<Entity> iterator = list.iterator(); iterator.hasNext();) {
//			Entity entity = iterator.next();
//			if (entity.getType() == CMPConfig.MENU_TYPE_REPORT) {
//				hasReport = true;
//			}
//		}
//		if (!hasReport) {
//			list.add(reportEntity);
//		}
//	}

}
