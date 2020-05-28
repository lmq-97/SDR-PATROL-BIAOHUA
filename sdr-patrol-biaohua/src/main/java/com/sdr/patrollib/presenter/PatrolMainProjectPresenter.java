package com.sdr.patrollib.presenter;

import com.sdr.patrollib.SDR_PATROL_BIAOHUA;
import com.sdr.patrollib.base.BasePresenter;
import com.sdr.patrollib.contract.PatrolMainProjectContract;
import com.sdr.patrollib.data.project.PatrolProject;
import com.sdr.patrollib.data.project.PatrolProjectItem;
import com.sdr.patrollib.http.PatrolHttp;
import com.sdr.patrollib.http.rx.PatrolRxBaseObserver;
import com.sdr.patrollib.http.rx.PatrolRxUtils;

import java.util.List;

/**
 * Created by HyFun on 2018/12/06.
 * Email: 775183940@qq.com
 * Description:
 */

public class PatrolMainProjectPresenter extends BasePresenter<PatrolMainProjectContract.View> implements PatrolMainProjectContract.Presenter {
    @Override
    public void getProjectList() {
        addSubscription(
                PatrolHttp.getService().getProjectList(SDR_PATROL_BIAOHUA.getInstance().getPatrolUser().getUserId())
                        .compose(PatrolRxUtils.<List<PatrolProjectItem>>transformer())
                        .compose(PatrolRxUtils.<List<PatrolProjectItem>>io_main())
                        .subscribeWith(new PatrolRxBaseObserver<List<PatrolProjectItem>>(view) {
                            @Override
                            public void onNext(List<PatrolProjectItem> patrolProjectItems) {
                                view.loadProjectListSuccess(patrolProjectItems);
                            }

                            @Override
                            public void onComplete() {
                                view.loadDataComplete();
                            }
                        })
        );
    }

    @Override
    public void getPorjectDetail(int projectId) {
        addSubscription(
                PatrolHttp.getService().getProjectDetail(projectId, SDR_PATROL_BIAOHUA.getInstance().getPatrolUser().getUserId())
                        .compose(PatrolRxUtils.<PatrolProject>transformer())
                        .compose(PatrolRxUtils.<PatrolProject>io_main())
                        .subscribeWith(new PatrolRxBaseObserver<PatrolProject>(view) {

                            @Override
                            public void onNext(PatrolProject patrolProject) {
                                view.loadProjectDetailSuccess(patrolProject);
                            }

                            @Override
                            public void onComplete() {
                                view.loadDataComplete();
                            }
                        })
        );
    }
}
