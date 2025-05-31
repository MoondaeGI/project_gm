package com.example.gitmanager.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeInsertDTO {
    private long noticeCategoryId;
    private String title;
    private String content;
}
