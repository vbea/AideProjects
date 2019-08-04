package com.tencent.mtademo;

import java.util.Properties;

import com.tencent.stat.StatConfig;
import com.tencent.stat.StatGameUser;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatLogger;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 登陆界面<br />
 * 建议开发者在自己的Activity基类中onPause和onResume函数调用对应的MTA接口，见{@link BaseActivity}
 * ，实现自动打点。<br />
 * 也可以在每个Activity类中的onPause和onResume函数调用MTA的{@link StatService#onPause(Context)}
 * 和{@link StatService#onResume(Context)}接口。
 * 
 * @author foreachli
 * 
 */
// 以继承BaseActivity基类的方式实现自动打点
// public class MTAMainActivity extends BaseActivity
public class MTAMainActivity extends Activity {

	private static StatLogger logger = new StatLogger("MTADemon");
	private Context ctx = null;
	private Button btn_sign_in;
	private Button btn_not_sign_in;
	private EditText txt_account;
	private EditText txt_passwd;

	static StatLogger getLogger() {
		return logger;
	}

	private void switchToFirstActivity() {
		Intent intent = new Intent();
		intent.setClass(MTAMainActivity.this, FirstActivity.class);
		startActivity(intent);
		// finish();
	}

	private boolean check(String acc, String pwd) {
		return acc.trim().length() > 0 && pwd.trim().length() > 0;
	}

	private View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.btn_sign_in:
				String account = txt_account.getText().toString();
				String pwd = txt_passwd.getText().toString();
				if (!check(account, pwd)) {
					Toast.makeText(ctx, "嘿，哥们，好歹输入点什么吧？\n若不需要登陆请使用左边的通道。", -1)
							.show();
					// 上报登陆失败事件
					StatService.trackCustomEvent(ctx, "login", "false");
					return;
				}

				// 在能够采集QQ号码的情况下，尽量上报QQ号码，用于用户画像分析
				StatService.reportQQ(ctx, account);
				StatGameUser gameUser = new StatGameUser();
				// 如果是游戏用户，调用游戏接口
				gameUser.setWorldName("world1");
				gameUser.setAccount(account);
				gameUser.setLevel("100");
				StatService.reportGameUser(ctx, gameUser);
				// 上报登陆成功自定义事件，统计登陆次数和用户数
				StatService.trackCustomEvent(ctx, "login", "true");
				// MTA登陆的标准事件
				Properties prop = new Properties();
				prop.setProperty("uid", account);
				StatService.trackCustomKVEvent(ctx, "OnLogin", prop);
				switchToFirstActivity();
				break;
			case R.id.btn_not_sign_in:
				Toast.makeText(ctx, "恭喜你越狱成功！", -1).show();
				// 上报非登陆自定义事件
				StatService.trackCustomEvent(ctx, "not_login", "1");
				StatConfig.setCustomUserId(ctx, "aa");
				switchToFirstActivity();
				break;
			default:
				break;
			}

		}

	};

	private void initActivity() {
		setTitle("登陆");
		ctx = this;
		btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
		btn_not_sign_in = (Button) findViewById(R.id.btn_not_sign_in);
		btn_sign_in.setOnClickListener(listener);
		btn_not_sign_in.setOnClickListener(listener);
		txt_account = (EditText) findViewById(R.id.txt_account);
		txt_passwd = (EditText) findViewById(R.id.txt_passwd);
	}

	/**
	 * 根据不同的模式，建议设置的开关状态，可根据实际情况调整，仅供参考。
	 * 
	 * @param isDebugMode
	 *            根据调试或发布条件，配置对应的MTA配置
	 */
	private void initMTAConfig(boolean isDebugMode) {
		logger.d("isDebugMode:" + isDebugMode);
		if (isDebugMode) { // 调试时建议设置的开关状态
			// 查看MTA日志及上报数据内容
			StatConfig.setDebugEnable(true);
			// 禁用MTA对app未处理异常的捕获，方便开发者调试时，及时获知详细错误信息。
			// StatConfig.setAutoExceptionCaught(false);
			// StatConfig.setEnableSmartReporting(false);
			// Thread.setDefaultUncaughtExceptionHandler(new
			// UncaughtExceptionHandler() {
			//
			// @Override
			// public void uncaughtException(Thread thread, Throwable ex) {
			// logger.error("setDefaultUncaughtExceptionHandler");
			// }
			// });
			// 调试时，使用实时发送
			StatConfig.setStatSendStrategy(StatReportStrategy.INSTANT);
		} else { // 发布时，建议设置的开关状态，请确保以下开关是否设置合理
			// 禁止MTA打印日志
			StatConfig.setDebugEnable(false);
			// 根据情况，决定是否开启MTA对app未处理异常的捕获
			StatConfig.setAutoExceptionCaught(true);
			// 选择默认的上报策略
			StatConfig.setStatSendStrategy(StatReportStrategy.APP_LAUNCH);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mtamain);
		

		// 打开debug开关，可查看mta上报日志或错误
		// 发布时，请务必要删除本行或设为false
		StatConfig.setDebugEnable(true);

		// 获取MTA MID等信息
		logger.d(StatConfig.getDeviceInfo(this).toString());
		// 用户自定义UserId
		// StatConfig.setCustomUserId(this, "1234");
		java.util.UUID.randomUUID();
		initActivity();

		// androidManifest.xml指定本activity最先启动，因此，MTA的初始化工作需要在onCreate中进行
		// 为了使得MTA配置及时生效，请确保MTA配置在调用StatService之前已被调用。
		// 推荐是在Activity.onCreate处初始化MTA设置
		// 根据不同的模式：调试或发布，初始化MTA设置
		initMTAConfig(true);

		/**
		 * 调用MTA一般需要3步：
		 * 1：配置manifest.xml权限
		 * 2：调用StatConfig相关的配置接口配置MTA
		 * 3:调用StatService相关的接口，开始统计！
		 */

		// StatCommonHelper.getLogger().setLogLevel(Log.VERBOSE);
		// 初始化并启动MTA
		// 第三方SDK必须按以下代码初始化MTA，其中appkey为规定的格式!!!
		// 其它普通的app可自行选择是否调用
		// try {
		// // 第三个参数必须为：com.tencent.stat.common.StatConstants.VERSION
		// // 用于MTA SDK版本冲突检测
		// StatService.startStatService(this, "A6TPGR6K3V94",
		// com.tencent.stat.common.StatConstants.VERSION);
		// } catch (com.tencent.stat.MtaSDkException e) {
		// // MTA初始化失败
		// logger.error("MTA start failed.");
		// }

		// // 获取在线参数
		// String onlineValue = StatConfig.getCustomProperty("onlineKey");
		// if(onlineValue.equalsIgnoreCase("on")){
		// // do something
		// }else{
		// // do something
		// }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_mtamain, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 如果本Activity是继承基类BaseActivity的，可注释掉此行。
		StatService.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 如果本Activity是继承基类BaseActivity的，可注释掉此行。
		StatService.onPause(this);
	}
}
