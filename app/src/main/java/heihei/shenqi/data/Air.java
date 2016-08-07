package heihei.shenqi.data;

/**
 * Created by Administrator on 2016/8/6.
 */
public class Air {
    String title;
    String img;
    String url;

    public Air(String title, String img, String url) {
        this.title = title;
        this.img = img;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
