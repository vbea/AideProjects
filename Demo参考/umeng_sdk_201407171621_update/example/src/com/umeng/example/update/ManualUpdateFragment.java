package com.umeng.example.update;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.umeng.example.R;
import com.umeng.update.UmengUpdateAgent;

public class ManualUpdateFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.umeng_example_manual_update,
				container, false);
		Button checkUpdate = (Button) root
				 .findViewById(R.id.umeng_example_update_btn_check_update);
		checkUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UmengUpdateAgent.setDefault();
				UmengUpdateAgent.forceUpdate(getActivity());
			}
		});
		return root;
	}
}
