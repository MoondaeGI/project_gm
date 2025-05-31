package com.example.gitmanager.file.service;

import com.example.gitmanager.file.dto.FileDetailDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<FileDetailDTO> findById(long mapperId);
    void insert(MultipartFile[] multipartFiles, long mapperId);
    void deleteAll(long mapperId);
    void delete(long id);
}
