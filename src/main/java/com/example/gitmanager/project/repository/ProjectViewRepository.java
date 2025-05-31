package com.example.gitmanager.project.repository;

import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.project.entity.ProjectView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectViewRepository extends JpaRepository<ProjectView, Long> {
    List<ProjectView> findByMember(Member member);
}
