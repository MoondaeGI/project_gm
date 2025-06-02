package com.example.gitmanager.project.service;

public interface ProjectMemberService {
    void insert(long projectId, String loginId);
    void delete(long projectId, String loginId);
    void toggleLeader(long projectId, String loginId);
}
