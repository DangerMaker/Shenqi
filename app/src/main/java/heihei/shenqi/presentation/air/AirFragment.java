package heihei.shenqi.presentation.air;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import heihei.frame.BannerModel;
import heihei.frame.view.GridMarginDecoration;
import heihei.shenqi.Config;
import heihei.shenqi.R;
import heihei.shenqi.data.Task;
import heihei.shenqi.data.source.remote.TasksRemoteDataSource;
import heihei.shenqi.video.PlayerActivity;
import heihei.shenqi.widget.BannerView;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * User: lyjq(1752095474)
 * Date: 2016-07-25
 */
public class AirFragment extends Fragment implements AirContract.View {

    @BindView(R.id.xrecycler)
    XRecyclerView mRecyclerView;

    @BindView(R.id.empty)
    RelativeLayout mEmpty;

    BannerView headerView;
    Context mContext;
    AirAdapter mAdapter;
    AirContract.Presenter mPresenter;
    private Unbinder unbinder;

    public static AirFragment newInstance() {
        return new AirFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        new AirPresenter(TasksRemoteDataSource.getInstance(mContext), this);
        mAdapter = new AirAdapter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_air, container, false);
        unbinder = ButterKnife.bind(this, root);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(5);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingListener(refreshListener);
        mRecyclerView.setLoadingMoreEnabled(false);

        View header = LayoutInflater.from(mContext).inflate(R.layout.header_air, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
        headerView = (BannerView) header.findViewById(R.id.header);
        mRecyclerView.addHeaderView(header);
//        mAdapter.setOnItemClickListener(onItemClickListener);
        return root;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mEmpty.setVisibility(View.GONE);
            mRecyclerView.refreshComplete();
        }
    }

    @Override
    public void showTasks(List<Task> tasks) {
        if (tasks != null) {
            List<BannerModel> bannerData = new ArrayList<>();
            Iterator<Task> iterator = tasks.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                if (i < 6) {
                    Task task = iterator.next();
                    String bigImg = task.getImg().replace("small", "big");
                    BannerModel model = new BannerModel(task.getTitle(), bigImg, task.getUrl());
                    bannerData.add(model);

                    iterator.remove();
                    i++;
                }else{
                    break;
                }
            }

//            for (int i = 0; i < 6; i++) {
//                Task task = tasks.get(i);
//                String bigImg = task.getImg().replace("small","big");
//                BannerModel model = new BannerModel(task.getTitle(),bigImg,task.getUrl());
//                bannerData.add(model);
//            }
            headerView.setData(bannerData);
            mAdapter.updateItems(tasks);
        }
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
    public void setPresenter(AirContract.Presenter presenter) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ;
        unbinder.unbind();
    }

    XRecyclerView.LoadingListener refreshListener = new XRecyclerView.LoadingListener() {
        @Override
        public void onRefresh() {
            mPresenter.onRefresh();
        }

        @Override
        public void onLoadMore() {

        }
    };

    AirAdapter.OnItemClickListener onItemClickListener = new AirAdapter.OnItemClickListener() {
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
}
