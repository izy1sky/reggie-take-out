package org.sunshiyi.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sunshiyi.common.R;
import org.sunshiyi.entity.User;
import org.sunshiyi.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public R<User> userLogin(@RequestBody Map<String, String> userInfo, HttpServletRequest request) {
        log.info("现在登录的用户是: {}", JSON.toJSONString(userInfo));
//      由于验证码部分没有使用短信验证码执行，因此直接就当做输入正确
//        查询是新用户还是老用户，新用户保存到user表中
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, userInfo.get("phone"));
        User user = userService.getOne(queryWrapper);
        if (user == null) {
            user = new User();
            user.setPhone(userInfo.get("phone"));
            userService.save(user);
        }
//        在session中保存用户ID
        request.getSession().setAttribute("user", user.getId());
        return R.success(user);
    }
}
