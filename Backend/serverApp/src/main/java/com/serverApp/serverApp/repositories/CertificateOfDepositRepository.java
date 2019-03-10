package com.serverApp.serverApp.repositories;


import com.serverApp.serverApp.models.CertificateOfDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateOfDepositRepository extends JpaRepository<CertificateOfDeposit, Long> {

}
