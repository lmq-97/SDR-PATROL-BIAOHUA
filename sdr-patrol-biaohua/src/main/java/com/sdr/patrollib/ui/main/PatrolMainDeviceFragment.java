package com.sdr.patrollib.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sdr.identification.RxSDRDeviceIdentification;
import com.sdr.patrollib.R;
import com.sdr.patrollib.SDR_PATROL_BIAOHUA;
import com.sdr.patrollib.base.fragment.PatrolBaseFragment;
import com.sdr.patrollib.contract.PatrolMainDeviceContract;
import com.sdr.patrollib.data.device.PatrolDevice;
import com.sdr.patrollib.data.device.PatrolDeviceRecord;
import com.sdr.patrollib.presenter.PatrolMainDevicePresenter;
import com.sdr.patrollib.support.PatrolNumNotifyDialog;
import com.sdr.patrollib.support.data.AttachmentLocal;
import com.sdr.patrollib.ui.target_device.PatrolTargetDeviceActivity;
import com.sdr.patrollib.util.PatrolRecordUtil;
import com.sdr.patrollib.util.PatrolUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.functions.Consumer;
import rx_activity_result2.Result;

/**
 * Created by HyFun on 2018/12/06.
 * Email: 775183940@qq.com
 * Description:
 */

public class PatrolMainDeviceFragment extends PatrolBaseFragment<PatrolMainDevicePresenter> implements PatrolMainDeviceContract.View {
    //    private SwipeRefreshLayout swipe;
//    private RecyclerView recyclerView;
    private Button button;

    @Override
    protected PatrolMainDevicePresenter instancePresenter() {
        return new PatrolMainDevicePresenter();
    }

    @Nullable
    @Override
    public View onCreateFragmentView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.patrol_fragment_main_device, null);
    }

    @Override
    protected void bindButterKnife(View view) {
        button = view.findViewById(R.id.patrol_fragment_device_btn_scan);
    }

    @Override
    public String getFragmentTitle() {
        return "设备巡检";
    }

    @Override
    protected void onFragmentFirstVisible() {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开扫码 需要申请相机权限
                RxSDRDeviceIdentification
                        .scan(PatrolMainDeviceFragment.this)
                        .subscribe(new Consumer<Result<Fragment>>() {
                            @Override
                            public void accept(Result<Fragment> fragmentResult) throws Exception {
                                String code = RxSDRDeviceIdentification.Helper.getScanResult(fragmentResult);
                                if (!TextUtils.isEmpty(code)) {
                                    showLoadingDialog("正在获取中...");
                                    presenter.getDeviceInfoFromQRcode(code);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                showErrorMsg(throwable.getMessage(),"");
                            }
                        });
            }
        });
    }

    @Override
    public void loadDeviceInfoFromCodeSuccess(final PatrolDevice patrolDevice) {

        final String title = patrolDevice.getProjectName();

        new PatrolNumNotifyDialog(getContext())
                .setPatrolType(PatrolNumNotifyDialog.PATROL_DEVICE)
                .setTitle(title)
                .setTargetNum(patrolDevice.getPatrolFacilityCheckItemsVos().size())
                .setPositiveListener(new PatrolNumNotifyDialog.OnclickTargetNumConfirmListener() {
                    @Override
                    public void onClick(AlertDialog dialog) {
                        // 点击开始按钮生成默认的record  保存至本地缓存  开启target activity
                        PatrolDeviceRecord record = new PatrolDeviceRecord();
                        record.setFacilityCheckId(patrolDevice.getId());
                        record.setFacilityCheckTitle(title);
                        record.setPatrolTime(new Date().getTime());
                        record.setPatrolEmployeeId(SDR_PATROL_BIAOHUA.getInstance().getPatrolUser().getUserId());
                        record.setPatrolEmployeeName(SDR_PATROL_BIAOHUA.getInstance().getPatrolUser().getUserName());
                        record.setHasReport(0);
                        List<PatrolDeviceRecord.Patrol_FacilityCheckRecordItemContents> contentList = new ArrayList<>();
                        List<PatrolDevice.PatrolFacilityCheckItemsVo> targetList = patrolDevice.getPatrolFacilityCheckItemsVos();
                        for (int i = 0; i < targetList.size(); i++) {
                            PatrolDevice.PatrolFacilityCheckItemsVo target = targetList.get(i);
                            List<PatrolDevice.PatrolFacilityCheckItemsVo.Patrol_FacilityCheckItemContents> pointList = target.getPatrolFacilityCheckItemContents();
                            for (int j = 0; j < pointList.size(); j++) {
                                PatrolDevice.PatrolFacilityCheckItemsVo.Patrol_FacilityCheckItemContents point = pointList.get(j);
                                // 初始化所有的contents
                                PatrolDeviceRecord.Patrol_FacilityCheckRecordItemContents content = new PatrolDeviceRecord.Patrol_FacilityCheckRecordItemContents();
                                content.setFacilityCheckRecordId(patrolDevice.getId());
                                content.setItemId(target.getId() + "");
                                content.setItemName(target.getName());
                                content.setCheckType(point.getCheckType());
                                content.setCheckContentId(point.getId() + "");
                                content.setItemTitle(point.getCheckName());
                                content.setCheckContent(point.getCheckContent());
                                content.setHasError(0);
                                content.setContentDesc(""); // 自己填写的
                                content.setDangerId(PatrolUtil.uuid());
                                content.setMeterReadingType(point.getMeterReadingType());
                                content.setMeterContent("");// 自己填写的
                                content.setMeterLowerLimit(point.getMeterLowerLimit());
                                content.setMeterUpperLimit(point.getMeterUpperLimit());
                                // 还有一个附件
                                content.setAttachmentLocalList(new ArrayList<AttachmentLocal>());
                                contentList.add(content);
                            }
                        }
                        record.setContents(contentList);
                        // 保存至本地
                        PatrolRecordUtil.saveDeviceRecord(patrolDevice, record);
                        // 跳转到巡查界面
                        PatrolTargetDeviceActivity.start(getActivity(), patrolDevice, record);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void loadDataComplete() {
        hideLoadingDialog();
    }
}
