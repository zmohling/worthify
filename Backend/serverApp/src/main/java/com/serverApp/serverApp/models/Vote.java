package com.serverApp.serverApp.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Vote {
    @Id
    private long id;

    @ManyToOne
    private Article article;

    @ElementCollection
    private List<Long> row;
}
