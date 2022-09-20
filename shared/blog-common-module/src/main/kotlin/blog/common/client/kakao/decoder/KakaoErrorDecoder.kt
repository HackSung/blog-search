package blog.common.client.kakao.decoder

import blog.boot.support.annotation.Slf4j
import blog.boot.web.common.exception.ApiRuntimeException
import blog.boot.web.common.status.ApiResponseCode
import feign.Response
import feign.codec.ErrorDecoder

@Slf4j
class KakaoErrorDecoder : ErrorDecoder {

    override fun decode(methodKey: String?, response: Response?): Exception {
        return ApiRuntimeException(ApiResponseCode.BLOG_SEARCH_EXTERNAL_API_ERROR)
    }
}