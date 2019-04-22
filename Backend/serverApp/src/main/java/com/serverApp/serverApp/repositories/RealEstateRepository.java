package com.serverApp.serverApp.repositories;


import com.serverApp.serverApp.models.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RealEstateRepository extends JpaRepository<RealEstate, Long> {

    @Query(value = "SELECT DISTINCT * FROM real_estate WHERE accountID = ?1", nativeQuery = true)
    RealEstate getRealEstate(String userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE real_estate SET address = ?1, city = ?2, state = ?3 WHERE accountID = ?4", nativeQuery = true)
    void editRealEstate(String address, String city, String state, String accountID);
}
