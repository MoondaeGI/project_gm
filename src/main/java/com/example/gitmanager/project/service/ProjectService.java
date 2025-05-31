package com.example.gitmanager.project.service;

import com.example.gitmanager.project.dto.ProjectDTO;

import java.util.List;

public interface ProjectService {
    List<ProjectDTO> findAll(int page);
    List<ProjectDTO> findByMemberId(long memberId, int page);
    ProjectDTO findById(long id);
    void insert(String url);
    void update();
    void delete(long id, String loginId);
}
