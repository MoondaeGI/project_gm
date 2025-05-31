package com.example.gitmanager.project.dto;

import com.example.gitmanager.project.entity.ProjectLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectLikeDTO {
    private long id;
    private long projectId;
    private long memberId;
    private LocalDateTime regDate;

    public static ProjectLikeDTO of(ProjectLike projectLike) {
        return ProjectLikeDTO.builder()
                .id(projectLike.getId())
                .projectId(projectLike.getProject().getId())
                .memberId(projectLike.getMember().getId())
                .regDate(projectLike.getRegDate())
                .build();
    }
}
