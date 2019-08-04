package com.example.searchcity.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.example.searchcity.util.StreamUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**用来操作城市数据库的helper类*/
public class CityDatabaseOpenHelper extends SQLiteOpenHelper {
	
	/**创建数据库的sql语句，改表包括城市id，城市名称，城市名称拼音，城市拼音的首字母*/
	private static final String CREATE_CITY = "create table city_info(_id integer primary key,city_name text,city_pinyin text,city_short text)";

	public CityDatabaseOpenHelper(Context context,  int version) {
		super(context, "city_info", null, version);
	}

	/**构建城市信息数据库，对城市数据进行长久保存*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CITY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
