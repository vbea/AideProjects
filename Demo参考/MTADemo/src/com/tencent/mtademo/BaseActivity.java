package com.tencent.mtademo;

import java.util.Properties;

import com.tencent.stat.EasyActivity;
import com.tencent.stat.EasyListActivity;
import com.tencent.stat.StatService;

import android.app.Activity;

/**
 * 建议开发者在app中定义的activity基类，可在此基类中的onPause和onResume函数分别调用MTA对应接口，<br />
 * 然后在子类中分别调用super.onResume()和super.onPause()实现自动打点。<br />
 * 同时，MTA也提供了类似的基类{@link EasyActivity}和{@link EasyListActivity}，可直接继承使用
 * 
 * @author foreachli
 * 
 */
public class BaseActivity extends Activity {

    @Override
    protected void onResume() {
        super.onResume();
        // 页面开始
        StatService.onResume(this);
    }

    public void onActivityOpen() { // 你的函数
        Properties prop = new Properties();
        prop.setProperty("aty", "抢票活动"); // 活动页面
        prop.setProperty("gid", "潜在付费用户"); // 用户组名称
        prop.setProperty("attr", "1"); // 用户属性（年龄、性别等）
        prop.setProperty("act_type", "1"); // 行为类型（最近30天启动过、最近30天使用时长超过、最近30天是否有过升级行为等）
        prop.setProperty("act_val", "10"); // 行为取值
        StatService.trackCustomKVEvent(this, "mta_tag_activity_open", prop);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 页面结束
        StatService.onPause(this);
    }

}
