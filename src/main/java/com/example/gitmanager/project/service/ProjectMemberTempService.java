package com.example.gitmanager.project.service;

import com.example.gitmanager.member.dto.member.MemberDTO;
import com.example.gitmanager.project.dto.ProjectDTO;
import com.example.gitmanager.project.dto.ProjectMemberTempDTO;

import java.util.List;

public interface ProjectMemberTempService {
    List<MemberDTO> findByProjectId(long projectId);
    List<ProjectDTO> findByLoginId(String loginId);
    void insert(ProjectMemberTempDTO dto, String loginId);
    void delete(long id, String loginId);
}
