package com.rhxmanager.controller;

import com.rhxmanager.model.Department;
import com.rhxmanager.model.Employe;
import com.rhxmanager.repository.DepartmentRepository;
import com.rhxmanager.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Arrays;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeRepository employeRepository;

    @GetMapping
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentRepository.findAllWithDetails());
        model.addAttribute("department", new Department());
        return "departments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Department department = departmentRepository.findByIdWithDetails(id).orElseThrow(() -> new IllegalArgumentException("Invalid department Id:" + id));
        model.addAttribute("department", department);
        model.addAttribute("allEmployees", employeRepository.findAllWithDepartment());
        return "edit-department";
    }

    @PostMapping("/create")
    public String createDepartment(@ModelAttribute Department department, RedirectAttributes redirectAttributes) {
        if (departmentRepository.findByDepartmentName(department.getDepartmentName()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "A department with this name already exists.");
        } else {
            departmentRepository.save(department);
            redirectAttributes.addFlashAttribute("success", "Department created successfully.");
        }
        return "redirect:/departments";
    }

    @PostMapping("/update/{id}")
    public String updateDepartment(@PathVariable int id, @ModelAttribute Department departmentDetails, @RequestParam(required = false) Integer managerId, @RequestParam(required = false) Integer[] employees, RedirectAttributes redirectAttributes) {
        Department department = departmentRepository.findByIdWithDetails(id).orElseThrow(() -> new IllegalArgumentException("Invalid department Id:" + id));

        department.setDepartmentName(departmentDetails.getDepartmentName());

        // Mettre à jour le manager
        if (managerId != null && managerId > 0) {
            department.setManager(employeRepository.findById(managerId).orElse(null));
        } else {
            department.setManager(null);
        }
        departmentRepository.save(department);

        Set<Integer> newMemberIds = (employees != null) ? Arrays.stream(employees).collect(Collectors.toSet()) : new HashSet<>();

        for (Employe currentMember : new HashSet<>(department.getEmployees())) {
            if (!newMemberIds.contains(currentMember.getId_employe())) {
                currentMember.setDepartment(null);
                employeRepository.save(currentMember);
            }
        }
        for (Integer newMemberId : newMemberIds) {
            Employe employee = employeRepository.findById(newMemberId).get();
            if (department.getId_department() != (employee.getDepartment() != null ? employee.getDepartment().getId_department() : 0)) {
                employee.setDepartment(department);
                employeRepository.save(employee);
            }
        }

        redirectAttributes.addFlashAttribute("success", "Department updated successfully.");
        return "redirect:/departments";
    }

    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable int id, RedirectAttributes redirectAttributes) {
        Department department = departmentRepository.findByIdWithDetails(id).orElseThrow(() -> new IllegalArgumentException("Invalid department Id:" + id));

        for (Employe employee : department.getEmployees()) {
            employee.setDepartment(null);
            employeRepository.save(employee);
        }

        departmentRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Department deleted successfully.");
        return "redirect:/departments";
    }
}