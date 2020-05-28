package com.sdr.patrollib.http;

import com.sdr.lib.http.HttpClient;
import com.sdr.patrollib.SDR_PATROL_BIAOHUA;

import okhttp3.OkHttpClient;

/**
 * Created by HyFun on 2018/12/06.
 * Email: 775183940@qq.com
 * Description:
 */

public class PatrolHttp {
    private static PatrolApi patrolApi;

    public static final PatrolApi getService() {
        if (patrolApi == null) {
            synchronized (PatrolHttp.class) {
                if (patrolApi == null) {
                    OkHttpClient okHttpClient = HttpClient.getInstance().getOkHttpClient();
                    okHttpClient = okHttpClient.newBuilder().addNetworkInterceptor(new PatrolJWTInterceptor()).build();
                    patrolApi = HttpClient.getInstance().createRetrofit(SDR_PATROL_BIAOHUA.getInstance().getUrl(), okHttpClient, PatrolApi.class);
                }
            }
        }
        return patrolApi;
    }
}
