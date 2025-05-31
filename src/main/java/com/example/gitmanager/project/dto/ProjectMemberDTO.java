package com.example.gitmanager.project.dto;

import com.example.gitmanager.project.entity.ProjectMember;
import com.example.gitmanager.util.enums.Yn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectMemberDTO {
    private long id;
    private long projectId;
    private long memberId;
    private String name;
    private String email;
    private String profileImg;
    private Yn leaderYn;
    private LocalDateTime joinDate;

    public static ProjectMemberDTO of(ProjectMember projectMember) {
        return ProjectMemberDTO.builder()
                .id(projectMember.getId())
                .projectId(projectMember.getProject().getId())
                .memberId(projectMember.getMember().getId())
                .name(projectMember.getMember().getName())
                .email(projectMember.getMember().getEmail())
                .profileImg(projectMember.getMember().getProfileImg())
                .leaderYn(projectMember.getLeaderYn())
                .joinDate(projectMember.getJoinDate())
                .build();
    }
}
