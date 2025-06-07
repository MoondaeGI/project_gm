package com.example.gitmanager.notice.service;

import com.example.gitmanager.notice.dto.notice.NoticeInsertDTO;
import com.example.gitmanager.notice.entity.Notice;
import com.example.gitmanager.notice.entity.NoticeCategory;
import com.example.gitmanager.notice.repository.NoticeCategoryRepository;
import com.example.gitmanager.notice.repository.NoticeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class NoticeServiceTest {
    @Autowired private NoticeService noticeService;
    @Autowired private NoticeRepository noticeRepository;
    @Autowired private NoticeCategoryRepository noticeCategoryRepository;

    private List<Notice> noticeList;
    private NoticeCategory noticeCategory;

    @BeforeEach
    public void setup() {
        noticeCategory = NoticeCategory.builder()
                .name("test")
                .build();
        noticeCategoryRepository.save(noticeCategory);

        List<Notice> addList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Notice notice = Notice.builder()
                    .noticeCategory(noticeCategory)
                    .title("test")
                    .content("test")
                    .build();
            noticeRepository.save(notice);
            addList.add(notice);
        }

        noticeList = addList;
    }

    @AfterEach
    public void teardown() {
        noticeCategoryRepository.delete(noticeCategory);
    }

    @DisplayName("findAll 테스트")
    @Test
    public void findAllTest() {
        assertThat(noticeService.findAll(1).size()).isEqualTo(10);
    }

    @DisplayName("insert 테스트")
    @Test
    public void insertTest() {
        NoticeInsertDTO noticeInsertDTO = NoticeInsertDTO.builder()
                .noticeCategoryId(noticeCategory.getId())
                .title("insert test")
                .content("test")
                .build();
        long noticeId = noticeService.insert(noticeInsertDTO);
        Notice notice = noticeRepository.findById(noticeId).get();

        assertThat(notice.getTitle())
                .isEqualTo("insert test");

        noticeRepository.delete(notice);
    }
}
