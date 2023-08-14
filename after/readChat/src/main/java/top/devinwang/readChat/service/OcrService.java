package top.devinwang.readChat.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author devinWang
 * @Date 2023/6/7 15:51
 */
public interface OcrService {
    /**
     * 通过ocr获取图片中的文本信息
     */
    String getMessage(MultipartFile file) throws IOException;
}
