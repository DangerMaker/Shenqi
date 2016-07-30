package heihei.shenqi.presentation.luluhei;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import heihei.shenqi.R;
import heihei.shenqi.data.source.remote.TasksRemoteDataSource;
import heihei.frame.util.ActivityUtils;

/**
 * User: lyjq(1752095474)
 * Date: 2016-07-25
 */
public class LuluheiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luluhei);

        LuluheiFragment luluheiFragment = (LuluheiFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (luluheiFragment == null) {
            luluheiFragment = LuluheiFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    luluheiFragment, R.id.contentFrame);
        }

        new LuluheiPresenter(TasksRemoteDataSource.getInstance(this),luluheiFragment);
    }
}
