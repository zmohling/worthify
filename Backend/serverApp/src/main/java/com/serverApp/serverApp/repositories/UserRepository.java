package com.serverApp.serverApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.serverApp.serverApp.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users WHERE email = ?1", nativeQuery = true)
    User getUser(String email);
}
