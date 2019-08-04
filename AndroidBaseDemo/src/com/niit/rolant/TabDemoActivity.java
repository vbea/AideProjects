package com.niit.rolant;

import com.cellcom.R;

import android.app.TabActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;

//标签控件Tab
public class TabDemoActivity extends TabActivity {

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("标签控件Tab");
		TabHost tabHost=getTabHost();
		LayoutInflater.from(this).inflate(R.layout.tab, tabHost.getTabContentView(),true);
		tabHost.addTab(tabHost.newTabSpec("biaoqian1").setIndicator("标签tab1").setContent(R.id.tab1));
		tabHost.addTab(tabHost.newTabSpec("biaoqian2").setIndicator("标签tab2").setContent(R.id.tab2));
		tabHost.addTab(tabHost.newTabSpec("biaoqian3").setIndicator("标签tab3").setContent(R.id.tab3));
	}	
}
