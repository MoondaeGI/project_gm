package com.example.gitmanager.file.service;

import com.example.gitmanager.file.dto.FileDetailDTO;
import com.example.gitmanager.file.dto.FilesDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<FileDetailDTO> findByParentId(FilesDTO dto);
    FileDetailDTO findById(long id);
    FileDetailDTO findBySystemFileName(String systemFileName);
    void insert(MultipartFile[] multipartFiles, FilesDTO dto);
    void update(MultipartFile[] multipartFiles, List<FileDetailDTO> fileDetailDTOList);
    void deleteAll(FilesDTO dto);
    void delete(long id);
    ByteArrayResource download(String fileName);
}
