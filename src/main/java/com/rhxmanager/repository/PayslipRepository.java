package com.rhxmanager.repository;

import com.rhxmanager.model.Payslip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayslipRepository extends JpaRepository<Payslip, Integer> {

    @Query("SELECT COUNT(p) > 0 FROM Payslip p WHERE p.employe.id_employe = :employeeId AND p.month = :month AND p.year = :year")
    boolean existsForEmployeeAndPeriod(int employeeId, int month, int year);

    @Query("SELECT p FROM Payslip p JOIN FETCH p.employe e WHERE " +
            "(:employeeId = 0 OR e.id_employe = :employeeId) AND " +
            "(:month = 0 OR p.month = :month) AND " +
            "(:year = 0 OR p.year = :year) " +
            "ORDER BY p.year DESC, p.month DESC")
    List<Payslip> findByCriteria(int employeeId, int month, int year);

    @Query("SELECT p FROM Payslip p JOIN FETCH p.employe e LEFT JOIN FETCH e.department WHERE p.id_payslip = :id")
    Optional<Payslip> findByIdWithDetails(int id);
}