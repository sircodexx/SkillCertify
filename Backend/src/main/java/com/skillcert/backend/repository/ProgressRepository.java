package com.skillcert.backend.repository;

import com.skillcert.backend.entity.Progress;
import com.skillcert.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByUser(User user);
    List<Progress> findByUserId(Long userId);
}

