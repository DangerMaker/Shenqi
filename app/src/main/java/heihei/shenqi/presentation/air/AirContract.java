package heihei.shenqi.presentation.air;

import java.util.List;

import heihei.shenqi.data.Task;
import heihei.shenqi.presentation.BasePresenter;
import heihei.shenqi.presentation.BaseView;

/**
 * User: lyjq(1752095474)
 * Date: 2016-07-25
 */
public class AirContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showTasks(List<Task> tasks);

        void addTasks(List<Task> tasks);

        void showError(String str);
        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void onRefresh();
    }
}
