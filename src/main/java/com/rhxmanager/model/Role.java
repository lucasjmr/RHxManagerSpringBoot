package com.rhxmanager.model;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "Role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_role;

    @Column(unique = true, nullable = false)
    private String roleName;

    // ------------------------------------------

    @ManyToMany(mappedBy = "roles")
    private Set<Employe> employees = new HashSet<>();

    // ------------------------------------------

    public Role() {
    }
    public int getId_role() {
        return id_role;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public Set<Employe> getEmployees() {
        return employees;
    }
    public void setEmployees(Set<Employe> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id_role=" + id_role +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}