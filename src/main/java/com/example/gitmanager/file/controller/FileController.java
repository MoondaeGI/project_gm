package com.example.gitmanager.file.controller;

import com.example.gitmanager.file.dto.FileDetailDTO;
import com.example.gitmanager.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@RequestMapping("/api/file")
@RestController
public class FileController {
    private final FileService fileService;

    @PostMapping("/download/{fileName}")
    public ResponseEntity<?> download(@PathVariable String fileName) {
        FileDetailDTO fileDetailDTO = fileService.findBySystemFileName(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                URLEncoder.encode(fileDetailDTO.getOriginFileName(), StandardCharsets.UTF_8) + "\"")
                .body(fileService.download(fileName));
    }
}
