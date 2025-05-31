package com.example.gitmanager.project.dto;

import com.example.gitmanager.project.entity.Project;
import com.example.gitmanager.util.enums.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectDTO {
    private long id;
    private String name;
    private String description;
    private String url;
    private ProjectType type;
    private int viewCount;
    private int likeCount;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    public static ProjectDTO of(Project project) {
        return ProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .url(project.getUrl())
                .type(project.getType())
                .viewCount(project.getProjectViewList().size())
                .likeCount(project.getProjectLikeList().size())
                .regDate(project.getRegDate())
                .modDate(project.getModDate())
                .build();
    }
}
