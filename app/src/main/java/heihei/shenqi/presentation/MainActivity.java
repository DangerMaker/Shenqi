package heihei.shenqi.presentation;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import heihei.frame.view.ViewPager;
import heihei.shenqi.R;
import heihei.shenqi.presentation.main.HomePageAdapter;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.layout_home)
    View layout_YoukuHome;
    @BindView(R.id.layout_channel)
    View layout_YoukuChannel;
    @BindView(R.id.layout_subscribe)
    View layout_YouKuSubscribe;
    @BindView(R.id.layout_vip)
    View layout_YoukuVip;
    @BindView(R.id.layout_user)
    View layout_YoukuUser;
    @BindView(R.id.home_pager)
    ViewPager viewpager;

    HomePageAdapter homeAdapter;
    private int initPos = 0;

    @OnClick({R.id.layout_home, R.id.layout_channel, R.id.layout_subscribe, R.id.layout_vip, R.id.layout_user})
    public void switchtab(View view) {
        switch (view.getId()) {
            case R.id.layout_home:
                switchTab(0);
                break;
            case R.id.layout_channel:
                switchTab(1);
                break;
            case R.id.layout_subscribe:
                switchTab(2);
                break;
            case R.id.layout_vip:
                switchTab(3);
                break;
            case R.id.layout_user:
                switchTab(4);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: " + "" );
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(savedInstanceState != null){
            initPos = savedInstanceState.getInt("pos");
        }
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        InitView();
        InitData();
    }

    private void InitView() {
        viewpager.setPagingEnabled(false);
        viewpager.setSmoothScroll(false);
    }

    private void InitData() {
        if (homeAdapter == null)
            this.homeAdapter = new HomePageAdapter(this, viewpager);
        viewpager.setAdapter(homeAdapter);
        switchTab(initPos);
    }

    public void switchTab(int paramInt) {
        this.layout_YoukuChannel.setSelected(false);
        this.layout_YoukuHome.setSelected(false);
        this.layout_YouKuSubscribe.setSelected(false);
        this.layout_YoukuUser.setSelected(false);
        this.layout_YoukuVip.setSelected(false);
        this.layout_YoukuChannel.setEnabled(true);
        this.layout_YoukuHome.setEnabled(true);
        this.layout_YouKuSubscribe.setEnabled(true);
        this.layout_YoukuUser.setEnabled(true);
        this.layout_YoukuVip.setEnabled(true);
        View localView = null;
        switch (paramInt) {
            default:
                return;
            case 0:
                localView = this.layout_YoukuHome;
                break;
            case 1:
                localView = this.layout_YoukuChannel;
                break;
            case 2:
                localView = this.layout_YouKuSubscribe;
                break;
            case 3:
                localView = this.layout_YoukuVip;
                break;
            case 4:
                localView = this.layout_YoukuUser;
                break;
        }

        if (localView != null) {
            localView.setSelected(true);
            localView.setEnabled(true);
        }
        if (viewpager.getCurrentItem() != paramInt) {
            viewpager.setCurrentItem(paramInt);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("pos",viewpager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }
}
