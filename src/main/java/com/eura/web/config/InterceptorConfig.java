/**
 * @author Woong Sung(WiNMasTeR)
 * 인터셉터 설정
 */
package com.eura.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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
        	.addPathPatterns("/meet/**","/meet/**/**","/modify_profile","/modify_myinfo","/myinfo")
            .excludePathPatterns("/*");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                // .exposedHeaders("Set-Cookie")
                .maxAge(3000);
    }
}
