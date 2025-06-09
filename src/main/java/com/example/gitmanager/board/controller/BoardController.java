package com.example.gitmanager.board.controller;

import com.example.gitmanager.board.service.board.BoardCategoryService;
import com.example.gitmanager.board.service.board.BoardService;
import com.example.gitmanager.board.service.board.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/board")
@RestController
public class BoardController {
    private final BoardService boardService;
    private final BoardCategoryService boardCategoryService;
    private final ReplyService replyService;
}
