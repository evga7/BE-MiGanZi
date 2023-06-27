package com.StreetNo5.StreetNo5.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
@Service
@RequiredArgsConstructor
public class GCSService {

    private final Storage storage;

    public Blob downloadFileFromGCS(String bucketName, String downloadFileName, String localFileLocation) {
        Blob blob = storage.get(bucketName, downloadFileName);
        blob.downloadTo(Paths.get(localFileLocation));
        return blob;
    }
}