package top.devinwang.readChat.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import top.devinwang.readChat.model.WXMessage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author devinWang
 * @Date 2023/6/8 9:49
 */
@Slf4j
@Component
public class CheckUtil {
    /**
     * 检查文本信息
     *
     * @param preContent 检查的内容
     * @param wxMessage  wx相关的数据
     * @return true表示通过，false表示没有检查通过
     */
    public static boolean checkMessage(String preContent, WXMessage wxMessage, String openid) {
        String content = new String(preContent.getBytes(), StandardCharsets.UTF_8);
        String accessToken = getToken(wxMessage);
        if (accessToken == null) {
            return false;
        }
        String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + accessToken;
        // 创建客户端
        CloseableHttpClient client = HttpClients.createDefault();
        // 创建一个post请求
        HttpPost request = new HttpPost(url);
        // 设置响应头
        request.setHeader("Content-Type", "application/json;charset=UTF-8");
        // 通过fastJson设置json数据
        JSONObject postData = new JSONObject();
        // 设置要检测的内容
        postData.put("content", content);
        postData.put("scene", 2);
        postData.put("version", 2);
        postData.put("openid", openid);
        String jsonString = postData.toString();
        request.setEntity(new StringEntity(jsonString, "utf-8"));
        // 处理发送请求
        try {
            HttpResponse response = client.execute(request);
            // 从响应模型中获取响应实体
            HttpEntity entity = response.getEntity();
            // 得到响应结果
            String result = EntityUtils.toString(entity, "utf-8");
            // 打印检验结果
            log.info("检验结果：[{}]", result);
            // 将响应结果变为json
            JSONObject resultJsonObject = JSONObject.parseObject(result);
            String res = resultJsonObject.getString("result");
            String label = JSONObject.parseObject(res).getString("label");
            log.info("label = [{}]", label);
            if ("100".equals(label)) {
                return true;
            }
        } catch (IOException e) {
            log.info(e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * 图片违规识别
     * true表示通过，false表示没有通过
     * @param inputStream
     * @param contentType
     * @param wxMessage
     * @return
     */
    public static boolean checkPhoto(InputStream inputStream, String contentType, WXMessage wxMessage) {
        // TODO: 测试
        String accessToken = getToken(wxMessage);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost("https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + accessToken);
        request.addHeader("Content-Type", "application/octet-stream");
        try {
            byte[] byt = new byte[inputStream.available()];
            inputStream.read(byt);
            request.setEntity(new ByteArrayEntity(byt, ContentType.create(contentType)));
            CloseableHttpResponse response = client.execute(request);
            HttpEntity httpEntity = response.getEntity();
            // 转成string
            String result = EntityUtils.toString(httpEntity, "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(result);
            log.info("result = [{}]", result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * 获取access_token
     */
    private static String getToken(WXMessage wxMessage) {
        // 发送请求获取 access_token
        String s = HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + wxMessage.getAppid() + "&secret=" + wxMessage.getSecret());
        JSONObject jsonObject = JSONObject.parseObject(s);
        String accessToken = (String) jsonObject.get("access_token");
        return accessToken;
    }
}
