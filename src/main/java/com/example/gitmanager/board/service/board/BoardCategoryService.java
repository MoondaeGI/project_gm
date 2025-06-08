package com.example.gitmanager.board.service.board;

import com.example.gitmanager.board.dto.board.BoardCategoryDTO;
import com.example.gitmanager.board.dto.board.BoardCategoryInsertDTO;
import com.example.gitmanager.board.dto.board.BoardCategoryUpdateDTO;

import java.util.List;

public interface BoardCategoryService {
    List<BoardCategoryDTO> findByProjectId(long projectId);
    long insert(BoardCategoryInsertDTO dto, String loginId);
    long update(BoardCategoryUpdateDTO dto, String loginId);
    void delete(long id, String loginId);
}
