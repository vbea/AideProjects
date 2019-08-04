package com.example.searchcity.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.searchcity.entity.CityEntity;

/**用来处理与城市数据库相关的操作*/
public class CityDao {

	private Context context;
	private CityDatabaseOpenHelper helper;
	
	/**代表Id字段*/
	private static final String ID = "_id";
	/**代表城市名称字段*/
	private static final String CITY_NAME = "city_name";
	/**代表城市拼音字段*/
	private static final String CITY_PINYIN = "city_pinyin";
	/**代表城市首字母字段*/
	private static final String CITY_SHORT = "city_short";
	
	/**表名*/
	private static final String TABLE_NAME = "city_info";
	
	public CityDao(Context context){
		this.context = context;
		helper = new CityDatabaseOpenHelper(context, 1);
	}

	/**将给定城市列表添加到数据库中*/
	public void addCity(ArrayList<CityEntity> citys){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		for(CityEntity entity : citys){
			values.clear();
			values.put(ID, entity.getId());
			values.put(CITY_NAME, entity.getCityName());
			values.put(CITY_PINYIN, entity.getCityPinyin());
			values.put(CITY_SHORT, entity.getShortName());
			db.insert(TABLE_NAME, null, values);
		}
		db.close();
	}
	
	/**根据城市名称、拼音或简称查询城市*/
	public ArrayList<CityEntity> getCitysForName(String name){
		ArrayList<CityEntity> citys = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, new String[]{ID,CITY_NAME,CITY_PINYIN,CITY_SHORT}, CITY_NAME +" like '"+name+"%' or "+CITY_PINYIN+" like '"+name+"%' or "+CITY_SHORT+" like '"+name+"%'", null, null, null, null);
		if(c != null){
			citys = new ArrayList<CityEntity>();
			while(c.moveToNext()){
				citys.add(new CityEntity(c.getInt(c.getColumnIndex(ID)),
						c.getString(c.getColumnIndex(CITY_NAME)), 
						c.getString(c.getColumnIndex(CITY_PINYIN)),
						c.getString(c.getColumnIndex(CITY_SHORT))));
			}
			c.close();
		}
		db.close();
		return citys;
	}
	
	
	/**判断城市是否为空*/
	public boolean cityIsExists(){
		boolean isExists = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, new String[]{"_id"}, null, null, null, null, "1");
		if(c != null && c.moveToFirst()){
			isExists = true;
		}
		if(c != null){
			c.close();
		}
		db.close();
		return isExists;
	}
}
