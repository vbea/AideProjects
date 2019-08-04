package com.umeng.example.update;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.example.R;
import com.umeng.update.UmengUpdateAgent;

public class AutoUpdateFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.umeng_example_auto_update,
				container, false);
		UmengUpdateAgent.setDefault();
		UmengUpdateAgent.update(getActivity());
		return root;
	}
}
