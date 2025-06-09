package com.example.gitmanager.file.dto;

import com.example.gitmanager.file.entity.Files;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilesDTO {
    private long id;
    private String mapperName;
    private long mapperId;

    public static FilesDTO of(Files files) {
        String mapperName;
        long mapperId;

        if (files.getNotice() != null) {
            mapperName = "notice";
            mapperId = files.getNotice().getId();
        } else if (files.getProject() != null) {
            mapperName = "project";
            mapperId = files.getProject().getId();
        } else if (files.getBoard() != null) {
            mapperName = "board";
            mapperId = files.getBoard().getId();
        } else {
            throw new IllegalArgumentException("Files is not mapped.");
        }

        return FilesDTO.builder()
                .id(files.getId())
                .mapperName(mapperName)
                .mapperId(mapperId)
                .build();
    }
}
