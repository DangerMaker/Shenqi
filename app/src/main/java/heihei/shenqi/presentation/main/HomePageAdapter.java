package heihei.shenqi.presentation.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/8/1.
 */
public class HomePageAdapter extends FragmentPagerAdapter{

    public final String TAG = this.getClass().getSimpleName();
    public static final int ITEM_COUNT = 5;
    public static final int POSITION_CHANNEL_ITEM = 1;
    public static final int POSITION_HOME_ITEM = 0;
    public static final int POSITION_SUBSCRIBE_ITEM = 2;
    public static final int POSITION_USER_ITEM = 4;
    public static final int POSITION_VIP_ITEM = 3;
    private SparseArray<Fragment> fragments;
    private ViewPager mViewPager;

    public HomePageAdapter(FragmentActivity fragmentActivity,ViewPager viewPager) {
        super(fragmentActivity.getSupportFragmentManager());
        this.mViewPager = viewPager;
        this.fragments = new SparseArray<>();
    }

    private Fragment CreateFragment(int paramInt)
    {
        Log.e(TAG, "CreateFragment: " + paramInt + "" );
        Fragment localObject = null;
        switch (paramInt)
        {
            default:
                return localObject;
            case POSITION_HOME_ITEM:
                return new EmptyFragment();
            case POSITION_CHANNEL_ITEM:
                return new EmptyFragment();
            case POSITION_SUBSCRIBE_ITEM:
                return new EmptyFragment();
            case POSITION_VIP_ITEM:
               return new EmptyFragment();
            case POSITION_USER_ITEM:
        }
        return new EmptyFragment();
    }

    @Override
    public Fragment getItem(int position) {
        Log.e(TAG, "getItem: :" + position + "" );
        Fragment fragment2 = (Fragment)this.fragments.get(position);
        Fragment fragment1 = fragment2;
        if(fragment2 == null){
            fragment1 = CreateFragment(position);
            this.fragments.put(position,fragment1);
        }
        ((EmptyFragment)fragment1).setTextView(position + "heihei");
        return fragment1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.e(TAG, "destroyItem: " );
//        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
