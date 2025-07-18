package com.skillcert.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillcert.backend.dto.CenterDTO;
import com.skillcert.backend.dto.EvaluationAssignmentDTO;
import com.skillcert.backend.service.CenterService;

@RestController
@RequestMapping("/api/centers")
public class CenterController {

    @Autowired
    private CenterService service;

    @GetMapping
    public List<CenterDTO> getAllCenters() {
        return service.getAllCenters();
    }

    @PostMapping
    public CenterDTO createCenter(@RequestBody CenterDTO dto) {
        return service.createCenter(dto);
    }

    @GetMapping("/{id}")
    public CenterDTO getCenterById(@PathVariable Long id) {
        return service.getCenterById(id);
    }

    @PutMapping("/{id}")
    public CenterDTO updateCenter(@PathVariable Long id, @RequestBody CenterDTO dto) {
        return service.updateCenter(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCenter(@PathVariable Long id) {
        service.deleteCenter(id);
    }

    @GetMapping("/{id}/students")
    public List<String> getStudents(@PathVariable Long id) {
        return service.getStudentsByCenterId(id);
    }

    @GetMapping("/{id}/evaluations")
    public List<String> getEvaluations(@PathVariable Long id) {
        return service.getEvaluationsByCenterId(id);
    }

    @PostMapping("/{id}/assign-evaluation")
    public void assignEvaluation(@PathVariable Long id, @RequestBody EvaluationAssignmentDTO dto) {
        service.assignEvaluationToCenter(id, dto);
    }
}
