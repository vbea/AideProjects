package com.niit.rolant;

import com.cellcom.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

//子菜单subMenu
public class SubMenuActivity extends Activity {

	private final static int ITEM_NEW_FILE=Menu.FIRST;
	private final static int ITEM_OPEN_FILE=Menu.FIRST+1;
	private final static int ITEM_COPY=Menu.FIRST+2;
	private final static int ITEM_CLOSE=Menu.FIRST+3;
	private final static int ITEM_SAVE=Menu.FIRST+4;
	private final static int ITEM_SAVE_ALL=Menu.FIRST+5;
	private final static int ITEM_CUT=Menu.FIRST+6;
	private final static int ITEM_PASTE=Menu.FIRST+7;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_menu);
		setTitle("单击Menu键看到效果！");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu file=menu.addSubMenu("文件");
		SubMenu edit=menu.addSubMenu("编辑");
		
		file.add(0,ITEM_NEW_FILE,0,"新建");
		file.add(0,ITEM_OPEN_FILE,0,"打开");
		file.add(0,ITEM_CLOSE,0,"关闭");
		file.add(0,ITEM_SAVE,0,"保存");
		file.add(0,ITEM_SAVE_ALL,0,"保存全部");
		
		
		edit.add(0,ITEM_COPY,0,"复制");
		edit.add(0,ITEM_CUT,0,"剪切");
		edit.add(0,ITEM_PASTE,0,"粘贴");
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ITEM_NEW_FILE:
			setTitle("新建文件！");
			break;
		case ITEM_OPEN_FILE:
			setTitle("打开文件");
			break;
		case ITEM_COPY:
			setTitle("复制文件");
			break;
		default:
			break;
		}
		return true;
	}
}
