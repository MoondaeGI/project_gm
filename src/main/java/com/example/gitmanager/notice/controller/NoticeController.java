package com.example.gitmanager.notice.controller;

import com.example.gitmanager.notice.dto.category.NoticeCategoryDTO;
import com.example.gitmanager.notice.dto.notice.NoticeDTO;
import com.example.gitmanager.notice.dto.notice.NoticeInsertDTO;
import com.example.gitmanager.notice.dto.notice.NoticeUpdateDTO;
import com.example.gitmanager.notice.dto.reply.NoticeReplyDTO;
import com.example.gitmanager.notice.dto.reply.NoticeReplyInsertDTO;
import com.example.gitmanager.notice.dto.reply.NoticeReplyUpdateDTO;
import com.example.gitmanager.notice.service.NoticeCategoryService;
import com.example.gitmanager.notice.service.NoticeReplyService;
import com.example.gitmanager.notice.service.NoticeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/notice")
@RestController
public class NoticeController {
    private final NoticeService noticeService;
    private final NoticeCategoryService noticeCategoryService;
    private final NoticeReplyService noticeReplyService;

    @GetMapping()
    public ResponseEntity<List<NoticeDTO>> findAllNotice(@RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(noticeService.findAll(page));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<NoticeDTO>> findNoticeByNoticeCategoryId(
            @PathVariable(name = "id") long noticeCategoryId, @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(noticeService.findByNoticeCategoryId(noticeCategoryId, page));
    }

    @GetMapping("/search")
    public ResponseEntity<List<NoticeDTO>> findNoticeByTitleContainingOrContentContaining(
            @RequestParam String title, @RequestParam String content, @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(noticeService.findByTitleContainingOrContentContaining(title, content, page));
    }

    @GetMapping("/category/search/{id}")
    public ResponseEntity<List<NoticeDTO>> findByCategoryAndTitleContainingOrContentContaining(
            @PathVariable(name = "id") long noticeCategoryId,
            @RequestParam String title, @RequestParam String content, @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(
                noticeService.findByCategoryAndTitleContainingOrContentContaining(noticeCategoryId, title, content, page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeDTO> findById(@PathVariable long id) {
        return ResponseEntity.ok(noticeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Void> insert(@RequestBody NoticeInsertDTO dto) {
        noticeService.insert(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody NoticeUpdateDTO dto) {
        noticeService.update(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        noticeService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/category")
    public ResponseEntity<List<NoticeCategoryDTO>> findAllNoticeCategory() {
        return ResponseEntity.ok(noticeCategoryService.findAll());
    }

    @PostMapping("/category")
    public ResponseEntity<Void> insert(@RequestBody String name) {
        noticeCategoryService.insert(name);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/category")
    public ResponseEntity<Void> update(@RequestBody NoticeCategoryDTO dto) {
        noticeCategoryService.update(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteNoticeCategory(@PathVariable long id) {
        noticeCategoryService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/reply")
    public ResponseEntity<List<NoticeReplyDTO>> findReplyAll(
            @PathVariable long id, @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(noticeReplyService.findByNoticeId(id, page));
    }

    @PostMapping("/reply")
    public ResponseEntity<Void> insert(@RequestBody NoticeReplyInsertDTO dto) {
        noticeReplyService.insert(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reply")
    public ResponseEntity<Void> update(@RequestBody NoticeReplyUpdateDTO dto, HttpServletRequest request) {
        String loginId = (String) request.getAttribute("loginId");

        noticeReplyService.update(dto, loginId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reply/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id, HttpServletRequest request) {
        String loginId = (String) request.getAttribute("loginId");

        noticeReplyService.delete(id, loginId);
        return ResponseEntity.ok().build();
    }
}
