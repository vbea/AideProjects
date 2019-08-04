package com.umeng.soexample.activity;

import static com.umeng.soexample.socialize.SocializeConfigDemo.DESCRIPTOR;
import static com.umeng.soexample.socialize.SocializeConfigDemo.SUPPORT_DOUBAN;
import static com.umeng.soexample.socialize.SocializeConfigDemo.SUPPORT_FACEBOOK;
import static com.umeng.soexample.socialize.SocializeConfigDemo.SUPPORT_GOOGLE;
import static com.umeng.soexample.socialize.SocializeConfigDemo.SUPPORT_QZONE;
import static com.umeng.soexample.socialize.SocializeConfigDemo.SUPPORT_RENR;
import static com.umeng.soexample.socialize.SocializeConfigDemo.SUPPORT_SINA;
import static com.umeng.soexample.socialize.SocializeConfigDemo.SUPPORT_TENC;
import static com.umeng.soexample.socialize.SocializeConfigDemo.SUPPORT_TWITTER;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.umeng.socialize.bean.Gender;
import com.umeng.socialize.bean.SnsAccount;
import com.umeng.socialize.bean.SocializeUser;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchUserListener;
import com.umeng.soexample.R;
import com.umeng.soexample.SlideMenu;
import com.umeng.soexample.socialize.SocializeConfigDemo;
import com.umeng.soexample.socialize.dashboard.MockDataHelper.BindNETiMGTask;

public class SoclialNavigationActivity extends NavigationActivity {
	private UMSocialService umSocialService;
	private SocializeUser mUser;
	private Drawable user_icon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		umSocialService = UMServiceFactory.getUMSocialService(DESCRIPTOR);
		umSocialService.getUserInfo(this, new FetchUserListener() {
			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(int status, SocializeUser user) {
				mUser = user;
				if (mUser != null
						&& mUser.mLoginAccount != null
						&& !TextUtils.isEmpty(mUser.mLoginAccount
								.getAccountIconUrl())) {
					String url = mUser.mLoginAccount.getAccountIconUrl();
					new BindNETiMGTask(url) {
						@Override
						public void onComplate(Drawable drawable) {
							user_icon = drawable;
						}
					}.execute();
				}
			}
		});

	}

	@Override
	protected void onSlideMenuHide(SlideMenu slidemenu) {
		SocializeConfigDemo.nofifyConfigChange(this);
	}

	@SuppressLint("NewApi")
	@Override
	protected void fillMenuContent(View menu) {
		// LayoutInflater inflater = (LayoutInflater)
		// SoclialNavigationActivity.this
		// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// ViewGroup vp = (ViewGroup) menu.findViewById(R.id.menu_content);
		// View content = inflater.inflate(R.layout.slide_menu_content, vp,
		// true);
		//
		// // user info
		// content.findViewById(R.id.msg_user_parent).setOnClickListener(
		// new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// umSocialService
		// .openUserCenter(SoclialNavigationActivity.this);
		// slidemenu.hide();
		// }
		// });
		//
		// SnsAccount ac = mUser == null ? null : mUser.mLoginAccount;
		// if (ac != null) {
		// TextView nameTv = (TextView) content.findViewById(R.id.msg_name);
		// nameTv.setText(ac.getUserName());
		//
		// TextView genderTv = (TextView) content
		// .findViewById(R.id.msg_gender);
		// Gender gender = ac.getGender();
		// String strGender = gender == null ? "未设置性别"
		// : (gender == Gender.FEMALE ? "女" : "男");
		// genderTv.setText(strGender);
		//
		// if (user_icon != null) {
		// ImageView imv = (ImageView) content.findViewById(R.id.msg_icon);
		// imv.setImageDrawable(user_icon);
		// }
		// }
		//
		// // share platform
		// Switch sinaSb = (Switch) content.findViewById(R.id.switch_sina);
		// sinaSb.setChecked(SUPPORT_SINA);
		// sinaSb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// SUPPORT_SINA = isChecked;
		// }
		// });
		//
		// Switch qzoneSb = (Switch) content.findViewById(R.id.switch_qzone);
		// qzoneSb.setChecked(SUPPORT_QZONE);
		// qzoneSb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// SUPPORT_QZONE = isChecked;
		// }
		// });
		//
		// Switch tencSb = (Switch) content.findViewById(R.id.switch_tenc);
		// tencSb.setChecked(SUPPORT_TENC);
		// tencSb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// SUPPORT_TENC = isChecked;
		// }
		// });
		//
		// Switch renrSb = (Switch) content.findViewById(R.id.switch_renr);
		// renrSb.setChecked(SUPPORT_RENR);
		// renrSb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// SUPPORT_RENR = isChecked;
		// }
		// });
		//
		// Switch doubanSb = (Switch) content.findViewById(R.id.switch_douban);
		// doubanSb.setChecked(SUPPORT_DOUBAN);
		// doubanSb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// SUPPORT_DOUBAN = isChecked;
		// }
		// });
		//
		// Switch facebookSb = (Switch)
		// content.findViewById(R.id.switch_facebook);
		// facebookSb.setChecked(SUPPORT_FACEBOOK);
		// facebookSb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// SUPPORT_FACEBOOK = isChecked;
		// }
		// });
		//
		// Switch twitterSb = (Switch)
		// content.findViewById(R.id.switch_twitter);
		// twitterSb.setChecked(SUPPORT_TWITTER);
		// twitterSb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// SUPPORT_TWITTER = isChecked;
		// }
		// });
		//
		// Switch googleSb = (Switch) content.findViewById(R.id.switch_google);
		// googleSb.setChecked(SUPPORT_GOOGLE);
		// googleSb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// SUPPORT_GOOGLE = isChecked;
		// }
		// });

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
