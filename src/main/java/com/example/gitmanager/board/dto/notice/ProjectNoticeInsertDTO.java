package com.example.gitmanager.board.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectNoticeInsertDTO {
    private String title;
    private String content;
    private long projectId;
}
