package com.zf.myzxing;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowActivity extends Activity {
	private TextView txt1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show);
		initView();
		initIntent();
	}

	private void initView() {
		txt1 = (TextView) findViewById(R.id.txt1);
	}

	private void initIntent() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			txt1.setText(bundle.getString("msg"));
		}
	}
}
