package top.devinwang.readChat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.devinwang.readChat.interceptor.LoginInterceptor;

/**
 * 注册拦截器
 * @author devinWang
 * @Date 2023/5/24 12:06
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login/**",
                                        "/article/getByPage",
                                        "/article/getByOpenidAndPage",
                                        "/article/addPageViews",
                                        "/article/getUserAndArticle",
                                        "/article/getLikeNum",
                                        "/article/getCollectNum",
                                        "/article/getComments",
                                        "/user/getMessage");
    }
}
