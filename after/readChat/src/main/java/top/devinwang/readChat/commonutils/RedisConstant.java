package top.devinwang.readChat.commonutils;

/**
 * @author devinWang
 * @Date 2023/5/28 14:30
 */
public class RedisConstant {
    private final static String USER_LOGIN  = "USER:LOGIN:";

    private final static String HOME_PAGE_DATA = "HOME:PAGE:DATA";

    private final static int ALL_ARTICLE_SIZE = 500;

    private final static String ARTICLE_PAGE_VIEWS = "ARTICLE:PAGE:VIEWS";

    private final static String ARTICLE_LIKE = "ARTICLE:LIKE";

    private final static String ARTICLE_COLLECT = "ARTICLE:COLLECT";

    private final static String USER_COLLECT = "USER:COLLECT:";

    private final static String USER_CONCERN = "USER:CONCERN";

    private final static String USER_CONCERNED = "USER:CONCERNED";

    private final static String FEED = "FEED:";

    public static String getUserLogin() {
        return USER_LOGIN;
    }

    public static String getHomePageData() {
        return HOME_PAGE_DATA;
    }

    public static int getAllArticleSize() {
        return ALL_ARTICLE_SIZE;
    }

    public static String getArticlePageViews() {
        return ARTICLE_PAGE_VIEWS;
    }

    public static String getArticleLike() {
        return ARTICLE_LIKE;
    }

    public static String getArticleCollect() {
        return ARTICLE_COLLECT;
    }

    public static String getUserCollect() {
        return USER_COLLECT;
    }

    public static String getUserConcern() {
        return USER_CONCERN;
    }

    public static String getUserConcerned() {
        return USER_CONCERNED;
    }

    public static String getFEED() {
        return FEED;
    }
}
