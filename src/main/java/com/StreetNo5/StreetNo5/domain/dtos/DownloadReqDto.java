package com.StreetNo5.StreetNo5.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DownloadReqDto {
    private String bucketName;
    private String downloadFileName;
    private String localFileLocation;
}