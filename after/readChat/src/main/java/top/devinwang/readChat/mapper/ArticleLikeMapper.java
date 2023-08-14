package top.devinwang.readChat.mapper;

import org.apache.ibatis.annotations.Param;
import top.devinwang.readChat.entity.ArticleLike;

import java.util.ArrayList;

/**
 * 文章点赞相关关系mapper
 *
 * @author wanght50855
 * @date 2023/8/6 23:06
 */
public interface ArticleLikeMapper {
    void addMultiArticleLike(@Param("articleLikeList") ArrayList<ArticleLike> articleLikeList);
}
