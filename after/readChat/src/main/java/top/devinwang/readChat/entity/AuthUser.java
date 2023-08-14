package top.devinwang.readChat.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * auth_user
 * @author 
 */
@Data
public class AuthUser implements Serializable {
    /**
     * 微信的openid
     */
    private String openid;

    /**
     * 微信昵称
     */
    private String nickName;

    /**
     * 性别 0：未知、1：男、2：女
     */
    private Integer gender;

    /**
     * 座右铭
     */
    private String motto;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 是否禁用 1 (true)，0 (false) 未禁用
     */
    private Integer isDisabled;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtUpdate;

    private static final long serialVersionUID = 1L;
}