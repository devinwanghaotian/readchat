package top.devinwang.readChat.commonutils;

/**
 *
 * @author devinWang
 * @Date 2023/4/27 21:38
 */
public class ResultCode {
    private static final Integer SUCCESS = 20001;
    private static final Integer ERROR = 20002;

    private static final Integer LOGIN = 30001;
    private static final Integer NOT_LOGIN = 30002;
    private static final Integer NOT_LOGIN_SKIP = 30003;

    public static Integer getSuccess() {
        return SUCCESS;
    }

    public static Integer getError() {
        return ERROR;
    }

    public static Integer getNotLogin() {
        return NOT_LOGIN;
    }

    public static Integer getLOGIN() {
        return LOGIN;
    }

    public static Integer getNotLoginSkip() {
        return NOT_LOGIN_SKIP;
    }
}
