package com.StreetNo5.StreetNo5.service;

import com.StreetNo5.StreetNo5.infra.gcs.GCSService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final GCSService gcsService;
    @Async
    public Future<String> resizeAndUploadDetailImage(MultipartFile imageFile) {
        try {
            // 리사이즈 및 업로드 로직
            String imageUrl = uploadDetailImage(imageFile);
            return new AsyncResult<>(imageUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public Future<String> resizeAndUploadThumbnailImage(MultipartFile imageFile) {
        try {
            // 리사이즈 및 업로드 로직
            String imageUrl = uploadThumbnailImage(imageFile);
            return new AsyncResult<>(imageUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String uploadDetailImage(MultipartFile imageFile) throws IOException {
        return gcsService.uploadDetailImage(imageFile);
    }

    private String uploadThumbnailImage(MultipartFile imageFile) throws IOException {
        return gcsService.uploadThumbnailImage(imageFile);
    }
}
