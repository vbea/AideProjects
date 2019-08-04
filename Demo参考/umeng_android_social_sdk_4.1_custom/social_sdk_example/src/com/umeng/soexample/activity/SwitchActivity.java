package com.umeng.soexample.activity;

import static com.umeng.soexample.socialize.SocializeConfigDemo.DESCRIPTOR;

import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.utils.Log;
import com.umeng.soexample.socialize.fragments.CommentFragment;
import com.umeng.soexample.socialize.fragments.OtherFragment;
import com.umeng.soexample.socialize.fragments.ShareFragment;
import com.umeng.ui.BaseSinglePaneActivity;

/**
 * @功能描述 : 
 *		即进行不同页面切换的主Activity, 进行Fragment替换. 各种onActivityResult回调在此执行
 *
 * @author :
 * @version:  [版本号, Aug 3, 2013]
 */
public class SwitchActivity extends BaseSinglePaneActivity {
	public static final int FLAG_SHARE_DEMO = 0;
	public static final int FLAG_COMMENT_DEMO = 1;
	public static final int FLAG_OTHER_DEMO = 2;

	public static final String FLAG_KEY = "flag_key";

	private int flag = -1;
	
	private UMSocialService controller;
	private Fragment content = null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		flag = getIntent().getExtras().getInt(FLAG_KEY, -1);
		controller = UMServiceFactory.getUMSocialService(DESCRIPTOR,
																RequestType.SOCIAL);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected Fragment onCreatePane() {
		switch (flag) {
		case FLAG_SHARE_DEMO:
			content = new ShareFragment();
			break;
		case FLAG_COMMENT_DEMO:
			content = new CommentFragment();
			break;
		case FLAG_OTHER_DEMO:
			content = new OtherFragment();
			break;
		}
		return content;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("", "#### ssoHandler.authorizeCallBack11111");
		String result = "null";
		try {
			Bundle b = data.getExtras();
			Set<String> keySet = b.keySet();
			if(keySet.size() > 0)
				result = "result size:"+keySet.size();
			for(String key : keySet){
				Object object = b.get(key);
				Log.d("TestData", "Result:"+key+"   "+object.toString());
			}
		}
		catch (Exception e) {

		}
		Log.d("TestData", "onActivityResult   " + requestCode + "   " + resultCode + "   " + result);
		
	    // 根据requestCode获取对应的SsoHandler
	    UMSsoHandler ssoHandler = controller.getConfig().getSsoHandler(requestCode) ;
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			Log.d("", "#### ssoHandler.authorizeCallBack");
		}
	}
}
