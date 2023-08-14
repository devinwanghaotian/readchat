package top.devinwang.readChat.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.devinwang.readChat.commonutils.R;
import top.devinwang.readChat.commonutils.ReadChatException;
import top.devinwang.readChat.utils.ExceptionUtil;

/**
 * @author devinWang
 * @Date 2023/5/22 11:19
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 全局异常处理
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e) {
        log.error(ExceptionUtil.getMessage(e));
        return R.error().message("服务器发生了异常...");
    }

    /**
     * 自定义异常处理
     */
    @ExceptionHandler(ReadChatException.class)
    @ResponseBody
    public R error(ReadChatException e) {
        log.error(ExceptionUtil.getMessage(e));
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
