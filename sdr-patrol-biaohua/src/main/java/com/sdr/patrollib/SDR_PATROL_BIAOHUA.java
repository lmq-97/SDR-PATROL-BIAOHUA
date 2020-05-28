package com.sdr.patrollib;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.sdr.patrollib.ui.main.PatrolMainActivity;

/**
 * Created by HyFun on 2018/12/06.
 * Email: 775183940@qq.com
 * Description:
 */

public class SDR_PATROL_BIAOHUA {
    private static SDR_PATROL_BIAOHUA SDRPATROLBIAOHUA;

    private SDR_PATROL_BIAOHUA() {
    }

    public static final SDR_PATROL_BIAOHUA getInstance() {
        if (SDRPATROLBIAOHUA == null) {
            synchronized (SDR_PATROL_BIAOHUA.class) {
                if (SDRPATROLBIAOHUA == null) {
                    SDRPATROLBIAOHUA = new SDR_PATROL_BIAOHUA();
                }
            }
        }
        return SDRPATROLBIAOHUA;
    }

    /**
     * 开启巡检主Activity
     *
     * @param context
     * @param patrolUser
     */
    public static final void start(Context context, PatrolUser patrolUser) {
        // 先设置用户
        getInstance().setPatrolUser(patrolUser);
        context.startActivity(new Intent(context, PatrolMainActivity.class));
    }


    private Application application;
    private boolean debug;
    private String url;

    /**
     * 初始化  在application中初始化
     *
     * @param application
     * @param url
     */
    public void init(Application application, boolean debug, String url) {
        this.application = application;
        this.debug = debug;
        this.url = url;
    }

    public Application getApplication() {
        return application;
    }

    public boolean isDebug() {
        return debug;
    }

    public String getUrl() {
        return url;
    }

    private PatrolUser patrolUser;

    public PatrolUser getPatrolUser() {
        return patrolUser;
    }

    public void setPatrolUser(PatrolUser patrolUser) {
        this.patrolUser = patrolUser;
    }

    private PatrolConfig patrolConfig = new PatrolConfig();

    public PatrolConfig getPatrolConfig() {
        return patrolConfig;
    }

    public void setPatrolConfig(PatrolConfig patrolConfig) {
        this.patrolConfig = patrolConfig;
    }

}
