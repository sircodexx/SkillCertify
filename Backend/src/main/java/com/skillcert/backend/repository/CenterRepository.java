package com.skillcert.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skillcert.backend.entity.Center;
import com.skillcert.backend.entity.Evaluation;
import com.skillcert.backend.entity.User;

@Repository
public interface CenterRepository extends JpaRepository<Center, Long> {
    // Buscar centros por estado
    List<Center> findByStatus(Center.CenterStatus status);

    // Buscar centros por ciudad
    List<Center> findByCityContainingIgnoreCase(String city);

    // Buscar centros por región
    List<Center> findByRegionContainingIgnoreCase(String region);

    // Buscar centros por nombre
    List<Center> findByNameContainingIgnoreCase(String name);

    // Obtener estudiantes de un centro específico
    @Query("SELECT uc.user FROM UserCenter uc WHERE uc.center.id = :centerId AND uc.status = 'ACTIVE'")
    List<User> findStudentsByCenterId(@Param("centerId") Long centerId);

    // Obtener evaluaciones asignadas a un centro
    @Query("SELECT ce.evaluation FROM CenterEvaluation ce WHERE ce.center.id = :centerId AND ce.status = 'ACTIVE'")
    List<Evaluation> findEvaluationsByCenterId(@Param("centerId") Long centerId);

    // Contar estudiantes activos por centro
    @Query("SELECT COUNT(uc) FROM UserCenter uc WHERE uc.center.id = :centerId AND uc.status = 'ACTIVE'")
    Long countActiveStudentsByCenterId(@Param("centerId") Long centerId);

    // Verificar si existe un centro con el mismo nombre
    boolean existsByNameIgnoreCase(String name);

    // Buscar centros con capacidad disponible
    @Query("SELECT c FROM Center c WHERE c.studentsCount < c.capacity AND c.status = 'ACTIVE'")
    List<Center> findCentersWithAvailableCapacity();
}
