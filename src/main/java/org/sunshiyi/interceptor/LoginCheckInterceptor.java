package org.sunshiyi.interceptor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.sunshiyi.common.R;
import org.sunshiyi.utils.BaseContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String[] paths = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/login",
                "/user/sendMsg"
        };
        boolean checked = check(paths, requestURI);
        if (checked) {
            return true;
        }

//        检查员工是否登陆了
        Long id = (Long) request.getSession().getAttribute("employee");
        if (id != null) {
            BaseContext.setCurrentId(id);
            return true;
        }
//        检查用户是否登录了
        Long userId = (Long) request.getSession().getAttribute("user");
        if (userId != null) {
            BaseContext.setCurrentId(userId);
            return true;
        }

        log.info("被拦截的请求是: {}", requestURI);
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return false;
    }

    private boolean check(String[] paths, String path) {
        for (String s : paths) {
            if (PATH_MATCHER.match(s, path)) {
                return true;
            }
        }
        return false;
    }
}
