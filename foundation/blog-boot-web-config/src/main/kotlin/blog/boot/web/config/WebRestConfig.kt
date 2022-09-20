package blog.boot.web.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.*

@Configuration
@EnableWebMvc
class WebRestConfig
{
    @Bean
    fun localeResolver(): LocaleResolver {
        val sessionLocaleResolver = SessionLocaleResolver()
        sessionLocaleResolver.setDefaultLocale(Locale.KOREA)
        return sessionLocaleResolver
    }

    @Bean
    fun mappingJackson2HttpMessageConverter(): MappingJackson2HttpMessageConverter {
        return MappingJackson2HttpMessageConverter()
    }

    @Primary
    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
            .registerKotlinModule()
            .registerModule(
                KotlinModule.Builder().configure(KotlinFeature.NullIsSameAsDefault, enabled = true).build()
            )
            .registerModule(JavaTimeModule())
    }

}