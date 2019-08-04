package com.niit.rolant;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

//OptionsMenu测试
public class OptionsMenuActivity extends Activity {

	private final static int ITEM0=Menu.FIRST;
	private final static int ITEM1=Menu.FIRST+1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("单击Menu键看到效果！");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//第一个参数：组ID 　第二个参数：菜单项ID　第三个参数：顺序号　第四个参数：菜单项上显示的内容
		menu.add(0,ITEM0,0,"开始");
		menu.add(0,ITEM1,0,"结束");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ITEM0:
			setTitle("开始游戏");
			break;
		case ITEM1:
			setTitle("结束游戏");
			break;
		default:
			break;
		}
		return true;
	}

}
