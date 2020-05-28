package com.sdr.patrollib.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sdr.lib.SDR_LIBRARY;
import com.sdr.patrollib.R;
import com.sdr.patrollib.SDR_PATROL_BIAOHUA;
import com.sdr.patrollib.base.activity.PatrolBaseActivity;
import com.sdr.patrollib.base.adapter.PatrolBaseFragmentPagerAdapter;
import com.sdr.patrollib.base.fragment.PatrolBaseSimpleFragment;
import com.sdr.patrollib.contract.PatrolMainContract;
import com.sdr.patrollib.data.device.PatrolDevice;
import com.sdr.patrollib.data.device.PatrolDeviceRecord;
import com.sdr.patrollib.data.project.PatrolProject;
import com.sdr.patrollib.data.project.PatrolProjectRecord;
import com.sdr.patrollib.presenter.PatrolMainPresenter;
import com.sdr.patrollib.support.PatrolFunctions;
import com.sdr.patrollib.support.PatrolUnFinishDialog;
import com.sdr.patrollib.support.data.PatrolTaskLocal;
import com.sdr.patrollib.ui.danger_handle.PatrolDangerListActivity;
import com.sdr.patrollib.ui.history.PatrolHistoryActivity;
import com.sdr.patrollib.ui.target_device.PatrolTargetDeviceActivity;
import com.sdr.patrollib.ui.target_project.PatrolTargetProjectActivity;
import com.sdr.patrollib.util.PatrolRecordUtil;
import com.sdr.patrollib.util.PatrolUtil;

import java.util.ArrayList;
import java.util.List;

public class PatrolMainActivity extends PatrolBaseActivity<PatrolMainPresenter> implements PatrolMainContract.View {

    private ImageView viewHeaderImage;
    private View viewHeaderContainer;
    private TextView viewHeaderTvUserName;
    private TextView viewHeaderTvUserPhone;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    // 操作菜单
    private View viewOperateDangerList;
    private View viewOperateDangerQuery;
    private View viewOperateHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_main);
        setDisplayHomeAsUpEnabled();
        initView();
        initData();
        initListener();
    }

    @Override
    protected PatrolMainPresenter instancePresenter() {
        return new PatrolMainPresenter();
    }

    private void initView() {
        viewHeaderImage = findViewById(R.id.patrol_main_header_iv_background);
        viewHeaderContainer = findViewById(R.id.patrol_main_header_container);
        viewHeaderTvUserName = findViewById(R.id.patrol_main_header_tv_username);
        viewHeaderTvUserPhone = findViewById(R.id.patrol_main_header_tv_userphone);
        tabLayout = findViewById(R.id.patrol_main_tab);
        viewPager = findViewById(R.id.patrol_main_viewpager);

        viewOperateDangerList = findViewById(R.id.patrol_main_ll_todo_task);
        viewOperateDangerQuery = findViewById(R.id.patrol_main_ll_danger_query);
        viewOperateHistory = findViewById(R.id.patrol_main_ll_inspect_history);

    }

    private void initData() {
        // header
        {
            SDR_LIBRARY.getInstance().getGlide().with(getContext()).load(R.mipmap.patrol_main_header_image).into(viewHeaderImage);
            viewHeaderContainer.setPadding(0, getHeaderBarHeight(), 0, 0);
            viewHeaderTvUserName.setText(SDR_PATROL_BIAOHUA.getInstance().getPatrolUser().getUserName());
            viewHeaderTvUserPhone.setText(SDR_PATROL_BIAOHUA.getInstance().getPatrolUser().getPhoneNum());
        }

        // container
        {
            int selectColor = getResources().getColor(R.color.colorPrimary);
            int normalColor = PatrolUtil.getColor60(selectColor);
            tabLayout.setTabTextColors(normalColor, selectColor);

            List<PatrolBaseSimpleFragment> fragmentList = new ArrayList<>();
            fragmentList.add(new PatrolMainProjectFragment());

            // 如果排除的功能列表中没有 则添加设备巡检
            if (!SDR_PATROL_BIAOHUA.getInstance().getPatrolUser().getExceptFuctionList().contains(PatrolFunctions.设备巡检)) {
                fragmentList.add(new PatrolMainDeviceFragment());
            }

            PatrolBaseFragmentPagerAdapter baseFragmentPagerAdapter = new PatrolBaseFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
            viewPager.setAdapter(baseFragmentPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }

        // 未完成任务
        {
            List<PatrolTaskLocal> patrolTaskLocals = new ArrayList<>();
            // 设备巡检
            PatrolDevice device = PatrolRecordUtil.getDevice();
            PatrolDeviceRecord deviceRecord = PatrolRecordUtil.getDeviceRecord();
            if (device != null && deviceRecord != null && !PatrolUtil.isRecordTimeOut(deviceRecord.getPatrolTime())) {
                patrolTaskLocals.add(new PatrolTaskLocal(PatrolTaskLocal.PATROL_TYPE_DEVICE, device, deviceRecord));
            }
            // 工程巡检
            PatrolProject mobile = PatrolRecordUtil.getProject();
            PatrolProjectRecord mobileRecord = PatrolRecordUtil.getProjectRecord();
            if (mobile != null && mobileRecord != null && !PatrolUtil.isRecordTimeOut(mobileRecord.getPatrolStartTime())) {
                patrolTaskLocals.add(new PatrolTaskLocal(PatrolTaskLocal.PATROL_TYPE_MOBILE, mobile, mobileRecord));
            }

            if (patrolTaskLocals.isEmpty()) return;
            PatrolUnFinishDialog patrolUnFinishDialog = new PatrolUnFinishDialog(getContext(), patrolTaskLocals)
                    .setOnClickOptionListener(onClickUnfinishDialogOptionListener);
            patrolUnFinishDialog.show();
        }
    }


    private void initListener() {
        viewOperateDangerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PatrolDangerListActivity.class));
            }
        });
        viewOperateDangerQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewOperateHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PatrolHistoryActivity.class));
            }
        });

    }


    @Override
    protected boolean isImageHeader() {
        return true;
    }

    // —————————————————————————监听事件—————————————————————————————————
    // 点击未完成任务dialog的   继续  放弃    按钮时的监听事件
    private PatrolUnFinishDialog.OnClickOptionListener onClickUnfinishDialogOptionListener = new PatrolUnFinishDialog.OnClickOptionListener() {
        @Override
        public void onClickGoon(int position, int patrolType, PatrolTaskLocal patrolTaskLocal, BaseQuickAdapter adapter) {
            if (patrolType == PatrolTaskLocal.PATROL_TYPE_DEVICE) {
                // 跳转到巡查界面
                PatrolDevice device = (PatrolDevice) patrolTaskLocal.getOrigin();
                PatrolDeviceRecord record = (PatrolDeviceRecord) patrolTaskLocal.getRecord();
                PatrolTargetDeviceActivity.start(getContext(), device, record);
            } else if (patrolType == PatrolTaskLocal.PATROL_TYPE_MOBILE) {
                PatrolProject mobile = (PatrolProject) patrolTaskLocal.getOrigin();
                PatrolProjectRecord mobileRecord = (PatrolProjectRecord) patrolTaskLocal.getRecord();
                PatrolTargetProjectActivity.start(getContext(), mobile, mobileRecord);
            }
        }

        @Override
        public void onClickDrop(int position, int patrolType, PatrolTaskLocal patrolTaskLocal, BaseQuickAdapter adapter) {
            if (patrolType == PatrolTaskLocal.PATROL_TYPE_DEVICE) {
                // 删除本地的数据
                PatrolRecordUtil.removeDeviceRecord();
            } else if (patrolType == PatrolTaskLocal.PATROL_TYPE_MOBILE) {
                PatrolRecordUtil.removeProjectRecord();
            }
            adapter.remove(position);
        }
    };

}
