package top.devinwang.readChat.commonutils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author devinWang
 * @Date 2023/5/22 15:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadChatException extends RuntimeException{
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 异常信息
     */
    private String msg;
}
