package top.devinwang.readChat.service;

import top.devinwang.readChat.commonutils.R;
import top.devinwang.readChat.entity.Comment;

/**
 * @author devinWang
 * @Date 2023/6/7 21:25
 */
public interface ArticleService {
    /**
     * 发布文章
     *
     * @param articleContent 文章的html形式
     * @return 返回存储是否成功
     */
    R upload(String articleContent);

    /**
     * 分页查询文章
     *
     * @param currentIdx 从currentIdx之前的文章id开始索引
     * @param pageSize   分页查询的条数
     * @return R
     */
    R getByPage(long currentIdx, int pageSize);

    /**
     * 根据openid获取文章，就是获取某个人的文章
     *
     * @param openid     获取该 openid 用户的文章
     * @param currentIdx 从currentIdx之前的文章id开始索引
     * @param pageSize   分页查询的条数
     * @return R
     */
    R getByOpenidAndPage(String openid, long currentIdx, int pageSize);

    /**
     * 根据 articleId 获取对应用户和文章的信息
     *
     * @param articleId 文章id
     * @return
     */
    R getUserAndArticleByArticleId(long articleId);

    /**
     * 根据文章id增加文章的阅读量
     * @param articleId 文章id
     * @return R
     */
    R addPageViews(Long articleId);

    /**
     * 判断当前用户是否点赞
     *
     * @param articleId 文章id
     * @return R
     */
    R isLike(Long articleId);

    /**
     * 获得当前当前文章的点赞数
     * @param articleId 文章id
     * @return R
     */
    R getLikeNum(Long articleId);

    /**
     * 点击点赞图标进行点赞或者取消点赞
     * @param articleId 文章id
     * @param isLike 是否进行点赞
     * @return R
     */
    R tapLike(Long articleId, Boolean isLike);

    /**
     * 获取当前文章的收藏数
     * @param articleId 文章id
     * @return R
     */
    R getCollectNum(Long articleId);

    /**
     * 判断当前用户是否收藏
     * @param articleId 文章id
     * @return R
     */
    R isCollect(Long articleId);

    /**
     * 点击收藏按钮进行的一系列操作
     * @param articleId 文章id
     * @param isCollect 是否收藏标识
     * @return R
     */
    R tapCollect(Long articleId, Boolean isCollect);

    /**
     * 分页查询当前用户的收藏夹
     * @param pageNo 当前页数
     * @param pageSize 每页有多少条数据
     * @return R
     */
    R getCollectByPage(Integer pageNo, Integer pageSize);

    /**
     * 获得当前用户是否给点进去的文章用户点赞
     * @param otherOpenid 文章所属用户
     * @return R
     */
    R isAttention(String otherOpenid);

    /**
     * 点击关注
     * @param otherOpenid 文章所属的openid
     * @param attention 当前页面是否关注
     * @return R
     */
    R tapAttention(String otherOpenid, Boolean attention);
    /**
     * 获取自己关注人的文章
     * @param pageNo 文章的当前页
     * @param pageSize 每页的文章数
     * @return R
     */
    R getDynamicByPage(Integer pageNo, Integer pageSize);

    /**
     * 获取指定文章的评论数据
     * @param articleId 文章id
     * @return R
     */
    R getComments(Long articleId);

    /**
     * 要上传的评论
     * @param comment 要上传的评论
     * @return R
     */
    R submitComment(Comment comment);

    R getMyCommentByPage(Integer pageNo, Integer pageSize);

    /**
     * 根据id删除评论
     * @param id 评论的id
     * @return R
     */
    R deleteCommentById(Long id);

    /**
     * 根据用户id获取文章数据
     * @param pageNo 第几页
     * @param pageSize 每页文章有多少条数据
     * @return R
     */
    R getMyArticleByPage(Integer pageNo, Integer pageSize);
    /**
     * 删除文章的信息
     * @param articleId 文章的id
     * @return R
     */
    R deleteArticleById(Long articleId);
}
