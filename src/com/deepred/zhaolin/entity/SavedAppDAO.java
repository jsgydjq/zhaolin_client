package com.deepred.zhaolin.entity;

import java.util.ArrayList;
import java.util.List;

import com.deepred.zhaolin.utils.SavedApp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class SavedAppDAO {
	private SQLiteDatabase database;

	public SavedAppDAO(Context context) {
		this.database = context.openOrCreateDatabase("saved",
				Context.MODE_PRIVATE, null);
		this.createTables();
	}

	public void dropTables() {
		database.execSQL("drop table saved_apps;");
	}

	private void createTables() {
		database.execSQL("create table if not exists saved_apps("
				+ " appname text primary key,"
				+ "count integer," + " icon text," + " size integer,"  
				+" distance real," +" user text" +");");
	}

	public void deleteAll() {
		SQLiteStatement statement = database
				.compileStatement("delete from saved_apps");
		statement.execute();
		statement.close();
	}

	public void create(SavedApp appInfo) {
		SQLiteStatement statement = database
				.compileStatement("insert or ignore into saved_apps(appname, count, icon, size, distance, user) " +
						"values(?,?,?,?,?,?)");
		statement.bindString(1, appInfo.getAppname());
		statement.bindLong(2, appInfo.getCount());
		statement.bindString(3, appInfo.getIcon());
		statement.bindLong(4, appInfo.getSize());
		statement.bindDouble(5, appInfo.getDistance());
		statement.bindString(6, appInfo.getUser());
		
		statement.execute();
		statement.close();
	}

	public List<SavedApp> list() {
		List<SavedApp> array = new ArrayList<SavedApp>();
		Cursor cursor = database.rawQuery("select * from saved_apps;",
				new String[] {});

		while (cursor.moveToNext()) {
			SavedApp object = new SavedApp();
			object.setAppname(cursor.getString(cursor.getColumnIndex("appname")));
			object.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
			object.setSize(cursor.getLong(cursor.getColumnIndex("size")));
			object.setCount(cursor.getInt(cursor.getColumnIndex("count")));
			object.setDistance(cursor.getFloat(cursor.getColumnIndex("distance")));
			object.setUser(cursor.getString(cursor.getColumnIndex("user")));
			array.add(object);
		}
		cursor.close();
		return array;
	}

	public List<String> getNames() {
		List<String> array = new ArrayList<String>();

		Cursor cursor = database.rawQuery("select appname from saved_apps",
				new String[] {});

		while (cursor.moveToNext()) {
			array.add(cursor.getString(0));
		}
		cursor.close();

		return array;
	}

	public void delete(String appname) {
		SQLiteStatement statement = database
				.compileStatement("delete from saved_apps where appname = ?");
		statement.bindString(1, appname);
		statement.execute();
		statement.close();
	}

	public boolean get(String appname) {
		SavedApp appInfo = null;
		Cursor cursor = database.rawQuery(
				"select * from saved_apps where appname = '"+ appname +"';",
				new String[]{});
		if (cursor.moveToFirst()) {
			appInfo = new SavedApp();
		}
		cursor.close();
		return !(appInfo == null);
	}
}
