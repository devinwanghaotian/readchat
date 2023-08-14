package top.devinwang.readChat.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 这个是首页数据文章数据
 * @author devinWang
 * @Date 2023/6/10 17:19
 */
@Data
public class ArticleVo {

    /**
     * 微信的openid
     */
    private String openid;

    /**
     * 座右铭
     */
    private String motto;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 微信昵称
     */
    private String nickName;

    /**
     * 文章的点击量
     */
    private Integer pageViews;

    /**
     * 文章的创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;



    /**
     * 文章的id
     */
    private Long articleId;

    /**
     * 文章的内容，格式为html格式
     */
    private String articleContent;
}
