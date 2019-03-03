package com.serverApp.serverApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.serverApp.serverApp.models.Loan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

}
