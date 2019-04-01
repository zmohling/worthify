package com.serverApp.serverApp.repositories;


import com.serverApp.serverApp.models.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RealEstateRepository extends JpaRepository<RealEstate, Long> {

    @Query(value = "SELECT DISTINCT * FROM real_estate WHERE accountID = ?1", nativeQuery = true)
    RealEstate getRealEstate(String userId);
}
