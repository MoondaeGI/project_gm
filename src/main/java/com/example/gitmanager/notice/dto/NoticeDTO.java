package com.example.gitmanager.notice.dto;

import com.example.gitmanager.notice.entity.Notice;
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
public class NoticeDTO {
    private long id;
    private String title;
    private String content;
    private long noticeCategoryId;
    private int viewCount;
    private Yn openYn;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    public static NoticeDTO of(Notice notice) {
        return NoticeDTO.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .noticeCategoryId(notice.getNoticeCategory().getId())
                .viewCount(notice.getViewCount())
                .openYn(notice.getOpenYn())
                .regDate(notice.getRegDate())
                .modDate(notice.getModDate())
                .build();
    }
}
