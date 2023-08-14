package top.devinwang.readChat.mapper;

import org.apache.ibatis.annotations.Param;
import top.devinwang.readChat.entity.Article;
import top.devinwang.readChat.entity.Comment;
import top.devinwang.readChat.entity.vo.ArticleVo;
import top.devinwang.readChat.entity.vo.ArticleDetailVo;
import top.devinwang.readChat.entity.vo.CommentVo;

import java.util.List;
import java.util.Set;

public interface ArticleMapper {
    /**
     * 增加一条文章数据
     * @param article 文章的相关相关信息
     */
    void add(Article article);

    /**
     * 通过时间来进行分页排序
     * @param currentIdx 当前要查询小于该id的数据
     * @param pageSize 查询的pageSize条数据
     * @return 查询结果
     */
    List<ArticleVo> selectByPage(@Param("currentIdx") long currentIdx, @Param("pageSize") int pageSize);

    /**
     * 根据openid获取文章，就是获取某个人的文章
     *
     * @param openid     获取该 openid 用户的文章
     * @param currentIdx 从currentIdx之前的文章id开始索引
     * @param pageSize   分页查询的条数
     * @return 文章数据 list
     */
    List<ArticleVo> selectByOpenidAndPage(@Param("openid") String openid,
                                          @Param("currentIdx") long currentIdx,
                                          @Param("pageSize") int pageSize);

    /**
     * 根据 articleId 获取对应用户和文章的信息
     *
     * @param articleId 文章id
     * @return
     */
    ArticleDetailVo getUserAndArticleByArticleId(@Param("articleId") long articleId);

    /**
     * 根据 articleId 和 openid 获取用户和文章的信息
     * @param articleId 文章的唯一标识
     * @return
     */
    ArticleVo getArticleVOByArticleId(@Param("articleId") long articleId);

    /**
     * 根据 articleId 集合获取数据集合
     * @param queryList articleId 集合
     * @return 数据集合
     */
    List<ArticleVo> getCollectByPage(@Param("queryList") List<Integer> queryList);

    /**
     * 根据 article 集合获取用户和文章数据
     * @param articleIdList article 集合
     * @return 用户和文章数据
     */
    List<ArticleVo> getDynamicByPage(@Param("articleIdList") List<Integer> articleIdList);

    /**
     * 根据 articleId 查询评论
     * @param articleId 文章的id
     * @return R
     */
    List<CommentVo> getCommentsByArticleId(@Param("articleId") Long articleId);

    void addComment(Comment comment);

    CommentVo getComment(@Param("id") Long id);

    List<Comment> getMyCommentByPage(@Param("openid") String openid);

    int deleteCommentById(@Param("id") Long id);

    List<ArticleVo> getMyArticleByPage(@Param("openid") String openid);

    int deleteArticleById(@Param("articleId") Long articleId);

    void delCommentByArticleId(@Param("articleId") Long articleId);

    void delArticleLikeByArticleId(@Param("articleId") Long articleId);

    void delCollectByArticleId(@Param("articleId") Long articleId);

    void delHotspotByArticleId(@Param("articleId") Long articleId);
}