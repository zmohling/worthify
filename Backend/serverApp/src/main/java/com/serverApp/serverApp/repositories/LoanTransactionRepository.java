package com.serverApp.serverApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.serverApp.serverApp.models.LoanTransaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

@Repository
public interface LoanTransactionRepository extends JpaRepository<LoanTransaction, Long>{

    @Query(value = "SELECT date FROM loanTransaction WHERE id = ?1", nativeQuery = true)
    Collection<Date> getDate(long id);

    @Query(value = "SELECT interestRate FROM loanTransaction WHERE id = ?1", nativeQuery = true)
    Collection<Double> getInterestRate(long id);

    @Query(value = "SELECT amount FROM loanTransaction WHERE id = ?1", nativeQuery = true)
    Collection<Double> getAmount(long id);
}
