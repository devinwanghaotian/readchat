package top.devinwang.readChat.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论类
 * @author wanght50855
 * @date 2023/8/9 22:16
 */
@Data
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 评论id
     */
    private Long id;

    /**
     * 评论的文章id
     */
    private Long articleId;

    /**
     * 父评论id
     */
    private Long pid;

    /**
     * 发布评论用户的id
     */
    private String openid;

    /**
     * 被回复者的id
     */
    private String replyId;

    /**
     * 被回复者的姓名
     */
    private String replyName;

    /**
     * 评论的内同
     */
    private String content;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtUpdate;
}
