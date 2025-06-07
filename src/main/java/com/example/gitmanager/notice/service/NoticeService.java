package com.example.gitmanager.notice.service;

import com.example.gitmanager.notice.dto.notice.NoticeDTO;
import com.example.gitmanager.notice.dto.notice.NoticeInsertDTO;
import com.example.gitmanager.notice.dto.notice.NoticeUpdateDTO;

import java.util.List;

public interface NoticeService {
    List<NoticeDTO> findAll(int page);
    List<NoticeDTO> findByNoticeCategoryId(long noticeCategoryId, int page);
    List<NoticeDTO> findByTitleContainingOrContentContaining(String title, String content, int page);
    List<NoticeDTO> findByCategoryAndTitleContainingOrContentContaining(long noticeCategoryId, String title, String content, int page);
    NoticeDTO findById(long id);
    long insert(NoticeInsertDTO dto);
    void update(NoticeUpdateDTO dto);
    void delete(long id);
    void toggleOpenYn(long id);
}
