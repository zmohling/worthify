package com.serverApp.serverApp.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "loan")
public class Loan implements Serializable{
    private static final long serialVersionUID = 4L;

    @Id
    private long id;

    @Column(name = "transaction")
    private String transaction;
}
