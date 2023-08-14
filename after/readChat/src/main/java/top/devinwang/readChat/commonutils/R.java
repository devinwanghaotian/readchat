package top.devinwang.readChat.commonutils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一返回结果的类
 *
 * @author devinWang
 * @Date 2023/4/27 21:28
 */
@Data
public class R {
    public Boolean success;

    private Integer code;

    private String message;

    private Map<String, Object> data = new HashMap<>();

    /**
     * 把构造方法设置为私有化
     */
    private R() {
    }

    /**
     * 成功的静态方法
     */
    public static R ok() {
        R r = new R();
        r.setSuccess(true);
        r.setCode(ResultCode.getSuccess());
        r.setMessage("成功");
        return r;
    }

    /**
     * 失败静态方法
     */
    public static R error() {
        R r = new R();
        r.setSuccess(false);
        r.setCode(ResultCode.getError());
        r.setMessage("失败");
        return r;
    }

    public R success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    public R data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public R data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }
}
