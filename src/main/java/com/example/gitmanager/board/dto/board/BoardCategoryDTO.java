package com.example.gitmanager.board.dto.board;

import com.example.gitmanager.board.entity.board.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardCategoryDTO {
    private long id;
    private String name;
    private int depth;
    private Long parentId;
    private long projectId;

    public static BoardCategoryDTO of(BoardCategory boardCategory) {
        return BoardCategoryDTO.builder()
                .id(boardCategory.getId())
                .name(boardCategory.getName())
                .depth(boardCategory.getDepth())
                .parentId(boardCategory.getParent().getId())
                .projectId(boardCategory.getProject().getId())
                .build();
    }
}
