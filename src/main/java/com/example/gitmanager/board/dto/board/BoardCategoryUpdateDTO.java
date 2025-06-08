package com.example.gitmanager.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardCategoryUpdateDTO {
    private long id;
    private String name;
    private int depth;
    private Long parentId;
}
