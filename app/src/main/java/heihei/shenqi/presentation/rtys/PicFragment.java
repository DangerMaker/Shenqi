package heihei.shenqi.presentation.rtys;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import heihei.frame.view.GridMarginDecoration;
import heihei.shenqi.Config;
import heihei.shenqi.R;
import heihei.shenqi.data.Task;
import heihei.shenqi.data.source.remote.TasksRemoteDataSource;
import heihei.shenqi.presentation.luluhei.LuluheiContract;
import heihei.shenqi.presentation.luluhei.LuluheiPresenter;
import heihei.shenqi.presentation.luluhei.TasksAdapter;
import heihei.shenqi.video.PlayerActivity;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * User: lyjq(1752095474)
 * Date: 2016-07-25
 */
public class PicFragment extends Fragment implements PicContract.View {

    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.empty)
    RelativeLayout mEmpty;

    Context mContext;
    PicAdapter mAdapter;
    PicContract.Presenter mPresenter;
    private Unbinder unbinder;

    private boolean hasStarted = false;
    private boolean isViewed = false;

    public static PicFragment newInstance() {
        return new PicFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        new PicPresenter(TasksRemoteDataSource.getInstance(mContext), this);
        mAdapter = new PicAdapter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_luluhei, container, false);
        unbinder = ButterKnife.bind(this, root);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(5);
        mRecyclerView.addItemDecoration(new GridMarginDecoration(getContext(), 8));
        mRecyclerView.setAdapter(mAdapter);
        refreshLayout.setOnRefreshListener(refreshListener);
        mAdapter.setListener(moreListener);
//        mAdapter.setOnItemClickListener(onItemClickListener);
        isViewed = true;
        return root;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mEmpty.setVisibility(View.GONE);
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showTasks(List<Task> tasks) {
        if (tasks != null)
            mAdapter.updateItems(tasks);
    }

    @Override
    public void addTasks(List<Task> tasks) {
        if (tasks != null)
            mAdapter.addItems(tasks);
    }

    @Override
    public void showError(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(PicContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasStarted)
            mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ;
        unbinder.unbind();
    }

    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mPresenter.onRefresh();
        }
    };

    PicAdapter.ListAdapterListener moreListener = new PicAdapter.ListAdapterListener() {
        @Override
        public void onListEnded() {
            mPresenter.onLoadMore();
        }
    };

    PicAdapter.OnItemClickListener onItemClickListener = new PicAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int postion) {
            Task task = mAdapter.getItem(postion);
            System.out.println(task.getTitle());
            String[] video = task.getUrl().split("/");
            Intent mpdIntent = new Intent(getActivity(), PlayerActivity.class)
//                    .setData(Uri.parse("http://www.luluhei.pw/media/player/config_m.php?vkey=18944"))
                    .setData(Uri.parse(Config.BASE_LULUHEI_URL + "/media/player/config_m.php?vkey=" + video[2]))
                    .putExtra(PlayerActivity.CONTENT_ID_EXTRA, "3pg")
                    .putExtra(PlayerActivity.CONTENT_TYPE_EXTRA, "0")
                    .putExtra(PlayerActivity.PROVIDER_EXTRA, task.getTitle());
            startActivity(mpdIntent);
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            hasStarted = true;
            Log.e(TAG, "setUserVisibleHint: 开始界面" + isVisibleToUser);
            if(isViewed) {
                mPresenter.subscribe();
            }
        } else {
            if (hasStarted) {
                hasStarted = false;
                Log.e(TAG, "setUserVisibleHint: 结束界面" + isVisibleToUser);
            }
        }
    }
}
