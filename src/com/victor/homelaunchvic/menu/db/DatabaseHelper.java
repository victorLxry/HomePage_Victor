package com.victor.homelaunchvic.menu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * com.Aina.Android Pro_SQLiteDatabase
 * 
 * @author 
 * @version 
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private String str = "";
	private String table = "";

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version, String str, String table) {
		super(context, name, factory, version);
		this.str = str;
		this.table = table;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(str);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE "+table);
		this.onCreate(db);

	}
}