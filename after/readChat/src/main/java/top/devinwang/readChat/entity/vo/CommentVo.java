package top.devinwang.readChat.entity.vo;

import lombok.Data;

/**
 * 前台评论显示内容
 *
 * @author wanght50855
 * @date 2023/8/10 9:46
 */
@Data
public class CommentVo {
    /**
     * 评论id
     */
    private Long id;

    /**
     * 评论的文章id
     */
    private Long articleId;

    /**
     * 评论用户头像
     */
    private String avatarUrl;

    /**
     * 评论人的姓名
     */
    private String nickName;

    /**
     * 评论的内容
     */
    private String content;

    /**
     * 评论的时间
     */
    private String gmtUpdate;

    /**
     * 回复该用户的id
     */
    private String replyId;

    /**
     * 回复该用户的昵称
     */
    private String replyName;

    /**
     * 评论所属的评论id
     */
    private Long pid;
}
