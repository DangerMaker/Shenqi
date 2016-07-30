package heihei.shenqi.data;

/**
 * User: lyjq(1752095474)
 * Date: 2016-07-23
 */
public class Task {
    String title;
    String img;
    String url;
    String length;

    public Task(String title, String img, String url, String length) {
        this.title = title;
        this.img = img;
        this.url = url;
        this.length = length;
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

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
