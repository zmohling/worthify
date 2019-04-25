package com.serverApp.serverApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.serverApp.serverApp.models.Accounts;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * JPA repository for Accounts
 *
 * @author Michael Davis
 *
 */
@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {

    @Query(value = "SELECT * FROM accounts WHERE CAST(SUBSTRING(accountID, 1, 8) as unsigned) = ?1", nativeQuery = true)
    Collection<Accounts> getAccounts(long userId);

    @Query(value = "SELECT DISTINCT * FROM accounts WHERE accountID = ?1", nativeQuery = true)
    Accounts getAccountsByAccountId(String userId);

    @Query(value = "SELECT * FROM accounts WHERE CAST(SUBSTRING(accountID, 1, 8) as unsigned) = ?1", nativeQuery = true)
    Accounts[] getAccountsById(long userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE accounts SET label = ?1, transactions = ?2 WHERE accountID = ?3", nativeQuery = true)
    void editAccount(String label, String transaction, String accountID);


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM accounts WHERE CAST(SUBSTRING(accountID, 1, 8) as unsigned) = ?1", nativeQuery = true)
    int deleteAllTiedToUser(int userId);

}
