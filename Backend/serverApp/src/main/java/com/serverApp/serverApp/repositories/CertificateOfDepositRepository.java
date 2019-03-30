package com.serverApp.serverApp.repositories;


import com.serverApp.serverApp.models.CertificateOfDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateOfDepositRepository extends JpaRepository<CertificateOfDeposit, Long> {

    @Query(value = "SELECT DISTINCT * FROM certificate_of_Deposit WHERE accounts_Id = ?1", nativeQuery = true)
    CertificateOfDeposit getCertificateOfDeposite(String userId);
}
