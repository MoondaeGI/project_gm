package com.example.gitmanager.notice.dto.notice;

import com.example.gitmanager.file.dto.FileDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeUpdateDTO {
    private long id;
    private String title;
    private String content;
    private MultipartFile[] multipartFiles;
    private List<FileDetailDTO> fileDetailDTOList;
}
