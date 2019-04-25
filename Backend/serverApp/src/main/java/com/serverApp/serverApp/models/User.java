package com.serverApp.serverApp.models;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity representing the users table
 *
 * @author Griffin Stout
 */
@Entity
@Table(name = "users")
public class User implements Serializable {
      private static final long serialVersionUID = 1L;

      @Id
      @GeneratedValue(strategy = GenerationType.AUTO)
      private long id;

      @Column(name = "lastName")
      private String lastName;

      @Column(name = "firstName", nullable = false)
      private String firstName;

      @Column(name = "email", nullable = false)
      private String email;

      @Column(name = "salt")
      private byte[] salt;

      @Column(name = "password", nullable = false)
      private String password;

      @Column(name = "type", nullable = false)
      private int type;

      public static long getSerialVersionUID() {
        return serialVersionUID;
      }

      public long getId() {
        return id;
      }

      public void setId(long id) {
        this.id = id;
      }

      public String getLastName() {
        return lastName;
      }

      public void setLastName(String lastName) {
        this.lastName = lastName;
      }

      public String getFirstName() {
        return firstName;
      }

      public void setFirstName(String firstName) {
        this.firstName = firstName;
      }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
      }

      public void setPassword(String password) {
        this.password = password;
      }

      public int getType() {
        return type;
      }

      public void setType(int type) {
        this.type = type;
      }
}
