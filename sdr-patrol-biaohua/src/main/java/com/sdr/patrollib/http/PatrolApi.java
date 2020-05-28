package com.sdr.patrollib.http;

import com.sdr.patrollib.data.BaseData;
import com.sdr.patrollib.data.danger.Maintenance_DefectTrackingInfo;
import com.sdr.patrollib.data.danger.PatrolDangerHandleType;
import com.sdr.patrollib.data.device.PatrolDevice;
import com.sdr.patrollib.data.device_history.PatrolHistoryDevice;
import com.sdr.patrollib.data.device_history.PatrolHistoryInfoDevice;
import com.sdr.patrollib.data.project.PatrolProject;
import com.sdr.patrollib.data.project.PatrolProjectItem;
import com.sdr.patrollib.data.project_history.PatrolHistoryInfoProject;
import com.sdr.patrollib.data.project_history.PatrolHistoryProject;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by HyFun on 2018/12/06.
 * Email: 775183940@qq.com
 * Description:
 */

public interface PatrolApi {
    //获取工程巡检列表
    @GET("app/app_patrol_mobile_check")
    Observable<BaseData<List<PatrolProjectItem>>> getProjectList(@Query("userId") String userId);

    // 获取工程巡检详情
    @GET("app/app_patrol_mobile_check/{projectId}")
    Observable<BaseData<PatrolProject>> getProjectDetail(@Path("projectId") int projectId, @Query("userId") String userId);

    // 获取设备巡检的详情
    @GET("app/app_patrol_facility_check")
    Observable<BaseData<PatrolDevice>> getDeviceInfo(@Query("userId") String userId, @Query("markCode") String code);

    // 巡检附件上传
    @POST("app/app_patrol_facility_check_records/app_upload")
    Observable<ResponseBody> postFile(@Body RequestBody Body);

    // 设备巡检记录上传
    @POST("app/app_patrol_facility_check_records")
    Observable<ResponseBody> postDeviceRecordJson(@Body RequestBody requestBody);

    // 工程巡检记录上传
    @POST("app/app_patrol_mobile_check_records")
    Observable<ResponseBody> postProjectRecordJson(@Body RequestBody requestBody);

    // 设备历史记录列表
    @GET("app/app_patrol_facility_check_records")
    Observable<BaseData<List<PatrolHistoryDevice>>> getHistoryDeviceList(@Query("startTime") long startTime, @Query("endTime") long endTime);

    // 设备历史记录详情
    @GET("app/app_patrol_facility_check_records/{id}")
    Observable<BaseData<PatrolHistoryInfoDevice>> getHistoryDeviceInfo(@Path("id") String id);

    // 工程历史记录列表
    @GET("app/app_patrol_mobile_check_records")
    Observable<BaseData<List<PatrolHistoryProject>>> getHistoryProjectList(@Query("startTime") long startTime, @Query("endTime") long endTime);

    // 工程历史记录详情
    @GET("app/app_patrol_mobile_check_records/{id}")
    Observable<BaseData<PatrolHistoryInfoProject>> getHistoryProjectInfo(@Path("id") String id);

    // 隐患列表
    @GET("app/maintenance_defect_info/page")
    Observable<ResponseBody> getDangerList(@Query("pageIndex") int pageNo, @Query("pageSize") int pageSize, @Query("processStep") String processStep);

    // 隐患解决流程
    @GET("app/maintenance_defect_tracking_info/{id}")
    Observable<BaseData<List<Maintenance_DefectTrackingInfo>>> getDangerFlowList(@Path("id") String id);

    // 隐患检查处理类型获取
    @GET("enums/Maintenance_DefectProcessingMethodEnum")
    Observable<BaseData<List<PatrolDangerHandleType>>> getDangerCheckHandleTypes();

    // 解决隐患上传记录
    @POST("app/maintenance_defect_tracking_info")
    Observable<ResponseBody> updateTrackInfo(@Body RequestBody requestBody);
}
