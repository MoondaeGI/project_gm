package com.example.gitmanager.file.dto;

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
    private String mapperEntity;
    private long mapperId;
}
