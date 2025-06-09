package com.example.gitmanager.board.service.board;

import com.example.gitmanager.board.dto.board.ReplyDTO;
import com.example.gitmanager.board.dto.board.ReplyInsertDTO;
import com.example.gitmanager.board.dto.board.ReplyUpdateDTO;

import java.util.List;

public interface ReplyService {
    List<ReplyDTO> findByBoardId(long boardId, int page);
    List<ReplyDTO> findByParentId(long parentId);
    long insert(ReplyInsertDTO dto, String loginId);
    long update(ReplyUpdateDTO dto, String loginId);
    void delete(long id, String loginId);
}
