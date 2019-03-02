package com.serverApp.serverApp.models;


import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "accounts")
public class Accounts implements Serializable{
    private static final long serialVersionUID = 3L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "type")
    private int type;

    @Column(name = "label")
    private String label;

    @Column(name = "userId")
    private long userId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}

