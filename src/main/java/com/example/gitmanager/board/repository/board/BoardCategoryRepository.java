package com.example.gitmanager.board.repository.board;

import com.example.gitmanager.board.entity.board.BoardCategory;
import com.example.gitmanager.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {
    List<BoardCategory> findByProject(Project project);
}
