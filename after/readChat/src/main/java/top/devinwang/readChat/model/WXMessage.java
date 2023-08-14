package top.devinwang.readChat.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author devinWang
 * @Date 2023/6/8 9:51
 */
@Component
@Data
@ConfigurationProperties(prefix = "weixin")
public class WXMessage {
    private String appid;

    private String secret;
}
