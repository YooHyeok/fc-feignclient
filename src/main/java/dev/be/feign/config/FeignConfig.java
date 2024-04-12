package dev.be.feign.config;

import dev.be.feign.feign.logger.FeignCustomLogger;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign의 Global한 설정 클래스 파일
 * 모든 Client에 일괄적으로 적용한다.
 */
@Configuration
public class FeignConfig {

    @Bean
    public Logger feignLogger() {
        return FeignCustomLogger.of();
    }
}
