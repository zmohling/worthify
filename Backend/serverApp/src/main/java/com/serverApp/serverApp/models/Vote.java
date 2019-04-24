package com.serverApp.serverApp.models;


import javax.persistence.*;

@Entity
@Table(name = "vote")
public class Vote {

    public Vote(long userId, int vote){
        setUserId(userId);
        setVote(vote);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
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
}
