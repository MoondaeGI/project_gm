package com.example.gitmanager.notice.service;

import com.example.gitmanager.file.dto.FilesDTO;
import com.example.gitmanager.file.service.FileService;
import com.example.gitmanager.notice.dto.notice.NoticeDTO;
import com.example.gitmanager.notice.dto.notice.NoticeInsertDTO;
import com.example.gitmanager.notice.dto.notice.NoticeUpdateDTO;
import com.example.gitmanager.notice.entity.Notice;
import com.example.gitmanager.notice.entity.NoticeCategory;
import com.example.gitmanager.notice.repository.NoticeCategoryRepository;
import com.example.gitmanager.notice.repository.NoticeRepository;
import com.example.gitmanager.util.enums.Yn;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeCategoryRepository noticeCategoryRepository;
    private final FileService fileService;

    @Override
    public List<NoticeDTO> findAll(int page) {
        int start = (page - 1) * 10;
        int end = Math.min(page * 10, noticeRepository.countBy());
        PageRequest pageRequest = PageRequest.of(start, end, Sort.by(Sort.Direction.DESC, "id"));

        return noticeRepository.findAll(pageRequest).getContent().stream()
                .map(NoticeDTO::of)
                .toList();
    }

    @Override
    public List<NoticeDTO> findByNoticeCategoryId(long noticeCategoryId, int page) {
        NoticeCategory noticeCategory = noticeCategoryRepository.findById(noticeCategoryId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d에 해당하는 번호를 가진 카테고리가 없습니다.", noticeCategoryId)));

        int start = (page - 1) * 10;
        int end = Math.min(page * 10, noticeRepository.countByNoticeCategory(noticeCategory));
        PageRequest pageRequest = PageRequest.of(start, end, Sort.by(Sort.Direction.DESC, "id"));

        return noticeRepository.findByNoticeCategory(noticeCategory, pageRequest).getContent().stream()
                .map(NoticeDTO::of)
                .toList();
    }

    @Override
    public List<NoticeDTO> findByTitleContainingOrContentContaining(String title, String content, int page) {
        int start = (page - 1) * 10;
        int end = Math.min(page * 10, noticeRepository.countByTitleContainingOrContentContaining(title, content));
        PageRequest pageRequest = PageRequest.of(start, end, Sort.by(Sort.Direction.DESC, "id"));

        return noticeRepository.findByTitleContainingOrContentContaining(title, content, pageRequest)
                .getContent().stream()
                .map(NoticeDTO::of)
                .toList();
    }

    @Override
    public List<NoticeDTO> findByCategoryAndTitleContainingOrContentContaining(
            long noticeCategoryId, String title, String content, int page) {
        NoticeCategory noticeCategory = noticeCategoryRepository.findById(noticeCategoryId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d에 해당하는 번호를 가진 카테고리가 없습니다.", noticeCategoryId)));

        int start = (page - 1) * 10;
        int end = Math.min(page * 10,
                noticeRepository.countByNoticeCategoryAndTitleContainingOrContentContaining(noticeCategory, title, content));
        PageRequest pageRequest = PageRequest.of(start, end, Sort.by(Sort.Direction.DESC, "id"));

        return noticeRepository
                .findByNoticeCategoryAndTitleContainingOrContentContaining(noticeCategory, title, content, pageRequest)
                .getContent().stream()
                .map(NoticeDTO::of)
                .toList();
    }

    @Transactional
    @Override
    public NoticeDTO findById(long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 공지사항은 존재하지 않습니다.", id)));
        notice.increaseViewCount();

        return NoticeDTO.of(notice);
    }

    @Transactional
    @Override
    public void insert(NoticeInsertDTO dto) {
        Notice notice = Notice.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .viewCount(0)
                .openYn(Yn.Y)
                .build();
        noticeRepository.save(notice);

        if (dto.getMultipartFiles() != null) {
            FilesDTO filesDTO = FilesDTO.builder()
                    .mapperName("notice")
                    .mapperId(notice.getId())
                    .build();
            fileService.insert(dto.getMultipartFiles(), filesDTO);
        }
    }

    @Transactional
    @Override
    public void update(NoticeUpdateDTO dto) {
        Notice notice = noticeRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 공지사항은 없습니다.", dto.getId())));
        notice.update(dto);

        fileService.update(dto.getMultipartFiles(), dto.getFileDetailDTOList());
    }

    @Transactional
    @Override
    public void delete(long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 공지사항은 없습니다.", id)));

        FilesDTO filesDTO = FilesDTO.builder()
                .mapperName("notice")
                .mapperId(notice.getId())
                .build();
        fileService.deleteAll(filesDTO);

        noticeRepository.delete(notice);
    }

    @Transactional
    @Override
    public void toggleOpenYn(long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 공지사항은 없습니다.", id)));
        notice.toggleOpenYn();
    }
}
