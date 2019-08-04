package com.umeng.soexample.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;

import com.umeng.soexample.NavigationHelper;
import com.umeng.soexample.R;

public class SwipeActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	protected SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	protected ViewPager mViewPager;
	protected PagerTitleStrip mPagerTitle;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		

		mViewPager.setAdapter(mSectionsPagerAdapter);
		
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		String[] titles ;
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			titles = NavigationHelper.getTitles();
		}

		@Override
		public Fragment getItem(int position) {
			
			return onGetFragmentItem(position);
		}

		@Override
		public int getCount() {
			return NavigationHelper.getItemCount();
		}

		@Override
		public CharSequence getPageTitle(int position) {

			return titles[position];
		}
	}
	
	public void smoothTo(int pos){
		mViewPager.setCurrentItem(pos, true);
	}

	public  Fragment onGetFragmentItem(int position) {
		return null;
	}

}
