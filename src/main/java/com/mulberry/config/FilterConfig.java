package com.mulberry.config;

import com.mulberry.filter.CorsFilter;
import com.mulberry.filter.TimingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<TimingFilter> timingFilterRegistration() {
        FilterRegistrationBean<TimingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TimingFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }

    // 与WebConfig冲突
//    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration() {
        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CorsFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(2);
        return registration;
    }
}
