package com.StreetNo5.StreetNo5.controller;

import com.StreetNo5.StreetNo5.domain.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
@RequiredArgsConstructor
public class ErrorController {

    private final ApiResponse apiResponse;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> MaxUploadError()
    {
        return apiResponse.fail("이미지 용량이 초과되었습니다.", HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> NullPointerException()
    {
        return apiResponse.fail("이미지를 첨부해주세요.", HttpStatus.BAD_REQUEST);
    }

}
