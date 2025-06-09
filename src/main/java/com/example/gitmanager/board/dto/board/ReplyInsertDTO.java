package com.example.gitmanager.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyInsertDTO {
    private String content;
    private Long parentId;
    private long boardId;
    private int depth;
}
