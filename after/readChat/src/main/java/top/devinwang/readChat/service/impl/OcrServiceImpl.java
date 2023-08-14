package top.devinwang.readChat.service.impl;

import com.baidu.aip.ocr.AipOcr;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.devinwang.readChat.service.OcrService;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author devinWang
 * @Date 2023/6/7 15:51
 */
@Slf4j
@Service
public class OcrServiceImpl implements OcrService {
    @Value("${baidu.app_id}")
    public String APP_ID;

    @Value("${baidu.app_key}")
    public String APP_KEY;

    @Value("${baidu.secret_key}")
    public String SECRET_KEY;

    /**
     * 通过图片获取文本信息
     */
    @Override
    public String getMessage(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        AipOcr aipOcr = new AipOcr(APP_ID, APP_KEY, SECRET_KEY);
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("language_type", "CHN_ENG");//识别语言类型，默认为中英文混合
        options.put("detect_direction", "true");//是否检查图片朝向，默认false不检查
        options.put("detect_language", "true");//是否检查语言，默认false不检查
        options.put("probability", "true");//是否返回识别结果中每一行的置信度


        // 调用接口，返回JSON格式数据
        JSONObject jsonObject = aipOcr.basicGeneral(bytes, options);
        //获取JSON对象里提取图片文字信息数组
        JSONArray jsonArray = jsonObject.getJSONArray("words_result");
        //循环打印信息
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < jsonArray.length(); i++) {
            sb.append(jsonArray.getJSONObject(i).get("words"));
        }
        String s = sb.toString();
        log.info("从图片中获取的文本信息: [{}]", s);
        return s;
    }
}
