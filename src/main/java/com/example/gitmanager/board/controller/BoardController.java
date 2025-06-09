package com.example.gitmanager.board.controller;

import com.example.gitmanager.board.dto.board.*;
import com.example.gitmanager.board.service.board.BoardCategoryService;
import com.example.gitmanager.board.service.board.BoardService;
import com.example.gitmanager.board.service.board.ReplyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/board")
@RestController
public class BoardController {
    private final BoardService boardService;
    private final BoardCategoryService boardCategoryService;
    private final ReplyService replyService;

    @GetMapping
    public ResponseEntity<List<BoardDTO>> findByProjectId(
            @RequestParam long projectId, @RequestParam(defaultValue = "1") int page){
        return ResponseEntity.ok(boardService.findByProjectId(projectId, page));
    }

    @GetMapping
    public ResponseEntity<List<BoardDTO>> findByBoardCategoryId(
            @RequestParam long boardCategoryId, @RequestParam(defaultValue = "1") int page){
        return ResponseEntity.ok(boardService.findByBoardCategoryId(boardCategoryId, page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> findById(@PathVariable long id){
        return ResponseEntity.ok(boardService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Long> insert(
            @RequestBody BoardInsertDTO dto, HttpServletRequest request) {
        String loginId = (String) request.getAttribute("loginId");

        return ResponseEntity.ok(boardService.insert(dto, loginId));
    }

    @PutMapping
    public ResponseEntity<Long> update(
            @RequestBody BoardUpdateDTO dto, HttpServletRequest request) {
        String loginId = (String) request.getAttribute("loginId");

        return ResponseEntity.ok(boardService.update(dto, loginId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id, HttpServletRequest request) {
        String loginId = (String) request.getAttribute("loginId");

        boardService.delete(id, loginId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/category/{projectId}")
    public ResponseEntity<List<BoardCategoryDTO>> findAllBoardCategory(
            @PathVariable long projectId) {
        return ResponseEntity.ok(boardCategoryService.findByProjectId(projectId));
    }

    @PostMapping("/category")
    public ResponseEntity<Long> insert(
            @RequestBody BoardCategoryInsertDTO dto, HttpServletRequest request) {
        String loginId = (String) request.getAttribute("loginId");

        return ResponseEntity.ok(boardCategoryService.insert(dto, loginId));
    }

    @PutMapping("/category")
    public ResponseEntity<Void> update(
            @RequestBody BoardCategoryUpdateDTO dto, HttpServletRequest request) {
        String loginId = (String) request.getAttribute("loginId");

        boardCategoryService.update(dto, loginId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteBoardCategory(
            @PathVariable long id, HttpServletRequest request) {
        String loginId = (String) request.getAttribute("loginId");

        boardCategoryService.delete(id, loginId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/reply")
    public ResponseEntity<List<ReplyDTO>> findReplyAll(
            @PathVariable long id, @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(replyService.findByBoardId(id, page));
    }

    @GetMapping("/reply/{parentId}")
    public ResponseEntity<List<ReplyDTO>> findByParentId(@PathVariable long parentId) {
        return ResponseEntity.ok(replyService.findByParentId(parentId));
    }

    @PostMapping("/reply")
    public ResponseEntity<Long> insert(@RequestBody ReplyInsertDTO dto, HttpServletRequest request) {
        String loginId = (String) request.getAttribute("loginId");

        return ResponseEntity.ok(replyService.insert(dto, loginId));
    }

    @PutMapping("/reply")
    public ResponseEntity<Void> update(@RequestBody ReplyUpdateDTO dto, HttpServletRequest request) {
        String loginId = (String) request.getAttribute("loginId");

        replyService.update(dto, loginId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reply/{id}")
    public ResponseEntity<Void> deleteReply(@PathVariable long id, HttpServletRequest request) {
        String loginId = (String) request.getAttribute("loginId");

        replyService.delete(id, loginId);
        return ResponseEntity.ok().build();
    }
}
