package com.example.gitmanager.notice.service;

import com.example.gitmanager.notice.dto.NoticeCategoryDTO;

import java.util.List;

public interface NoticeCategoryService {
    List<NoticeCategoryDTO> findAll();
    void insert(String name);
    void update(NoticeCategoryDTO dto);
    void delete(long id);
}
