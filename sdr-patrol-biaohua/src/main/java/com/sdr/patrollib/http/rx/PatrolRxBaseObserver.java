package com.sdr.patrollib.http.rx;

import com.sdr.lib.mvp.AbstractView;
import com.sdr.lib.rx.observer.RxObserver;

/**
 * Created by HyFun on 2018/12/06.
 * Email: 775183940@qq.com
 * Description:
 */

public abstract class PatrolRxBaseObserver<T> extends RxObserver<T,AbstractView> {
    public PatrolRxBaseObserver() {
    }

    public PatrolRxBaseObserver(AbstractView mView) {
        super(mView);
    }
}
