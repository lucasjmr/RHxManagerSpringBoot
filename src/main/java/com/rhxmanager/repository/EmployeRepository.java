package com.rhxmanager.repository;

import com.rhxmanager.model.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Integer> {

    Optional<Employe> findByUsername(String username);

    @Query("SELECT e FROM Employe e LEFT JOIN FETCH e.roles LEFT JOIN FETCH e.projects WHERE e.id_employe = :id")
    Optional<Employe> findForEdit(int id);

    @Query("SELECT e FROM Employe e LEFT JOIN FETCH e.department")
    List<Employe> findAllWithDepartment();

    List<Employe> findByDepartmentIsNull();

    @Query("SELECT DISTINCT e FROM Employe e LEFT JOIN e.roles r WHERE " +
            "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.jobName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.grade) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.roleName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Employe> searchByKeyword(String keyword);

    @Query("SELECT e FROM Employe e LEFT JOIN FETCH e.projects WHERE e.id_employe = :id")
    Optional<Employe> findByIdWithProjects(int id);
}