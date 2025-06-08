package com.example.gitmanager.board.service.board;

import com.example.gitmanager.board.dto.board.BoardCategoryDTO;

import java.util.List;

public interface BoardCategoryService {
    List<BoardCategoryDTO> findByProjectId(long projectId);
    void insert(String name, long projectId, String loginId);
    void update(String name, String loginId);
    void delete(long id, String loginId);
}
