package com.umeng.soexample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SlideMenu {
	private boolean menuShown = false;
	private View menu;
	private ViewGroup content;
	private FrameLayout parent;
	private int menuSize;
	private static int statusHeight = 0;
	private Activity act;
	private boolean isLeft = false;
	private final int MENU_WIDTH = 250;
	private static final int ANIM_SPEED = 300;

	public SlideMenu(Activity act) {
		this.act = act;
	}

	// call this in your onCreate() for screen rotation
	public void checkEnabled() {
		if (menuShown)
			this.show(false);
	}

	public void show() {
		// get the height of the status bar
		if (statusHeight == 0) {
			Rect rectgle = new Rect();
			Window window = act.getWindow();
			window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
			statusHeight = rectgle.top;
		}
		this.show(true);
	}

	private void show(boolean animate) {
		menuSize = SlideUtils.dpToPx(MENU_WIDTH, act);
		content = (ViewGroup) act.findViewById(android.R.id.content).getParent();

		ViewGroup.MarginLayoutParams parm = (ViewGroup.MarginLayoutParams) content.getLayoutParams();
		if (isLeft)
			parm.setMargins(menuSize, 0, -menuSize, 0);
		else
			parm.setMargins(-menuSize, 0, menuSize, 0);
		content.setLayoutParams(parm);
		// animation for smooth slide-out
		TranslateAnimation ta = new TranslateAnimation(isLeft ? -menuSize : menuSize, 0, 0, 0);
		ta.setDuration(ANIM_SPEED);

		FrameLayout.LayoutParams lays = new FrameLayout.LayoutParams(-1, -1);

		try {
			parent = (FrameLayout) content.getParent();
			lays.setMargins(0, statusHeight, 0, 0);
		}
		catch (Exception e) {
			parent = getRootFrameLayout(content);
			lays.setMargins(0, 0, 0, 0);
		}

		LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		menu = inflater.inflate(R.layout.slide_menu, null);
		fillContent(menu);
		menu.setLayoutParams(lays);
		parent.addView(menu);

		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) menu.findViewById(R.id.menu_content)
																						.getLayoutParams();
		layoutParams.addRule(isLeft	? RelativeLayout.ALIGN_PARENT_LEFT
									: RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

		menu.findViewById(R.id.overlay).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SlideMenu.this.hide();
			}
		});
		SlideUtils.enableDisableViewGroup((ViewGroup) parent.findViewById(android.R.id.content)
																.getParent(), false);

		if (animate) {
			content.startAnimation(ta);
			menu.startAnimation(ta);
		}
		// ((ExtendedViewPager)
		// act.findViewById(R.id.viewpager)).setPagingEnabled(false);
		// ((ExtendedPagerTabStrip)
		// act.findViewById(R.id.viewpager_tabs)).setNavEnabled(false);
		menuShown = true;
	}

	/**
	 * for samsung i9300
	 * 
	 * @param content
	 * @return
	 */
	private FrameLayout getRootFrameLayout(ViewGroup content) {
		try {
			ViewGroup pc = (ViewGroup) content.getParent();
			if (!(pc instanceof FrameLayout)) {
				for (int i = 0; i < 10; i++) {
					pc = (ViewGroup) pc.getParent();
					if (pc instanceof FrameLayout)
						return (FrameLayout) pc;
				}
				return null;
			} else
				return (FrameLayout) pc;
		}
		catch (Exception e) {}
		return null;
	}

	public void fillContent(View menu) {

	}

	public void hide() {
		TranslateAnimation ta = new TranslateAnimation(0, isLeft ? -menuSize : menuSize, 0, 0);
		ta.setDuration(ANIM_SPEED);
		menu.startAnimation(ta);
		parent.removeView(menu);

		TranslateAnimation tra = new TranslateAnimation(isLeft ? menuSize : -menuSize, 0, 0, 0);
		tra.setDuration(ANIM_SPEED);
		content.startAnimation(tra);
		ViewGroup.MarginLayoutParams parm = (ViewGroup.MarginLayoutParams) content.getLayoutParams();
		parm.setMargins(0, 0, 0, 0);
		content.setLayoutParams(parm);
		SlideUtils.enableDisableViewGroup((LinearLayout) parent.findViewById(android.R.id.content)
																.getParent(), true);
		menuShown = false;
	}

	public boolean isShow() {
		return menuShown;
	}
}