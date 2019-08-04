package com.umeng.example.fb;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.example.R;
import com.umeng.fb.FeedbackAgent;
import com.umeng.ui.BaseSinglePaneActivity;

public class FbHome extends BaseSinglePaneActivity {
	private static final String TAG = FbHome.class.getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected Fragment onCreatePane() {
		return new FbHomeFragment();
	}

	/**
	 * Do not change this to anonymous class as it will crash when orientation
	 * changes.
	 * 
	 * @author GC
	 * 
	 */
	public static class FbHomeFragment extends Fragment {
		Context mContext;
		FeedbackAgent agent;

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			mContext = activity;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Log.d(TAG, "onCreateView");
			View root = inflater.inflate(R.layout.umeng_example_fb_home,
					container, false);
			agent = new FeedbackAgent(this.getActivity());
			// to sync the default conversation. If there is new reply, there
			// will be notification in the status bar. If you do not want
			// notification, you can use
			// agent.getDefaultConversation().sync(listener);
			// instead.

			agent.sync();

			root.findViewById(R.id.umeng_example_fb_home_btn_simple)
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							agent.startFeedbackActivity();
						}
					});
			return root;
		}
	}
}