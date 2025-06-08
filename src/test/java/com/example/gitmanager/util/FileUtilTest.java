package com.example.gitmanager.util;

import com.example.gitmanager.file.dto.FileDetailDTO;
import com.example.gitmanager.file.dto.FilesDTO;
import com.example.gitmanager.project.entity.Project;
import com.example.gitmanager.project.repository.ProjectRepository;
import com.example.gitmanager.util.enums.ProjectType;
import com.example.gitmanager.util.util.FileUtil;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class FileUtilTest {
    @Autowired private FileUtil fileUtil;
    @Autowired private Storage storage;

    @Mock private ProjectRepository projectRepository;

    @Value("${gcp.bucket.name}") private String bucketName;

    private MockMultipartFile testFile;
    private final String TEST_FILE_NAME = "test.txt";
    private String filePath;

    @BeforeEach
    public void setup() {
        testFile = new MockMultipartFile(
                TEST_FILE_NAME, TEST_FILE_NAME, "text/plain", "test".getBytes());
    }

    @AfterEach
    public void teardown() {
        BlobId blobId = BlobId.of(bucketName, filePath);
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
        // 입력받을 더미 프로젝트 생성
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

        filePath = result.getPath().replace(String.format("https://storage.googleapis.com/%s/", bucketName), "");
        Blob blob = storage.get(BlobId.of(bucketName, filePath));

        assertThat(blob).isNotNull();  // 파일이 존재하는지 확인
        assertThat(new String(blob.getContent())).isEqualTo("test");

        projectRepository.delete(project);
    }

    @DisplayName("프로필 이미지 업로드 테스트")
    @Test
    public void profileImageUploadTest() throws IOException {
        String path = fileUtil.uploadProfileImg(testFile);
        filePath = path.replace(String.format("https://storage.googleapis.com/%s/", bucketName), "");
        Blob blob = storage.get(BlobId.of(bucketName, filePath));

        assertThat(blob).isNotNull();  // 파일이 존재하는지 확인
        assertThat(new String(blob.getContent())).isEqualTo("test");
    }

    @DisplayName("다운로드 테스트")
    @Test
    public void downloadTest() throws IOException {
        filePath = "test/" + TEST_FILE_NAME;
        storage.createFrom(
                BlobInfo.newBuilder(BlobId.of(bucketName, filePath)).build(),
                testFile.getInputStream());

        String url = String.format("https://storage.googleapis.com/%s/%s", bucketName, filePath);
        ByteArrayResource resource = fileUtil.download(url);

        assertThat(resource).isNotNull();
        assertThat(new String(resource.getByteArray())).isEqualTo("test");
    }
}
