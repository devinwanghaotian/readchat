package top.devinwang.readChat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author devinWang
 * @Date 2023/4/25 17:56
 */
@EnableScheduling
@SpringBootApplication
@MapperScan("top.devinwang.readChat.mapper")
public class ReadChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReadChatApplication.class, args);
    }
}
