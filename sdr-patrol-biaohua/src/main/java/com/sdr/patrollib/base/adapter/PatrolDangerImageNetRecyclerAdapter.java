package com.sdr.patrollib.base.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyfun.preview.FunPreview;
import com.orhanobut.logger.Logger;
import com.sdr.lib.SDR_LIBRARY;
import com.sdr.patrollib.R;
import com.sdr.patrollib.SDR_PATROL_BIAOHUA;
import com.sdr.patrollib.base.PatrolGlideUrl;
import com.sdr.patrollib.data.AttachementInfo;
import com.sdr.patrollib.util.PatrolUtil;

/**
 * Created by HyFun on 2018/12/14.
 * Email: 775183940@qq.com
 * Description:
 */

public class PatrolDangerImageNetRecyclerAdapter extends BaseQuickAdapter<AttachementInfo, BaseViewHolder> {

    public PatrolDangerImageNetRecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, AttachementInfo item) {
        final int position = helper.getLayoutPosition();

        ImageView imageView = helper.getView(R.id.patrol_add_danger_image_recycler_item_iv_image);
        ImageView ivDelete = helper.getView(R.id.patrol_add_danger_image_recycler_item_iv_delete);
        ImageView ivPlayVideo = helper.getView(R.id.patrol_add_danger_image_recycler_item_iv_play_video);
        TextView tvFileSize = helper.getView(R.id.patrol_add_danger_image_recycler_item_tv_file_size);

        final String fileUrl = SDR_PATROL_BIAOHUA.getInstance().getUrl() + item.getAttchPath().substring(1);
        final String fileType = PatrolUtil.getFileType(fileUrl);


        if (fileType.equals("jpg") || fileType.equals("mp4")) {
            // 说明是图片  、  视频
            SDR_LIBRARY.getInstance().getGlide().with(mContext)
                    .load(new PatrolGlideUrl(fileUrl))
                    .apply(
                            new RequestOptions()
                                    .frame(10)
                                    .centerCrop()
                    )
                    .into(imageView);
        } else {
            // 不是图片、视频  是附件
            SDR_LIBRARY.getInstance().getGlide().with(mContext)
                    .load(R.mipmap.patrol_ic_file)
                    .into(imageView);
        }

        ivDelete.setVisibility(View.GONE);
        tvFileSize.setVisibility(View.GONE);
        ivPlayVideo.setVisibility(fileType.equals("mp4") ? View.VISIBLE : View.GONE);

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fileType.equals("jpg")) {
                    FunPreview.previewImage(mContext, fileUrl, false);
                } else if (fileType.equals("mp4")) {
                    Logger.d("视频地址:" + fileUrl);
                    FunPreview.previewVideo(mContext, fileUrl);
                } else {
                    // 浏览器打开
                    PatrolUtil.openUrlByBrowser(mContext, fileUrl);
                }
            }
        });

    }


    /**
     * 设置adapter
     *
     * @param recyclerView
     * @return
     */
    public static final PatrolDangerImageNetRecyclerAdapter setAdapter(RecyclerView recyclerView) {
        PatrolDangerImageNetRecyclerAdapter patrolDangerImageListRecyclerAdapter = new PatrolDangerImageNetRecyclerAdapter(R.layout.patrol_layout_item_recycler_danger_image);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 3));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(patrolDangerImageListRecyclerAdapter);
        return patrolDangerImageListRecyclerAdapter;
    }
}
