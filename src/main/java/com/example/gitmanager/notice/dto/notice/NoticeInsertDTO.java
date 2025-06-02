package com.example.gitmanager.notice.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeInsertDTO {
    private long noticeCategoryId;
    private String title;
    private String content;
    private MultipartFile[] multipartFiles;
}
