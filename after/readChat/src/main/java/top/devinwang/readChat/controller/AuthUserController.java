package top.devinwang.readChat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.devinwang.readChat.commonutils.R;
import top.devinwang.readChat.entity.AuthUser;
import top.devinwang.readChat.service.AuthUserService;

/**
 * @author devinWang
 * @Date 2023/6/11 12:16
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class AuthUserController {
    @Autowired
    private AuthUserService authUserService;

    /**
     * 通过 openid 获取用户数据
     * @param openid 查询用户的openid
     * @return R
     */
    @GetMapping("/getMessage")
    public R getMessageById(@RequestParam("openid") String openid) {
        AuthUser authUser = authUserService.getMessageById(openid);
        if (authUser == null) {
            return R.error().message("获取用户数据失败...");
        }
        return R.ok().data("user", authUser);
    }
}
