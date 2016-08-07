package heihei.frame;

/**
 * Created by Administrator on 2016/8/7.
 */
public class BannerModel {
    String title;
    String img;
    String url;

    public BannerModel(String title, String img, String url) {
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
