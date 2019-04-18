package com.serverApp.serverApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.serverApp.serverApp.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Query(value = "SELECT * FROM users WHERE type = 0", nativeQuery = true)
    User[] listAll();

    @Modifying
    @Transactional
    @Query(value ="UPDATE users u set u.salt = ?1, u.password = ?2 WHERE u.email = ?3", nativeQuery = true)
    void changePassword(byte[] salt, String password, String email);

    @Modifying
    @Transactional
    @Query(value ="UPDATE users u set u.email = ?1 WHERE u.email = ?2", nativeQuery = true)
    void changeEmail(String changedEmail, String email);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users WHERE id = ?1", nativeQuery = true)
    int deleteUser(int id);
}
