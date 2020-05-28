package com.sdr.patrollib.http;

import android.text.TextUtils;

import com.sdr.patrollib.SDR_PATROL_BIAOHUA;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by HyFun on 2018/12/06.
 * Email: 775183940@qq.com
 * Description:
 */

public class PatrolJWTInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (SDR_PATROL_BIAOHUA.getInstance().getPatrolUser() != null && !TextUtils.isEmpty(SDR_PATROL_BIAOHUA.getInstance().getPatrolUser().getAccessToken())) {
            request = request.newBuilder().addHeader("Authorization", SDR_PATROL_BIAOHUA.getInstance().getPatrolUser().getAccessToken()).build();
        }
        return chain.proceed(request);
    }
}
