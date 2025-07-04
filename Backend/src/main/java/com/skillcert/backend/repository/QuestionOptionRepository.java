package com.skillcert.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillcert.backend.entity.QuestionOption;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {
    
}