package com.sdr.patrollib.ui.target_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sdr.patrollib.R;
import com.sdr.patrollib.SDR_PATROL_BIAOHUA;
import com.sdr.patrollib.base.activity.PatrolBaseActivity;
import com.sdr.patrollib.contract.PatrolTargetProjectContract;
import com.sdr.patrollib.data.project.PatrolProject;
import com.sdr.patrollib.data.project.PatrolProjectRecord;
import com.sdr.patrollib.presenter.PatrolTargetProjectPresenter;
import com.sdr.patrollib.support.data.AttachmentLocal;
import com.sdr.patrollib.support.data.PatrolTargetView;
import com.sdr.patrollib.support.location.PatrolMobileMapChangeListener;
import com.sdr.patrollib.support.location.PatrolMobileMapHelperV2;
import com.sdr.patrollib.ui.target_project.adapter.PatrolTargetProjectDangerRecyclerAdapter;
import com.sdr.patrollib.ui.target_project.adapter.PatrolTargetProjectTargetRecyclerAdapter;
import com.sdr.patrollib.util.PatrolRecordUtil;
import com.sdr.patrollib.util.PatrolUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PatrolTargetProjectActivity extends PatrolBaseActivity<PatrolTargetProjectPresenter> implements PatrolTargetProjectContract.View,
        PatrolMobileMapChangeListener {
    private static final String PROJECT = "PROJECT";
    private static final String PROJECT_RECORD = "PROJECT_RECORD";
    private static final int REQUEST_CODE_OPEN_ADD_DANGER = 3;

    /**
     * 视图相关
     */
    private RecyclerView viewRecyclerTarget, viewRecyclerDanger;
    private FrameLayout viewMapContainer; // 地图容器
    private MapView mapView;  // 地图
    private TextView tvTime; // 巡查时长
    private TextView tvLength; // 巡查距离
    private TextView tvLocationType; // 定位类型
    private TextView tvDebugInfo; // debug info
    private Button buttonSubmit;  // 提交

    private PatrolProject patrolProject;
    private PatrolProjectRecord patrolProjectRecord;


    private LinkedList<PatrolTargetView> views = new LinkedList<>();
    private AMap mAMap;
    private PatrolMobileMapHelperV2 patrolMobileMapHelper;

    // adapter
    private PatrolTargetProjectTargetRecyclerAdapter patrolTargetProjectTargetRecyclerAdapter;
    private PatrolProject.PatrolMobileCheckItemsVo currentTarget;
    private PatrolTargetProjectDangerRecyclerAdapter patrolTargetProjectDangerRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_target_project);
        initIntent();
        initView();
        initData();
        initListener();
        mapView.onCreate(savedInstanceState);
    }

    private void initIntent() {
        Intent intent = getIntent();
        patrolProject = (PatrolProject) intent.getSerializableExtra(PROJECT);
        patrolProjectRecord = (PatrolProjectRecord) intent.getSerializableExtra(PROJECT_RECORD);
        setDisplayHomeAsUpEnabled();
        toolBar.inflateMenu(R.menu.patrol_menu_target_project);
        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.patrol_menu_id_mobile_target_map_view) {
                    addView(new PatrolTargetView("巡查路线", viewMapContainer));
                }
                return true;
            }
        });
    }

    private void initView() {
        viewRecyclerTarget = findViewById(R.id.patrol_target_rv_target);
        viewRecyclerDanger = findViewById(R.id.patrol_target_rv_danger);
        viewMapContainer = findViewById(R.id.patrol_target_map_container);

        mapView = findViewById(R.id.patrol_target_map_view_map_view);
        tvTime = findViewById(R.id.patrol_target_map_view_tv_time);
        tvLength = findViewById(R.id.patrol_target_map_view_tv_length);
        tvLocationType = findViewById(R.id.patrol_target_map_view_tv_type);
        tvDebugInfo = findViewById(R.id.patrol_target_map_view_tv_debug_info);
        if (SDR_PATROL_BIAOHUA.getInstance().isDebug()) {
            ((View) tvDebugInfo.getParent()).setVisibility(View.VISIBLE);
        } else {
            ((View) tvDebugInfo.getParent()).setVisibility(View.GONE);
        }

        buttonSubmit = findViewById(R.id.patrol_target_project_btn_submit);
    }

    private void initData() {
        mAMap = mapView.getMap();
        addView(new PatrolTargetView(patrolProjectRecord.getMobileCheckName() + "-工程巡查", viewRecyclerTarget));
        patrolMobileMapHelper = new PatrolMobileMapHelperV2(getContext(), patrolProjectRecord, this);
        // 地图
        UiSettings uiSettings = mAMap.getUiSettings();
        uiSettings.setRotateGesturesEnabled(false); // 旋转手势 关闭
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(17f));

        // target
        patrolTargetProjectTargetRecyclerAdapter = new PatrolTargetProjectTargetRecyclerAdapter(R.layout.patrol_layout_item_recycler_target, patrolProject.getItems(), patrolProjectRecord);
        patrolTargetProjectTargetRecyclerAdapter.setEmptyView(getEmptyView());
        patrolTargetProjectTargetRecyclerAdapter.setOnItemClickListener(targetItemClickListener);
        viewRecyclerTarget.setLayoutManager(new GridLayoutManager(getContext(), 2));
        viewRecyclerTarget.setAdapter(patrolTargetProjectTargetRecyclerAdapter);
    }

    private void initListener() {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(getContext())
                        .title("提交")
                        .content("是否提交巡查记录？")
                        .negativeText("取消")
                        .positiveText("上传")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                // 提交巡查记录  结束时间  提交时间  隐患数量
                                Date nowDate = new Date();
                                patrolProjectRecord.setPatrolEndTime(nowDate.getTime());
                                patrolProjectRecord.setPatrolSubmitTime(nowDate.getTime());
                                patrolProjectRecord.setErrorCount(PatrolUtil.getPatrolMobileDangerCount(patrolProjectRecord));
                                // 提交
                                // 先上传附件 再上传json
                                final List<AttachmentLocal> needUploadFileList = new ArrayList<>();
                                List<AttachmentLocal> notExitsFileList = new ArrayList<>();
                                long attatchSize = 0;
                                // 初始化 文件是否存在
                                List<PatrolProjectRecord.Patrol_MobileCheckRecordItemContents> contentList = patrolProjectRecord.getItems();
                                for (int i = 0; i < contentList.size(); i++) {
                                    PatrolProjectRecord.Patrol_MobileCheckRecordItemContents content = contentList.get(i);
                                    List<AttachmentLocal> attachmentLocalList = content.getAttachmentLocalList();
                                    for (int j = 0; j < attachmentLocalList.size(); j++) {
                                        AttachmentLocal attachment = attachmentLocalList.get(j);
                                        File file = new File(attachment.getFilePath());
                                        // 如果文件不存在  将文件设置成不存在的状态
                                        if (!file.exists()) {
                                            attachment.setStatus(AttachmentLocal.NO_FILE);
                                            notExitsFileList.add(attachment);
                                        } else if (attachment.getStatus() == AttachmentLocal.NOT_UPLOADED) {
                                            needUploadFileList.add(attachment);
                                            attatchSize += attachment.getFileSize();
                                        }
                                    }
                                }

                                if (needUploadFileList.isEmpty()) {
                                    presenter.postProjectRecord(patrolProjectRecord);
                                } else {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("记录中的附件共" + (needUploadFileList.size() + notExitsFileList.size()) + "个\n");
                                    sb.append("需要上传" + needUploadFileList.size() + "个\n");
                                    sb.append("不存在的附件" + notExitsFileList.size() + "个\n");
                                    sb.append("预计消耗流量" + PatrolUtil.formatFileSize(attatchSize));
                                    new MaterialDialog.Builder(getContext())
                                            .title("上传附件")
                                            .content(sb.toString())
                                            .negativeText("取消")
                                            .positiveText("上传")
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    presenter.postAttachment(needUploadFileList, patrolProjectRecord);
                                                }
                                            })
                                            .show();
                                }
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    protected PatrolTargetProjectPresenter instancePresenter() {
        return new PatrolTargetProjectPresenter();
    }


    @Override
    protected void onResume() {
        if (mapView != null)
            mapView.onResume();
        patrolMobileMapHelper.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
        patrolMobileMapHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mapView != null)
            mapView.onDestroy();
        patrolMobileMapHelper.onDestory();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (mapView != null)
            mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }


    @Override
    protected void setNavigationOnClickListener() {
        if (views.size() >= 2) {
            removeView();
        } else {
            // 询问是否放弃此次巡检
            new MaterialDialog.Builder(getContext())
                    .title("提示")
                    .content("是否放弃此次工程巡查，并删除巡查记录？")
                    .negativeText("继续")
                    .positiveText("放弃")
                    .positiveColor(Color.RED)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            patrolMobileMapHelper.setNeedBackLocation(false);
                            PatrolRecordUtil.removeProjectRecord();
                            finish();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onBackPressed() {
        setNavigationOnClickListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        patrolMobileMapHelper.setNeedBackLocation(true);
        if (requestCode == REQUEST_CODE_OPEN_ADD_DANGER && resultCode == RESULT_OK) {
            PatrolProject.PatrolMobileCheckItemsVo.Patrol_MobileCheckItems itemContent = (PatrolProject.PatrolMobileCheckItemsVo.Patrol_MobileCheckItems) data.getSerializableExtra(PatrolProjectAddDangerActivity.CONTENT);
            List<PatrolProjectRecord.Patrol_MobileCheckRecordItemContents> dangerList = (List<PatrolProjectRecord.Patrol_MobileCheckRecordItemContents>) data.getSerializableExtra(PatrolProjectAddDangerActivity.DANGER_LIST);
            // 找到record中的 并移除
            Iterator<PatrolProjectRecord.Patrol_MobileCheckRecordItemContents> iterator = patrolProjectRecord.getItems().iterator();
            while (iterator.hasNext()) {
                PatrolProjectRecord.Patrol_MobileCheckRecordItemContents next = iterator.next();
                if (next.getPatrolIndexId() == itemContent.getId()) {
                    iterator.remove();
                }
            }
            // 设置坐标
            LinkedList<LatLng> positionList = patrolMobileMapHelper.getPositionList();
            if (!positionList.isEmpty()) {
                for (int i = 0; i < dangerList.size(); i++) {
                    PatrolProjectRecord.Patrol_MobileCheckRecordItemContents patrol_mobileCheckRecordItemContents = dangerList.get(i);
                    patrol_mobileCheckRecordItemContents.setDangerLat(positionList.getLast().latitude);
                    patrol_mobileCheckRecordItemContents.setDangerLng(positionList.getLast().longitude);
                }
            }

            patrolProjectRecord.getItems().addAll(dangerList);
            // 保存至本地
            PatrolRecordUtil.saveProjectRecord(patrolProjectRecord);
            //  更新adapter
            patrolTargetProjectDangerRecyclerAdapter.notifyDataSetChanged();
            patrolTargetProjectTargetRecyclerAdapter.notifyDataSetChanged();
        }
    }

    // ——————————————————————LISTENER——————————————————————————
    private BaseQuickAdapter.OnItemClickListener targetItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            currentTarget = patrolTargetProjectTargetRecyclerAdapter.getItem(position);
            List<PatrolProject.PatrolMobileCheckItemsVo.Patrol_MobileCheckItems> items = currentTarget.getItems();
            addView(new PatrolTargetView(currentTarget.getName(), viewRecyclerDanger));
            patrolTargetProjectDangerRecyclerAdapter = new PatrolTargetProjectDangerRecyclerAdapter(R.layout.patrol_layout_item_recycler_danger, items, patrolProjectRecord);
            patrolTargetProjectDangerRecyclerAdapter.setEmptyView(getEmptyView());
            patrolTargetProjectDangerRecyclerAdapter.setOnItemClickListener(dangerItemClickListener);
            viewRecyclerDanger.setLayoutManager(new LinearLayoutManager(getContext()));
            viewRecyclerDanger.setAdapter(patrolTargetProjectDangerRecyclerAdapter);
        }
    };

    private BaseQuickAdapter.OnItemClickListener dangerItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            patrolMobileMapHelper.setNeedBackLocation(false);
            PatrolProject.PatrolMobileCheckItemsVo.Patrol_MobileCheckItems content = patrolTargetProjectDangerRecyclerAdapter.getItem(position);
            PatrolProjectAddDangerActivity.start(getActivity(), REQUEST_CODE_OPEN_ADD_DANGER, currentTarget, content, patrolProjectRecord);
        }
    };


    // ——————————————————————PRIVATE——————————————————————————


    private void addView(PatrolTargetView targetView) {
        setTitle(targetView.getTitle());
        for (PatrolTargetView target : views) {
            target.getView().setVisibility(View.GONE);
        }
        targetView.getView().setVisibility(View.VISIBLE);
        views.add(targetView);
        if (targetView.getView() == viewMapContainer) {
            toolBar.getMenu().getItem(0).setVisible(false);
        }
    }

    private void removeView() {
        if (views.isEmpty()) return;
        // 移除最后一个
        PatrolTargetView targetView = views.removeLast();
        targetView.getView().setVisibility(View.GONE);
        if (targetView.getView() == viewMapContainer) {
            toolBar.getMenu().getItem(0).setVisible(true);
        }
        PatrolTargetView last = views.getLast();
        setTitle(last.getTitle());
        last.getView().setVisibility(View.VISIBLE);
    }

    /**
     * 创建一个开始marker
     */

    private MarkerOptions createStartMarker(LatLng latLng, int markerRes) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng)
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(markerRes))
                .zIndex(1)
                // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                .setFlat(false);//设置marker平贴地图效果
        return options;
    }

    /**
     * 开启此activity
     *
     * @param context
     * @param patrolProject
     * @param patrolProjectRecord
     */
    public static final void start(Context context, PatrolProject patrolProject, PatrolProjectRecord patrolProjectRecord) {
        Intent intent = new Intent(context, PatrolTargetProjectActivity.class);
        intent.putExtra(PROJECT, patrolProject);
        intent.putExtra(PROJECT_RECORD, patrolProjectRecord);
        context.startActivity(intent);
    }

    // ——————————————————————地图回调————————————————————————————
    @Override
    public void onLocationChanged(AMapLocation location, String message) {
        tvLocationType.setText(location.getProvider());
        tvDebugInfo.setText(message);
    }

    @Override
    public void onPatrolTimeChange(int patrolTime, String timeStr) {
        // 设置界面上的时间
        tvTime.setText(timeStr);
        // 只更新时间 不保存
        patrolProjectRecord.setPatrolTime(patrolTime);
    }

    @Override
    public void onPatrolLengthChange(boolean isNewData, double patrolLength, List<LatLng> latLngList) {
        tvLength.setText(patrolLength + "m");
        // 更新地图坐标  绘制线
        mAMap.clear();
        if (!latLngList.isEmpty()) {
            LatLng firstLatlng = latLngList.get(0);
            LatLng lastLatlng = latLngList.get(latLngList.size() - 1);
            mAMap.addMarker(createStartMarker(firstLatlng, R.mipmap.patrol_map_marker_start));
            // 将地图视图移动至最后一个
            mAMap.animateCamera(CameraUpdateFactory.newLatLng(lastLatlng));
            mAMap.addPolyline(PatrolUtil.createMapLine(getContext(), latLngList));
            if (latLngList.size() > 1) {
                mAMap.addMarker(createStartMarker(lastLatlng, R.mipmap.patrol_map_marker_location));
            }
        }
        // 保留两位小数
        if (isNewData) {
            patrolProjectRecord.setPatrolLength(patrolLength);
            // 设置坐标
            patrolProjectRecord.setPatrolCoors(PatrolUtil.latlngListToString(latLngList));
            // 更新缓存
            PatrolRecordUtil.saveProjectRecord(patrolProjectRecord);
        }
    }


    //——————————————————————————————VIEW——————————————————————————————————————————

    @Override
    public void showUploadAttachFaileDialog(final List<AttachmentLocal> attachmentLocals) {
        new MaterialDialog.Builder(getContext())
                .title("附件上传未完成")
                .content("还有" + attachmentLocals.size() + "个附件上传失败，是否继续上传？")
                .cancelable(false)
                .negativeText("跳过")
                .positiveText("继续")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.postProjectRecord(patrolProjectRecord);
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.postAttachment(attachmentLocals, patrolProjectRecord);
                    }
                })
                .show();
    }

    @Override
    public void uploadProjectRecordSuccess() {
        // 上传成功
        showSuccessMsg("上传工程记录成功", "");
        // 删除本地缓存
        PatrolRecordUtil.removeProjectRecord();
        patrolMobileMapHelper.setNeedBackLocation(false);
        finish();
    }

}
