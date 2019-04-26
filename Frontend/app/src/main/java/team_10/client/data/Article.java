package team_10.client.data;

/**
 * Class that stores an articles values for title, description, url, and picture url.
 */
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

    /**
     * Gets the url.
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets the title.
     * @return title
     */
    public String getTitle() { return title; }

    /**
     * Gets the description.
     * @return description
     */
    public String getDescription() { return description; }

    /**
     * Gets the picture url.
     * @return picture url
     */
    public String getPictureUrl() { return pictureUrl; }

    private static int lastContactId = 0;
}