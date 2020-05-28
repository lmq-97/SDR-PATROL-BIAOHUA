package com.sdr.patrollib.ui.danger_handle.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdr.patrollib.R;
import com.sdr.patrollib.data.danger.Maintenance_DefectProcessingStepEnum;
import com.sdr.patrollib.data.danger.PatrolDanger;
import com.sdr.patrollib.support.PatrolConstant;

import java.util.Date;

/**
 * Created by HyFun on 2018/12/17.
 * Email: 775183940@qq.com
 * Description:
 */

public class PatrolDangerListRecyclerAdapter extends BaseQuickAdapter<PatrolDanger, BaseViewHolder> {

    public PatrolDangerListRecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, PatrolDanger item) {
        TextView tvType = helper.getView(R.id.patrol_danger_list_recycler_item_tv_type);
        TextView tvStatus = helper.getView(R.id.patrol_danger_list_recycler_item_tv_status);
        ImageView ivAttach = helper.getView(R.id.patrol_danger_list_recycler_item_iv_attach);

        TextView tvStation = helper.getView(R.id.patrol_danger_list_recycler_item_tv_station);
        TextView tvTarget = helper.getView(R.id.patrol_danger_list_recycler_item_tv_target);
        TextView tvCheckTitle = helper.getView(R.id.patrol_danger_list_recycler_item_tv_check_title);
        TextView tvDesc = helper.getView(R.id.patrol_danger_list_recycler_item_tv_desc);

        TextView tvUser = helper.getView(R.id.patrol_danger_list_recycler_item_tv_user);
        TextView tvTime = helper.getView(R.id.patrol_danger_list_recycler_item_tv_time);

        tvType.setText(item.getDefectSource());
        tvStatus.setText(item.getProcessStep());
        tvStatus.setTextColor(getStatusColor(item.getProcessStep()));
        // 站点
        tvStation.setText(item.getProjectName());
        // 指标
        tvTarget.setText(item.getSubject());
        // 巡查标题
        tvCheckTitle.setText(item.getTitle());
        // 隐患描述
        String desc = item.getContentDesc();
        if (!TextUtils.isEmpty(desc)) {
            tvDesc.setText(desc);
            ((View) tvDesc.getParent()).setVisibility(View.VISIBLE);
        } else {
            ((View) tvDesc.getParent()).setVisibility(View.GONE);
        }


        ivAttach.setVisibility(item.getAttachInfos() == null || item.getAttachInfos().isEmpty() ? View.GONE : View.VISIBLE);

        tvUser.setText("上报人：" + item.getReportEmployeeName() + "");
        tvTime.setText(PatrolConstant.DATE_TIME_FORMAT.format(new Date(item.getReportTime())));

    }

    private int getStatusColor(String status) {
        if (status.equals(Maintenance_DefectProcessingStepEnum.工程检查.toString()) || status.equals(Maintenance_DefectProcessingStepEnum.检查处理.toString())) {
            return mContext.getResources().getColor(R.color.patrol_color_error);
        } else if (status.equals(Maintenance_DefectProcessingStepEnum.隐患已处理.toString())) {
            return mContext.getResources().getColor(R.color.patrol_color_gray);
        } else {
            return mContext.getResources().getColor(R.color.patrol_color_orange);
        }
    }
}
