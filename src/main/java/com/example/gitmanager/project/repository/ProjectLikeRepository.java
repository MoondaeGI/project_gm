package com.example.gitmanager.project.repository;

import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.project.entity.Project;
import com.example.gitmanager.project.entity.ProjectLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectLikeRepository extends JpaRepository<ProjectLike, Long> {
    List<ProjectLike> findByMember(Member member);
    ProjectLike findByProjectAndMember(Project project, Member member);
}
