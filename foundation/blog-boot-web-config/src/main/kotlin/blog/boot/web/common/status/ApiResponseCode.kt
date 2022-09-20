package blog.boot.web.common.status

import org.springframework.http.HttpStatus

enum class ApiResponseCode(val httpStatus: HttpStatus, val code: String, val message: String, val dispMessage: String) {
    SUCCESS(HttpStatus.OK, "BL200", "success", "success"),
    BAD_PARAMETER(HttpStatus.BAD_REQUEST, "BL400", "bad parameter", "잘못된 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "BL404", "not found", "not found"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "BL405", "invalid method", "지원하지 않는 method 입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "BL500", "internal server error", "서버 내부 에러가 발생하였습니다."),
    BLOG_SEARCH_EXTERNAL_API_ERROR(HttpStatus.OK, "EX500", "external api error", "블로그검색 외부API호출이 실패하였습니다."),
    ;

    companion object {
        private val map: MutableMap<String, ApiResponseCode> = HashMap()

        fun fromCode(code: String): ApiResponseCode? {
            return map[code]
        }

        init {
            for (type in values()) {
                map[type.code] = type
            }
        }
    }

}