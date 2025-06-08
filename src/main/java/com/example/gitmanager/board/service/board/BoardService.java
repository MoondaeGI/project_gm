package com.example.gitmanager.board.service.board;

import com.example.gitmanager.board.dto.board.BoardDTO;
import com.example.gitmanager.board.dto.board.BoardInsertDTO;
import com.example.gitmanager.board.dto.board.BoardUpdateDTO;

import java.util.List;

public interface BoardService {
    List<BoardDTO> findByProjectId(long projectId, int page);
    List<BoardDTO> findByBoardCategoryId(long boardCategoryId, int page);
    BoardDTO findById(long id);
    long insert(BoardInsertDTO dto, String loginId);
    long update(BoardUpdateDTO dto, String loginId);
    void delete(long id, String loginId);
}
