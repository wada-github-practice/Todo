package com.example.demo.dto;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * ファイルアップロードのフォームクラス
 */
@Data
public class UploadFile {
    private MultipartFile multipartFile;
}