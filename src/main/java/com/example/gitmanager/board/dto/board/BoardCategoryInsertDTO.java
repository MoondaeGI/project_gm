package com.example.gitmanager.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardCategoryInsertDTO {
    private String name;
    private int depth;
    private Long parentId;
    private long projectId;
}
