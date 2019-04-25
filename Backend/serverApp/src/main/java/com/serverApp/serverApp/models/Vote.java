package com.serverApp.serverApp.models;


import javax.persistence.*;

/**
 * Entity representing the vote table
 *
 * @author Griffin Stout
 */
@Entity
@Table(name = "vote")
public class Vote {

    public Vote(long userId, int vote, Article article){
        setUserId(userId);
        setVote(vote);
        setArticle(article);
    }

    public Vote(){}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(name = "userId")
    private long userId;

    @Column(name = "vote")
    private int vote; //either 1, 0, or -1

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
