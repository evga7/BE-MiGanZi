package com.StreetNo5.StreetNo5.infra.gcs;

import com.StreetNo5.StreetNo5.service.PostImgFileService;
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
    private final PostImgFileService postImgFileService;

    public String uploadDetailImage(MultipartFile multipartFile) throws IOException {
        // !!!!!!!!!!!이미지 업로드 관련 부분!!!!!!!!!!!!!!!
        String ext = multipartFile.getContentType(); // 파일의 형식 ex) JPG
        /*MultipartFile detailImage = postImgFileService.resizeAttachment(multipartFile.getName(),"jpg" , multipartFile, 350, 467);*/
        MultipartFile detailImage = postImgFileService.resize(multipartFile, multipartFile.getContentType().substring(6), 350, 467);
        String uuid = UUID.randomUUID().toString(); // Google Cloud Storage에 저장될 파일 이름
        // Cloud에 이미지 업로드
        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, uuid)
                        .setContentType(ext)
                        .build(),
                detailImage.getInputStream()
        );

        return bucketUrl+uuid;
    }

    public String uploadThumbnailImage(MultipartFile multipartFile) throws IOException {
        // !!!!!!!!!!!이미지 업로드 관련 부분!!!!!!!!!!!!!!!
        String ext = multipartFile.getContentType(); // 파일의 형식 ex) JPG
        /*MultipartFile thumbnailImage = postImgFileService.resizeAttachment(multipartFile.getName(),"jpg" , multipartFile, 126, 169);*/
        MultipartFile thumbnailImage = postImgFileService.resize(multipartFile, multipartFile.getContentType().substring(6), 126, 169);

        String uuid = UUID.randomUUID().toString(); // Google Cloud Storage에 저장될 파일 이름
        // Cloud에 이미지 업로드
        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, uuid)
                        .setContentType(ext)
                        .build(),
                thumbnailImage.getInputStream()
        );

        return bucketUrl+uuid;
    }


}