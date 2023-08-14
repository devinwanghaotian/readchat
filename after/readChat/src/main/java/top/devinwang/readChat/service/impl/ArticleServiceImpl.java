package top.devinwang.readChat.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import top.devinwang.readChat.commonutils.PageBean;
import top.devinwang.readChat.commonutils.R;
import top.devinwang.readChat.commonutils.RedisConstant;
import top.devinwang.readChat.entity.Article;
import top.devinwang.readChat.entity.AuthUser;
import top.devinwang.readChat.entity.Comment;
import top.devinwang.readChat.entity.vo.ArticleVo;
import top.devinwang.readChat.entity.vo.ArticleDetailVo;
import top.devinwang.readChat.entity.vo.CommentVo;
import top.devinwang.readChat.mapper.ArticleMapper;
import top.devinwang.readChat.model.WXMessage;
import top.devinwang.readChat.service.ArticleService;
import top.devinwang.readChat.utils.CheckUtil;
import top.devinwang.readChat.utils.ThreadLocalHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author devinWang
 * @Date 2023/6/7 21:26
 */
@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ThreadLocalHandler threadLocalHandler;
    @Autowired
    private WXMessage wxMessage;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 上传文章
     * @param articleContent 文章的html形式
     * @return R
     */
    @Override
    public R upload(String articleContent) {
        // 1. 增加数据到 MySQL
        AuthUser user = threadLocalHandler.getUser();
        String openid = user.getOpenid();
        if (!CheckUtil.checkMessage(articleContent, wxMessage, openid)) {
            return R.error().message("出现非法字符");
        }
        Article article = new Article();
        article.setUserId(openid);
        article.setArticleContent(articleContent);
        articleMapper.add(article);

        ArticleVo articleVo = articleMapper.getArticleVOByArticleId(article.getArticleId());
        // 2. 在 Redis 中增加数据
        String key = RedisConstant.getHomePageData();
        redisTemplate.opsForZSet().add(key, articleVo, articleVo.getArticleId());

        // 3. 查询自己的粉丝
        Set<String> followsOpenid = (Set<String>) redisTemplate.opsForHash().get(RedisConstant.getUserConcerned(), openid);
        followsOpenid = followsOpenid == null ? new HashSet<>() : followsOpenid;
        // 将自己的文章推送给粉丝
        for (String item : followsOpenid) {
            redisTemplate.opsForZSet().add(RedisConstant.getFEED() + item, articleVo.getArticleId(), System.currentTimeMillis());
        }

        // 4. 获得首页缓存数据的条目数
        long count = redisTemplate.opsForZSet().size(key);

        int allArticleSize;
        if (count > (allArticleSize = RedisConstant.getAllArticleSize())) {
            // 5. 如果缓存中的条目数大于500，删除后100条数据
            Set<ZSetOperations.TypedTuple<String>> set = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, allArticleSize - 1);
            redisTemplate.delete(key);
            redisTemplate.opsForZSet().add(key, set);
        }
        return R.ok();
    }

    /**
     * 根据主键进行分页查询
     * @param currentIdx 当前要查询小于该id的数据
     * @param pageSize 查询的条目数
     * @return R
     */
    @Override
    public R getByPage(long currentIdx, int pageSize) {
        if (pageSize <= 0) {
            return R.error().message("传入的参数错误" + " pageSize = " + pageSize);
        }

        // 1. 从 redis 中取值
        // 1.1 redis 中获取所有值
        Set<ArticleVo> set = redisTemplate.opsForZSet().reverseRange(RedisConstant.getHomePageData(), 0, -1);
        // 1.2 如果没有缓存直接从数据库中取值
        if (set != null && set.size() != 0) {
            List<ArticleVo> articleList = Arrays.asList(set.toArray(new ArticleVo[0]));
            articleList.sort((o1, o2) -> (int) (o2.getArticleId() - o1.getArticleId()));
            // 1.3 如果要请求的文章的id小于等于zset中的倒数第 pageSize 个id，直接从数据库中取值
            if (articleList.size() - pageSize >= 0 && currentIdx > articleList.get(articleList.size() - pageSize).getArticleId()) {
                // 1.4 找到小于currentIdx的10个数据，并返回
                int idx = -1;
                for (int i = 0; i < articleList.size(); i++) {
                    int id = Math.toIntExact(articleList.get(i).getArticleId());
                    if (currentIdx > id) {
                        idx = i;
                        break;
                    }
                }
                if (idx != -1) {
                    List<ArticleVo> sublist = articleList.subList(idx, idx + pageSize);
                    // 1.5 获取分页文章的浏览量
                    getMultiPageViews(sublist);
                    return R.ok().data("list", sublist).data("currentIdx", articleList.get(idx + pageSize - 1).getArticleId());
                }
            }
        }

        List<ArticleVo> list = articleMapper.selectByPage(currentIdx, pageSize);
        if (list == null || list.size() == 0) {
            return R.ok().data("list", new ArrayList<ArticleVo>()).data("currentIdx", currentIdx);
        }
        getMultiPageViews(list);
        Long articleId = list.get(list.size() - 1).getArticleId();
        return R.ok().data("list", list).data("currentIdx", articleId);
    }

    /**
     * 从 Redis 中获取 articleVo 的 pageViews 的值
     * @param articleVoList articleVo列表
     */
    private void getMultiPageViews(List<ArticleVo> articleVoList) {
        List<String> articleIdList = new ArrayList<>();
        for (ArticleVo articleVo : articleVoList) {
            articleIdList.add(articleVo.getArticleId().toString());
        }
        List<Integer> pageViewsList = redisTemplate.opsForHash().multiGet(RedisConstant.getArticlePageViews(), articleIdList);
        for (int i = 0; i < articleVoList.size(); i++) {
            ArticleVo articleVo = articleVoList.get(i);
            int pageViews;
            if (pageViewsList.get(i) != null) {
                pageViews = pageViewsList.get(i);
            } else {
                pageViews = 0;
            }
            articleVo.setPageViews(pageViews);
        }
    }

    /**
     * 根据openid获取文章，就是获取某个人的文章，根据主键分页
     *
     * @param openid     获取该 openid 用户的文章
     * @param currentIdx 从currentIdx之前的文章id开始索引
     * @param pageSize   分页查询的条数
     * @return R
     */
    @Override
    public R getByOpenidAndPage(String openid, long currentIdx, int pageSize) {
        if (pageSize <= 0) {
            return R.error().message("传入的参数错误" + " pageSize = " + pageSize);
        }
        List<ArticleVo> list = articleMapper.selectByOpenidAndPage(openid, currentIdx, pageSize);
        if (list == null || list.size() == 0) {
            return R.ok().data("list", list).data("currentIdx", currentIdx);
        }
        // 获取浏览量
        getMultiPageViews(list);
        Long articleId = list.get(list.size() - 1).getArticleId();
        return R.ok().data("list", list).data("currentIdx", articleId);
    }

    /**
     * 根据 articleId 获取对应用户和文章的信息
     *
     * @param articleId 文章id
     * @return
     */
    @Override
    public R getUserAndArticleByArticleId(long articleId) {
        ArticleDetailVo articleDetailVo = articleMapper.getUserAndArticleByArticleId(articleId);
        return R.ok().data("userAndArticle", articleDetailVo);
    }

    /**
     * 根据文章id增加文章的阅读量
     * @param articleId 文章id
     * @return R
     */
    @Override
    public R addPageViews(Long articleId) {
        redisTemplate.opsForHash().increment(RedisConstant.getArticlePageViews(), articleId.toString(), 1L);
        return R.ok().message("数据增加成功");
    }

    @Override
    public R isLike(Long articleId) {
        String openid = threadLocalHandler.getUser().getOpenid();
        Set<String> set = (Set<String>) redisTemplate.opsForHash().get(RedisConstant.getArticleLike(), articleId.toString());
        if (set != null && set.contains(openid)) {
            return R.ok().data("isLike", true);
        }
        return R.ok().data("isLike", false);
    }

    @Override
    public R getLikeNum(Long articleId) {
        Set<String> set = (Set<String>) redisTemplate.opsForHash().get(RedisConstant.getArticleLike(), articleId.toString());
        int likeNum = 0;
        if (set != null) {
            likeNum = set.size();
        }
        return R.ok().data("likeNum", likeNum);
    }
    /**
     * 点击点赞图标进行点赞或者取消点赞
     * @param articleId 文章id
     * @param isLike 是否进行点赞
     * @return R
     */
    @Override
    public R tapLike(Long articleId, Boolean isLike) {
        Set<String> set = (Set<String>) redisTemplate.opsForHash().get(RedisConstant.getArticleLike(), articleId.toString());
        String openid = threadLocalHandler.getUser().getOpenid();
        if (set == null) {
            set = new HashSet<>();
        }
        if (isLike) {
            set.remove(openid);
        } else {
            set.add(openid);

        }
        redisTemplate.opsForHash().put(RedisConstant.getArticleLike(), articleId.toString(), set);
        return R.ok().data("like", !isLike);
    }

    /**
     * 获取该文章的收藏量
     * @param articleId 文章id
     * @return R
     */
    @Override
    public R getCollectNum(Long articleId) {
        Set<String> set = (Set<String>) redisTemplate.opsForHash().get(RedisConstant.getArticleCollect(), articleId.toString());
        int collectNum = 0;
        if (set != null) {
            collectNum = set.size();
        }
        return R.ok().data("collectNum", collectNum);
    }

    @Override
    public R isCollect(Long articleId) {
        String openid = threadLocalHandler.getUser().getOpenid();
        if (openid == null) {
            return R.ok().data("collect", false);
        }
        Set<String> set = (Set<String>) redisTemplate.opsForHash().get(RedisConstant.getArticleCollect(), articleId.toString());
        if (set != null) {
            if (set.contains(openid)) {
                return R.ok().data("collect", true);
            }
        }
        return R.ok().data("collect", false);
    }

    @Override
    public R tapCollect(Long articleId, Boolean isCollect) {
        Set<String> set = (Set<String>) redisTemplate.opsForHash().get(RedisConstant.getArticleCollect(), articleId.toString());
        String openid = threadLocalHandler.getUser().getOpenid();
        if (set == null) {
            set = new HashSet<>();
        }
        if (isCollect) {
            set.remove(openid);
            redisTemplate.opsForZSet().remove(RedisConstant.getUserCollect() + openid, articleId);
        } else {
            set.add(openid);
            redisTemplate.opsForZSet().add(RedisConstant.getUserCollect() + openid, articleId, System.currentTimeMillis());

        }
        redisTemplate.opsForHash().put(RedisConstant.getArticleCollect(), articleId.toString(), set);
        return R.ok().data("collect", !isCollect);
    }

    /**
     * 分页查询当前用户的收藏夹
     * @param pageNo 当前页数
     * @param pageSize 每页有多少条数据
     * @return R
     */
    @Override
    public R getCollectByPage(Integer pageNo, Integer pageSize) {
        String openid = threadLocalHandler.getUser().getOpenid();
        Set<Integer> set = redisTemplate.opsForZSet().reverseRange(RedisConstant.getUserCollect() + openid, 0, -1);
        set = set == null ? new HashSet<>() : set;
        ArrayList<Integer> articleIdList = new ArrayList<>(set);
        PageBean<Integer> pageBean = new PageBean<>();
        pageBean.queryPager(pageNo, pageSize, articleIdList);
        List<Integer> queryList = pageBean.getList();
        if (queryList == null || queryList.size() == 0) {
            return R.ok().data("articleVoList", new ArrayList<>());
        }
        List<ArticleVo> articleVoList = articleMapper.getCollectByPage(queryList);
        getMultiPageViews(articleVoList);
        return R.ok().data("articleVoList", articleVoList);
    }

    @Override
    public R isAttention(String otherOpenid) {
        String myOpenid = threadLocalHandler.getUser().getOpenid();
        Set<String> set = (Set<String>) redisTemplate.opsForHash().get(RedisConstant.getUserConcern(), myOpenid);
        if (set != null && set.contains(otherOpenid)) {
            return R.ok().data("attention", true);
        }
        return R.ok().data("attention", false);
    }

    /**
     * 点击关注
     * @param otherOpenid 文章所属的openid
     * @param attention 当前页面是否关注
     * @return R
     */
    @Override
    public R tapAttention(String otherOpenid, Boolean attention) {
        String myOpenid = threadLocalHandler.getUser().getOpenid();
        Set<String> myConcernSet = (Set<String>) redisTemplate.opsForHash().get(RedisConstant.getUserConcern(), myOpenid);
        Set<String> otherConcernedSet = (Set<String>) redisTemplate.opsForHash().get(RedisConstant.getUserConcerned(), otherOpenid);
        if (myConcernSet == null) {
            myConcernSet = new HashSet<>();
        }
        if (otherConcernedSet == null) {
            otherConcernedSet = new HashSet<>();
        }
        if (attention) {
            myConcernSet.remove(otherOpenid);
            otherConcernedSet.remove(myOpenid);
        } else {
            myConcernSet.add(otherOpenid);
            otherConcernedSet.add(myOpenid);
        }
        redisTemplate.opsForHash().put(RedisConstant.getUserConcern(), myOpenid, myConcernSet);
        redisTemplate.opsForHash().put(RedisConstant.getUserConcerned(), otherOpenid, otherConcernedSet);
        return R.ok().data("attention", !attention);
    }

    /**
     * 获取自己关注人的文章
     * @param pageNo 文章的当前页
     * @param pageSize 每页的文章数
     * @return R
     */
    @Override
    public R getDynamicByPage(Integer pageNo, Integer pageSize) {
        String openid = threadLocalHandler.getUser().getOpenid();
        Set<Integer> articleIdSet = redisTemplate.opsForZSet().reverseRange(RedisConstant.getFEED() + openid, 0, -1);
        articleIdSet = articleIdSet == null ? new HashSet<>() : articleIdSet;
        List<Integer> articleIdList = new ArrayList<>(articleIdSet);
        PageBean<Integer> pageBean = new PageBean<>();
        pageBean.queryPager(pageNo, pageSize, articleIdList);
        articleIdList = pageBean.getList();
        if (articleIdList == null || articleIdList.size() == 0) {
            return R.ok().data("article", new ArrayList<>());
        }
        List<ArticleVo> articleVoList = articleMapper.getDynamicByPage(articleIdList);
        getMultiPageViews(articleVoList);
        return R.ok().data("article", articleVoList);
    }

    /**
     * 获取指定文章的评论数据
     * @param articleId 文章id
     * @return R
     */
    @Override
    public R getComments(Long articleId) {
        List<CommentVo> comments = articleMapper.getCommentsByArticleId(articleId);
        ArrayList<CommentVo> commentList = new ArrayList<>();
        ArrayList<CommentVo> commentListReply = new ArrayList<>();
        for (CommentVo comment : comments) {
            if (comment.getPid() == 0) {
                commentList.add(comment);
            } else {
                commentListReply.add(comment);
            }
        }
        return R.ok().data("comment_list", commentList).data("comment_list_reply", commentListReply);
    }

    @Override
    public R submitComment(Comment comment) {
        String openid = threadLocalHandler.getUser().getOpenid();
        comment.setOpenid(openid);
        articleMapper.addComment(comment);
        CommentVo commentVo = articleMapper.getComment(comment.getId());
        return R.ok().data("comment", commentVo);
    }

    @Override
    public R getMyCommentByPage(Integer pageNo, Integer pageSize) {
        String openid = threadLocalHandler.getUser().getOpenid();
        PageHelper.startPage(pageNo, pageSize);
        List<Comment> commentList = articleMapper.getMyCommentByPage(openid);
        PageInfo<Comment> pageInfo = new PageInfo<>(commentList);
        return R.ok().data("commentList", pageInfo.getList());
    }

    /**
     * 根据id删除评论
     * @param id 评论的id
     * @return R
     */
    @Override
    public R deleteCommentById(Long id) {
        int success = articleMapper.deleteCommentById(id);
        if (success > 0) {
            return R.ok().data("success", true);
        } else {
            return R.ok().data("success", false);
        }
    }

    /**
     * 根据用户id获取文章数据
     * @param pageNo 第几页
     * @param pageSize 每页文章有多少条数据
     * @return R
     */
    @Override
    public R getMyArticleByPage(Integer pageNo, Integer pageSize) {
        String openid = threadLocalHandler.getUser().getOpenid();
        PageHelper.startPage(pageNo, pageSize);
        List<ArticleVo> articleVoList = articleMapper.getMyArticleByPage(openid);
        PageInfo<ArticleVo> pageInfo = new PageInfo<>(articleVoList);
        List<ArticleVo> article = pageInfo.getList();
        getMultiPageViews(article);
        return R.ok().data("article", article);
    }


    /**
     * 删除文章的信息
     * @param articleId 文章的id
     * @return R
     */
    @Override
    public R deleteArticleById(Long articleId) {
        // 1. 删除文章表中的该条数据
        int success = articleMapper.deleteArticleById(articleId);
        if (success == 0) {
            return R.ok().data("success", false);
        }
        // 2. 删除该文章相关的评论
        articleMapper.delCommentByArticleId(articleId);
        // 删除Redis中的数据
        redisTemplate.opsForZSet().removeRangeByScore(RedisConstant.getHomePageData(), articleId, articleId);

        // 3. 删除该文章的点赞信息
        articleMapper.delArticleLikeByArticleId(articleId);
        // 删除Redis中的数据
        redisTemplate.opsForHash().delete(RedisConstant.getArticleLike(), articleId.toString());

        // 4. 删除该文章的收藏信息
        articleMapper.delCollectByArticleId(articleId);
        // 删除Redis中的数据
        redisTemplate.opsForHash().delete(RedisConstant.getArticleCollect(), articleId.toString());

        // 5. 删除浏览量的数据
        articleMapper.delHotspotByArticleId(articleId);
        // 删除Redis中的数据
        redisTemplate.opsForHash().delete(RedisConstant.getArticlePageViews(), articleId.toString());

        return R.ok().data("success", true);
    }


}
