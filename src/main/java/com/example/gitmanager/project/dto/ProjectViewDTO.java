package com.example.gitmanager.project.dto;

import com.example.gitmanager.project.entity.ProjectView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectViewDTO {
    private long id;
    private long projectId;
    private long memberId;
    private LocalDateTime regDate;

    public static ProjectViewDTO of(ProjectView projectView) {
        return ProjectViewDTO.builder()
                .id(projectView.getId())
                .projectId(projectView.getProject().getId())
                .memberId(projectView.getMember().getId())
                .regDate(projectView.getRegDate())
                .build();
    }
}
