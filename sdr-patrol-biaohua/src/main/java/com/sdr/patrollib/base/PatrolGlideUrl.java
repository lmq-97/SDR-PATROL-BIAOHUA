package com.sdr.patrollib.base;

import com.bumptech.glide.load.model.GlideUrl;

/**
 * Created by HyFun on 2019/05/29.
 * Email: 775183940@qq.com
 * Description:
 */
public class PatrolGlideUrl extends GlideUrl {
    public PatrolGlideUrl(String url) {
        super(format(url));
    }


    private static String format(String url) {
        return url.replaceAll("\\\\", "/");
    }
}
