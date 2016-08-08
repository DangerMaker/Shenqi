package heihei.shenqi.presentation.rtys;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import heihei.shenqi.data.Task;
import heihei.shenqi.data.source.remote.TasksRemoteDataSource;
import heihei.shenqi.presentation.luluhei.LuluheiContract;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * User: lyjq(1752095474)
 * Date: 2016-07-25
 */
public class PicPresenter implements PicContract.Presenter {

    private final TasksRemoteDataSource mSource;
    private final PicContract.View mView;

    private final static int INIT_PAGE_NUM = 1;
    private boolean getDate = false;
    private CompositeSubscription mSubscriptions;
    private int mPage = INIT_PAGE_NUM;

    public PicPresenter(@NonNull TasksRemoteDataSource mSource, @NonNull PicContract.View mView) {
        this.mSource = checkNotNull(mSource);
        this.mView = checkNotNull(mView);
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        if(!getDate) {
            mView.setLoadingIndicator(true);
            onRefresh();
        }
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    public void loadTasks() {
        Log.e("mPage",mPage + "");
        mSubscriptions.clear();
        Subscription subscription = mSource
                .getRtys(mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Task>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError");
                        mView.setLoadingIndicator(false);
                        mView.showError("Error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(List<Task> tasks) {
                        System.out.println("onNext");
                        getDate = true;
                        if(mPage == INIT_PAGE_NUM) {
                            mView.showTasks(tasks);
                        }else{
                            mView.addTasks(tasks);
                        }
                    }
                });

        mSubscriptions.add(subscription);
    }

    @Override
    public void onRefresh() {
        mPage = INIT_PAGE_NUM;
        loadTasks();
    }

    @Override
    public void onLoadMore() {
        mPage = mPage + 1;
        loadTasks();
    }
}
