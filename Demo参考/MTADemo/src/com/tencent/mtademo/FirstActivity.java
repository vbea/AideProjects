package com.tencent.mtademo;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.tencent.stat.StatAppMonitor;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatLogger;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends Activity {
	StatLogger logger = MTAMainActivity.getLogger();

	private Context ctx = null;
	private Button btn_customevent_args_count = null;
	private Button btn_customevent_kv_count = null;
	private Button btn_customevent_args_duration_begin = null;
	private Button btn_customevent_args_duration_end = null;
	private Button btn_customevent_kv_duration_begin = null;
	private Button btn_customevent_kv_duration_end = null;
	private Button btn_report_error = null;
	private Button btn_report_exception = null;
	private Button btn_catch_unhandled_exception = null;
	private Button btn_init_nativeCrash = null;
	private Button btn_catch_native_crash = null;
	private Button btn_monitor_events = null;

	public void onActivityOpen() { // 你的函数
		Properties prop = new Properties();
		prop.setProperty("aty", "抢票活动"); // 活动页面
		prop.setProperty("gid", "潜在付费用户"); // 用户组名称
		prop.setProperty("attr", "1"); // 用户属性（年龄、性别等）
		prop.setProperty("act_type", "1"); // 行为类型（最近30天启动过、最近30天使用时长超过、最近30天是否有过升级行为等）
		prop.setProperty("act_val", "10"); // 行为取值
		StatService.trackCustomKVEvent(this, "mta_tag_activity_open", prop);
	}

	public void onActivityClick2() { // 你的函数
		Properties prop = new Properties();
		prop.setProperty("aty", "抢票活动"); // 活动页面
		prop.setProperty("btn", "报名"); // 按钮
		prop.setProperty("gid", "潜在付费用户"); // 用户组名称
		prop.setProperty("attr ", "1"); // 用户属性（年龄、性别等）
		prop.setProperty("act_type ", "1"); // 行为类型（最近30天启动过、最近30天使用时长超过、最近30天是否有过升级行为等）
		prop.setProperty("act_val ", "10"); // 行为取值
		StatService.trackCustomKVEvent(this, "mta_tag_activity_click", prop);
	}

	public void onUserPay() { // 你的函数
		Properties prop = new Properties();
		prop.setProperty("scene", "通关"); // 付费场景
		prop.setProperty("amount", "350"); // 付费金额
		prop.setProperty("way", "手机支付"); // 付费方式（可选择上报）
		StatService.trackCustomKVEvent(this, "mta_tag_user_pay", prop);
	}

	private View.OnClickListener l = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (ctx == null) {
				return;
			}
			int id = v.getId();
			switch (id) {
			case R.id.btn_customevent_args_count:
				StatService.trackCustomEvent(ctx, "trackCustomEvent", "args");
				StatService.trackCustomEvent(ctx, "trackCustomEvent", "");
				StatService.trackCustomEvent(ctx, "trackCustomEvent", null);
				break;
			case R.id.btn_customevent_kv_count:
				Properties prop = new Properties();
				prop.setProperty("key", "value");
				prop.setProperty("key2", "value2");
				StatService.trackCustomKVEvent(ctx, "trackCustomKVEvent", prop);
				prop = new Properties();
				prop.setProperty("a", "b");
				StatService.trackCustomKVEvent(ctx, "trackCustomKVEvent",
						new Properties());
				StatService.trackCustomKVEvent(ctx, "trackCustomKVEvent", null);

				prop = new Properties();
				prop.setProperty("num", "3434");
				StatService.trackCustomKVEvent(ctx, "trackCustomKVEvent", prop);
				break;
			case R.id.btn_customevent_args_duration_begin:
				StatService.trackCustomBeginEvent(ctx, "trackCustomEvent",
						"loadConfigFile");
				break;
			case R.id.btn_customevent_args_duration_end:
				StatService.trackCustomEndEvent(ctx, "trackCustomEvent",
						"loadConfigFile");
				break;
			case R.id.btn_customevent_kv_duration_begin:
				Properties properties = new Properties();
				properties.setProperty("load", "config");
				StatService.trackCustomBeginKVEvent(ctx, "trackCustomEvent",
						properties);
				break;
			case R.id.btn_customevent_kv_duration_end:
				properties = new Properties();
				properties.setProperty("load", "config");
				StatService.trackCustomEndKVEvent(ctx, "trackCustomEvent",
						properties);
				properties.clear();
				break;
			case R.id.btn_report_error:
				StatService.reportError(ctx, "I hate error.");
				break;
			case R.id.btn_report_exception:
				try {
					String myNull = null;
					int length = myNull.length();
				} catch (NullPointerException ex) {
					StatService.reportException(ctx, ex);
				}
				break;
			case R.id.btn_monitor_events:
				// 新建监控接口对象
				StatAppMonitor monitor = new StatAppMonitor("ping:www.qq.com");
				// 接口开始执行
				String ip = "www.qq.com";
				Runtime run = Runtime.getRuntime();
				java.lang.Process proc = null;
				try {
					String str = "ping -c 3 -i 0.2 -W 1 " + ip;
					long starttime = System.currentTimeMillis();
					proc = run.exec(str);
					int retCode = proc.waitFor();
					long difftime = System.currentTimeMillis() - starttime;
					// 设置接口耗时
					monitor.setMillisecondsConsume(difftime);
					// 设置接口返回码
					monitor.setReturnCode(retCode);
					// 设置请求包大小，若有的话
					monitor.setReqSize(1000);
					// 设置响应包大小，若有的话
					monitor.setRespSize(2000);
					// 设置抽样率，默认为1，表示100%。如果是50%，则填2(100/50)，如果是25%，则填4(100/25)，以此类推。
					// monitor.setSampling(2);
					if (retCode == 0) {
						logger.debug("ping连接成功");
						// 标记为成功
						monitor.setResultType(StatAppMonitor.SUCCESS_RESULT_TYPE);
					} else {
						logger.debug("ping测试失败");
						// 标记为逻辑失败，可能由网络未连接等原因引起的，但对于业务来说不是致命的，是可容忍的
						monitor.setResultType(StatAppMonitor.LOGIC_FAILURE_RESULT_TYPE);
					}
				} catch (Exception e) {
					logger.e(e);
					// 接口调用出现异常，致命的，标识为失败
					monitor.setResultType(StatAppMonitor.FAILURE_RESULT_TYPE);
				} finally {
					proc.destroy();
				}
				// 上报接口监控的信息
				StatService.reportAppMonitorStat(ctx, monitor);

				
				
				Map<String, Integer> map = new HashMap<String, Integer>();
				map.put("www.qq.com", 80);
				map.put("pingma.qq.com", 80);
				StatService.testSpeed(ctx, map);
				StatService.testSpeed(ctx);
				break;
			case R.id.btn_catch_unhandled_exception:
				int i = 1 / 0;
				break;
			case R.id.btn_init_nativeCrash:
				String tombfilepath = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/my";
				StatConfig.initNativeCrashReport(ctx, null);
				// StatConfig.setCustomUserId(ctx, "myuid");
				// StatService.commitEvents(ctx, -1);
				break;
			case R.id.btn_catch_native_crash:
				StatService.startNewSession(ctx);
//				StatNativeCrashReport.doNativeCrashTest();
				// StatService.startNewSession(ctx);
				// StatConfig.setCustomUserId(ctx, null);
				break;
			default:
				break;
			}
		}
	};

	String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
				.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		// 高级功能：在线配置更新
		// String onOrOff = StatConfig.getCustomProperty("switch", "off");
		// logger.debug("switch" + onOrOff);
		// if(onOrOff.equalsIgnoreCase("on")){
		// // 打开某项功能
		// }else{
		// // 禁用某项功能
		// }
		// String appid = "互联的appid";
		// String appkey = "Aqc" + appid;
		// StatConfig.setAutoExceptionCaught(false); // 禁止捕获app未处理的异常
		// StatConfig.setEnableSmartReporting(true); // 禁止WIFI网络实时上报
		// StatConfig.setSendPeriodMinutes(24 * 60); // PERIOD间隔周期，24小时
		// StatConfig.setStatSendStrategy(StatReportStrategy.PERIOD); //
		// PERIOD上报策略
		// try {
		// StatService.startStatService(this, appkey,
		// com.tencent.stat.common.StatConstants.VERSION);
		// } catch (MtaSDkException e) {
		// Log.e("DEBUG", "MTA init Failed.");
		// }
		initActivity();
	}

	private void initActivity() {
		setTitle("常用统计分析");
		ctx = this;

		btn_customevent_args_count = (Button) findViewById(R.id.btn_customevent_args_count);
		btn_customevent_kv_count = (Button) findViewById(R.id.btn_customevent_kv_count);
		btn_customevent_args_duration_begin = (Button) findViewById(R.id.btn_customevent_args_duration_begin);
		btn_customevent_args_duration_end = (Button) findViewById(R.id.btn_customevent_args_duration_end);
		btn_customevent_kv_duration_begin = (Button) findViewById(R.id.btn_customevent_kv_duration_begin);
		btn_customevent_kv_duration_end = (Button) findViewById(R.id.btn_customevent_kv_duration_end);
		btn_report_error = (Button) findViewById(R.id.btn_report_error);
		btn_report_exception = (Button) findViewById(R.id.btn_report_exception);
		btn_catch_unhandled_exception = (Button) findViewById(R.id.btn_catch_unhandled_exception);
		btn_catch_native_crash = (Button) findViewById(R.id.btn_catch_native_crash);
		btn_init_nativeCrash = (Button) findViewById(R.id.btn_init_nativeCrash);
		btn_monitor_events = (Button) findViewById(R.id.btn_monitor_events);

		btn_customevent_args_count.setOnClickListener(l);
		btn_customevent_kv_count.setOnClickListener(l);
		btn_customevent_args_duration_begin.setOnClickListener(l);
		btn_customevent_args_duration_end.setOnClickListener(l);
		btn_customevent_kv_duration_begin.setOnClickListener(l);
		btn_customevent_kv_duration_end.setOnClickListener(l);
		btn_report_error.setOnClickListener(l);
		btn_report_exception.setOnClickListener(l);
		btn_catch_unhandled_exception.setOnClickListener(l);
		btn_init_nativeCrash.setOnClickListener(l);
		btn_catch_native_crash.setOnClickListener(l);
		btn_monitor_events.setOnClickListener(l);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_first, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}
}
