package blog.common.client.naver

import blog.boot.constant.CommonConstant.HEADER_NAVER_CLIENT_ID
import blog.boot.constant.CommonConstant.HEADER_NAVER_CLIENT_SECRET
import blog.common.client.kakao.decoder.NaverErrorDecoder
import blog.common.client.naver.dto.NaverBlogSearchRequest
import blog.common.client.naver.dto.NaverBlogSearchResponse
import feign.RequestInterceptor
import feign.codec.ErrorDecoder
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(value = "naver", url = "\${external-api.naver.url}", primary = false, configuration = [NaverApi.FeignConfig::class])
interface NaverApi {

    @GetMapping("/v1/search/blog.json")
    fun searchBlog(@SpringQueryMap request: NaverBlogSearchRequest): NaverBlogSearchResponse

    class FeignConfig {
        @Value("\${external-api.naver.client-id}")
        val clientId: String? = null

        @Value("\${external-api.naver.client-secret}")
        val clientSecret: String? = null

        @Bean
        fun requestInterceptor() : RequestInterceptor {
            return RequestInterceptor {
                it.header(HEADER_NAVER_CLIENT_ID, clientId)
                it.header(HEADER_NAVER_CLIENT_SECRET, clientSecret)
            }
        }

        @Bean
        fun errorDecoder(): ErrorDecoder {
            return NaverErrorDecoder()
        }
    }
}