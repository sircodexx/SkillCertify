package com.skillcert.backend.service;


import java.util.List;

import com.skillcert.backend.dto.CenterDTO;
import com.skillcert.backend.dto.EvaluationAssignmentDTO;

public interface CenterService {
    List<CenterDTO> getAllCenters();
    CenterDTO getCenterById(Long id);
    CenterDTO createCenter(CenterDTO centerDTO);
    CenterDTO updateCenter(Long id, CenterDTO centerDTO);
    void deleteCenter(Long id);
    List<String> getStudentsByCenterId(Long centerId); // Placeholder
    List<String> getEvaluationsByCenterId(Long centerId); // Placeholder
    void assignEvaluationToCenter(Long centerId, EvaluationAssignmentDTO evalDTO); // Placeholder
}
