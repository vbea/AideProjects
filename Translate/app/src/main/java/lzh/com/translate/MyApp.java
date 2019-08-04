package lzh.com.translate;

import android.app.Application;

/**
 *
 * Created by laizuhong on 2016/12/22.
 */

public class MyApp extends Application{
    public static MyApp instance;

    public static MyApp getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }
}
