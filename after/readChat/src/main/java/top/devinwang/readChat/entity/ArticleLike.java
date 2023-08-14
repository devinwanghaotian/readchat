package top.devinwang.readChat.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章点赞表的实体类
 *
 * @author wanght50855
 * @date 2023/8/6 22:52
 */
@Data
public class ArticleLike implements Serializable {
    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 用户id
     */
    private String openid;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtUpdate;

    private static final long serialVersionUID = 1L;
}
