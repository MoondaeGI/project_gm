package com.example.gitmanager.notice.dto;

import com.example.gitmanager.notice.entity.NoticeReply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeReplyDTO {
    private long id;
    private long writerId;
    private String writerName;
    private long noticeId;
    private int depth;
    private String content;
    private long parentId;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    public static NoticeReplyDTO of(NoticeReply noticeReply) {
        return NoticeReplyDTO.builder()
                .id(noticeReply.getId())
                .writerId(noticeReply.getWriter().getId())
                .writerName(noticeReply.getWriter().getName())
                .noticeId(noticeReply.getNotice().getId())
                .depth(noticeReply.getDepth())
                .content(noticeReply.getContent())
                .parentId(noticeReply.getParent().getId())
                .regDate(noticeReply.getRegDate())
                .modDate(noticeReply.getModDate())
                .build();
    }
}
