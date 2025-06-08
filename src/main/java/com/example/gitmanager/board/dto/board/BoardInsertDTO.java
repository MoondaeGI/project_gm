package com.example.gitmanager.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardInsertDTO {
    private String title;
    private String content;
    private long categoryId;
    MultipartFile[] multipartFiles;
}
