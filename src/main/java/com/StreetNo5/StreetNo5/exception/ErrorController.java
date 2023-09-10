package com.StreetNo5.StreetNo5.exception;


import com.StreetNo5.StreetNo5.entity.dto.ApiResponse;
import jakarta.validation.ConstraintViolationException;
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
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> LessContentSizeException()
    {
        return apiResponse.fail("게시글은 2~500자 사이로 입력해주세요.", HttpStatus.BAD_REQUEST);
    }


}
