package top.devinwang.readChat.timer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.devinwang.readChat.commonutils.RedisConstant;
import top.devinwang.readChat.entity.ArticleHotspot;
import top.devinwang.readChat.entity.ArticleLike;
import top.devinwang.readChat.mapper.ArticleHotspotMapper;
import top.devinwang.readChat.mapper.ArticleLikeMapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

/**
 * 将Redis中的点赞信息存储在数据库中
 *
 * @author wanght50855
 * @date 2023/8/6 21:13
 */

@Component
@Slf4j
public class ArticleLikeTimer {

    @Autowired
    private ArticleHotspotMapper articleHotspotMapper;

    @Autowired
    private ArticleLikeMapper articleLikeMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(cron ="0 24 23 * * ?")
    public void pageViewsPersistence() {
        log.info("时间: [{}]  点赞数据持久化, 定时任务开始执行...", Calendar.getInstance().getTime());
        // 1. 将每篇文章的点击量放到MySQL中
        Map<String, Set<String>> map = redisTemplate.opsForHash().entries(RedisConstant.getArticleLike());
        ArrayList<ArticleHotspot> articleHotspotList = new ArrayList<>();
        // 点赞相关信息
        ArrayList<ArticleLike> articleLikeList = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
            // 点赞量统计
            ArticleHotspot articleHotspot = new ArticleHotspot();
            articleHotspot.setArticleId(Long.valueOf(entry.getKey()));
            articleHotspot.setLikeNum(entry.getValue().size());
            articleHotspotList.add(articleHotspot);

            // 点赞相关信息统计
            Long articleId = Long.valueOf(entry.getKey());
            Set<String> set = entry.getValue();
            for (String openid : set) {
                ArticleLike articleLike = new ArticleLike();
                articleLike.setArticleId(articleId);
                articleLike.setOpenid(openid);
                articleLikeList.add(articleLike);
            }
        }
        log.info("articleHotspotList = [{}]", articleHotspotList);
        log.info("articleLikeList = [{}]", articleLikeList);
        articleHotspotMapper.addMultiLikeNum(articleHotspotList);
        // 2. 将点赞的相关信息存储在点赞表中
        articleLikeMapper.addMultiArticleLike(articleLikeList);
    }
}
