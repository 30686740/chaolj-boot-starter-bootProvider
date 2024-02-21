package com.chaolj.core.bootUtils.bootConfig;

import com.chaolj.core.MyConst;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 控制器拦截配置
@Configuration
public class TokenTrackConfig implements WebMvcConfigurer {
    // 拦截优先级
    private final int order = -99;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // SSOToken
        registry.addInterceptor(new TokenTrackInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(MyConst.INTERCEPTOR_EXCLUDE)
                .order(this.order);
    }
}
