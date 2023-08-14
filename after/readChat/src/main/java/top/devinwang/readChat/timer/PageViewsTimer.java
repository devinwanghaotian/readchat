package top.devinwang.readChat.timer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.devinwang.readChat.commonutils.RedisConstant;
import top.devinwang.readChat.entity.ArticleHotspot;
import top.devinwang.readChat.mapper.ArticleHotspotMapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class PageViewsTimer {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ArticleHotspotMapper articleHotspotMapper;

    @Scheduled(cron ="0 15 4 * * ?")
    public void pageViewsPersistence() {
        log.info("时间: [{}]  阅读量持久化, 定时任务开始执行...", Calendar.getInstance().getTime());
        Map<String, Integer> pageViewsMap = redisTemplate.opsForHash().entries(RedisConstant.getArticlePageViews());
        List<ArticleHotspot> articleHotspotList = new ArrayList<>();
        for (Map.Entry<String, Integer> item : pageViewsMap.entrySet()) {
            ArticleHotspot articleHotspot = new ArticleHotspot();
            articleHotspot.setArticleId(Long.valueOf(item.getKey()));
            articleHotspot.setPageViews(item.getValue());
            articleHotspotList.add(articleHotspot);
        }
        articleHotspotMapper.addMultiPageViews(articleHotspotList);
    }

}
