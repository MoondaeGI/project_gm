package com.example.gitmanager.project.service;

public interface ProjectLikeService {
    void insert(long projectId, String loginId);
    void delete(long projectId, String loginId);
}
