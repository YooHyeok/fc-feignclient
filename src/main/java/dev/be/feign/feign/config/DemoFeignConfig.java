package dev.be.feign.feign.config;

import dev.be.feign.feign.interceptor.DemoFeignInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoFeignConfig {
    @Bean
    public DemoFeignInterceptor feignInterceptor() {
//        return new DemoFeignInterceptor();
        return DemoFeignInterceptor.of(); //RequiredArgumentConstructor의 staticName으로 지정한 팩토리 메소드
    }
}
