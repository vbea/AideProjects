package com.umeng.soexample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.umeng.soexample.R;
import com.umeng.ui.BaseSinglePaneActivity;

/**
 * @功能描述 : 
 *		Android 4.0 以下的系统显示的首页Activity
 *
 * @原 作 者 :  mrsimple
 * @版 本 号 :  [版本号, Aug 8, 2013]
 *
 * @修 改 人 :  mrsimple
 * @修改内容 :
 */
public class SocialHomeActivity extends BaseSinglePaneActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected Fragment onCreatePane() {

		return new XpHomeFragment();
	}

	/**
	 * 
	 * @author Jhen
	 * 
	 */
	public static class XpHomeFragment extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View root = inflater.inflate(R.layout.umeng_example_socialize_home, container, false);
			root.findViewById(R.id.umeng_example_socialize_home_btn_share_mod).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(), SwitchActivity.class);
							intent.putExtra(SwitchActivity.FLAG_KEY, SwitchActivity.FLAG_SHARE_DEMO);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							getActivity().startActivity(intent);
						}
					});
			root.findViewById(R.id.umeng_example_socialize_home_btn_comment_mod).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(), SwitchActivity.class);
							intent.putExtra(SwitchActivity.FLAG_KEY, SwitchActivity.FLAG_COMMENT_DEMO);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							getActivity().startActivity(intent);
						}
					});
			root.findViewById(R.id.umeng_example_socialize_home_btn_other_mod).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(), SwitchActivity.class);
							intent.putExtra(SwitchActivity.FLAG_KEY, SwitchActivity.FLAG_OTHER_DEMO);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							getActivity().startActivity(intent);
						}
					});
			root.findViewById(R.id.umeng_example_socialize_home_btn_common_config)
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Toast.makeText(getActivity(), "敬请期待。。", 1).show();
						}
					});
			root.findViewById(R.id.umeng_example_socialize_home_btn_analytics_mod).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(), SoclialNavigationActivity.class);
							getActivity().startActivity(intent);
						}
					});
			root.findViewById(R.id.umeng_example_socialize_home_btn_customplatform_mod).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(), CustomPlatformActivity.class);
							getActivity().startActivity(intent);
						}
					});

			return root;
		}
	}
}