package com.umeng.soexample.activity;

import static com.umeng.soexample.socialize.SocializeConfigDemo.DESCRIPTOR;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.soexample.R;
import com.umeng.soexample.socialize.SocializeConfigDemo;


public class WelActivity extends Activity {
	View parentView;
	Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.umeng_example_socialize_welcome);
		
		com.umeng.socialize.utils.Log.LOG = true;
		SocializeConstants.SHOW_ERROR_CODE = true;
		
		parentView = findViewById(R.id.parent);
		
		if(android.os.Build.VERSION.SDK_INT >= 14){
			intent = new Intent(this, SoclialNavigationActivity.class);
		}else{
			intent = new Intent(this, SocialHomeActivity.class);
		}
		
//		AlphaAnimation showAnim = new AlphaAnimation(0.5f, 1.0f);
//		showAnim.setDuration(500);
//		parentView.startAnimation(showAnim);
		
		//改变全局为Demo可控的config 对象
		UMServiceFactory.getUMSocialService(DESCRIPTOR, RequestType.SOCIAL).setGlobalConfig(SocializeConfigDemo.getSocialConfig(this));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				WelActivity.this.startActivity(intent);
				WelActivity.this.finish();
			}
		},0);
	}
	
	private class UMAnimationListener implements AnimationListener{
		@Override
		public void onAnimationEnd(Animation animation) {
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationStart(Animation animation) {
		}
		
	}
}
