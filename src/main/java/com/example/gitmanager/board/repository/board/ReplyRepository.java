package com.example.gitmanager.board.repository.board;

import com.example.gitmanager.board.entity.board.Board;
import com.example.gitmanager.board.entity.board.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByParent(Reply reply);

    @Query(value = "SELECT r " +
            "FROM Reply r " +
            "WHERE r.board = :board AND r.depth = 0",
        countQuery = "SELECT COUNT(r) " +
                "FROM Reply r " +
                "WHERE r.board = :board AND r.depth = 0"
    )
    Page<Reply> findByBoard(Board board, Pageable pageable);

    @Query("SELECT COUNT(r) " +
            "FROM Reply r " +
            "WHERE r.board = :board AND r.depth = 0")
    int countByBoard(Board board);
}
