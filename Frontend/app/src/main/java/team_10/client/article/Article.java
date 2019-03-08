package team_10.client.article;

public class Article {
    private String url;
    private String title;
    private String description;

    public Article(String name) {
        url = name;
    }

    public String getUrl() {
        return url;
    }

    private static int lastContactId = 0;
}
