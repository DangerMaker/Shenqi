package heihei.shenqi.data.source.remote;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import heihei.shenqi.Config;
import heihei.shenqi.data.Task;
import heihei.shenqi.data.source.TasksDataSource;
import rx.Observable;
import rx.Subscriber;

/**
 * User: lyjq(1752095474)
 * Date: 2016-07-23
 */
public class TasksRemoteDataSource implements TasksDataSource {

    private static TasksRemoteDataSource INSTANCE;
    private static Context mContext;

    //    private TaskApi service;
    private static String BASE_URL = Config.BASE_LULUHEI_URL;
    private static String BASE_AIR＿URL = "http://airav.cc";
    private static String BASE_RTYS＿URL = "http://www.27270.com/ent/rentiyishu/list_32_";

    public static TasksRemoteDataSource getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (INSTANCE == null) {
            INSTANCE = new TasksRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private TasksRemoteDataSource() {

    }

    @Override
    public Observable<List<Task>> getTasks(final int mPage) {

        return Observable.create(new Observable.OnSubscribe<List<Task>>() {
            @Override
            public void call(Subscriber<? super List<Task>> subscriber) {
                if (isThereInternetConnection()) {
                    try {
                        Document response = Jsoup.connect(BASE_URL + "/videos?o=bw&page=" + Integer.toString(mPage)).get();
                        if (response != null) {
                            subscriber.onNext(parseToTasks(response));
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new NetworkConnectionException());
                        }
                    } catch (Exception e) {
                        subscriber.onError(new NetworkConnectionException(e.getCause()));
                    }
                } else {
                    subscriber.onError(new NetworkConnectionException());
                }
            }
        });
    }

    @Override
    public Observable<List<Task>> getAirs(final int mPage) {

        return Observable.create(new Observable.OnSubscribe<List<Task>>() {
            @Override
            public void call(Subscriber<? super List<Task>> subscriber) {
                if (isThereInternetConnection()) {
                    try {
                        Document response = Jsoup.connect(BASE_AIR＿URL + "/index.aspx?status=1&idx=" + Integer.toString(mPage)).get();
                        if (response != null) {
                            subscriber.onNext(parseToAirs(response));
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new NetworkConnectionException());
                        }
                    } catch (Exception e) {
                        subscriber.onError(new NetworkConnectionException(e.getCause()));
                    }
                } else {
                    subscriber.onError(new NetworkConnectionException());
                }
            }
        });
    }

    @Override
    public Observable<List<Task>> getRtys(final int mPage) {
        return Observable.create(new Observable.OnSubscribe<List<Task>>() {
            @Override
            public void call(Subscriber<? super List<Task>> subscriber) {
                if (isThereInternetConnection()) {
                    try {
                        Document response = Jsoup.connect(BASE_RTYS＿URL + Integer.toString(mPage) + ".html").get();
                        if (response != null) {
                            subscriber.onNext(parseToRtys(response));
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new NetworkConnectionException());
                        }
                    } catch (Exception e) {
                        subscriber.onError(new NetworkConnectionException(e.getCause()));
                    }
                } else {
                    subscriber.onError(new NetworkConnectionException());
                }
            }
        });
    }

    private List<Task> parseToTasks(Document document) {
        Elements elements = document.select("div[class^=well well-sm]");
        List<Task> tasks = new ArrayList<>();
        for (Element element : elements) {
            Element imgElement = element.select("img").first();
            if (imgElement == null) {
                continue;
            }
            String imgUrl = imgElement.attr("src");
            System.out.println("img -------->" + imgUrl);

            String title = imgElement.attr("title");
            System.out.println("title ------->" + title);

            Elements lenElement = element.select("div[class^=duration]");
            String length = lenElement.text();
            System.out.println("length ------->" + length);

            Element urlElement = element.select("a").first();
            String href = urlElement.attr("href");
            System.out.println("href ------->" + href);
            Task task = new Task(title,imgUrl,href,length);
            tasks.add(task);
        }
        return tasks;
    }

    private List<Task> parseToAirs(Document document) {
        Elements elements = document.select("li[class^=listItem]");
        List<Task> tasks = new ArrayList<>();
        for (Element element : elements) {
            Element imgElement = element.select("img").first();
            if (imgElement == null) {
                continue;
            }
            String imgUrl = imgElement.attr("src");
            System.out.println("img -------->" + imgUrl);

            Elements lenElement = element.select("h3[class^=one_name ga_name]");
            String title = lenElement.text();
            System.out.println("title ------->" + title);

            Elements hrefElement = element.select("a[class^=ga_click]");
            String href = hrefElement.attr("href");
            System.out.println("href ------->" + href);

            Task task = new Task(title,imgUrl,href,"");
            tasks.add(task);
        }
        return tasks;
    }

    private List<Task> parseToRtys(Document document) {
//        Elements elements = document.select("div[class^=MeinvTuPianBox]");
        Elements elements = document.select("div[class^=libox]");
        Iterator<Element> iterator = elements.iterator();
        int i = 0;

        while (iterator.hasNext()) {
            if (i < 8) {
                iterator.next();
                iterator.remove();
                i++;
            }else{
                break;
            }
        }

        List<Task> tasks = new ArrayList<>();
        for (Element element : elements) {
            Element imgElement = element.select("img").first();
            if (imgElement == null) {
                continue;
            }
            String imgUrl = imgElement.attr("lazysrc");
            System.out.println("img -------->" + imgUrl);

            Elements titleElement = element.select("p");
            String title = titleElement.text();
            System.out.println("title ------->" + title);

            Elements hrefElement = element.select("a");
            String href = hrefElement.attr("href");
            System.out.println("href ------->" + href);

            Task task = new Task(title,imgUrl,href,"");
            tasks.add(task);
        }
        return tasks;
    }

    private boolean isThereInternetConnection() {
        boolean isConnected;

        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());
        return isConnected;
    }
}
