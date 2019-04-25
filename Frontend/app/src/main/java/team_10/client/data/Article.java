package team_10.client.data;

public class Article {
    private String url;
    private String title;
    private String description;
    private String pictureUrl;

    public Article(String titles, String descriptions, String pictureUrls, String name) {
        url = name;
        title = titles;
        description = descriptions;
        pictureUrl = pictureUrls;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public String getPictureUrl() { return pictureUrl; }

    private static int lastContactId = 0;
}