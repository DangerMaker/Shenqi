package heihei.shenqi.presentation.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import heihei.shenqi.R;

/**
 * Created by Administrator on 2016/8/4.
 */
public class HomeContainerFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    ContainerAdapter mContainerAdapter;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.choices_toolbar)
    AmazingToolbar toolbar;
    ArrayList<VestFragment> vestData;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.vestData = new ArrayList(3);
        this.mContainerAdapter = new ContainerAdapter(getChildFragmentManager());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_container, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mViewPager.setPageMargin(16);
        this.mViewPager.setOffscreenPageLimit(3);
        this.mViewPager.setAdapter(this.mContainerAdapter);
        this.toolbar.setViewPager(this.mViewPager, this.mContainerAdapter);
        initTabsData();
        notifyDataSetChanged();
    }

    private void initTabsData(){
        vestData.clear();
        VestFragment fragment1 = new VestFragment(new EmptyFragment(),"图片");
        VestFragment fragment2 = new VestFragment(new EmptyFragment(),"视频");
        VestFragment fragment3 = new VestFragment(new EmptyFragment(),"文字");
        ((EmptyFragment)fragment1.getFragment()).setTextView("tab1");
        ((EmptyFragment)fragment2.getFragment()).setTextView("tab2");
        ((EmptyFragment)fragment3.getFragment()).setTextView("tab3");
        vestData.add(fragment1);
        vestData.add(fragment2);
        vestData.add(fragment3);
    }

    private void notifyDataSetChanged()
    {
        this.mContainerAdapter.setData(this.vestData);
        this.mContainerAdapter.notifyDataSetChanged();
        if (1 == this.mViewPager.getCurrentItem())
            return;
        this.mViewPager.setCurrentItem(1, false);
        this.toolbar.notifyDataSetChanged();
    }

    public void setUserVisibleHint(boolean paramBoolean) {
        super.setUserVisibleHint(paramBoolean);
        try {
            Fragment localFragment = this.mContainerAdapter.getItem(this.mViewPager.getCurrentItem());
            if (localFragment != null)
                localFragment.setUserVisibleHint(paramBoolean);
            return;
        } catch (Exception localException) {
            Log.e("HomeContainerFragment", localException.getMessage());
        }
    }

    public static class ContainerAdapter extends FragmentPagerAdapter {

        List<VestFragment> vests;

        public ContainerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ((VestFragment) this.vests.get(position)).getFragment();
        }

        @Override
        public int getCount() {
            if (this.vests == null)
                return 0;
            return this.vests.size();
        }

        @Override
        public long getItemId(int position) {
            return ((VestFragment) this.vests.get(position)).hashCode();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ((VestFragment) this.vests.get(position)).getName();
        }

        public void setData(List<VestFragment> vests) {
            this.vests = vests;
        }
    }

    private static class VestFragment {
        Fragment fragment;
        String name;

        public VestFragment(Fragment fragment, String name) {
            this.fragment = fragment;
            this.name = name;
        }

        public Fragment getFragment() {
            return this.fragment;
        }

        public String getName() {
            return this.name;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
