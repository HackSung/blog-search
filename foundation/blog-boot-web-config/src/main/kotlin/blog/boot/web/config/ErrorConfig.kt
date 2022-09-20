package blog.boot.web.config

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.server.ErrorPage
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus

class ErrorConfig {
    @Bean
    fun webServerFactory(): ConfigurableServletWebServerFactory {
        val factory = TomcatServletWebServerFactory()
        factory.addErrorPages(ErrorPage(HttpStatus.NOT_FOUND, "/error/404"))
        factory.addErrorPages(ErrorPage("/error/default"))
        return factory
    }
}