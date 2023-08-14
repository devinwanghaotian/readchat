package top.devinwang.readChat.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO 类描述
 *
 * @author wanght50855
 * @date 2023/8/7 14:03
 */
@Data
public class ArticleCollect implements Serializable {
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
