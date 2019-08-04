package com.umeng.soexample.activity;

import static com.umeng.soexample.socialize.SocializeConfigDemo.DESCRIPTOR;
import im.yixin.sdk.api.BaseReq;
import im.yixin.sdk.api.BaseResp;

import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.soexample.socialize.fragments.CustomPlatformFragment;
import com.umeng.ui.BaseSinglePaneActivity;

public class CustomPlatformActivity extends BaseSinglePaneActivity{
	
	@Override
	protected Fragment onCreatePane() {
		CustomPlatformFragment cp  = new CustomPlatformFragment();
		return cp;
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
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
		
	    UMSocialService controller = UMServiceFactory.getUMSocialService(DESCRIPTOR,
							RequestType.SOCIAL);
		// 根据requestCode获取对应的SsoHandler
		UMSsoHandler ssoHandler = controller.getConfig().getSsoHandler(requestCode) ;
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			Log.d("", "#### ssoHandler.authorizeCallBack");
		}
	}

}
