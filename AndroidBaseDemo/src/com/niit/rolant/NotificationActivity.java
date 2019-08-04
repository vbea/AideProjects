package com.niit.rolant;

import com.cellcom.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

//在状态栏中提示
public class NotificationActivity extends Activity {

	private Button sumButton;
	private Button cloudyButton;
	private Button rainButton;
	private Button defaultSound;
	private NotificationManager notificationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification);
		setTitle("天气预报！");
		sumButton=(Button)findViewById(R.id.sum_1);
		cloudyButton=(Button)findViewById(R.id.cloudy_1);
		rainButton=(Button)findViewById(R.id.rain_1);
		defaultSound=(Button)findViewById(R.id.defaultSound);
		
		sumButton.setOnClickListener(new SumButtonListener());
		cloudyButton.setOnClickListener(new CloudyButtonListener());
		rainButton.setOnClickListener(new RainButtonListener());
		defaultSound.setOnClickListener(new DefaultSoundListener());
		notificationManager=(NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	class SumButtonListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			setWeather("晴","天气","好2",R.drawable.sun);
		}
	}
	
	class CloudyButtonListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			setWeather("阴","天气","一般2",R.drawable.cloudy);
		}
	}
	
	class RainButtonListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			setWeather("下雨","天气","不好2",R.drawable.rain);
		}
	}
	
	class DefaultSoundListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			//播放a.mp3文件
			/*MediaPlayer mediaPlayer;
			mediaPlayer = MediaPlayer.create(NotificationActivity.this, R.drawable.a);
			mediaPlayer.setLooping(true);
			mediaPlayer.start();*/
			//setDefault(Notification.DEFAULT_SOUND);
			
			setSound();
		}
		
	}
	
	private void setWeather(String tickerText,String title,String content,int drawable){
		Notification notification=new Notification(drawable, tickerText, System.currentTimeMillis());
		Intent intent=new Intent();
		intent.setClass(this, MainActivity.class);
		PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, intent, 0);
		notification.setLatestEventInfo(this, title, content, pendingIntent);
		notificationManager.notify(R.layout.notification, notification);
	}
	
	private void setDefault(int defaults){
		Intent intent=new Intent();
		intent.setClass(this, MainActivity.class);
		PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, intent, 0);
		String title="默认： 天气预报";
		String content="晴";
		Notification notification=new Notification(R.drawable.sun,content,System.currentTimeMillis());
		notification.setLatestEventInfo(this, title, content, pendingIntent);
		notification.defaults=defaults;
		notificationManager.notify(R.layout.notification, notification);
	}
	
	private void setSound(){
		Intent intent=new Intent();
		intent.setClass(this, MainActivity.class);
		PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, intent, 0);
		String title="默认： 声音测试";
		String content="晴";
		Notification notification=new Notification(R.drawable.sun,content,System.currentTimeMillis());
		notification.setLatestEventInfo(this, title, content, pendingIntent);
		notification.sound=Uri.parse("file:///sdcard/mp3/nobody.mp3");
		notificationManager.notify(R.layout.notification, notification);
	}
}
