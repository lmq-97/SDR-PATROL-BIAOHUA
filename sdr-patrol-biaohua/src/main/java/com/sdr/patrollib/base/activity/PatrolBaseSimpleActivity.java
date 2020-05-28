package com.sdr.patrollib.base.activity;

import android.view.View;

import com.sdr.lib.base.BaseActivity;
import com.sdr.lib.mvp.AbstractView;
import com.sdr.lib.ui.dialog.SDRLoadingDialog;
import com.sdr.lib.util.AlertUtil;
import com.sdr.patrollib.R;

/**
 * Created by HyFun on 2018/12/06.
 * Email: 775183940@qq.com
 * Description:
 */

public class PatrolBaseSimpleActivity extends BaseActivity implements AbstractView {

    /**
     * 获取空数据的
     *
     * @return
     */
    protected View getEmptyView() {
        return getLayoutInflater().inflate(R.layout.sdr_layout_public_empty_view, null);
    }

    // —————————————————————VIEW———————————————————————

    private SDRLoadingDialog progressDialog;

    @Override
    public void showLoadingDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new SDRLoadingDialog.Builder(getContext())
                    .content(msg)
                    .build();
        }
        progressDialog.setContent(msg);
    }

    @Override
    public void hideLoadingDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void showSuccessMsg(String msg, String content) {
        AlertUtil.showPositiveToastTop(msg, content);
    }

    @Override
    public void showErrorMsg(String msg, String content) {
        AlertUtil.showNegativeToastTop(msg, content);
    }

    @Override
    public void showNormalMsg(String msg, String content) {
        AlertUtil.showNormalToastTop(msg, content);
    }

}
