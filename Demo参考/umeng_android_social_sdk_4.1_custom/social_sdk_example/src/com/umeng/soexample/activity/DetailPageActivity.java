package com.umeng.soexample.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.view.ActionBarView;
import com.umeng.soexample.R;

public class DetailPageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.umeng_example_socialize_home_activity);

		Bundle extras = getIntent().getExtras();
		String ad_words = extras.getString("ad_words");
		String des = extras.getString("des");
		Bitmap bt = (Bitmap) extras.get("bitmap");

		TextView tv = (TextView) findViewById(R.id.text);
		tv.setText(ad_words + "\r\r\r"+des);
		ViewGroup parent = (ViewGroup) findViewById(R.id.root);

		ActionBarView socializeActionBar = new ActionBarView(this, ad_words);

		LayoutParams layoutParams = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		socializeActionBar.setLayoutParams(layoutParams);

		UMSocialService controller = UMServiceFactory.getUMSocialService(ad_words, RequestType.SOCIAL);
		controller.setShareContent(des);
		parent.addView(socializeActionBar);
		
		if(bt != null){
			controller.setShareMedia(new UMImage(this, bt)) ;
			//controller.setShareImage(new UMImage(this, bt));
			Log.d("Socail", "extra bitmap....");
		}else{
			Log.d("Socail", "no bitmap....");
		}

	}

}
