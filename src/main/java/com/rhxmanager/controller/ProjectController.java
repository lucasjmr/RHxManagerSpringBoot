package com.rhxmanager.controller;

import com.rhxmanager.model.Employe;
import com.rhxmanager.model.Project;
import com.rhxmanager.model.ProjectState;
import com.rhxmanager.repository.EmployeRepository;
import com.rhxmanager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private EmployeRepository employeRepository;

    @GetMapping
    public String listProjects(Model model) {
        model.addAttribute("projects", projectRepository.findAllWithDetails());
        model.addAttribute("project", new Project());
        model.addAttribute("allStates", ProjectState.values());
        return "projects";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Project project = projectRepository.findByIdWithDetails(id).orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + id));
        model.addAttribute("project", project);
        model.addAttribute("allEmployees", employeRepository.findAll());
        model.addAttribute("allStates", ProjectState.values());
        return "edit-project";
    }

    @PostMapping("/create")
    public String createProject(@ModelAttribute Project project, RedirectAttributes redirectAttributes) {
        projectRepository.save(project);
        redirectAttributes.addFlashAttribute("success", "Project created successfully.");
        return "redirect:/projects";
    }

    @PostMapping("/update/{id}")
    public String updateProject(@PathVariable int id, @ModelAttribute Project projectDetails, @RequestParam(required = false) Integer projectLeadId, @RequestParam(required = false) Integer[] employees, RedirectAttributes redirectAttributes) {
        Project project = projectRepository.findByIdWithDetails(id).orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + id));

        project.setProjectName(projectDetails.getProjectName());
        project.setState(projectDetails.getState());

        if (projectLeadId != null && projectLeadId > 0) {
            project.setProjectLead(employeRepository.findById(projectLeadId).orElse(null));
        } else {
            project.setProjectLead(null);
        }

        Set<Integer> newEmployeeIds = (employees != null) ? Arrays.stream(employees).collect(Collectors.toSet()) : new HashSet<>();
        Set<Employe> currentEmployees = new HashSet<>(project.getEmployees());

        for (Employe currentEmployee : currentEmployees) {
            if (!newEmployeeIds.contains(currentEmployee.getId_employe())) {
                project.removeEmployee(currentEmployee);
                employeRepository.save(currentEmployee);
            }
        }
        for (Integer newEmployeeId : newEmployeeIds) {
            if (project.getEmployees().stream().noneMatch(e -> e.getId_employe() == newEmployeeId)) {
                Employe employeeToAdd = employeRepository.findById(newEmployeeId).get();
                project.addEmployee(employeeToAdd);
            }
        }

        projectRepository.save(project);
        redirectAttributes.addFlashAttribute("success", "Project updated successfully.");
        return "redirect:/projects";
    }

    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable int id, RedirectAttributes redirectAttributes) {
        Project project = projectRepository.findByIdWithDetails(id).orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + id));

        for (Employe employee : new HashSet<>(project.getEmployees())) {
            project.removeEmployee(employee);
            employeRepository.save(employee);
        }

        projectRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Project deleted successfully.");
        return "redirect:/projects";
    }
}