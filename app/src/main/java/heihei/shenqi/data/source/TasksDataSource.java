package heihei.shenqi.data.source;

import java.util.List;

import heihei.shenqi.data.Task;
import rx.Observable;

/**
 * User: lyjq(1752095474)
 * Date: 2016-07-23
 */
public interface TasksDataSource {

    Observable<List<Task>> getTasks(int page);

    Observable<List<Task>> getAirs(int page);

    Observable<List<Task>> getRtys(int page);
}
