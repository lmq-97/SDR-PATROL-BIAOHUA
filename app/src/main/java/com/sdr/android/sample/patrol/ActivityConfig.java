package com.sdr.android.sample.patrol;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.sdr.lib.base.BaseActivityConfig;

/**
 * Created by HyFun on 2019/05/23.
 * Email: 775183940@qq.com
 * Description:
 */

public class ActivityConfig extends BaseActivityConfig {

    public ActivityConfig(Context context) {
        super(context);
    }

    @Override
    public int onHeaderBarStatusViewAlpha() {
        return super.onHeaderBarStatusViewAlpha();
    }

    @Override
    public int onHeaderBarTitleGravity() {
        return super.onHeaderBarTitleGravity();
    }

    @Override
    public int onHeaderBarToolbarRes() {
        return R.layout.layout_public_toolbar_white;
    }

    @Override
    public Drawable onHeaderBarDrawable() {
        return new ColorDrawable(context.getResources().getColor(R.color.colorPrimary));
    }
}
