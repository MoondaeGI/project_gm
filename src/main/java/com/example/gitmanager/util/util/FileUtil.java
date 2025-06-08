package com.example.gitmanager.util.util;

import com.example.gitmanager.file.dto.FileDetailDTO;
import com.example.gitmanager.file.dto.FilesDTO;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class FileUtil {
    private final Storage storage;

    @Value("${gcp.bucket.name}")
    private String bucketName;

    public FileDetailDTO upload(FilesDTO dto, MultipartFile file) throws IOException {
        String originFileName = file.getOriginalFilename();
        String systemFileName = UUID.randomUUID() + "_" + originFileName;
        String filePath = String.format("%s/%s/%s", dto.getMapperName(), LocalDate.now(), systemFileName);

        storage.createFrom(
                BlobInfo.newBuilder(
                        BlobId.of(bucketName, filePath)).build(), file.getInputStream());
        String url = String.format("https://storage.googleapis.com/%s/%s",
                bucketName, filePath);

        return FileDetailDTO.builder()
                .filesId(dto.getId())
                .originFileName(originFileName)
                .systemFileName(systemFileName)
                .fileSize(file.getSize())
                .path(url)
                .build();
    }

    public String uploadProfileImg(MultipartFile file) throws IOException {
        String originFileName = file.getOriginalFilename();
        String systemFileName = UUID.randomUUID() + "_" + originFileName;
        String filePath = String.format("member/%s/%s", LocalDate.now(), systemFileName);

        storage.createFrom(
                BlobInfo.newBuilder(
                        BlobId.of(bucketName, filePath)).build(), file.getInputStream());

        return String.format("https://storage.googleapis.com/%s/member/%s/%s",
                bucketName, LocalDate.now(), systemFileName);
    }

    public void delete(String path) {
        storage.delete(getBlobId(path));
    }

    public ByteArrayResource download(String path) {
        try {
            Blob blob = storage.get(getBlobId(path));

            if (blob == null) {
                throw new IllegalArgumentException("해당 파일은 존재하지 않습니다.");
            }

            return new ByteArrayResource(blob.getContent());
        } catch (Exception e) {
                throw new RuntimeException("다운로드가 실패했습니다.");
        }
    }

    private BlobId getBlobId(String path) {
        String filePath = path
                .replace(String.format("https://storage.googleapis.com/%s/", bucketName), "");

        return BlobId.of(bucketName, filePath);
    }
}
