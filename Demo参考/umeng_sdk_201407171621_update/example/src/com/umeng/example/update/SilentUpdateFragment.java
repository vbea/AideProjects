package com.umeng.example.update;

import com.umeng.example.R;
import com.umeng.update.UmengUpdateAgent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SilentUpdateFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.umeng_example_silent_update,
				container, false);
		UmengUpdateAgent.setDefault();
		UmengUpdateAgent.silentUpdate(getActivity());
		return root;
	}
}
