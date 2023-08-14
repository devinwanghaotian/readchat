package top.devinwang.readChat.mapper;

import org.apache.ibatis.annotations.Param;
import top.devinwang.readChat.entity.ArticleCollect;

import java.util.ArrayList;

/**
 * TODO 类描述
 *
 * @author wanght50855
 * @date 2023/8/7 20:36
 */
public interface ArticleCollectMapper {
    void addMultiArticleCollect(@Param("articleCollectList") ArrayList<ArticleCollect> articleCollectList);
}
