package com.victor.homelaunchvic.menu.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.victor.homelaunchvic.R;

public class BaseLunchFlipper extends LunchFlipper {
	private SaBaseMenuLayout baseMenuLayout;

	public BaseLunchFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BaseLunchFlipper(Context context) {
		this(context, null);
	}

	public void setParentLayout(SaBaseMenuLayout baseMenuLayout) {
		this.baseMenuLayout = baseMenuLayout;
	}

	public SaBaseMenuLayout getParentLayout() {
		return baseMenuLayout;
	}

	/**
	 * 设置个模块的条数，以及是否有未读消息
	 * 
	 * @param entity
	 */
	void setItemCount(Entity entity) {
//		if (setNotifiAndUc(entity))
//			return;
		for (LunchItem item : lunchItemList) {
			if (item.getEntity().getType() == entity.getType()) {
				if (entity.getCount() != -1) {
					TextView tvCount = (TextView) item
							.findViewById(R.id.tv_typeCounte);
					item.getEntity().setCount(entity.getCount());
					if (entity.getCount() != 0) {
						tvCount.setVisibility(View.VISIBLE);
						tvCount.setText(""
								+ (item.getEntity().getCount() > 999 ? "999+"
										: item.getEntity().getCount()) + "");
					} else {
						tvCount.setVisibility(View.GONE);
					}
				}
				item.getEntity().setNews(entity.isNews());
				View ivNew = item.findViewById(R.id.iv_newmessage);
				if (entity.isNews()) {
					ivNew.setVisibility(View.VISIBLE);
				} else {
					ivNew.setVisibility(View.GONE);
				}
				item.measure(
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				item.layout(item.getLeft(), item.getTop(), item.getRight(),
						item.getBottom());
				if (!isMoveRunnable) {
					invalidate();
					requestLayout();
				}
			}
		}
	}

//	private boolean setNotifiAndUc(Entity entity) {
//		boolean falg = false;
//		if (entity.getType() == MPrivilegeConstant.C_iMPrivilegeMenu_Message
//				|| entity.getType() == MPrivilegeConstant.C_iMPrivilegeMenu_IM) {
//			LogM.i("menu",entity.getType()+"消息数："+entity.getCount());
//			falg = true;
//			if (baseMenuLayout != null) {
//				if (entity.getCount() != -1) {
//					TextView tvCount=null;
//					if(entity.getType() == MPrivilegeConstant.C_iMPrivilegeMenu_Message)
//					tvCount = (TextView) baseMenuLayout
//							.findViewById(R.id.tv_typeCounte);
//					else
//						tvCount = (TextView) baseMenuLayout
//						.findViewById(R.id.tv_imtypeCounte);
//					if (entity.getCount() != 0) {
//						tvCount.setVisibility(View.VISIBLE);
//						tvCount.setText(
//								 (entity.getCount() > 999 ? "999+" : entity
//										.getCount()) +"");
//					} else {
//						tvCount.setVisibility(View.INVISIBLE);
//					}
//				}
//				View ivNew = baseMenuLayout.findViewById(R.id.iv_newmessage);
////				if (entity.isNews()) {
////					ivNew.setVisibility(View.VISIBLE);
////				} else {
////					ivNew.setVisibility(View.GONE);
////				}
//			}
//		}
//		return falg;
//	}

	/**
	 * 检查模块有变化（包括位置顺序变化，模板条数变化），如果有变化才更新，避免不必要的更新
	 * 
	 * @param list
	 * @return
	 */
	public boolean isHasUpdate(List<Entity> list) {
		boolean flag = false;
		if (lunchItemList == null || lunchItemList.size() == 0) {
			flag = true;
		}
		List<Entity> reallyList=new ArrayList<Entity>();
		for(Entity entity:list){
			if(!entity.isHide()){
				reallyList.add(entity);
			}
		}
		if (reallyList.size() == lunchItemList.size()) {
			for (int i = 0; i < reallyList.size(); i++) {
				if (!flag) {
					Entity en1 = reallyList.get(i);
					Entity en2 = lunchItemList.get(i).getEntity();
					if (en1.getType() != en2.getType()
							|| (en1.getCount() != en2.getCount())
							|| (en1.isNews() != en2.isNews())) {
						flag = true;
					}

				}
			}
		} else {
			flag = true;
		}
		return flag;
	}

	public List<Entity> getArrayName() {
		List<Entity> array = new ArrayList<Entity>();
//		for (int i = 0; i < lunchItemList.size(); i++) {
//			for (int j = i + 1; j < lunchItemList.size(); j++) {
//				Entity entity = lunchItemList.get(i).getEntity();
//				if (entity.getSourceID() == lunchItemList.get(j).getEntity().getSourceID()) {
//					lunchItemList.remove(j);
//				}
//			}
//			array.add(lunchItemList.get(i).getEntity());
//		}
		return array;
	}

}
