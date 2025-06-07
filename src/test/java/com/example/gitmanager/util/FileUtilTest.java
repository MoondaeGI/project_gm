package com.example.gitmanager.util;

import com.example.gitmanager.file.dto.FileDetailDTO;
import com.example.gitmanager.file.dto.FilesDTO;
import com.example.gitmanager.notice.entity.Notice;
import com.example.gitmanager.notice.repository.NoticeRepository;
import com.example.gitmanager.project.entity.Project;
import com.example.gitmanager.project.repository.ProjectRepository;
import com.example.gitmanager.util.enums.ProjectType;
import com.example.gitmanager.util.util.FileUtil;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class FileUtilTest {
    @Autowired private FileUtil fileUtil;
    @Autowired private Storage storage;
    @Autowired private ProjectRepository projectRepository;

    @Value("${gcp.bucket.name}") private String bucketName;

    private MockMultipartFile testFile;
    private final String TEST_FILE_NAME = "test.txt";

    @BeforeEach
    public void setup() {
        testFile = new MockMultipartFile(
                TEST_FILE_NAME, TEST_FILE_NAME, "text/plain", "test".getBytes());
    }

    @AfterEach
    public void teardown() {
        BlobId blobId = BlobId.of(bucketName, TEST_FILE_NAME);
        storage.delete(blobId);
    }

    @DisplayName("setup 작동 여부 확인")
    @Test
    public void setupTest() {
        assert fileUtil != null;
        assert storage != null;
        assert bucketName != null;
        assert testFile != null;
    }

    @DisplayName("gcp 업로드 테스트")
    @Test
    public void uploadTest() throws IOException {
        Project project = Project.builder()
                .name("test")
                .description("test")
                .url("test")
                .type(ProjectType.PUBLIC)
                .build();
        projectRepository.save(project);

        FilesDTO dto = FilesDTO.builder()
                .mapperName("notice")
                .mapperId(project.getId())
                .build();
        FileDetailDTO result = fileUtil.upload(dto, testFile);

        assertThat(result.getPath())
                .isEqualTo(String.format("https://storage.googleapis.com/notice/%s/%s",
                        LocalDate.now(), result.getSystemFileName()));
    }
}
