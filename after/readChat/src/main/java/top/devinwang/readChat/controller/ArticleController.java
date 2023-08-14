package top.devinwang.readChat.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.devinwang.readChat.commonutils.R;
import top.devinwang.readChat.entity.Comment;
import top.devinwang.readChat.service.ArticleService;

/**
 * @author devinWang
 * @Date 2023/6/7 21:23
 */
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 上传文章内容
     *
     * @param articleContent 文章的 HTML 形式
     * @return R
     */
    @PostMapping("/upload")
    public R upload(String articleContent) {
        return articleService.upload(articleContent);
    }

    /**
     * 分页查询文章
     *
     * @param currentIdx 从currentIdx之前的文章id开始索引
     * @param pageSize   分页查询的条数
     * @return R
     */
    @GetMapping("/getByPage")
    public R getByPage(@RequestParam("currentIdx") long currentIdx, @RequestParam("pageSize") int pageSize) {
        return articleService.getByPage(currentIdx, pageSize);
    }

    /**
     * 根据openid获取文章，就是获取某个人的文章
     *
     * @param openid     获取该 openid 用户的文章
     * @param currentIdx 从currentIdx之前的文章id开始索引
     * @param pageSize   分页查询的条数
     * @return R
     */
    @GetMapping("/getByOpenidAndPage")
    public R getByOpenid(@RequestParam("openid") String openid,
                         @RequestParam("currentIdx") long currentIdx,
                         @RequestParam("pageSize") int pageSize) {
        return articleService.getByOpenidAndPage(openid, currentIdx, pageSize);
    }

    /**
     * 根据 articleId 获取对应用户和文章的信息
     * @param articleId
     */
    @GetMapping("/getUserAndArticle")
    public R getUserAndArticle(@RequestParam("articleId") long articleId) {
        return articleService.getUserAndArticleByArticleId(articleId);
    }

    /**
     * 根据文章id增加文章的阅读量
     * @param articleId 文章id
     * @return R
     */
    @PostMapping("/addPageViews")
    public R addPageViews(@RequestParam("articleId") Long articleId) {
        return articleService.addPageViews(articleId);
    }

    /**
     * 判断当前用户是否点赞
     * @param articleId 文章id
     * @return R
     */
    @PostMapping("/isLike")
    public R isLike(@RequestParam("articleId") Long articleId) {
        return articleService.isLike(articleId);
    }

    /**
     * 获得当前当前文章的点赞数
     * @param articleId 文章id
     * @return R
     */
    @PostMapping("/getLikeNum")
    public R getLikeNum(@RequestParam("articleId") Long articleId) {
        return articleService.getLikeNum(articleId);
    }


    /**
     * 点击点赞图标进行点赞或者取消点赞
     * @param articleId 文章id
     * @param isLike 是否进行点赞
     * @return R
     */
    @PostMapping("/tapLike")
    public R tapLike(@RequestParam("articleId") Long articleId, @RequestParam("like") Boolean isLike) {
        if (articleId == null || isLike == null) {
            return R.error().message("后台发生异常");
        }
        return articleService.tapLike(articleId, isLike);
    }

    /**
     * 获取当前文章的收藏数
     * @param articleId 文章id
     * @return R
     */
    @PostMapping("/getCollectNum")
    public R getCollectNum(@RequestParam("articleId") Long articleId) {
        return articleService.getCollectNum(articleId);
    }

    /**
     * 判断当前用户是否收藏
     * @param articleId 文章id
     * @return R
     */
    @PostMapping("/isCollect")
    public R isCollect(@RequestParam("articleId") Long articleId) {
        return articleService.isCollect(articleId);
    }

    /**
     * 点击收藏按钮进行的一系列操作
     * @param articleId 文章id
     * @param isCollect 是否收藏标识
     * @return R
     */
    @PostMapping("/tapCollect")
    public R tapCollect(@RequestParam("articleId") Long articleId, @RequestParam("collect") Boolean isCollect) {
        if (articleId == null || isCollect == null) {
            return R.error().message("后台发生异常");
        }
        return articleService.tapCollect(articleId, isCollect);
    }

    /**
     * 分页查询当前用户的收藏夹
     * @param pageNo 当前页数
     * @param pageSize 每页有多少条数据
     * @return R
     */
    @GetMapping("/getCollectByPage")
    public R getCollectByPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return articleService.getCollectByPage(pageNo, pageSize);

    }

    /**
     * 获得当前用户是否给点进去的文章用户点赞
     * @param otherOpenid 文章所属用户
     * @return R
     */
    @GetMapping("/isAttention")
    public R isAttention(@RequestParam("otherOpenid") String otherOpenid) {
        return articleService.isAttention(otherOpenid);
    }

    /**
     * 点击关注
     * @param otherOpenid 文章所属的openid
     * @param attention 当前页面是否关注
     * @return R
     */
    @PostMapping("/tapAttention")
    public R tapAttention(@RequestParam("otherOpenid") String otherOpenid,
                          @RequestParam("attention") Boolean attention) {
        return articleService.tapAttention(otherOpenid, attention);
    }

    /**
     * 获取自己关注人的文章
     * @param pageNo 文章的当前页
     * @param pageSize 每页的文章数
     * @return R
     */
    @GetMapping("/getDynamicByPage")
    public R getDynamicByPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return articleService.getDynamicByPage(pageNo, pageSize);
    }

    /**
     * 获取指定文章的评论数据
     * @param articleId 文章id
     * @return R
     */
    @GetMapping("/getComments")
    public R getComments(@RequestParam("articleId") Long articleId) {
        return articleService.getComments(articleId);
    }

    /**
     * 要上传的评论
     * @param comment 要上传的评论
     * @return R
     */
    @PostMapping("/submitComment")
    public R submitComment(Comment comment) {
        return articleService.submitComment(comment);
    }


    /**
     * 根据openid获取该用户的评论数据
     * @param pageNo 第几页
     * @param pageSize 一页有多少条记录
     * @return R
     */
    @GetMapping("/getMyCommentByPage")
    public R getMyCommentByPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return articleService.getMyCommentByPage(pageNo, pageSize);
    }

    /**
     * 根据id删除评论
     * @param id 评论的id
     * @return R
     */
    @PostMapping("/deleteCommentById")
    public R deleteCommentById(@Param("id") Long id) {
        return articleService.deleteCommentById(id);
    }

    /**
     * 根据用户id获取文章数据
     * @param pageNo 第几页
     * @param pageSize 每页文章有多少条数据
     * @return R
     */
    @GetMapping("/getMyArticleByPage")
    public R getMyArticleByPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return articleService.getMyArticleByPage(pageNo, pageSize);
    }

    /**
     * 删除文章的信息
     * @param articleId 文章的id
     * @return R
     */
    @PostMapping("/deleteArticleById")
    public R deleteArticleById(@Param("articleId") Long articleId) {
        return articleService.deleteArticleById(articleId);
    }
}

