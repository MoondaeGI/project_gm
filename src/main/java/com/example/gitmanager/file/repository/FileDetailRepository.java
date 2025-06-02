package com.example.gitmanager.file.repository;

import com.example.gitmanager.file.entity.FileDetail;
import com.example.gitmanager.file.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileDetailRepository extends JpaRepository<FileDetail, Long> {
    List<FileDetail> findByFiles(Files files);
    Optional<FileDetail> findBySystemFileName(String systemFileName);
}
