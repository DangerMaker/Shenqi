package heihei.shenqi;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * User: lyjq(1752095474)
 * Date: 2016-07-24
 */
public class AmazingApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
