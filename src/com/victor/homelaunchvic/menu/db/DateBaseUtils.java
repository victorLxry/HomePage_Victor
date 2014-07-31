package com.victor.homelaunchvic.menu.db;

import java.util.List;

import android.app.Activity;
import android.database.Cursor;

import com.victor.homelaunchvic.menu.view.Entity;
import com.victor.homelaunchvic.utils.JSONUtil;

public class DateBaseUtils {
	public static void saveLunchState(Activity activity, List<Entity> array) {
		LunchDataBaseAdapter lunchDB = new LunchDataBaseAdapter(activity);
		long useID = 198702280216l;
		if (array.size() == 0)// 在打开一个新页面而且没有展开菜单的情况下，array的size==0，此时不用保留数据
			return;
		lunchDB.open();
		Cursor cursor = lunchDB.select(useID,1212);

			String update = JSONUtil.writeEntityToJSONString(array);
			if (!cursor.moveToFirst()) {
				lunchDB.insert(useID, update, 1212);
			} else {
				lunchDB.update(update, useID,1212);
			}

		cursor.close();
		lunchDB.close();
	}
}
