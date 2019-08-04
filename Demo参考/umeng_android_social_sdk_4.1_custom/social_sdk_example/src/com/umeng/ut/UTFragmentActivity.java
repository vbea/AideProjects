package com.umeng.ut;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.umeng.socialize.common.SocializeConstants;
import com.umeng.ui.BaseSinglePaneActivity;

public class UTFragmentActivity extends BaseSinglePaneActivity {

	public static final String FLAG_CLASS = "class-name";
	private String strClz;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle extras = getIntent().getExtras();
		strClz = extras != null ? extras.getString(FLAG_CLASS) :  null;
		super.onCreate(savedInstanceState);
		
		com.umeng.socialize.utils.Log.LOG = true;
		SocializeConstants.SHOW_ERROR_CODE = true;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Fragment onCreatePane() {
		try {
			Class<Fragment> clz = (Class<Fragment>) Class.forName(strClz);
			return clz.newInstance();
		}
		catch (Exception e) {
			throw new RuntimeException("", e);
		}
	}

}
