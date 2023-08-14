package top.devinwang.readChat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.devinwang.readChat.commonutils.R;
import top.devinwang.readChat.service.OssService;

/**
 * @author devinWang
 * @Date 2023/6/7 12:53
 */
@Slf4j
@RestController
public class OssController {

    @Autowired
    private OssService ossService;

    /**
     * 上传图片
     */
    @PostMapping("/oss/upload")
    public R uploadOssFile(@RequestParam("file") MultipartFile file) {
        // 获取上传文件
        // 返回上传到oss的路径
        String url = ossService.uploadFileAvatar(file);
        log.info("url = [{}]", url);
        if (url == null) {
            return R.error().message("图片违规");
        }

        return R.ok().data("url", url);
    }
}