package com.example.gitmanager.notice.dto.reply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeReplyUpdateDTO {
    private long id;
    private long writerId;
    private String content;
}
