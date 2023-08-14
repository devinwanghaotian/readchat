package top.devinwang.readChat.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author devinWang
 * @Date 2023/6/7 12:54
 */
public interface OssService {
    /**
     * 上传文件
     */
    String uploadFileAvatar(MultipartFile file);
}
