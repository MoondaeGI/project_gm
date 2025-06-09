package com.example.gitmanager.board.dto.board;

import com.example.gitmanager.board.entity.board.Reply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyDTO {
    private long id;
    private String content;
    private int depth;
    private long parentId;
    private long boardId;
    private long writerId;
    private String writerName;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    public static ReplyDTO of(Reply reply) {
        return ReplyDTO.builder()
                .id(reply.getId())
                .content(reply.getContent())
                .depth(reply.getDepth())
                .parentId(reply.getParent().getId())
                .boardId(reply.getBoard().getId())
                .writerId(reply.getWriter().getId())
                .writerName(reply.getWriter().getMember().getName())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .build();
    }
}
