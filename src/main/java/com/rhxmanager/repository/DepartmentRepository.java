package com.rhxmanager.repository;

import com.rhxmanager.model.Department;
import com.rhxmanager.model.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Optional<Department> findByDepartmentName(String departmentName);

    List<Department> findByManager(Employe manager);

    @Query("SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.employees LEFT JOIN FETCH d.manager")
    List<Department> findAllWithDetails();

    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.employees LEFT JOIN FETCH d.manager WHERE d.id_department = :id")
    Optional<Department> findByIdWithDetails(int id);
}