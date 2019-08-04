package com.example.searchcity.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.example.searchcity.R;
import com.example.searchcity.dao.CityDao;
import com.example.searchcity.entity.CityEntity;
import com.example.searchcity.util.PinYinUtil;
import com.example.searchcity.util.StreamUtil;

public class MainActivity extends Activity {
	
	private CityDao cityDao;
	private PinYinUtil pyUtil;
	
	/**加载数据等待对话框*/
	private AlertDialog waitDialog;
	/**加载数据完成消息使用的关键字*/
	private static final int MESSAGE_CITY_LOADED = 1;
	ArrayList<CityEntity> citys = new ArrayList<CityEntity>();
	
	/**获取外部数据库中的城市列表，并将其存入新表中（如果有服务端支持，应该将获取城市计算拼音的逻辑放入服务端，并提供获取城市列表及对应拼音的接口）*/
	private void initCityList(){
		cityDao = new CityDao(this);
		if(cityDao.cityIsExists()){   //初始化只进行一次，可通过判断数据库中是否有数据，也可通过SharedPreferences实现
			return;
		}
		waitDialog = new AlertDialog.Builder(this).setTitle("提醒").setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage("正在加载数据，请稍后\n初次加载比较耗时，需要2-4分钟左右。再次启动就不会有任何问题。请亲耐心等待哦").setCancelable(false).create();
		waitDialog.show();
		final Handler handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				//数据加载完成后关闭对话框
				if(msg.what == MESSAGE_CITY_LOADED){
					waitDialog.cancel();
				}
			};
		};
		//初次加载比较耗时，所以需要新启动线程完成加载，并通过对话框告知用户。这段逻辑最好放到服务端，这里只是为了实现功能
		new Thread(){
			public void run() {
				pyUtil = new PinYinUtil();
				
				String dbFileName = "weather_info";
				File file = new File("data/data/"+getPackageName()+"/databases/"+dbFileName); 
				try {
					//从外部资源文件夹中获取城市数据库，并将其保存到数据库文件夹中
					StreamUtil.getInputStream(getAssets().open(dbFileName), new FileOutputStream(file));  //将外部资源中的数据库文件拷贝到数据库文件夹中
					SQLiteDatabase db = openOrCreateDatabase(dbFileName, Context.MODE_PRIVATE, null);
					Cursor c = db.query("city_info", new String[]{"_id","city_name"}, null, null, null, null, null);
					if(c!=null){
						//查询外部数据库中的城市数据，并在新构建的数据库中将原始数据和对应的拼音/首字母也保存到数据库中，待后续查询使用
						while(c.moveToNext()){
							int id = c.getInt(c.getColumnIndex("_id"));  //获取id
							String cityName = c.getString(c.getColumnIndex("city_name"));  //获取城市名称
							citys.add(new CityEntity(id, cityName, pyUtil.getStringPinYin(cityName), pyUtil.getFirstSpell(cityName)));  //pyutil是汉字与拼音转化的工具类，需要引入Jar包：pinyin4j
						}
						c.close();
					}
					db.close();
					cityDao.addCity(citys);
					citys.clear();
					handler.sendEmptyMessage(MESSAGE_CITY_LOADED);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();
		
	}
	
	private SearchView sv;
	private ListView lv;
	
	private void initView(){
		sv = (SearchView)findViewById(R.id.searchview);
		lv = (ListView)findViewById(R.id.listview);
	}
	
	private ArrayAdapter<CityEntity> cityAdapter;
	
	private void setAdapter(){
		cityAdapter = new ArrayAdapter<CityEntity>(this, android.R.layout.simple_list_item_1, citys);
		lv.setAdapter(cityAdapter);
	}
	
	private void addListener(){
		
		//为搜索框添加搜索文字的监听器
		sv.setOnQueryTextListener(new OnQueryTextListener() {
			
			//当用户点击提交时执行
			@Override
			public boolean onQueryTextSubmit(String query) {
				
				return true;
			}
			
			//当用户输入文字改变时执行
			@Override
			public boolean onQueryTextChange(String newText) {
				if(newText == null || newText.length() == 0){
					update(null);  //如果用户删除了搜索内容，Listview中的内容也应该清空
				}else{
					ArrayList<CityEntity> newCitys = cityDao.getCitysForName(newText);
					update(newCitys);
				}
				return true;
			}
		});
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(MainActivity.this, "您已经选中："+citys.get(position).getCityName(), Toast.LENGTH_SHORT).show();
			}
			
		});
	}
	
	/**该方法用来更新adapter中的内容，并刷新界面*/
	private void update(ArrayList<CityEntity> newCitys){
		citys.clear();
		if(newCitys != null && newCitys.size() != 0){
			citys.addAll(newCitys);
		}
		cityAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initCityList();
		initView();
		setAdapter();
		addListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
