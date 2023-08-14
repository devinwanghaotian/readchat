package top.devinwang.readChat.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ArticleHotspot implements Serializable {
    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 点击量
     */
    private Integer pageViews;

    /**
     * 点赞量
     */
    private Integer likeNum;

    /**
     * 收藏数
     */
    private Integer CollectNum;

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
