package com.example.gitmanager.project.repository;

import com.example.gitmanager.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findAll(@NonNull Pageable pageable);
}
