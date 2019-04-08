package com.serverApp.serverApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.serverApp.serverApp.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users WHERE email = ?1", nativeQuery = true)
    User getUser(String email);

    @Query(value = "SELECT count(*) FROM users WHERE email = ?1", nativeQuery = true)
    int checkEmail(String email);

    @Query(value = "SELECT * FROM users WHERE email = ?1 AND password = ?2", nativeQuery = true)
    User getUser(String email, String password);

    @Query(value = "SELECT * FROM users WHERE email = ?1 AND password = ?2 AND type = 1", nativeQuery = true)
    User getAdmin(String email, String password);

    @Query(value = "SELECT count(*) FROM users WHERE password = ?1", nativeQuery = true)
    int checkUserExists(String password);

    @Query(value = "SELECT id FROM users WHERE password = ?1", nativeQuery = true)
    long getUserID(String password);

    @Query(value = "SELECT * FROM users WHERE password = ?1 AND type = 1", nativeQuery = true)
    User getAdmin(String password);
}
