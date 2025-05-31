package com.example.gitmanager.file.dto;

import com.example.gitmanager.file.entity.FileDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDetailDTO {
    private long id;
    private long filesId;
    private String originFileName;
    private String systemFileName;
    private String path;
    private long fileSize;

    public static FileDetailDTO of(FileDetail fileDetail) {
        return FileDetailDTO.builder()
                .id(fileDetail.getId())
                .filesId(fileDetail.getFiles().getId())
                .originFileName(fileDetail.getOriginFileName())
                .systemFileName(fileDetail.getSystemFileName())
                .path(fileDetail.getPath())
                .fileSize(fileDetail.getFileSize())
                .build();
    }
}
