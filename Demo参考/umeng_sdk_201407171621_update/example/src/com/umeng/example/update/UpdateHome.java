package com.umeng.example.update;

import java.io.File;
import java.lang.reflect.Field;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar.Action;
import com.umeng.example.R;
import com.umeng.example.util.UpdateExampleConfig;
import com.umeng.ui.BaseNavigationDrawerActivity;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateConfig;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class UpdateHome extends BaseNavigationDrawerActivity {
	// private static final String LOG_TAG = UpdateHome.class.getName();
	private static final int ABOUT_UPDATE = 0;
	private static final int AUTO_UPDATE = 1;
	private static final int MANUAL_UPDATE = 2;
	private static final int SILENT_UPDATE = 3;
	private static final int UPDATE_SETTING = 4;
	private static final int UPDATE_PROCESS = 5;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mDemoTitles;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.umeng_example_update_main);

		mTitle = mDrawerTitle = getString(R.string.umeng_example_home_btn_update);
		mDemoTitles = getResources().getStringArray(
				R.array.umeng_update_demo_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mDemoTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getUActionBar().setTitle(mTitle);
			}

			public void onDrawerOpened(View drawerView) {
				getUActionBar().setTitle(mDrawerTitle);
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		UpdateConfig.setDebug(true);
		try {
			Class<?> toggleClass = Class
					.forName("android.support.v4.app.ActionBarDrawerToggle");
			Field field = toggleClass.getDeclaredField("mSlider");
			field.setAccessible(true);
			getUActionBar().setNavigationDrawerImage(
					(Drawable) field.get(mDrawerToggle));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (savedInstanceState == null) {
			selectItem(0);
		}
		getUActionBar().addAction(new Action() {

			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

					@Override
					public void onUpdateReturned(int updateStatus,
							UpdateResponse updateInfo) {
						UmengUpdateAgent.setDefault();
						if (updateStatus == UpdateStatus.Yes) {
							UpdateExampleConfig.mResponse = updateInfo;
						}
						if (UpdateExampleConfig.mResponse != null) {
							File file = UmengUpdateAgent.downloadedFile(
									UpdateHome.this,
									UpdateExampleConfig.mResponse);
							String md5 = new String(
									UpdateExampleConfig.mResponse.new_md5);
							UpdateExampleConfig.mResponse.new_md5 = "";
							UmengUpdateAgent.ignoreUpdate(UpdateHome.this,
									UpdateExampleConfig.mResponse);
							UpdateExampleConfig.mResponse.new_md5 = md5;
							if (file != null) {
								if (file.delete()) {
									Toast.makeText(UpdateHome.this, "删除完成",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(UpdateHome.this, "删除失败",
											Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(UpdateHome.this, "删除完成",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(UpdateHome.this, "删除失败",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
				UmengUpdateAgent.setUpdateAutoPopup(false);
				UmengUpdateAgent.setUpdateOnlyWifi(false);
				UmengUpdateAgent.update(UpdateHome.this);
			}

			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return android.R.drawable.ic_menu_delete;
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getUActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;

		switch (position) {
		case ABOUT_UPDATE:
			fragment = new AboutUpdateFragment();
			break;
		case AUTO_UPDATE:
			fragment = new AutoUpdateFragment();
			break;
		case MANUAL_UPDATE:
			fragment = new ManualUpdateFragment();
			break;
		case SILENT_UPDATE:
			fragment = new SilentUpdateFragment();
			break;
		case UPDATE_SETTING:
			fragment = new UpdateSettingFragment();
			break;
		case UPDATE_PROCESS:
			fragment = new UpdateProcessFragment();
			break;
		}
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mDemoTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}
}