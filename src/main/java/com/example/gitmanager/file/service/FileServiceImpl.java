package com.example.gitmanager.file.service;

import com.example.gitmanager.file.dto.FileDetailDTO;
import com.example.gitmanager.file.dto.FilesDTO;
import com.example.gitmanager.file.entity.FileDetail;
import com.example.gitmanager.file.entity.Files;
import com.example.gitmanager.file.repository.FileDetailRepository;
import com.example.gitmanager.file.repository.FilesRepository;
import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.member.repository.MemberRepository;
import com.example.gitmanager.notice.entity.Notice;
import com.example.gitmanager.notice.repository.NoticeRepository;
import com.example.gitmanager.project.entity.Project;
import com.example.gitmanager.project.repository.ProjectRepository;
import com.example.gitmanager.util.util.FileUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {
    private final FilesRepository filesRepository;
    private final FileDetailRepository fileDetailRepository;
    private final NoticeRepository noticeRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    private final FileUtil fileUtil;

    @Transactional
    @Override
    public List<FileDetailDTO> findByParentId(FilesDTO dto) {
        Files files = filesRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 파일이 없습니다.", dto.getId())));

        return fileDetailRepository.findByFiles(files).stream()
                .map(FileDetailDTO::of)
                .toList();
    }

    @Transactional
    @Override
    public FileDetailDTO findById(long id) {
        return FileDetailDTO.of(fileDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 파일이 존재하지 앖습니다.", id))));
    }

    @Transactional
    @Override
    public FileDetailDTO findBySystemFileName(String systemFileName) {
        return FileDetailDTO.of(fileDetailRepository.findBySystemFileName(systemFileName)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 이름을 가진 파일이 존재하지 않습니다.", systemFileName))));
    }

    @Transactional
    @Override
    public void insert(MultipartFile[] multipartFiles, FilesDTO dto) {
        Files files = create(dto);
        filesRepository.save(files);

        List<FileDetail> fileDetailList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            try {
                FileDetailDTO fileDetail = fileUtil.upload(dto, multipartFile);
                fileDetailList.add(FileDetail.of(fileDetail, files));
            } catch (Exception e) {
                for (FileDetail fileDetail : fileDetailList) {
                    fileUtil.delete(fileDetail.getSystemFileName());
                }
            }
        }

        fileDetailRepository.saveAll(fileDetailList);
    }

    @Transactional
    @Override
    public void update(MultipartFile[] multipartFiles, List<FileDetailDTO> fileDetailDTOList) {
        long filesId = fileDetailDTOList.get(0).getFilesId();
        Files files = filesRepository.findById(filesId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 파일은 존재하지 않습니다.", filesId)));

        List<FileDetail> originalFileList = fileDetailRepository.findByFiles(files);
        for (FileDetail originFile : originalFileList) {
            boolean isExist = false;
            for (FileDetailDTO updatedFile : fileDetailDTOList) {
                if (originFile.getId() == updatedFile.getId()) {
                    isExist = true;
                    break;
                }
            }

            if (!isExist) {
                fileUtil.delete(originFile.getSystemFileName());
                originalFileList.remove(originFile);
                fileDetailRepository.delete(originFile);
            }
        }

        FilesDTO filesDTO = FilesDTO.of(files);
        List<FileDetail> uploadFileList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            try {
                FileDetailDTO uploadFileDTO = fileUtil.upload(filesDTO, multipartFile);
                uploadFileList.add(FileDetail.of(uploadFileDTO, files));
            } catch (Exception e) {
                for (FileDetail fileDetail : uploadFileList) {
                    fileUtil.delete(fileDetail.getSystemFileName());
                }
            }
        }
        fileDetailRepository.saveAll(uploadFileList);

        if (originalFileList.isEmpty() && uploadFileList.isEmpty() ) {
            filesRepository.delete(files);
        }
    }

    @Transactional
    @Override
    public void deleteAll(FilesDTO dto) {
        Files files = create(dto);

        List<FileDetail> fileDetailList = fileDetailRepository.findByFiles(files);
        for (FileDetail fileDetail : fileDetailList) {
            fileUtil.delete(fileDetail.getSystemFileName());
        }
        filesRepository.delete(files);
    }

    @Transactional
    @Override
    public void delete(long id) {
        FileDetail fileDetail = fileDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 파일이 존재하지 앖습니다.", id)));

        fileUtil.delete(fileDetail.getSystemFileName());
        fileDetailRepository.delete(fileDetail);
    }

    @Override
    public ByteArrayResource download(String fileName) {
        return fileUtil.download(fileName);
    }

    private Files create(FilesDTO dto) {
        if (dto.getMapperName().equals("notice")) {
            Notice notice = noticeRepository.findById(dto.getMapperId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("%d의 번호를 가진 공지사항이 없습니다.", dto.getMapperId())));

            return Files.builder()
                    .notice(notice)
                    .build();
        } else if (dto.getMapperName().equals("project")) {
            Project project = projectRepository.findById(dto.getMapperId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("%d의 번호를 가진 프로젝트가 없습니다.", dto.getMapperId())));

            return Files.builder()
                    .project(project)
                    .build();
        } else {
            throw new IllegalArgumentException("잘못된 경로의 파일입니다.");
        }
    }
}
