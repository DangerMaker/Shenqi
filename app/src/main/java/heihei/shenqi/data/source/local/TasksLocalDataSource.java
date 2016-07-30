package heihei.shenqi.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import heihei.shenqi.data.Task;
import heihei.shenqi.data.source.TasksDataSource;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import heihei.shenqi.data.source.local.TasksPersistenceContract.TaskEntry;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * User: lyjq(1752095474)
 * Date: 2016-07-23
 */
public class TasksLocalDataSource implements TasksDataSource {

    private static TasksLocalDataSource INSTANCE;
    private final BriteDatabase mDatabaseHelper;
    private Func1<Cursor, Task> mTaskMapperFunction;

    // Prevent direct instantiation.
    private TasksLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        TasksDbHelper dbHelper = new TasksDbHelper(context);
        SqlBrite sqlBrite = SqlBrite.create();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, Schedulers.io());
        mTaskMapperFunction = new Func1<Cursor, Task>() {
            @Override
            public Task call(Cursor c) {
                String title = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
                String img = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_IMG));
                String url = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_URL));
                String length = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_LEN));
                return new Task(title, img, url, length);
            }
        };
    }

    public static TasksLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TasksLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Task>> getTasks(int page) {
        String[] projection = {
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_IMG,
                TaskEntry.COLUMN_NAME_URL,
                TaskEntry.COLUMN_NAME_LEN
        };
        String sql = String.format("SELECT %s FROM %s", TextUtils.join(",", projection), TaskEntry.TABLE_NAME);
        return mDatabaseHelper.createQuery(TaskEntry.TABLE_NAME, sql)
                .mapToList(mTaskMapperFunction);
    }
}
