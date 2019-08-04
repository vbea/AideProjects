package com.umeng.example.tools;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.umeng.example.UmengHome;
import com.umeng.ui.BaseSinglePaneActivity;

public class ToolsHome extends BaseSinglePaneActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected Fragment onCreatePane() {
		return new UmengHome.UmengHomeDashboardFragment();
	}
}