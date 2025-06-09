package com.example.gitmanager.board.repository.notice;

import com.example.gitmanager.board.entity.notice.ProjectNotice;
import com.example.gitmanager.board.entity.notice.ProjectNoticeView;
import com.example.gitmanager.project.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectNoticeViewRepository extends JpaRepository<ProjectNoticeView, Long> {
    boolean existsByProjectNoticeAndProjectMember(ProjectNotice projectNotice, ProjectMember projectMember);
}
