package com.example.gitmanager.project.repository;

import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.project.entity.Project;
import com.example.gitmanager.project.entity.ProjectMemberTemp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectMemberTempRepository extends JpaRepository<ProjectMemberTemp, Long> {
    List<ProjectMemberTemp> findByMember(Member member);
    List<ProjectMemberTemp> findByProject(Project project);
    ProjectMemberTemp findByProjectAndMember(Project project, Member member);
}
