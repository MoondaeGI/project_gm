package com.example.gitmanager.notice.dto.reply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeReplyInsertDTO {
    private long id;
    private long noticeId;
    private long writerId;
    private int depth;
    private String content;
    private long parentId;
}
