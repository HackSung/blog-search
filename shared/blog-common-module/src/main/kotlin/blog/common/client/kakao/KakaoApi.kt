package blog.common.client.kakao

import blog.common.client.kakao.decoder.KakaoErrorDecoder
import blog.common.client.kakao.dto.KakaoBlogSearchRequest
import blog.common.client.kakao.dto.KakaoBlogSearchResponse
import feign.RequestInterceptor
import feign.codec.ErrorDecoder
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(value = "kakao", url = "\${external-api.kakao.url}", primary = false, configuration = [KakaoApi.FeignConfig::class])
interface KakaoApi {

    @GetMapping("/v2/search/blog")
    fun searchBlog(@SpringQueryMap request: KakaoBlogSearchRequest): KakaoBlogSearchResponse

    class FeignConfig {
        @Value("\${external-api.kakao.api-key}")
        val accessToken: String? = null

        @Bean
        fun requestInterceptor() : RequestInterceptor {
            return RequestInterceptor { it.header(HttpHeaders.AUTHORIZATION, "KakaoAK $accessToken") }
        }

        @Bean
        fun errorDecoder(): ErrorDecoder {
            return KakaoErrorDecoder()
        }
    }
}