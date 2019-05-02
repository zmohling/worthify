package com.serverApp.serverApp.repositories;


import com.serverApp.serverApp.models.CertificateOfDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * JPA repository for CertificateOfDeposit
 *
 * @author Michael Davis
 */
@Repository
public interface CertificateOfDepositRepository extends JpaRepository<CertificateOfDeposit, Long> {

    @Query(value = "SELECT DISTINCT * FROM certificate_of_deposit WHERE accountID = ?1", nativeQuery = true)
    CertificateOfDeposit getCertificateOfDeposite(String userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE certificate_of_deposit SET maturity_date = ?1 WHERE accountID = ?2", nativeQuery = true)
    void editCertificateOfDeposit(java.sql.Date date, String accountID);
}
