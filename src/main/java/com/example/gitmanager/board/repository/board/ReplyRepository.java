package com.example.gitmanager.board.repository.board;

import com.example.gitmanager.board.entity.board.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
