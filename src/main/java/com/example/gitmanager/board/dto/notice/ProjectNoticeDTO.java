package com.example.gitmanager.board.dto.notice;

import com.example.gitmanager.board.entity.notice.ProjectNotice;
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
public class ProjectNoticeDTO {
    private long id;
    private String title;
    private String content;
    private Yn openYn;
    private int viewCount;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    public static ProjectNoticeDTO of(ProjectNotice projectNotice) {
        return ProjectNoticeDTO.builder()
                .id(projectNotice.getId())
                .title(projectNotice.getTitle())
                .content(projectNotice.getContent())
                .openYn(projectNotice.getOpenYn())
                .viewCount(projectNotice.getProjectNoticeViewList().size())
                .regDate(projectNotice.getRegDate())
                .modDate(projectNotice.getModDate())
                .build();
    }
}
