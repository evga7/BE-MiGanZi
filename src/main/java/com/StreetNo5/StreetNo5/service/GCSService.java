package com.StreetNo5.StreetNo5.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GCSService {

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;
    @Value("${gcp.bucket.url}")
    private String bucketUrl;
    private final Storage storage;
    public String updateMemberInfo(MultipartFile multipartFile) throws IOException {
        // !!!!!!!!!!!이미지 업로드 관련 부분!!!!!!!!!!!!!!!
        String uuid = UUID.randomUUID().toString(); // Google Cloud Storage에 저장될 파일 이름
        String ext = multipartFile.getContentType(); // 파일의 형식 ex) JPG
        // Cloud에 이미지 업로드
        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, uuid)
                        .setContentType(ext)
                        .build(),
                multipartFile.getInputStream()
        );

        return bucketUrl+uuid;
    }

}