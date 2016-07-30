package heihei.shenqi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import heihei.shenqi.data.Task;
import heihei.shenqi.data.source.remote.TasksRemoteDataSource;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
//    TasksAdapter adapter;
    TasksRemoteDataSource mTasksRemoteDataSource;

    @BindColor(R.color.colorGray)
    int gray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        GridLayoutManager mLayoutManager = new GridLayoutManager(this,2);
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setHasFixedSize(true);
//            mRecyclerView.setAdapter(adapter == null ? adapter = new TasksAdapter(this) : adapter);
            mRecyclerView.setItemViewCacheSize(5);
        }

        mTasksRemoteDataSource = TasksRemoteDataSource.getInstance(this);

        mTasksRemoteDataSource
                .getTasks(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Task>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onNext(List<Task> task) {
                        System.out.println("onNext");
//                        adapter.updateItems(task);
                    }
                });
    }

}
