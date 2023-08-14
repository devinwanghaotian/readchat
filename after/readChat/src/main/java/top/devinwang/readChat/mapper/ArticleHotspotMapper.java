package top.devinwang.readChat.mapper;

import org.apache.ibatis.annotations.Param;
import top.devinwang.readChat.entity.ArticleHotspot;

import java.util.ArrayList;
import java.util.List;

public interface ArticleHotspotMapper {
    void addMultiPageViews(@Param("articleHotspotList") List<ArticleHotspot> articleHotspotList);

    void addMultiLikeNum(@Param("articleHotspotList") ArrayList<ArticleHotspot> articleHotspotList);

    void addMultiCollectNum(ArrayList<ArticleHotspot> articleHotspotList);
}
