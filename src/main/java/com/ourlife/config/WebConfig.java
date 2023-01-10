package com.ourlife.config;

import com.ourlife.argumentResolver.ValidateTokenArgumentResolver;
import com.ourlife.utils.Impl.JwtTokenUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtTokenUtils jwtTokenUtils;

    public WebConfig(JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ValidateTokenArgumentResolver(jwtTokenUtils));
    }



}