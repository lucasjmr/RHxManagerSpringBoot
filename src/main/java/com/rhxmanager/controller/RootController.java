package com.rhxmanager.controller;

import com.rhxmanager.model.ProjectState;
import com.rhxmanager.repository.DepartmentRepository;
import com.rhxmanager.repository.EmployeRepository;
import com.rhxmanager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @Autowired
    private EmployeRepository employeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/")
    public String root() {
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalEmployees", employeRepository.count());
        model.addAttribute("totalDepartments", departmentRepository.count());
        model.addAttribute("totalProjects", projectRepository.count());
        model.addAttribute("activeProjects", projectRepository.countByState(ProjectState.WORKED_ON));

        return "dashboard";
    }
}