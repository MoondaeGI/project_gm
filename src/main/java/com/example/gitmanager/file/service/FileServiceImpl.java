package com.example.gitmanager.file.service;

import com.example.gitmanager.file.dto.FileDetailDTO;
import com.example.gitmanager.file.repository.FileDetailRepository;
import com.example.gitmanager.file.repository.FilesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {
    private final FilesRepository filesRepository;
    private final FileDetailRepository fileDetailRepository;

    @Override
    public List<FileDetailDTO> findById(long mapperId) {
        return List.of();
    }

    @Override
    public void insert(MultipartFile[] multipartFiles, long mapperId) {

    }

    @Override
    public void deleteAll(long mapperId) {

    }

    @Override
    public void delete(long id) {

    }
}
