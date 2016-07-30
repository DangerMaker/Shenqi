package heihei.shenqi.presentation.luluhei;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import heihei.shenqi.data.Task;
import heihei.shenqi.data.source.remote.TasksRemoteDataSource;
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
public class LuluheiPresenter implements LuluheiContract.Presenter {

    private final TasksRemoteDataSource mSource;
    private final LuluheiContract.View mView;

    private final static int INIT_PAGE_NUM = 1;
    private CompositeSubscription mSubscriptions;
    private int mPage = INIT_PAGE_NUM;

    public LuluheiPresenter(@NonNull TasksRemoteDataSource mSource, @NonNull LuluheiContract.View mView) {
        this.mSource = checkNotNull(mSource);
        this.mView = checkNotNull(mView);
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mView.setLoadingIndicator(true);
        onRefresh();
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    public void loadTasks() {
        Log.e("mPage",mPage + "");
        mSubscriptions.clear();
        Subscription subscription = mSource
                .getTasks(mPage)
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
                    }

                    @Override
                    public void onNext(List<Task> tasks) {
                        System.out.println("onNext");
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
