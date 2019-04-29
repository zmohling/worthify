package team_10.client.data;

/**
 * Class that stores an articles values for title, description, url, and picture url.
 */
public class Article {
    private String url;
    private String title;
    private String description;
    private String pictureUrl;
    private String articleID;
    private String articleVotes;
    private String currentVote;

    public Article(String titles, String descriptions, String pictureUrls, String name, String articleIDs, String articleVotess, String currentVotes) {
        url = name;
        title = titles;
        description = descriptions;
        pictureUrl = pictureUrls;
        articleID = articleIDs;
        articleVotes = articleVotess;
        currentVote = currentVotes;
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

    /**
     * Gets the article's ID
     * @return article ID
     */
    public String getArticleID() { return articleID; }

    public String getArticleVotes() { return articleVotes; }

    public void setArticleVotes(String newVotes) { articleVotes = newVotes; }

    public String getCurrentVote() { return currentVote; }

    public void setCurrentVote(String newVote) { currentVote = newVote; }

    private static int lastContactId = 0;
}