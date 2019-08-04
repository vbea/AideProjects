package com.umeng.soexample.activity;


import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ArrayAdapter;

import com.umeng.socialize.utils.Log;
import com.umeng.soexample.NavigationHelper;
import com.umeng.soexample.R;
public class DropActivity extends SwipeActivity implements ActionBar.OnNavigationListener {
	public static final String ARG_SECTION_POSITION = "section_number";

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	protected ActionBar actionBar;
	protected PagerTitleStrip mPagerTitle;
	private Animation showAnim, hideAnim;
	private Handler mPagerTitleHander;
	private boolean autoHide = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set up the action bar to show a dropdown list.
		actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		String[] titles = NavigationHelper.getTitles();
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
		new ArrayAdapter<String>(	actionBar.getThemedContext(),
									R.layout.navigation_list_item,
									android.R.id.text1,
									titles),
												this);

		mPagerTitle = getPagerTitle();
		initFlickerAnimations();

		mPagerTitleHander = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case 1:// init show
					sendEmptyMessage(3);
					sendEmptyMessageDelayed(2, 2000);
					break;
				case 2:// hide page title
					 if (isShowTitle && autoHide) {
						 mPagerTitle.startAnimation(hideAnim);
					 }
					break;
				case 3:// show page title
					if (!isShowTitle) {
						isShowTitle = true;
						ViewParent parent = mPagerTitle.getParent();
						if (parent == null) {
							// mViewPager.addView( mPagerTitle,
							// LayoutParams.MATCH_PARENT,
							// LayoutParams.WRAP_CONTENT);
							mViewPager.addView(mPagerTitle);
							mPagerTitle.startAnimation(showAnim);
						}
					}
					break;
				}
			};
		};

		mPagerTitleHander.sendEmptyMessageDelayed(1, 100);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int pos) {
				actionBar.setSelectedNavigationItem(pos);
				onPageChanged(pos);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}

			@Override
			public void onPageScrollStateChanged(int state) {
				Log.d("TestData", "onPageScrollStateChanged state:" + state);
				if (state == ViewPager.SCROLL_STATE_DRAGGING) {
					mPagerTitleHander.removeMessages(2);
					if(autoHide)
						mPagerTitleHander.sendEmptyMessage(3);
				} else if (state == ViewPager.SCROLL_STATE_IDLE) {
					mPagerTitleHander.sendEmptyMessageDelayed(2, 2000);
				}
			}
		});

	}
	
	protected void onPageChanged(int pos) {
	}

	private PagerTitleStrip getPagerTitle() {
		PagerTabStrip pagerTabStrip = (PagerTabStrip) View.inflate(	this,
																	R.layout.pager_tab_title,
																	null);
		android.support.v4.view.ViewPager.LayoutParams lp = new ViewPager.LayoutParams();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.TOP;
		pagerTabStrip.setLayoutParams(lp);
		return pagerTabStrip;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		smoothTo(position);
		if (!isShowTitle)
			mPagerTitleHander.sendEmptyMessage(1);
		return true;
	}

	@Override
	public Fragment onGetFragmentItem(int position) {
		String title = NavigationHelper.getTitle(position);
		Fragment fragment = NavigationHelper.getFragment(title);

		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_POSITION, position);
		fragment.setArguments(args);
		return fragment;
	}

	private boolean isShowTitle = false;

	private void initFlickerAnimations() {
		hideAnim = new AlphaAnimation(1.0f, 0.0f);
		hideAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {}

			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				mViewPager.removeView(mPagerTitle);
				isShowTitle = false;
			}
		});
		hideAnim.setDuration(500);

		showAnim = new AlphaAnimation(0.5f, 1.0f);
		showAnim.setDuration(150);

	}

}
