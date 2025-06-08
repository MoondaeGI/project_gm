package com.example.gitmanager.board.dto.board;

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
public class BoardUpdateDTO {
    private long id;
    private String title;
    private String content;
    private long categoryId;
    MultipartFile[] multipartFiles;
    List<FileDetailDTO> fileDetailDTOList;
}
