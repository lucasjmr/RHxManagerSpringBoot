package com.rhxmanager.repository;

import com.rhxmanager.model.Employe;
import com.rhxmanager.model.Project;
import com.rhxmanager.model.ProjectState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    long countByState(ProjectState state);

    List<Project> findByProjectLead(Employe projectLead);

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.employees LEFT JOIN FETCH p.projectLead")
    List<Project> findAllWithDetails();

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.employees LEFT JOIN FETCH p.projectLead WHERE p.id_project = :id")
    Optional<Project> findByIdWithDetails(int id);
}