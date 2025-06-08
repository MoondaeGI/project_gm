package com.example.gitmanager.file.service;

import com.example.gitmanager.file.dto.FileDetailDTO;
import com.example.gitmanager.file.dto.FilesDTO;
import com.example.gitmanager.file.entity.FileDetail;
import com.example.gitmanager.file.entity.Files;
import com.example.gitmanager.file.repository.FileDetailRepository;
import com.example.gitmanager.file.repository.FilesRepository;
import com.example.gitmanager.project.entity.Project;
import com.example.gitmanager.project.repository.ProjectRepository;
import com.example.gitmanager.util.enums.ProjectType;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class FileServiceTest {
    @Autowired private FileService fileService;
    @Autowired private FilesRepository filesRepository;
    @Autowired private FileDetailRepository fileDetailRepository;
    @Autowired private ProjectRepository projectRepository;

    @Autowired private Storage storage;

    @Value("${gcp.bucket.name}") private String bucketName;

    private List<MockMultipartFile> testFileList = new ArrayList<>();
    private List<FileDetail> resultFileList = new ArrayList<>();

    @BeforeEach
    public void setup() {
        for (int i = 0; i < 10; i++) {
            testFileList.add(new MockMultipartFile(
                    "test" + i + ".txt", "test" + i + ".txt", "text/plain",
                    String.format("test%d", i).getBytes()));
        }
    }

    @AfterEach
    public void teardown() {
        for (FileDetail fileDetailDTO : resultFileList) {
            String filePath = fileDetailDTO.getPath()
                    .replace(String.format("https://storage.googleapis.com/%s/", bucketName), "");
            storage.delete(bucketName, filePath);
        }
    }

    @DisplayName("findByParentId 테스트")
    @Transactional
    @Test
    public void findByParentIdTeest() throws IOException {
        // given
        Project project = projectRepository.save(Project.builder()
                .name("test")
                .description("test")
                .url("test")
                .type(ProjectType.PUBLIC)
                .build());

        Files files = filesRepository.save(Files.builder()
                .project(project)
                .build());

        for (MockMultipartFile testFile : testFileList) {
            String systemFileName = UUID.randomUUID() + "_" + testFile.getOriginalFilename();
            String path = String.format(
                    "https://storage.googleapis.com/%s/project/%s/%s", bucketName, LocalDate.now(), systemFileName);
            String filePath = path.replace(String.format("https://storage.googleapis.com/%s/", bucketName), "");

            storage.createFrom(
                    BlobInfo.newBuilder(BlobId.of(bucketName, filePath)).build(), testFile.getInputStream());

            FileDetail fileDetail = fileDetailRepository.save(FileDetail.builder()
                    .files(files)
                    .originFileName(testFile.getOriginalFilename())
                    .systemFileName(systemFileName)
                    .fileSize(testFile.getSize())
                    .path(path)
                    .build());
            resultFileList.add(fileDetail);
        }

        // when
        FilesDTO dto = FilesDTO.of(files);
        List<FileDetailDTO> result = fileService.findByParentId(dto);

        // then
        for (FileDetailDTO fileDetailDTO : result) {
            String filePath = fileDetailDTO.getPath()
                    .replace(String.format("https://storage.googleapis.com/%s/", bucketName), "");
            BlobId blobId = BlobId.of(bucketName, filePath);

            assertThat(fileDetailDTO.getSystemFileName())
                    .isEqualTo(storage.get(blobId).getName()
                            .replace(String.format("project/%s/", LocalDate.now()), ""));
        }

        // teardown
        projectRepository.delete(project);
    }

    @DisplayName("insert 테스트")
    @Transactional
    @Test
    public void insertTest() {
        // given
        Project project = projectRepository.save(Project.builder()
                .name("test")
                .description("test")
                .url("test")
                .type(ProjectType.PUBLIC)
                .build());

        // when
        FilesDTO dto = FilesDTO.builder()
                .mapperName("project")
                .mapperId(project.getId())
                .build();
        fileService.insert(testFileList.toArray(MultipartFile[]::new), dto);

        // then
        for (MultipartFile testFile : testFileList) {
            FileDetail fileDetail = fileDetailRepository.findByOriginFileName(testFile.getName()).get();

            String filePath = fileDetail.getPath()
                    .replace(String.format("https://storage.googleapis.com/%s/", bucketName), "");
            BlobId blobId = BlobId.of(bucketName, filePath);

            assertThat(fileDetail.getSystemFileName())
                    .isEqualTo(storage.get(blobId).getName()
                            .replace(String.format("project/%s/", LocalDate.now()), ""));
        }

        // teardown
        projectRepository.delete(project);
    }

    @DisplayName("update 테스트")
    @Transactional
    @Test
    public void updateTest() throws IOException {
        // given
        Project project = projectRepository.save(Project.builder()
                .name("test")
                .description("test")
                .url("test")
                .type(ProjectType.PUBLIC)
                .build());

        Files files = filesRepository.save(Files.builder()
                .project(project)
                .build());

        for (MockMultipartFile testFile : testFileList) {
            String systemFileName = UUID.randomUUID() + "_" + testFile.getOriginalFilename();
            String path = String.format(
                    "https://storage.googleapis.com/%s/project/%s/%s", bucketName, LocalDate.now(), systemFileName);
            String filePath = path.replace(String.format("https://storage.googleapis.com/%s/", bucketName), "");

            storage.createFrom(
                    BlobInfo.newBuilder(BlobId.of(bucketName, filePath)).build(), testFile.getInputStream());

            FileDetail fileDetail = fileDetailRepository.save(FileDetail.builder()
                    .files(files)
                    .originFileName(testFile.getOriginalFilename())
                    .systemFileName(systemFileName)
                    .fileSize(testFile.getSize())
                    .path(path)
                    .build());
            resultFileList.add(fileDetail);
        }

        List<MockMultipartFile> updateFileList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            updateFileList.add(new MockMultipartFile(
                    "update" + i + ".txt", "update" + i + ".txt", "text/plain",
                    String.format("update%d", i).getBytes()));
        }

        // when
        fileService.update(
                updateFileList.toArray(MultipartFile[]::new), resultFileList.stream().map(FileDetailDTO::of).toList());

        // then
        List<FileDetail> result = fileDetailRepository.findByFiles(files);
        assertThat(result.size()).isEqualTo(20);

        // teardown
        projectRepository.delete(project);
        for (FileDetail fileDetailDTO : resultFileList) {
            String filePath = fileDetailDTO.getPath()
                    .replace(String.format("https://storage.googleapis.com/%s/", bucketName), "");
            storage.delete(BlobId.of(bucketName, filePath));
        }
    }

    @DisplayName("기존 파일 삭제 + 추가가 섞인 update 테스트")
    @Transactional
    @Test
    public void updateTest2() throws IOException {
        // given
        Project project = projectRepository.save(Project.builder()
                .name("test")
                .description("test")
                .url("test")
                .type(ProjectType.PUBLIC)
                .build());

        Files files = filesRepository.save(Files.builder()
                .project(project)
                .build());

        for (MockMultipartFile testFile : testFileList) {
            String systemFileName = UUID.randomUUID() + "_" + testFile.getOriginalFilename();
            String path = String.format(
                    "https://storage.googleapis.com/%s/project/%s/%s", bucketName, LocalDate.now(), systemFileName);
            String filePath = path.replace(String.format("https://storage.googleapis.com/%s/", bucketName), "");

            storage.createFrom(
                    BlobInfo.newBuilder(BlobId.of(bucketName, filePath)).build(), testFile.getInputStream());

            FileDetail fileDetail = fileDetailRepository.save(FileDetail.builder()
                    .files(files)
                    .originFileName(testFile.getOriginalFilename())
                    .systemFileName(systemFileName)
                    .fileSize(testFile.getSize())
                    .path(path)
                    .build());
            resultFileList.add(fileDetail);
        }

        List<MockMultipartFile> updateFileList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            updateFileList.add(new MockMultipartFile(
                    "update" + i + ".txt", "update" + i + ".txt", "text/plain",
                    String.format("update%d", i).getBytes()));
        }

        List<FileDetailDTO> updatedFileDetailDTOList = resultFileList.stream()
                .map(FileDetailDTO::of)
                .filter(fileDetailDTO -> fileDetailDTO.getOriginFileName().contains("5"))
                .toList();

        // when
        fileService.update(
                updateFileList.toArray(MultipartFile[]::new), updatedFileDetailDTOList);

        // then
        List<FileDetail> result = fileDetailRepository.findByFiles(files);
        assertThat(result.size()).isEqualTo(11);

        for (FileDetail fileDetail : result) {
            assertThat(fileDetail.getOriginFileName()).isNotEqualTo("test0.txt");
        }

        // teardown
        projectRepository.delete(project);
        for (FileDetail fileDetailDTO : resultFileList) {
            String filePath = fileDetailDTO.getPath()
                    .replace(String.format("https://storage.googleapis.com/%s/", bucketName), "");
            storage.delete(BlobId.of(bucketName, filePath));
        }
    }
}
