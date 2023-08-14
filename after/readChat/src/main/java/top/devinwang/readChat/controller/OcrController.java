package top.devinwang.readChat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.devinwang.readChat.commonutils.R;
import top.devinwang.readChat.service.OcrService;

import java.io.IOException;

/**
 * @author devinWang
 * @Date 2023/6/7 15:35
 */

@Controller
@RequestMapping("/ocr")
public class OcrController {
    @Autowired
    private OcrService ocrService;

    @PostMapping("/upload")
    @ResponseBody
    public R uploadOcrFile(@RequestParam("file") MultipartFile file) throws IOException {
        String message = ocrService.getMessage(file);

        return R.ok().data("message", message);
    }
}
