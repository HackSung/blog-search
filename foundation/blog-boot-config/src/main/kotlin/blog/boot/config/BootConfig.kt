package blog.boot.config

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor
import blog.boot.support.ApplicationContextProvider
import blog.boot.support.annotation.Slf4j

@Configuration
@EnableAutoConfiguration
@EnableAspectJAutoProxy
@EnableConfigurationProperties
@Slf4j
class BootConfig {

    @Bean
    fun methodValidationPostProcessor(): MethodValidationPostProcessor {
        return MethodValidationPostProcessor()
    }

    @Bean
    fun applicationContextProvider(): ApplicationContextProvider {
        return ApplicationContextProvider()
    }
}