package top.devinwang.readChat.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.devinwang.readChat.model.WXMessage;
import top.devinwang.readChat.service.OssService;
import top.devinwang.readChat.utils.CheckUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author devinWang
 * @Date 2023/6/7 12:54
 */
@Slf4j
@Service
public class OssServiceImpl implements OssService {
    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.file.keyid}")
    private String keyId;

    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;

    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;

    @Autowired
    private WXMessage wxMessage;
    /**
     * 上传文件
     */
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, keyId, keySecret);

            // 获取上传文件输入流
            InputStream inputStream = file.getInputStream();

            // 克隆流文件
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            ByteArrayInputStream copyInputStream = new ByteArrayInputStream(baos.toByteArray());
            inputStream = new ByteArrayInputStream(baos.toByteArray());

            // 图片检测
            if (!CheckUtil.checkPhoto(copyInputStream, file.getContentType(), wxMessage)) {
                return null;
            }
            // 获取上传文件名称
            String fileName = file.getOriginalFilename();

            //1. 在文件夹里面添加随机唯一的值
            String uuid = UUID.randomUUID().toString().replace("-", "");
            fileName = uuid + fileName;

            //2. 把文件按照日期进行分类
            String dataPath = new DateTime().toString("yyyy/MM/dd");

            // 拼接
            fileName = dataPath + "/" +fileName;
            log.info("fileName=======>[{}]", fileName);
            // 创建PutObject请求。
            // 第一个参数 Bucket名称
            // 第二个参数 上传到oss文件路径和文件名称    aa/bb/1.jpg
            // 第三个参数 上传文件输入流
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient
            ossClient.shutdown();
            // 把上传之后的路径进行返回
            // 需要把上传到阿里云oss路径手动拼接出来
            String result = "https://" + bucketName + "." + endpoint + "/" + fileName;
            log.info("图片地址==========>[{}]", result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
