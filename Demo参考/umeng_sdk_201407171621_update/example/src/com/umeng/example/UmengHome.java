package com.umeng.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.umeng.example.analytics.AnalyticsHome;
import com.umeng.example.fb.FbHome;
import com.umeng.example.update.UpdateHome;
import com.umeng.ui.BaseSinglePaneActivity;

public class UmengHome extends BaseSinglePaneActivity {
	private static boolean OPTIONS_BACK_BUTTON_KILL_PROCESS = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// swtich this on to show debug log. Remember to set this to false before you release the app
	}

	@Override
	protected Fragment onCreatePane() {
		return new UmengHomeDashboardFragment();
	}

	public static class UmengHomeDashboardFragment extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View root = inflater.inflate(
					R.layout.umeng_example_home_dashboard_fragment, container,
					false);
			root.findViewById(R.id.umeng_example_home_btn_analytics)
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							startActivity(new Intent(getActivity(),
									AnalyticsHome.class));
						}
					});
			/*
			 * root.findViewById(R.id.umeng_example_home_btn_tools)
			 * .setOnClickListener(new View.OnClickListener() {
			 * 
			 * @Override public void onClick(View v) { startActivity(new
			 * Intent(getActivity(), FbHome.class)); }
			 * 
			 * });
			 */

			root.findViewById(R.id.umeng_example_home_btn_update)
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							startActivity(new Intent(getActivity(),
									UpdateHome.class));
						}

					});
			root.findViewById(R.id.umeng_example_home_btn_fb)
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							startActivity(new Intent(getActivity(),
									FbHome.class));
							/*
							 * UMFeedbackService.setGoBackButtonVisible();
							 * UMFeedbackService
							 * .openUmengFeedbackSDK(UmengHomeDashboardFragment
							 * .this .getActivity());
							 */
						}
					});
			root.findViewById(R.id.umeng_example_home_btn_plus)
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Toast.makeText(getActivity(),
									R.string.umeng_example_home_hint_wait,
									Toast.LENGTH_LONG).show();
						}
					});
			return root;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (OPTIONS_BACK_BUTTON_KILL_PROCESS) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				int pid = android.os.Process.myPid();
				android.os.Process.killProcess(pid);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
