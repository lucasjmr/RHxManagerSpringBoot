package com.rhxmanager.model;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "Department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_department;

    @Column(unique = true, nullable = false)
    private String departmentName;

    // ------------------------------------------

    @OneToOne
    @JoinColumn(name = "masterChief_id", referencedColumnName = "id_employe")
    private Employe manager;

    @OneToMany(mappedBy = "department")
    private Set<Employe> employees = new HashSet<>();

    // ------------------------------------------

    public Department() {
    }
    public int getId_department() {
        return id_department;
    }
    public void setId_department(int id_department) {
        this.id_department = id_department;
    }
    public String getDepartmentName() {
        return departmentName;
    }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    public Employe getManager() {
        return manager;
    }
    public void setManager(Employe manager) {
        this.manager = manager;
    }
    public Set<Employe> getEmployees() {
        return employees;
    }
    public void setEmployees(Set<Employe> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id_department=" + id_department +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}