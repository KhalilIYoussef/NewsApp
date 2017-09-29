package khaliliyoussef.newsapp.model;


public class News {


    private String section;
    private String title;
    private String date;
    private String url;

    public News(String section, String title, String date, String url) {
        this.section = section;
        this.title = title;
        this.date = date;
        this.url = url;
    }

    public String getSection() {
        return section;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getURL() {
        return url;
    }
    public void setSection(String section) {
        this.section = section;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
