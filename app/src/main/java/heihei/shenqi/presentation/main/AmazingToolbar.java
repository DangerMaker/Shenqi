package heihei.shenqi.presentation.main;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ButtonBarLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import heihei.shenqi.R;

/**
 * Created by Administrator on 2016/8/4.
 */
public class AmazingToolbar extends FrameLayout {
    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.center_tab)
    protected TextView center_tab;
    @BindView(R.id.left_tab)
    protected TextView left_tab;
    @BindView(R.id.right_tab)
    protected TextView right_tab;
    @BindView(R.id.searchBtn)
    protected ImageView searchBtn;
    @BindView(R.id.moreBtn)
    protected ImageView moreBtn;
    @BindView(R.id.tabLayout)
    protected RelativeLayout tabLayout;

    private ViewPager mViewPager;
    private HomeContainerFragment.ContainerAdapter mAdapter;
    private Context mContext;
    private boolean clicked;
    private int currentPosition;
    private float currentPositionOffset = 0.0F;
    private Paint rectPaint;

    private int tabCount;
    private ColorStateList tabTextColor;
    private TextView[] tabs;

    @BindColor(R.color.youku_blue)
    int ColorBlue;

    public AmazingToolbar(Context context) {
        this(context, null);
    }

    public AmazingToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AmazingToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setWillNotDraw(false);
        inflate(context, R.layout.amazing_tool_bar, this);
        ButterKnife.bind(this);

        this.tabs = new TextView[]{this.left_tab, this.center_tab, this.right_tab};
        this.tabTextColor = this.center_tab.getTextColors();

        this.rectPaint = new Paint();
        this.rectPaint.setAntiAlias(true);
        this.rectPaint.setStyle(Paint.Style.FILL);
        this.rectPaint.setColor(ColorBlue);
    }

    public void setViewPager(ViewPager paramViewPager, HomeContainerFragment.ContainerAdapter paramContainerAdapter) {
        this.mAdapter = paramContainerAdapter;
        this.mViewPager = paramViewPager;
        this.mViewPager.addOnPageChangeListener(new PagerListener());
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawLine(canvas);
    }

    private void drawLine(Canvas canvas) {
        if ((isInEditMode()) || (this.tabCount == 0) || (this.tabCount == 1))
            return;

        int i = getHeight();
        TextView localTextView = this.tabs[this.currentPosition];
        int j = localTextView.getPaddingLeft();
        float left = localTextView.getLeft();
        float right = localTextView.getRight();

        float left1 = left;
        float right1 = right;

        if(this.currentPositionOffset > 0.0F){
            if(this.currentPosition < tabCount - 1){
                localTextView = tabs[currentPosition + 1];
                left1 = localTextView.getLeft();
                right1 = localTextView.getRight();
                left1 = this.currentPositionOffset * left1 + (1.0F - this.currentPositionOffset) * left;
                right1 = this.currentPositionOffset * right1 + (1.0F - this.currentPositionOffset) * right;
            }
        }

        right = j;
        left = j;
        j = tabLayout.getLeft();
        float f5 = j;
        float f6 = j;
        canvas.drawRect(left1 + right + f5, i - 4, right1 - left + f6, i, this.rectPaint);
    }

    public void notifyDataSetChanged() {
        this.tabCount = this.mAdapter.getCount();
        TabClick localTabClick;
        localTabClick = new TabClick();

        for (int i = 0; i < tabCount; i++) {
            this.tabs[i].setOnClickListener(localTabClick);
            this.tabs[i].setText(this.mAdapter.getPageTitle(i));
            if (i != this.mViewPager.getCurrentItem())
                continue;
            this.tabs[i].setSelected(true);
        }

    }

    private class PagerListener extends ViewPager.SimpleOnPageChangeListener{
        public PagerListener() {
        }
        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < tabCount ; i++) {
                TextView textView = tabs[i];
                if(i == position) {
                    textView.setSelected(true);
                }else{
                    textView.setSelected(false);
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.e(TAG, "onPageScrolled: position" + position );
            Log.e(TAG, "onPageScrolled: positionOffset" + positionOffset );
            Log.e(TAG, "onPageScrolled: positionOffsetPixels" + positionOffsetPixels );
            currentPositionOffset = positionOffset;
            currentPosition = position;
            invalidate();
        }
    }

    private class TabClick
            implements View.OnClickListener {
        private TabClick() {
        }

        public void onClick(View paramView) {

            for (int i = 0; i < tabCount; i++) {
                if (tabs[i] == paramView) {
                    mViewPager.setCurrentItem(i);
                }
            }
        }
    }
}
