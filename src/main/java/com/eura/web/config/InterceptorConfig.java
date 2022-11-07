/**
 * @author Woong Sung(WiNMasTeR)
 * 인터셉터 설정
 */
package com.eura.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.eura.web.config.Interceptor.ApiAuthorityInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private ApiAuthorityInterceptor apiAuthorityInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
            .addInterceptor(apiAuthorityInterceptor)
        	.addPathPatterns("/meet/**")
            .excludePathPatterns("/*");
    }
}
