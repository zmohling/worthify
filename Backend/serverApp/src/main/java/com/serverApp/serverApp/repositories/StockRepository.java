package com.serverApp.serverApp.repositories;

import com.serverApp.serverApp.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query(value = "SELECT DISTINCT * FROM stocks WHERE accountID = ?1", nativeQuery = true)
    Stock getStock(String userId);
}
