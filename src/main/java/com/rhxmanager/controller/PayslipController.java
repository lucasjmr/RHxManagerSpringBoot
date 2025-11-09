package com.rhxmanager.controller;

import com.rhxmanager.model.Employe;
import com.rhxmanager.model.Payslip;
import com.rhxmanager.repository.EmployeRepository;
import com.rhxmanager.repository.PayslipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Random;

@Controller
@RequestMapping("/payslips")
public class PayslipController {

    @Autowired
    private PayslipRepository payslipRepository;
    @Autowired
    private EmployeRepository employeRepository;

    @GetMapping
    public String listPayslips(@RequestParam(defaultValue = "0") int employeeId, @RequestParam(defaultValue = "0") int month, @RequestParam(defaultValue = "0") int year, Model model) {

        model.addAttribute("payslips", payslipRepository.findByCriteria(employeeId, month, year));
        model.addAttribute("allEmployees", employeRepository.findAll());

        model.addAttribute("selectedEmployeeId", employeeId);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedYear", year);

        return "payslips";
    }

    @GetMapping("/view/{id}")
    public String viewPayslip(@PathVariable int id, Model model) {
        Payslip payslip = payslipRepository.findByIdWithDetails(id).orElseThrow(() -> new IllegalArgumentException("Invalid payslip Id:" + id));
        model.addAttribute("payslip", payslip);
        return "payslip-details";
    }

    @PostMapping("/create")
    public String createPayslip(@RequestParam int employeeId, @RequestParam int month, @RequestParam int year, RedirectAttributes redirectAttributes) {

        if (payslipRepository.existsForEmployeeAndPeriod(employeeId, month, year)) {
            redirectAttributes.addFlashAttribute("error", "A payslip already exists for this employee and period.");
            return "redirect:/payslips";
        }

        Employe employee = employeRepository.findById(employeeId).orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + employeeId));

        Random random = new Random();
        double bonus = 50 + (450 * random.nextDouble());
        double deductions = 20 + (280 * random.nextDouble());
        double netToPay = employee.getSalary() + bonus - deductions;

        Payslip payslip = new Payslip();
        payslip.setEmploye(employee);
        payslip.setMonth(month);
        payslip.setYear(year);
        payslip.setBonus(bonus);
        payslip.setDeductions(deductions);
        payslip.setNet(netToPay);

        payslipRepository.save(payslip);
        redirectAttributes.addFlashAttribute("success", "Payslip generated successfully.");
        return "redirect:/payslips";
    }
}