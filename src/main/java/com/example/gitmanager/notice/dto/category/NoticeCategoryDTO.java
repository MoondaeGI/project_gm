package com.example.gitmanager.notice.dto.category;

import com.example.gitmanager.notice.entity.NoticeCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeCategoryDTO {
    private long id;
    private String name;

    public static NoticeCategoryDTO of(NoticeCategory noticeCategory) {
        return NoticeCategoryDTO.builder()
                .id(noticeCategory.getId())
                .name(noticeCategory.getName())
                .build();
    }
}
