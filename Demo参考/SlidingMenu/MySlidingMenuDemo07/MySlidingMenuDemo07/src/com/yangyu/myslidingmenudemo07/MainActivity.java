package com.yangyu.myslidingmenudemo07;

import android.os.Bundle;
import android.view.MenuItem;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("Sliding Content only");
		
		initSlidingMenu();
	}

	/**
	 * 初始化滑动菜单
	 */
	private void initSlidingMenu(){
		// 设置滑动菜单打开后的视图界面
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new SampleListFragment()).commit();

		// 设置主界面视图
		setContentView(R.layout.content_frame);
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new SampleListFragment()).commit();
		
		// 设置当打开滑动菜单时，ActionBar不能够跟随着一起滑动
		setSlidingActionBarEnabled(false);
		
		// 设置滑动菜单的属性值
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		getActionBar().setDisplayHomeAsUpEnabled(true);			
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;	
		}			
		return super.onOptionsItemSelected(item);
	}
	
}
