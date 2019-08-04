package com.umeng.soexample.socialize.dashboard;

import static com.umeng.soexample.socialize.SocializeConfigDemo.DESCRIPTOR;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.markupartist.android.widget.ActionBar;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.soexample.R;
import com.umeng.soexample.socialize.fragments.ActionbarFragment;
import com.umeng.ui.BaseSinglePaneActivity;

public class ActionBarExample extends BaseSinglePaneActivity {

	@Override
	protected Fragment onCreatePane() {
		return new ActionbarFragment();
	}

	@Override
	protected void onResume() {
		super.onResume();
		ActionBar actionBar = getUActionBar();
		View personalView = actionBar.findViewById(R.id.actionbar_personal_button);
		personalView.setVisibility(View.VISIBLE);
		personalView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UMServiceFactory.getUMSocialService(DESCRIPTOR, RequestType.SOCIAL).openUserCenter(
						ActionBarExample.this);
			}
		});
	}

}
