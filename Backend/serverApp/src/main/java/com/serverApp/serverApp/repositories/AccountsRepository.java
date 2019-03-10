package com.serverApp.serverApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.serverApp.serverApp.models.Accounts;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {

    @Query(value = "SELECT * FROM accounts WHERE CAST(SUBSTRING(accountID, 1, 8) as unsigned) = ?1", nativeQuery = true)
    Collection<Accounts> getAccounts(Long userId);

    @Query(value = "SELECT * FROM accounts WHERE CAST(SUBSTRING(accountID, 1, 8) as unsigned) = ?1", nativeQuery = true)
    Accounts[] getAccountsById(long userId);

}
