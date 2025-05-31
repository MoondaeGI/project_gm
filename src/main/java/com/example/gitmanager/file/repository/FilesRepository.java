package com.example.gitmanager.file.repository;

import com.example.gitmanager.file.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilesRepository extends JpaRepository<Files, Long> {
}
