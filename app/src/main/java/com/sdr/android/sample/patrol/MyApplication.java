package com.sdr.android.sample.patrol;

import android.app.Application;

import com.sdr.lib.SDR_LIBRARY;
import com.sdr.patrollib.SDR_PATROL_BIAOHUA;

/**
 * Created by HyFun on 2020/4/24.
 * Email: 775183940@qq.com
 * Description:
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // SDR LIBRARY
        ActivityConfig activityConfig = new ActivityConfig(getApplicationContext());
        SDR_LIBRARY.register(this, activityConfig);
        // 巡检
        SDR_PATROL_BIAOHUA.getInstance().init(this,BuildConfig.DEBUG,"http://58.240.174.254:9059/mcs/");
    }
}
