package com.serverApp.serverApp.repositories;

import com.serverApp.serverApp.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

/**
 * JPA repository for stocks
 *
 * @author Michael Davis
 *
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query(value = "SELECT DISTINCT * FROM stocks WHERE accountID = ?1", nativeQuery = true)
    Stock getStock(String userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE stocks SET ticker = ?1 WHERE accountID = ?2", nativeQuery = true)
    void editStock(String ticker, String accountID);

    @Transactional
    @Modifying
    @Query(value = "UPDATE stocks SET date = ?1 WHERE accountID = ?2", nativeQuery = true)
    void editDate(Date date, String accountID);
}
