package com.umeng.soexample.activity;

import static com.umeng.soexample.socialize.SocializeConfigDemo.DESCRIPTOR;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.utils.Log;
import com.umeng.soexample.R;
import com.umeng.soexample.SlideMenu;

/**
 * @功能描述 : Demo主Activity
 * 
 * @author :
 * @version: [版本号, Aug 3, 2013]
 */
@SuppressLint("NewApi")
public class NavigationActivity extends DropActivity {

//	private int meId_setting = -1;			// actionbar上的调出SlideMenu的id
//	protected SlideMenu slidemenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		slidemenu = new SlideMenu(this) {
//			@Override
//			public void show() {
//				onSlideMenuShow(slidemenu);
//				super.show();
//			}
//
//			@Override
//			public void hide() {
//				onSlideMenuHide(slidemenu);
//				super.hide();
//			}
//
//			@Override
//			public void fillContent(View menu) {
//				NavigationActivity.this.fillMenuContent(menu);
//			}
//		};
//		slidemenu.checkEnabled();
		actionBar.setIcon(R.drawable.umeng_example_socialize_action_icon);
	}

	@Override
	protected void onPageChanged(int pos) {
		if (pos > 0) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		} else
			actionBar.setDisplayHomeAsUpEnabled(false);

	}

	protected void fillMenuContent(View menu) {
	}

	protected void onSlideMenuShow(SlideMenu slidemenu) {
	}

	protected void onSlideMenuHide(SlideMenu slidemenu) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuItem settingItem = menu.add("Action Button");
//		settingItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//		settingItem.setIcon(R.drawable.umeng_socialize_example_setting);
//		meId_setting = settingItem.getItemId();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (android.R.id.home == item.getItemId()) {
			mViewPager.setCurrentItem(0, true);
		} 
//		else if (item.getItemId() == meId_setting) {
//			if (slidemenu.isShow())
//				slidemenu.hide();
//			else
//				slidemenu.show();
//		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		UMSocialService controller = UMServiceFactory
				.getUMSocialService(DESCRIPTOR);

		// 根据requestCode获取对应的SsoHandler
		UMSsoHandler ssoHandler = controller.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			Log.d("", "#### ssoHandler.authorizeCallBack");
		}
	}

	@Override
	public void onBackPressed() {
//		if (slidemenu.isShow()) {
//			slidemenu.hide();
//		} else {
//			finish();
//		}
		finish();
	}

}
