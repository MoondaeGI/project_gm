package com.example.gitmanager.board.repository.board;

import com.example.gitmanager.board.entity.board.Board;
import com.example.gitmanager.board.entity.board.BoardCategory;
import com.example.gitmanager.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findByBoardCategory(BoardCategory boardCategory, Pageable pageable);
    int countByBoardCategory(BoardCategory boardCategory);

    @Query(value = "SELECT b " +
            "FROM Board b " +
            "JOIN BoardCategory bc ON b.boardCategory = bc " +
            "JOIN Project p ON p = bc.project " +
            "WHERE p = :project",
        countQuery = "SELECT COUNT(b) " +
                "FROM Board b " +
                "JOIN BoardCategory bc ON b.boardCategory = bc " +
                "JOIN Project p ON p = bc.project " +
                "WHERE p = :project"
    )
    Page<Board> findByProject(Project project, Pageable pageable);
    @Query("SELECT COUNT(b) " +
            "FROM Board b " +
            "JOIN BoardCategory bc ON b.boardCategory = bc " +
            "JOIN Project p ON p = bc.project " +
            "WHERE p = :project")
    int countByProject(Project project);
}
