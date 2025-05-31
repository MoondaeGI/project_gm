package com.example.gitmanager.notice.service;

import com.example.gitmanager.notice.dto.NoticeCategoryDTO;
import com.example.gitmanager.notice.entity.NoticeCategory;
import com.example.gitmanager.notice.repository.NoticeCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NoticeCategoryServiceImpl implements NoticeCategoryService {
    private final NoticeCategoryRepository noticeCategoryRepository;

    @Override
    public List<NoticeCategoryDTO> findAll() {
        return noticeCategoryRepository.findAll().stream()
                .map(NoticeCategoryDTO::of)
                .toList();
    }

    @Transactional
    @Override
    public void insert(String name) {
        NoticeCategory noticeCategory = noticeCategoryRepository.findByName(name);
        if (noticeCategory != null) {
            throw new IllegalArgumentException("해당 이름의 카테고리는 이미 존재합니다.");
        }

        NoticeCategory savedNoticeCategory = noticeCategoryRepository.save(
                NoticeCategory.builder()
                        .name(name)
                        .build());
        noticeCategoryRepository.save(savedNoticeCategory);
    }

    @Transactional
    @Override
    public void update(NoticeCategoryDTO dto) {
        NoticeCategory noticeCategory = noticeCategoryRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 카테고리가 없습니다.", dto.getId())));
        noticeCategory.update(dto.getName());
    }

    @Transactional
    @Override
    public void delete(long id) {
        NoticeCategory noticeCategory = noticeCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 카테고리가 없습니다.", id)));
        noticeCategoryRepository.delete(noticeCategory);
    }
}
