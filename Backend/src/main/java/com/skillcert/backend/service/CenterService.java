package com.skillcert.backend.service;


import java.util.List;

import com.skillcert.backend.dto.CenterDTO;
import com.skillcert.backend.dto.EvaluationAssignmentDTO;
import com.skillcert.backend.dto.EvaluationDTO;
import com.skillcert.backend.dto.StudentDTO;

public interface CenterService {
    List<CenterDTO> getAllCenters();
    CenterDTO getCenterById(Long id);
    CenterDTO createCenter(CenterDTO centerDTO);
    CenterDTO updateCenter(Long id, CenterDTO centerDTO);
    void deleteCenter(Long id);
    List<StudentDTO> getStudentsDTOByCenterId(Long centerId);
    List<EvaluationDTO> getEvaluationsDTOByCenterId(Long centerId);
    void assignEvaluationToCenter(Long centerId, EvaluationAssignmentDTO evalDTO);
}
