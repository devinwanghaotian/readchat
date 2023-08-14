package top.devinwang.readChat.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 这个是详情页数据
 * @author wanght50855
 * @Date 2023/7/10 19:17
 */
@Data
public class ArticleDetailVo {
    /**
     * 微信的openid
     */
    private String openid;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 文章的点击量
     */
    private Long pageViews;

    /**
     * 微信昵称
     */
    private String nickName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date userGmtCreate;

    /**
     * 座右铭
     */
    private String motto;

    /**
     * 文章的id
     */
    private Long articleId;

    /**
     * 文章的内容，格式为html格式
     */
    private String articleContent;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date articleGmtCreate;
}
