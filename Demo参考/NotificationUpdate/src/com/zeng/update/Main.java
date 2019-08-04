package com.zeng.update;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
	private static final String TAG = "Main";
	private MyApp app;
	private int currentVersionCode;
	private String versiontext;
	private UpdataInfo info;
	private TextView tv_splash_version;
	private LinearLayout ll_splash_main;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 判断服务器版本号 和客户端的版本号 是否相同
			if (isNeedUpdate(versiontext)) {
				Log.i(TAG, "弹出来升级对话框");
				showUpdateDialog();
			}
		}
	};
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 取消标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		
		app = (MyApp) getApplication();

		ll_splash_main = (LinearLayout) this.findViewById(R.id.ll_splash_main);
		tv_splash_version = (TextView) this
				.findViewById(R.id.tv_splash_version);
		versiontext = getVersion();
		
		// 让当前的activity延时两秒钟 检查更新
		new Thread(){

			@Override
			public void run() {
				super.run();
				try {
					sleep(2000);
					handler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}.start();
		
		


		tv_splash_version.setText(versiontext);
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(2000);
		ll_splash_main.startAnimation(aa);

		// 完成窗体的全屏显示 // 取消掉状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
			/*
				// TODO Auto-generated method stub
				PackageManager manager = Main.this.getPackageManager();
				try {
					PackageInfo info = manager.getPackageInfo(Main.this.getPackageName(), 0);
					String appVersion = info.versionName; // 版本名
					currentVersionCode = info.versionCode; // 版本号
					System.out.println(currentVersionCode + " " + appVersion);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch blockd
					e.printStackTrace();
				}
				//上面是获取manifest中的版本数据，我是使用versionCode
				//在从服务器获取到最新版本的versionCode,比较
				showUpdateDialog();*/
			}

	
	private void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("检测到新版本");
		builder.setMessage("是否下载更新?");
		builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent it = new Intent(Main.this, NotificationUpdateActivity.class);
				startActivity(it);
//				MapApp.isDownload = true;
				app.setDownload(true);
				//loadMainUI();
				finish();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				loadMainUI();
			}
		});
		builder.show();
	}
	

	/**
	 * 
	 * @param versiontext
	 *            当前客户端的版本号信息
	 * @return 是否需要更新
	 */
	private boolean isNeedUpdate(String versiontext) {
		UpdataInfoService service = new UpdataInfoService(this);
		try {
			info = service.getUpdataInfo(R.string.updataurl);
			String version = info.getVersion();
			if (versiontext.equals(version)) {
				Log.i(TAG, "版本相同,无需升级, 进入主界面");
				loadMainUI();
				return false;
			} else {
				Log.i(TAG, "版本不同,需要升级");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "获取更新信息异常", 0).show();
			Log.i(TAG, "获取更新信息异常, 进入主界面");
			loadMainUI();
			return false;
		}

	}
	
	private void loadMainUI() {
		Intent intent = new Intent(this, MainTabActivity.class);
		startActivity(intent);
		finish(); // 把当前activity从任务栈里面移除

	}
	
	/**
	 * 获取当前应用程序的版本号
	 * 
	 * @return
	 */
	private String getVersion() {
		try {
			PackageManager manager = getPackageManager();
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (Exception e) {

			e.printStackTrace();
			return "版本号未知";
		}
	}
}
