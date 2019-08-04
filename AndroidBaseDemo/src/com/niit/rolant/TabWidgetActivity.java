package com.niit.rolant;

import com.cellcom.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

//注意这个activity继承的是TabActivity
public class TabWidgetActivity extends TabActivity {

	//声明TabHost对象
	private TabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_widget);
		
		//获取TabHoast对象
		mTabHost=getTabHost();
		/* 为TabHost添加标签 */
		//新建一个newTabSpec(newTabSpec)
		//设置其标签和图标(setIndicator)
		//设置内容(setContent)
		mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("TAB1",getResources().getDrawable(R.drawable.img1)).setContent(R.id.textview1));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("TAB2",getResources().getDrawable(R.drawable.img2)).setContent(R.id.textview2));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator("TAB3",getResources().getDrawable(R.drawable.img3)).setContent(R.id.textview3));
		
		//设置TabHost的背景颜色
	    mTabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));
	    
	    //显示第一个tab
	    mTabHost.setCurrentTab(0);
	    
	  //标签切换事件处理，setOnTabChangedListener 
	    mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				Dialog dialog=new AlertDialog.Builder(TabWidgetActivity.this)
				.setTitle("提醒你")
				.setMessage("你当前选中的是"+tabId+"标签！")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(TabWidgetActivity.this, "你单击了确定按钮！", Toast.LENGTH_LONG).show();
						dialog.cancel();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(TabWidgetActivity.this, "你单击了取消按钮！", Toast.LENGTH_LONG).show();
						dialog.cancel();
					}
				})
				.create();
				dialog.show();
			}
		});
	}

}
