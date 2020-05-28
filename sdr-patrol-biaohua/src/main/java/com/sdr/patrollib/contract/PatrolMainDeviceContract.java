package com.sdr.patrollib.contract;

import com.sdr.lib.mvp.AbstractPresenter;
import com.sdr.lib.mvp.AbstractView;
import com.sdr.patrollib.data.device.PatrolDevice;

/**
 * Created by HyFun on 2018/12/06.
 * Email: 775183940@qq.com
 * Description:
 */

public interface PatrolMainDeviceContract {
    interface View extends AbstractView {
        void loadDeviceInfoFromCodeSuccess(PatrolDevice patrolDevice);

        void loadDataComplete();
    }

    interface Presenter extends AbstractPresenter<View> {
        void getDeviceInfoFromQRcode(String code);
    }
}
