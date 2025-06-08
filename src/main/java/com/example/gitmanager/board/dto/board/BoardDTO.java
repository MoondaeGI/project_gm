package com.example.gitmanager.board.dto.board;

import com.example.gitmanager.board.entity.board.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDTO {
    private long id;
    private String title;
    private String content;
    private long categoryId;
    private long writerId;
    private String writerName;
    private int viewCount;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    public static BoardDTO of(Board board) {
        return BoardDTO.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writerId(board.getWriter().getId())
                .writerName(board.getWriter().getMember().getName())
                .categoryId(board.getBoardCategory().getId())
                .viewCount(board.getViewCount())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();
    }
}
