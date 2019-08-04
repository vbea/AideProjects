package com.umeng.soexample;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class SlideUtils {

	public static int dpToPx(int dp, Context ctx) {
		Resources r = ctx.getResources();
		return (int) TypedValue.applyDimension(	TypedValue.COMPLEX_UNIT_DIP,
												dp,
												r.getDisplayMetrics());
	}

	// originally:
	// http://stackoverflow.com/questions/5418510/disable-the-touch-events-for-all-the-views
	// modified for the needs here
	public static void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
		int childCount = viewGroup.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View view = viewGroup.getChildAt(i);
			if (view.isFocusable())
				view.setEnabled(enabled);
			if (view instanceof ViewGroup) {
				enableDisableViewGroup((ViewGroup) view, enabled);
			} else if (view instanceof ListView) {
				if (view.isFocusable())
					view.setEnabled(enabled);
				ListView listView = (ListView) view;
				int listChildCount = listView.getChildCount();
				for (int j = 0; j < listChildCount; j++) {
					if (view.isFocusable())
						listView.getChildAt(j).setEnabled(false);
				}
			}
		}
	}
}
