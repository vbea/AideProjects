package com.umeng.soexample.socialize.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.umeng.soexample.socialize.dashboard.MockDataHelper.TV;
import com.umeng.soexample.socialize.fragments.DetailPageFragment;
import com.umeng.ui.BaseSinglePaneActivity;



public class ActionBarExampleDetail extends BaseSinglePaneActivity {
	@Override
	protected Fragment onCreatePane() {
		TV tv = getIntent().getExtras().getParcelable("TV");
		return new DetailPageFragment(tv);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


}
