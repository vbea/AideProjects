package com.vbea.video;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.*;
import android.view.View.*;
import android.view.*;

public class MainActivity extends Activity
{
	private VideoView videoView;
	private MediaController mc;
	private LinearLayout li;
	private EditText edit;
	private int hei;
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/*this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
								  WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
		setContentView(R.layout.main);
		videoView = (VideoView) this.findViewById(R.id.videoView);
		edit = (EditText) findViewById(R.id.editFile);
		Button btn_play = (Button) findViewById(R.id.btnPlay);
		li = (LinearLayout) findViewById(R.id.linearLayout);
		mc = new MediaController(this);
		
        //videoView.setVideoURI(Uri.parse(""));
		btn_play.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				videoView.setMediaController(mc);
				videoView.setVideoPath("/sdcard/Android/data/com.vbea.video/video/"+edit.getText().toString());
				videoView.requestFocus();
				videoView.start();
				LinearLayout.LayoutParams lap = (LinearLayout.LayoutParams) li.getLayoutParams();
				hei = lap.height;
				lap.height = 0;

				li.setLayoutParams(lap);
			}
		});
		/*videoView.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				LinearLayout.LayoutParams lap = (LinearLayout.LayoutParams) li.getLayoutParams();
				hei = lap.height;
				lap.height = 0;
				
				li.setLayoutParams(lap);
			}
		});*/
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0, 1, 0, "退出");
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case 1:
				finish();
		}
		return true;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			LinearLayout.LayoutParams lap = (LinearLayout.LayoutParams) li.getLayoutParams();
			if (lap.height == 0)
			{
				lap.height = hei;
			}
			else
			{
				lap.height = 0;
			}
			li.setLayoutParams(lap);
			
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
