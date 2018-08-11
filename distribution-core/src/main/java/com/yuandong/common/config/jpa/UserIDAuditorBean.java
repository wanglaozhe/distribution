package com.yuandong.common.config.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.yuandong.vo.SecurityUser;
import com.yuandong.vo.UserDetailVo;


@Configuration
public class UserIDAuditorBean implements AuditorAware<String> {
    @Override
    public String getCurrentAuditor() {
       SecurityContext ctx = SecurityContextHolder.getContext();
        if (ctx == null) {
            return "unknow";
        }
        if (ctx.getAuthentication() == null) {
        	return "unknow";
        }
        if (ctx.getAuthentication().getPrincipal() == null) {
        	return "unknow";
        }
        Object principal = ctx.getAuthentication().getPrincipal();
        if (principal.getClass().isAssignableFrom(SecurityUser.class)) {
            return ((UserDetailVo)principal).getUserName();
        } else {
            return "unknow";
        }
    }
}
