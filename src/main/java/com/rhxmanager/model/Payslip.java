package com.rhxmanager.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Payslip", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employe_id", "month", "year"})
})
public class Payslip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_payslip;

    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private int year;

    private double bonus;
    private double deductions;

    @Column(nullable = false)
    private double net;

    // ------------------------------------------

    @ManyToOne(optional = false)
    @JoinColumn(name = "employe_id", nullable = false)
    private Employe employe;

    // ------------------------------------------

    public Payslip() {
    }
    public int getId_payslip() {
        return id_payslip;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public double getBonus() {
        return bonus;
    }
    public void setBonus(double bonus) {
        this.bonus = bonus;
    }
    public double getDeductions() {
        return deductions;
    }
    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }
    public double getNet() {
        return net;
    }
    public void setNet(double net) {
        this.net = net;
    }
    public Employe getEmploye() {
        return employe;
    }
    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    @Override
    public String toString() {
        return "Payslip{" +
                "id_payslip=" + id_payslip +
                ", month=" + month +
                ", year=" + year +
                ", bonus=" + bonus +
                ", deductions=" + deductions +
                ", net=" + net +
                ", employe=" + employe +
                '}';
    }
}