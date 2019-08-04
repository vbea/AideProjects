package com.niit.rolant;

import com.cellcom.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.TextView;

public class ContextMenuActivity extends Activity {

	private final static int ITEM0=Menu.FIRST;
	private final static int ITEM1=Menu.FIRST+1;
	private final static int ITEM2=Menu.FIRST+2;
	private TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.context_menu);
		setTitle("单击Menu键看到效果！");
		tv=(TextView)findViewById(R.id.tv1);
		
		this.registerForContextMenu(tv);//将上下文注册到TextView
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, ITEM0, 0, "红色背景");
		menu.add(0,ITEM1,0,"绿色背景");
		menu.add(0,ITEM2,0,"白色背景");
		
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ITEM0:
			tv.setTextColor(Color.RED);
			break;
		case ITEM1:
			tv.setTextColor(Color.GREEN);
			break;
		case ITEM2:
			tv.setTextColor(Color.WHITE);
			break;
		default:
			break;
		}
		return true;
	}
	
}
