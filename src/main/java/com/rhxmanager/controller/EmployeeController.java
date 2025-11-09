package com.rhxmanager.controller;

import com.rhxmanager.model.*;
import com.rhxmanager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired private EmployeRepository employeRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping
    public String listOrSearchEmployees(@RequestParam(required = false) String keyword, Model model) {
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("employees", employeRepository.searchByKeyword(keyword));
            model.addAttribute("searchKeyword", keyword);
        } else {
            model.addAttribute("employees", employeRepository.findAll());
        }
        if (!model.containsAttribute("employee")) {
            model.addAttribute("employee", new Employe());
        }
        return "employees";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Employe employee = employeRepository.findForEdit(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
        model.addAttribute("employee", employee);
        model.addAttribute("allRoles", roleRepository.findAll());
        model.addAttribute("allProjects", projectRepository.findAll());
        return "edit-employee";
    }

    @PostMapping("/create")
    public String createEmployee(@ModelAttribute Employe employee, RedirectAttributes redirectAttributes) {
        if (employeRepository.findByUsername(employee.getUsername()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Username '" + employee.getUsername() + "' is already taken.");
            redirectAttributes.addFlashAttribute("employee", employee);
            return "redirect:/employees";
        }

        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        Role defaultRole = roleRepository.findByRoleName("EMPLOYE")
                .orElseThrow(() -> new IllegalStateException("Default role 'EMPLOYE' not found."));
        employee.setRoles(Set.of(defaultRole));

        employeRepository.save(employee);
        redirectAttributes.addFlashAttribute("success", "Employee created successfully.");
        return "redirect:/employees";
    }

    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable int id, @ModelAttribute Employe employeeDetails,
                                 @RequestParam(required = false) Integer[] roles,
                                 @RequestParam(required = false) Integer[] projects,
                                 RedirectAttributes redirectAttributes) {

        Employe employee = employeRepository.findForEdit(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));

        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setGrade(employeeDetails.getGrade());
        employee.setJobName(employeeDetails.getJobName());
        employee.setSalary(employeeDetails.getSalary());

        if (employeeDetails.getPassword() != null && !employeeDetails.getPassword().isEmpty()) {
            employee.setPassword(passwordEncoder.encode(employeeDetails.getPassword()));
        }

        Set<Role> assignedRoles = new HashSet<>();
        if (roles != null) {
            for (int roleId : roles) {
                roleRepository.findById(roleId).ifPresent(assignedRoles::add);
            }
        }
        employee.setRoles(assignedRoles);

        Set<Project> assignedProjects = new HashSet<>();
        if (projects != null) {
            for (int projectId : projects) {
                projectRepository.findById(projectId).ifPresent(assignedProjects::add);
            }
        }
        employee.setProjects(assignedProjects);

        employeRepository.save(employee);
        redirectAttributes.addFlashAttribute("success", "Employee updated successfully.");
        return "redirect:/employees";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        Employe employeeToDelete = employeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));

        projectRepository.findByProjectLead(employeeToDelete).forEach(p -> {
            p.setProjectLead(null);
            projectRepository.save(p);
        });
        departmentRepository.findByManager(employeeToDelete).forEach(d -> {
            d.setManager(null);
            departmentRepository.save(d);
        });

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        boolean selfDelete = currentUsername.equals(employeeToDelete.getUsername());

        employeRepository.delete(employeeToDelete);

        if (selfDelete) {
            session.invalidate();
            redirectAttributes.addFlashAttribute("flashMessage", "Your account has been successfully deleted.");
            return "redirect:/login";
        }

        redirectAttributes.addFlashAttribute("success", "Employee deleted successfully.");
        return "redirect:/employees";
    }
}