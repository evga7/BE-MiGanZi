package com.StreetNo5.StreetNo5.service;


import jakarta.transaction.Transactional;
import marvin.image.MarvinImage;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@Transactional
public class PostImgFileService {
    public MultipartFile resizeAttachment(String fileName, String fileFormatName, MultipartFile multipartFile,
                                           int targetWidth, int targetHeight) {
        try {
            // MultipartFile -> BufferedImage Convert

            BufferedImage image = ImageIO.read(multipartFile.getInputStream());

            // 원하는 px로 Width와 Height 수정
            int originWidth = image.getWidth();
            int originHeight = image.getHeight();

            // origin 이미지가 resizing될 사이즈보다 작을 경우 resizing 작업 안 함
            if (originWidth < targetWidth && originHeight < targetHeight)
                return multipartFile;

            MarvinImage imageMarvin = new MarvinImage(image);

            Scale scale = new Scale();
            scale.load();
            scale.setAttribute("newWidth", targetWidth);
            scale.setAttribute("newHeight", targetHeight);
            scale.process(imageMarvin.clone(), imageMarvin, null, null, false);

            BufferedImage imageNoAlpha = imageMarvin.getBufferedImageNoAlpha();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imageNoAlpha, fileFormatName, baos);
            baos.flush();
            baos.close();

            return new MockMultipartFile(fileName, baos.toByteArray());

        } catch (IOException e) {
            // 파일 리사이징 실패시 예외 처리
            throw new IllegalArgumentException();
        }
    }

    public MultipartFile resize(MultipartFile multipartFile,String fileFormatName,int width, int height)
            throws IOException {

        BufferedImage inputImage = ImageIO.read(multipartFile.getInputStream());  // 받은 이미지 읽기

        BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());
        int originWidth = inputImage.getWidth();
        int originHeight = inputImage.getHeight();

        // origin 이미지가 resizing될 사이즈보다 작을 경우 resizing 작업 안 함
        if (originWidth < width && originHeight < height)
            return multipartFile;
        // 입력받은 리사이즈 길이와 높이
        Image scaledInstance = outputImage.getScaledInstance(width, height, Image.SCALE_FAST);
        Graphics2D graphics2D = outputImage.createGraphics();
        /*graphics2D.drawImage(inputImage, 0, 0, width, height, null); // 그리기*/
        graphics2D.drawImage(scaledInstance, 0, 0, width, height, null); // 그리기
        graphics2D.dispose(); // 자원해제
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(outputImage, fileFormatName, baos);
        inputImage.flush();
        outputImage.flush();
        baos.flush();
        baos.close();

        return new MockMultipartFile(multipartFile.getName(), baos.toByteArray());
    }
}

