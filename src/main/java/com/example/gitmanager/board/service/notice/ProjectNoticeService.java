package com.example.gitmanager.board.service.notice;

import com.example.gitmanager.board.dto.notice.ProjectNoticeDTO;
import com.example.gitmanager.board.dto.notice.ProjectNoticeInsertDTO;
import com.example.gitmanager.board.dto.notice.ProjectNoticeUpdateDTO;

import java.util.List;

public interface ProjectNoticeService {
    List<ProjectNoticeDTO> findByProjectId(long projectId, int page);
    ProjectNoticeDTO findById(long id, String loginId);
    long insert(ProjectNoticeInsertDTO dto, String loginId);
    long update(ProjectNoticeUpdateDTO dto, String loginId);
    void delete(long id, String loginId);
    void toggleYn(long id, String loginId);
}
