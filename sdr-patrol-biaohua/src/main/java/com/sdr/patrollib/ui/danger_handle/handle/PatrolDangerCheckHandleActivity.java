package com.sdr.patrollib.ui.danger_handle.handle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sdr.lib.util.CommonUtil;
import com.sdr.patrollib.R;
import com.sdr.patrollib.SDR_PATROL_BIAOHUA;
import com.sdr.patrollib.base.activity.PatrolBaseActivity;
import com.sdr.patrollib.contract.PatrolDangerCheckHandleContract;
import com.sdr.patrollib.data.danger.Maintenance_DefectProcessingStepEnum;
import com.sdr.patrollib.data.danger.Maintenance_DefectTrackingInfo;
import com.sdr.patrollib.data.danger.PatrolDanger;
import com.sdr.patrollib.data.danger.PatrolDangerHandleType;
import com.sdr.patrollib.presenter.PatrolDangerCheckHandlePresenter;
import com.sdr.patrollib.ui.danger_handle.PatrolDangerSolveActivity;

import java.util.Date;
import java.util.List;

public class PatrolDangerCheckHandleActivity extends PatrolBaseActivity<PatrolDangerCheckHandlePresenter> implements PatrolDangerCheckHandleContract.View {
    private static final String PATROL_DANGER = "PATROL_DANGER";

    /**
     * 视图相关
     */
    private RadioGroup radioGroup;
    private EditText editText;
    private Button buttonSubmit;


    private PatrolDanger patrolDanger;

    private String handleMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_danger_check_handle);
        setTitle("检查处理");
        setDisplayHomeAsUpEnabled();
        patrolDanger = (PatrolDanger) getIntent().getSerializableExtra(PATROL_DANGER);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        radioGroup = findViewById(R.id.patrol_danger_solve_check_handle_radio_group);
        editText = findViewById(R.id.patrol_danger_solve_check_handle_edt_opinion);
        buttonSubmit = findViewById(R.id.patrol_danger_solve_check_handle_btn_confirm);
    }

    private void initData() {
        // 获取数据
        showLoadingView();
        presenter.getDangerCheckHandleTypeList();
    }

    private void initListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                handleMethod = radioButton.getText().toString();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(handleMethod) || TextUtils.isEmpty(editText.getText().toString().trim())) {
                    showErrorMsg("不能为空", "");
                    return;
                }
                String content = "处理类型：" + handleMethod + "\n" + "处理意见：" + editText.getText().toString().trim();

                new MaterialDialog.Builder(getContext())
                        .title("提示")
                        .content(content)
                        .negativeText("取消")
                        .positiveText("确定")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                // 生成 7 个
                                Maintenance_DefectTrackingInfo trackingInfo = new Maintenance_DefectTrackingInfo();
                                trackingInfo.setDefectId(patrolDanger.getId());
                                trackingInfo.setStepEmployeeId(SDR_PATROL_BIAOHUA.getInstance().getPatrolUser().getUserId());
                                trackingInfo.setStepEmployeeName(SDR_PATROL_BIAOHUA.getInstance().getPatrolUser().getUserName());
                                trackingInfo.setProcessingStep(Maintenance_DefectProcessingStepEnum.检查处理.toString());
                                trackingInfo.setProcessingMethod(handleMethod);
                                trackingInfo.setHandlingOpinions(editText.getText().toString().trim());
                                trackingInfo.setProcessingTime(new Date().getTime());

                                Intent intent = new Intent();
                                intent.putExtra(PatrolDangerSolveActivity.TRACK_INFO, trackingInfo);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    protected PatrolDangerCheckHandlePresenter instancePresenter() {
        return new PatrolDangerCheckHandlePresenter();
    }


    // ———————————————————————PRIVATE———————————————————————

    /**
     * 开启此activity
     *
     * @param activity
     * @param requestCode
     * @param patrolDanger
     */
    public static final void start(Activity activity, int requestCode, PatrolDanger patrolDanger) {
        Intent intent = new Intent(activity, PatrolDangerCheckHandleActivity.class);
        intent.putExtra(PATROL_DANGER, patrolDanger);
        activity.startActivityForResult(intent, requestCode);
    }

    // ———————————————————————VIEW———————————————————————
    @Override
    public void loadDangerCheckHandleTypeListSuccess(List<PatrolDangerHandleType> typeList) {
        radioGroup.removeAllViews();
        for (PatrolDangerHandleType handleType : typeList) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(handleType.getValue());
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            int padding = CommonUtil.dip2px(getContext(), 10);
            radioButton.setPadding(padding, padding, padding, padding);
            radioGroup.addView(radioButton, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void loadDataComplete() {
        showContentView();
    }
}
