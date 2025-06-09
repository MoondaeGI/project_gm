package com.example.gitmanager.board.repository.notice;

import com.example.gitmanager.board.entity.notice.ProjectNotice;
import com.example.gitmanager.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectNoticeRepository extends JpaRepository<ProjectNotice, Long> {
    Page<ProjectNotice> findByProject(Project project, Pageable pageable);
    int countByProject(Project project);
}
