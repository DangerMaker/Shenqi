package heihei.shenqi.presentation.luluhei;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import heihei.frame.view.GridMarginDecoration;
import heihei.shenqi.R;
import heihei.shenqi.data.Task;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * User: lyjq(1752095474)
 * Date: 2016-07-25
 */
public class LuluheiFragment extends Fragment implements LuluheiContract.View {

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
//    @BindView(R.id.progressbar)
//    ProgressBar mProgressBar;
    @BindView(R.id.empty)
    RelativeLayout mEmpty;

    TasksAdapter mAdapter;
    LuluheiContract.Presenter mPresenter;

    public static LuluheiFragment newInstance() {
        return new LuluheiFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new TasksAdapter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_luluhei, container, false);
        ButterKnife.bind(this, root);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(5);
        mRecyclerView.addItemDecoration(new GridMarginDecoration(getContext(), 8));
        mRecyclerView.setAdapter(mAdapter);
        refreshLayout.setOnRefreshListener(refreshListener);
        mAdapter.setListener(moreListener);
        mAdapter.setOnItemClickListener(onItemClickListener);
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
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(LuluheiContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mPresenter.onRefresh();
        }
    };

    TasksAdapter.ListAdapterListener moreListener = new TasksAdapter.ListAdapterListener() {
        @Override
        public void onListEnded() {
            mPresenter.onLoadMore();
        }
    };

    TasksAdapter.OnItemClickListener onItemClickListener = new TasksAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int postion) {
            Task task = mAdapter.getItem(postion);
            System.out.println(task.getTitle());
        }
    };
}
