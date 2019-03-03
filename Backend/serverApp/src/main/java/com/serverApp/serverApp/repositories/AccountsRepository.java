package com.serverApp.serverApp.repositories;

import com.serverApp.serverApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.serverApp.serverApp.models.Accounts;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {

    @Query(value = "SELECT * FROM accounts WHERE userId = ?1", nativeQuery = true)
    Collection<Accounts> getAccounts(long userId);

}
