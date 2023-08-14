package top.devinwang.readChat.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * article
 * @author 
 */
@Data
public class Article implements Serializable {
    /**
     * 文章的id
     */
    private Long articleId;

    /**
     * 文章的内容，格式为html格式
     */
    private String articleContent;

    /**
     * 发布文章用户的id
     */
    private String userId;

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