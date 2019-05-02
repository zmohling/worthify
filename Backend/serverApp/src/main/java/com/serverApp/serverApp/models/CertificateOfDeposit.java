package com.serverApp.serverApp.models;


import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 *
 * Entity representing the CertificateOfDeposit table
 * @author Michael Davis
 */
@Entity
@Table(name = "certificateOfDeposit")
public class CertificateOfDeposit implements Serializable {
    private static final long serialVersionUID = 5L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "accountID")
    private String accountID;

    @Column(name = "maturityDate")
    private java.sql.Date maturityDate;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountsId() {
        return accountID;
    }

    public void setAccountsId(String accountID) {
        this.accountID = accountID;
    }

    public Date getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
    }
}

