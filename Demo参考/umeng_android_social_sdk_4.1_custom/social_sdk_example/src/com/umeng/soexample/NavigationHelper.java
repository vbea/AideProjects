package com.umeng.soexample;

import android.support.v4.app.Fragment;

import com.umeng.socialize.utils.Log;
import com.umeng.soexample.socialize.fragments.ActionbarFragment;
import com.umeng.soexample.socialize.fragments.CommentFragment;
import com.umeng.soexample.socialize.fragments.CustomPlatformFragment;
import com.umeng.soexample.socialize.fragments.OtherFragment;
import com.umeng.soexample.socialize.fragments.ShareFragment;

public class NavigationHelper {
	public static final String SHARE_MOD = "分享接口";
	public static final String COMMENT_MOD = "评论接口";

	public static final String SOCIAL_MOD = "解决方案示例";
	public static final String OTHER_MOD = "用户接口";
	public static final String CUSTOM_MOD = "微信/QQ";
	private static final String[] titles = new String[]{SHARE_MOD,CUSTOM_MOD,OTHER_MOD,COMMENT_MOD,SOCIAL_MOD};

	public static String[] getTitles(){
		return titles;
	}
	
	public static int getItemCount(){
		return titles.length;
	}
	
	public static  String getTitle(int pos){
		return titles[pos];
	}
	
	public static Fragment getFragment(String title){
		Log.d("TestData", "getTitle "+ title);
		Fragment fragment = null;
		if (title.equals(NavigationHelper.SHARE_MOD)) {
			fragment = new ShareFragment();
		} else if (title.equals(NavigationHelper.COMMENT_MOD)) {
			fragment = new CommentFragment();
		} else if (title.equals(NavigationHelper.SOCIAL_MOD)) {
			fragment = new ActionbarFragment();
		} else if (title.equals(NavigationHelper.OTHER_MOD)) {
			fragment = new OtherFragment();
		} else if (title.equals(NavigationHelper.CUSTOM_MOD)) {
			fragment = new CustomPlatformFragment();
		}
		
		return fragment;
	}
}
