package top.devinwang.readChat.timer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.devinwang.readChat.commonutils.RedisConstant;
import top.devinwang.readChat.entity.ArticleCollect;
import top.devinwang.readChat.entity.ArticleHotspot;
import top.devinwang.readChat.mapper.ArticleCollectMapper;
import top.devinwang.readChat.mapper.ArticleHotspotMapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

/**
 * 将Redis中的收藏信息存储在数据库中
 *
 * @author wanght50855
 * @date 2023/8/7 13:25
 */
@Slf4j
@Component
public class ArticleCollectTimer {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ArticleHotspotMapper articleHotspotMapper;

    @Autowired
    private ArticleCollectMapper articleCollectMapper;


    @Scheduled(cron ="0 34 21 * * ?")
    public void pageViewsPersistence() {
        log.info("时间: [{}]  收藏数据持久化, 定时任务开始执行...", Calendar.getInstance().getTime());
        // 1. 将每篇文章的收藏量放到MySQL中
        Map<String, Set<String>> map = redisTemplate.opsForHash().entries(RedisConstant.getArticleCollect());
        ArrayList<ArticleHotspot> articleHotspotList = new ArrayList<>();
        // 收藏相关信息
        ArrayList<ArticleCollect> articleCollectList = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
            // 收藏量统计
            ArticleHotspot articleHotspot = new ArticleHotspot();
            articleHotspot.setArticleId(Long.valueOf(entry.getKey()));
            articleHotspot.setCollectNum(entry.getValue().size());
            articleHotspotList.add(articleHotspot);

            // 收藏相关信息统计
            Long articleId = Long.valueOf(entry.getKey());
            Set<String> set = entry.getValue();
            for (String openid : set) {
                ArticleCollect articleCollect = new ArticleCollect();
                articleCollect.setArticleId(articleId);
                articleCollect.setOpenid(openid);
                articleCollectList.add(articleCollect);
            }
        }
        log.info("articleHotspotList = [{}]", articleHotspotList);
        log.info("articleCollectList = [{}]", articleCollectList);
        articleHotspotMapper.addMultiCollectNum(articleHotspotList);
        // 2. 将点赞的相关信息存储在点赞表中
        articleCollectMapper.addMultiArticleCollect(articleCollectList);
    }

}
